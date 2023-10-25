package com.example.adminreference.enumeration;

public enum TagType {

    TEST("test");
    private final String value;

    TagType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
