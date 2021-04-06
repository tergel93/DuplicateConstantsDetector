package me.tergel.annotation.processor;

class DuplicateMemberError extends Error {
    DuplicateMemberError(String key, String val) {
        super("Detect duplicate const String: \"" + val + "\" in " + key);
    }
}