package com.example.adminreference.dto;

import com.example.adminreference.enumeration.ServiceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ShopStatusHistDto {

    private int shopId;
    private ServiceStatus statusCd;
    private String statusCdDesc;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statusApplyDt;
    private String engUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt;

    public String getStatusCdDesc() {
        return statusCd != null ? statusCd.getDesc() : "-";
    }
}
