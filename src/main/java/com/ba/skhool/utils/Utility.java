package com.ba.skhool.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utility {

	public static void copyNonNullProperties(Object source, Object target) {
		BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> nullProps = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				nullProps.add(pd.getName());
			}
		}

		String[] ignoreProps = nullProps.toArray(new String[0]);
		BeanUtils.copyProperties(source, target, ignoreProps);
	}

}
