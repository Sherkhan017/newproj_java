package com.example.social.domain;

public interface EntityFactory<T> {
    T create();

    default T createAndValidate(Validator<T> validator) {
        T item = create();
        validator.validate(item);
        return item;
    }

    static String normalize(String input) {
        return input == null ? "" : input.trim();
    }
}
