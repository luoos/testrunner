package edu.illinois.cs.testrunner.data.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Utils {
    static List<Method> getAllMethods(Class clz) {
        List<Method> methods = new ArrayList<>();
        Set<String> methodNameSet = new HashSet<>();
        for (; clz != null; clz = clz.getSuperclass()) {
            for (Method m : clz.getDeclaredMethods()) {
                if (methodNameSet.contains(m.getName())) {
                    // exclude override method
                    continue;
                }
                methods.add(m);
                methodNameSet.add(m.getName());
            }
        }
        return methods;
    }
}
