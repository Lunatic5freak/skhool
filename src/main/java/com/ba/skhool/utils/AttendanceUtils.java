package com.ba.skhool.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.ba.skhool.constants.AttendanceStatus;

public class AttendanceUtils {

	private static final int BITS_PER_DAY = 3;

	public static byte[] updateAttendance(byte[] bitmap, LocalDate startDate, LocalDate targetDate,
			AttendanceStatus status) {
		int dayCount = (int) ChronoUnit.DAYS.between(startDate, targetDate) + 1;
		if (dayCount <= 0) {
			throw new IllegalArgumentException("targetDate cannot be before startDate");
		}
		return updateAttendance(bitmap, dayCount, status);

	}

	public static byte[] updateAttendance(byte[] bitmap, int dayCount, AttendanceStatus status) {
		int totalBits = dayCount * BITS_PER_DAY;
		int requiredBytes = (int) Math.ceil(totalBits / 8.0);

		// Resize bitmap if needed
		if (bitmap == null || bitmap.length < requiredBytes) {
			byte[] newBitmap = new byte[requiredBytes];
			if (bitmap != null) {
				System.arraycopy(bitmap, 0, newBitmap, 0, bitmap.length);
			}
			bitmap = newBitmap;
		}

		// Fill all unmarked intermediate days with NOT_MARKED
//		for (int dayIndex = 0; dayIndex < dayCount - 2; dayIndex++) {
//			set3Bit(bitmap, dayIndex, AttendanceStatus.NOT_MARKED.getCode());
//		}

		// Set target day with given status
		set3BitIfUnset(bitmap, dayCount, status.getCode());

		return bitmap;
	}

	public static void set3BitIfUnset(byte[] bitmap, int dayIndex, int code) {
		int bitOffset = (dayIndex - 1) * BITS_PER_DAY;
		int byteIndex = bitOffset / 8;
		int bitOffsetInByte = bitOffset % 8;

		int bitsAvailableInFirstByte = 8 - bitOffsetInByte;

		if (bitsAvailableInFirstByte >= BITS_PER_DAY) {
			// ✅ All 3 bits fit in one byte
			int shift = bitsAvailableInFirstByte - BITS_PER_DAY;
			int mask = 0b111 << shift;

			int current = (bitmap[byteIndex] & mask) >> shift;
			if (current != code) {
				bitmap[byteIndex] &= ~mask;
				bitmap[byteIndex] |= (code << shift);
			}
		} else {
			// ❗ Bits cross byte boundary — must check bounds first
			if (byteIndex + 1 >= bitmap.length) {
				throw new IllegalArgumentException("Bitmap too small for day index " + dayIndex);
			}

			int firstPartBits = bitsAvailableInFirstByte;
			int secondPartBits = BITS_PER_DAY - firstPartBits;

			// Extract current value (combine parts)
			int firstPart = bitmap[byteIndex] & ((1 << firstPartBits) - 1);
			int secondPart = (bitmap[byteIndex + 1] >> (8 - secondPartBits)) & ((1 << secondPartBits) - 1);
			int current = (firstPart << secondPartBits) | secondPart;

			if (current != code) {
				// Clear existing
				bitmap[byteIndex] &= ~((1 << firstPartBits) - 1);
				bitmap[byteIndex + 1] &= ~(((1 << secondPartBits) - 1) << (8 - secondPartBits));

				// Set new
				int firstToSet = code >> secondPartBits;
				int secondToSet = code & ((1 << secondPartBits) - 1);

				bitmap[byteIndex] |= firstToSet;
				bitmap[byteIndex + 1] |= (secondToSet << (8 - secondPartBits));
			}
		}
	}

//	public static AttendanceStatus readAttendance(byte[] bitmap, LocalDate startDate, LocalDate targetDate) {
//		int dayIndex = (int) ChronoUnit.DAYS.between(startDate, targetDate);
//		int bitOffset = dayIndex * BITS_PER_DAY;
//		int byteIndex = bitOffset / 8;
//		int bitIndexInByte = bitOffset % 8;
//
//		if (bitmap == null || byteIndex >= bitmap.length)
//			return AttendanceStatus.NOT_MARKED;
//
//		int shift = 8 - BITS_PER_DAY - bitIndexInByte;
//		int mask = 0b111 << shift;
//		int code = (bitmap[byteIndex] & mask) >> shift;
//
//		return AttendanceStatus.fromCode(code);
//	}

	public static AttendanceStatus readAttendance(byte[] bitmap, LocalDate startDate, LocalDate targetDate) {
		if (bitmap == null) {
			return AttendanceStatus.NOT_MARKED;
		}
		if (targetDate.isBefore(startDate)) {
			throw new IllegalArgumentException("Target date cannot be before start date");
		}

		int dayIndex = (int) ChronoUnit.DAYS.between(startDate, targetDate);
		int startBit = dayIndex * BITS_PER_DAY;

		// Extract BITS_PER_DAY bits starting from startBit position
		int code = extractBits(bitmap, startBit, BITS_PER_DAY);

		return (code == -1) ? AttendanceStatus.NOT_MARKED : AttendanceStatus.fromCode(code);
	}

	/**
	 * Helper method to extract specified number of bits from bitmap starting at
	 * given bit position
	 * 
	 * @param bitmap   the byte array
	 * @param startBit starting bit position (0-based)
	 * @param numBits  number of bits to extract
	 * @return extracted value or -1 if out of bounds
	 */
	private static int extractBits(byte[] bitmap, int startBit, int numBits) {
		int totalBitsNeeded = startBit + numBits;
		int totalBytesNeeded = (totalBitsNeeded + 7) / 8; // Ceiling division

		if (totalBytesNeeded > bitmap.length) {
			return -1; // Out of bounds
		}

		int result = 0;
		int bitsExtracted = 0;

		while (bitsExtracted < numBits) {
			int currentByteIndex = (startBit + bitsExtracted) / 8;
			int bitPositionInByte = (startBit + bitsExtracted) % 8;
			int bitsAvailableInByte = 8 - bitPositionInByte;
			int bitsToExtract = Math.min(bitsAvailableInByte, numBits - bitsExtracted);

			// Create mask for bits to extract from current byte
			int mask = ((1 << bitsToExtract) - 1) << (8 - bitPositionInByte - bitsToExtract);
			int extractedFromByte = (bitmap[currentByteIndex] & mask) >> (8 - bitPositionInByte - bitsToExtract);

			// Add extracted bits to result (shift left to make room)
			result = (result << bitsToExtract) | extractedFromByte;
			bitsExtracted += bitsToExtract;
		}

		return result;
	}

	public static Byte get3BitCode(byte[] bitmap, LocalDate startDate, LocalDate date) {
		int dayIndex = (int) ChronoUnit.DAYS.between(startDate, date);
		int bitOffset = dayIndex * 3;
		int byteIndex = bitOffset / 8;
		int bitIndexInByte = bitOffset % 8;

		if (byteIndex >= bitmap.length)
			return null;

		int shift = 8 - 3 - bitIndexInByte;
		if (shift < 0 || shift > 5)
			return null; // unsafe shift

		int mask = 0b111 << shift;
		return (byte) ((bitmap[byteIndex] & mask) >> shift);
	}

}
