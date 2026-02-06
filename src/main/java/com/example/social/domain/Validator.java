package com.example.social.domain;

@FunctionalInterface
public interface Validator<T> {
    void validate(T target);
}
