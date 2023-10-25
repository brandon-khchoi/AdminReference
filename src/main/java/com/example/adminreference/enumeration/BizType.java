package com.example.adminreference.enumeration;

public enum BizType {
    P("개인"), C("법인");

    private final String desc;

    BizType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
