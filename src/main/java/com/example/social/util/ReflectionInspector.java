package com.example.social.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public final class ReflectionInspector {
    private ReflectionInspector() {
    }

    public static List<String> fieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .toList();
    }
}
