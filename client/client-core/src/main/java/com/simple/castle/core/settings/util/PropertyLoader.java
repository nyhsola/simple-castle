package com.simple.castle.core.settings.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

public final class PropertyLoader {

    private PropertyLoader() {
    }

    public static String loadData(String path) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(PropertyLoader.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException exception) {
            throw new AssertionError("Missing such props", exception);
        }
    }

    public static Properties load(String name) {
        Properties properties = new Properties();
        try {
            properties.load(PropertyLoader.class.getResourceAsStream(name));
        } catch (IOException exception) {
            throw new AssertionError("Missing such props", exception);
        }
        return properties;
    }

}
