package com.example.social.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class InMemoryDataPool<T> {
    private final List<T> pool = new ArrayList<>();

    public void replaceAll(List<T> data) {
        pool.clear();
        pool.addAll(data);
    }

    public List<T> all() {
        return new ArrayList<>(pool);
    }

    public List<T> filter(Predicate<T> predicate) {
        return pool.stream().filter(predicate).toList();
    }

    public List<T> sort(Comparator<T> comparator) {
        return pool.stream().sorted(comparator).toList();
    }
}
