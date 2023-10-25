package com.example.adminreference.enumeration;

public enum ServiceStatus {

    READY(1001, "입점대기"),
    OPEN(1002, "오픈"),
    STOP(1003, "사용정지"),
    SUSPEND(1004, "일시정지");

    private final int code;
    private final String desc;

    ServiceStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
