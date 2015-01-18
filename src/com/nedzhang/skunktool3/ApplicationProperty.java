package com.nedzhang.skunktool3;

import com.nedzhang.util.ResourceBundleUtil;

public class ApplicationProperty {

	private static final String PROPERTY_BASE = "skunkwerk_support_tool";

	private ApplicationProperty() {
	}

	public static String get(final String key) {
		return ResourceBundleUtil.getStringResource(ApplicationProperty.class,
				PROPERTY_BASE, key);
	}
}
