package com.simple.castle.utils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public final class StringTool {
    private StringTool() {

    }

    public static Collection<String> getValuesByPattern(Set<String> modelNames, String pattern) {
        return modelNames.stream()
                .filter(nodes -> nodes.matches(pattern))
                .collect(Collectors.toList());
    }
}
