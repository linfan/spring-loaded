package org.springsource.loaded.agent;

/**
 * @author flin
 */
public class TransformEscapeClassUtils {

    private static String[] slashSeparatedEscapeClassPrefixes = new String[] {
        // JDK
        "java/",
        "javax/",
        "sun/",
        "com/apple/jdk/",
        // Frameworks
        "org/springframework/",
        "org/mockito/",
        "net/sf/cglib/",
        // Spring loaded
        "org/springsource/loaded/"
    };

    public static String[] getEscapeClassPrefixes() {
        return slashSeparatedEscapeClassPrefixes;
    }
}
