package edu.illinois.cs.testrunner.execution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String junit5Regex = "\\[class:([\\w.]+).*\\[method:(\\w+)\\(";
    private static final Pattern junit5Pattern = Pattern.compile(junit5Regex);

    /**
     * Turn the uniqueId from identifier into full qualified method name.
     *
     * For example:
     * uniqueId: [engine:junit-jupiter]/[class:com.luojl.demo.JUnit5DemoTest]/[method:TestC()]
     * full qualified name: com.luojl.demo.JUnit5DemoTest#TestC
     *
     */
    public static String toFullQualifiedName(String identifierUniqueId) {
        Matcher matcher = junit5Pattern.matcher(identifierUniqueId);
        matcher.find();  // should always find
        return matcher.group(1) + "#" + matcher.group(2);
    }
}
