package com.example.adminreference.config.security;

public enum ClaimType {
    AUTHORITY("authority"),
    ADMIN_ID("adminId");

    public final String name;

    ClaimType(String name) {
        this.name = name;
    }
}
