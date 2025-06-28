package com.ba.skhool.constants;

import java.util.Arrays;

public enum AttendanceStatus {
	NOT_MARKED(0b000), PRESENT(0b001), ABSENT(0b010), LATE(0b011), HOLIDAY(0b100), LEAVE(0b101);

	private final int code;

	AttendanceStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static AttendanceStatus fromCode(int code) {
		return Arrays.stream(values()).filter(s -> s.code == code).findFirst().orElse(NOT_MARKED);
	}
}
