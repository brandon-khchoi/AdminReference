package com.example.adminreference.enumeration;

import com.sun.xml.bind.v2.TODO;

public enum AccountState {

    ACTIVE("A"),

    PENDING("P"),


    //TODO: 나중에 변경 예정 뭔가 애매한듯?
    REJECTED("R");

    private final String code;

    AccountState(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
