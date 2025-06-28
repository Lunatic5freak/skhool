package com.ba.skhool.student.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.ba.skhool.student.dto.FilterCriteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SpecificationBuilder<T> {

	public Specification<T> build(List<FilterCriteria> filters) {
		if (filters == null || filters.isEmpty())
			return null;

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			Map<String, List<Predicate>> orGroups = new HashMap<>();

			for (FilterCriteria filter : filters) {
				Predicate predicate = buildPredicate(filter, root, cb);
				if ("OR".equalsIgnoreCase(filter.getCondition())) {
					orGroups.computeIfAbsent(filter.getField(), k -> new ArrayList<>()).add(predicate);
				} else {
					predicates.add(predicate); // Default AND
				}
			}

			// Combine OR conditions
			for (List<Predicate> group : orGroups.values()) {
				predicates.add(cb.or(group.toArray(new Predicate[0])));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	private Predicate buildPredicate(FilterCriteria filter, Root<T> root, CriteriaBuilder cb) {
		Path<?> path = root.get(filter.getField());

		switch (filter.getOperator().toUpperCase()) {
		case "EQUALS":
			return cb.equal(path, filter.getValue());
		case "LIKE":
			return cb.like(cb.lower(path.as(String.class)), "%" + filter.getValue().toString().toLowerCase() + "%");
		case "IN":
			CriteriaBuilder.In<Object> inClause = cb.in(path);
			Collection<?> values = (Collection<?>) filter.getValue();
			values.forEach(inClause::value);
			return inClause;
		case "GREATER_THAN":
			return cb.greaterThan(path.as(Comparable.class), (Comparable) filter.getValue());
		case "LESS_THAN":
			return cb.lessThan(path.as(Comparable.class), (Comparable) filter.getValue());
		default:
			throw new IllegalArgumentException("Unknown operator: " + filter.getOperator());
		}
	}
}
