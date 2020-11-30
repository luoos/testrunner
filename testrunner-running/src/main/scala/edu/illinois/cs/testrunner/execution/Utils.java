package edu.illinois.cs.testrunner.execution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String junit5NestedRegex = "\\[class:([\\w.]+).*\\[nested-class:([\\w.]+).*\\[method:([\\w().]+)";
    private static final Pattern junit5NestedPattern = Pattern.compile(junit5NestedRegex);
    private static final String junit5Regex = "\\[class:([\\w.]+).*\\[method:([\\w().]+)";
    private static final Pattern junit5Pattern = Pattern.compile(junit5Regex);
    private static final String junit4Regex = "\\[test:(\\w+)\\(([\\w.]+)\\)";
    private static final Pattern junit4Pattern = Pattern.compile(junit4Regex);

    /**
     * Turn the uniqueId from identifier into fully qualified method name.
     *
     * For JUnit 5 nested test:
     * uniqueId: [engine:junit-jupiter]/[class:com.luojl.demo.InheritedTest]/[nested-class:NestedTest]/[method:NestedTestB()]
     * fully qualified name: com.luojl.demo.InheritedTest$NestedTest#NestedTestB()
     *
     * For JUnit 5:
     * uniqueId: [engine:junit-jupiter]/[class:com.luojl.demo.JUnit5DemoTest]/[method:TestC(java.lang.String)]
     * full qualified name: com.luojl.demo.JUnit5DemoTest#TestC(java.lang.String)
     *
     * For JUnit 4:
     * uniqueId: [engine:junit-vintage]/[runner:com.luojl.demo.JUnit4DemoTest]/[test:TestA4(com.luojl.demo.JUnit4DemoTest)]
     * full qualified name: com.luojl.demo.JUnit4DemoTest#TestA4
     */
    public static String toFullyQualifiedName(String identifierUniqueId) {
        Matcher matcher = junit5NestedPattern.matcher(identifierUniqueId);
        if (matcher.find()) {
            // found JUnit 5 nested test pattern
            return matcher.group(1) + "$" + matcher.group(2) + "#" + matcher.group(3);
        }
        matcher = junit5Pattern.matcher(identifierUniqueId);
        if (matcher.find()) {
            // found JUnit 5 pattern
            return matcher.group(1) + "#" + matcher.group(2);
        }
        // fall back to JUnit 4
        matcher = junit4Pattern.matcher(identifierUniqueId);
        if (matcher.find()) {
            return matcher.group(2) + "#" + matcher.group(1);
        }
        throw new IllegalStateException(
            "Fail to parse identifierUniqueId: " + identifierUniqueId);
    }
}
