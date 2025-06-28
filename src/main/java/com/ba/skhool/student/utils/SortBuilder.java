package com.ba.skhool.student.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import com.ba.skhool.student.dto.SortableRequest;

public class SortBuilder {

	public static <T extends SortableRequest> Sort buildSort(T request) {
		if (request == null)
			return Sort.unsorted();
		List<SortableRequest.SortField> sortFields = request.getSortFields();

		if (sortFields == null || sortFields.isEmpty()) {
			return Sort.unsorted();
		}

		return Sort.by(
				sortFields.stream().map(s -> "desc".equalsIgnoreCase(s.getDirection()) ? Sort.Order.desc(s.getField())
						: Sort.Order.asc(s.getField())).collect(Collectors.toList()));
	}
}
