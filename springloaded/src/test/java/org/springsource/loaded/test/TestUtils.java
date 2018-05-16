
package org.springsource.loaded.test;

public class TestUtils {

    public enum Lang {
        java,
        groovy
    }

	public static String getPathToClasses(String path) {
		return getPathToClasses(path, Lang.java, false);
	}

    public static String getPathToClasses(String path, Lang lang) {
        return getPathToClasses(path, lang, false);
    }

    public static String getPathToClasses(String path, Boolean isTestFolder) {
        return getPathToClasses(path, Lang.java, isTestFolder);
    }

	public static String getPathToClasses(String path, Lang lang, Boolean isTestFolder) {
		if (Boolean.parseBoolean(System.getProperty("springloaded.tests.useGradleBuildDir", "false"))) {
			return path + "/build/classes/" + lang.name() + "/" + (isTestFolder ? "test" : "main");
		}
		else {
			return path + "/bin";
		}
	}
}
