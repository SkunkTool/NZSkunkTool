package com.nedzhang.skunktool3;

public class SkunkSetting {

	private static SkunkSetting instance;
	private static Object instanceLocker = new Object();

	public final String STERLING_INTEROP_URL;

	public final String STERLING_INTEROP_PROG_ID;

	public final boolean VERBOSE_MODE;

	public final boolean TESTER_MODE;

	public final String FILE_STORE_PATH;

	public final String STERLING_API_JAVADOC_URL;

	public final String HARNESS_TO_LOAD;

	private SkunkSetting() {

		STERLING_INTEROP_URL = readAndTrimStringPorperty("STERLING_INTEROP_URL");

		STERLING_INTEROP_PROG_ID = readAndTrimStringPorperty("STERLING_INTEROP_PROG_ID");

		VERBOSE_MODE = readBooleanProperty("VERBOSE_MODE");

		TESTER_MODE = readBooleanProperty("TESTER_MODE");

		FILE_STORE_PATH = readAndTrimStringPorperty("FILE_STORE_PATH");

		STERLING_API_JAVADOC_URL = readAndTrimStringPorperty("STERLING_API_JAVADOC_URL");

		HARNESS_TO_LOAD = readAndTrimStringPorperty("HARNESS_TO_LOAD");
		
		if (VERBOSE_MODE) {
			System.out.println("STERLING_INTEROP_URL : " + STERLING_INTEROP_URL);
			System.out.println("STERLING_INTEROP_PROG_ID : " + STERLING_INTEROP_PROG_ID);
			System.out.println("VERBOSE_MODE : " + VERBOSE_MODE);
			System.out.println("TESTER_MODE : " + TESTER_MODE);
			System.out.println("FILE_STORE_PATH : " + FILE_STORE_PATH);
			System.out.println("STERLING_API_JAVADOC_URL : " + STERLING_API_JAVADOC_URL);
			System.out.println("HARNESS_TO_LOAD : " + HARNESS_TO_LOAD);
		}

	}

	private String readAndTrimStringPorperty(String propertyName) {
		String propertyValue = ApplicationProperty.get(propertyName);

		return propertyValue == null || propertyValue.length() == 0 ? propertyValue
				: propertyValue.trim();
	}

	private boolean readBooleanProperty(String propertyName) {
		String propertyStringValue = readAndTrimStringPorperty(propertyName);

		return SkunkUtil.parseBoolean(propertyStringValue);
	}

	public static SkunkSetting getInstance() {

		if (instance == null) {
			synchronized (instanceLocker) {
				if (instance == null) {
					instance = new SkunkSetting();
				}
			}
		}

		return instance;

	}
}
