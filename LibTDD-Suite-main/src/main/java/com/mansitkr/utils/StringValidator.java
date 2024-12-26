package com.mansitkr.utils;

public class StringValidator {
    public static void validateString(String value, String message) {
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException(message);
        }
    }
}
