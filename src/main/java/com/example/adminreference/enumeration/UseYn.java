package com.example.adminreference.enumeration;

public enum UseYn {
    N(false), Y(true);

    private final boolean flag;

    UseYn(boolean flag) {
        this.flag = flag;
    }

    public boolean isUsable() {
        return flag;
    }
}

