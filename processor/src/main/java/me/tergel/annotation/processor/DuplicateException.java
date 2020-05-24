package me.tergel.annotation.processor;

class DuplicateException extends Error {
    DuplicateException(String key, String val) {
        super("Detect duplicate const String: \"" + val + "\" in " + key);
    }
}