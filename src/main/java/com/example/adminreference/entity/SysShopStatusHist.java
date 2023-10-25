package com.example.adminreference.entity;

import com.example.adminreference.common.converter.ServiceStatusConverter;
import com.example.adminreference.enumeration.ServiceStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sys_shop_status_hist")
public class SysShopStatusHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;
    private int shopId;
    @Convert(converter = ServiceStatusConverter.class)
    private ServiceStatus statusCd;
    private LocalDate statusApplyDt;

    @CreatedBy
    private long regId;
    @Column(insertable = false, updatable = false)
    private LocalDateTime regDt;

    public static SysShopStatusHist newInstance(int shopId, ServiceStatus statusCd, LocalDate statusApplyDt) {
        SysShopStatusHist sysShopStatusHist = new SysShopStatusHist();
        sysShopStatusHist.shopId = shopId;
        sysShopStatusHist.statusCd = statusCd;
        sysShopStatusHist.statusApplyDt = statusApplyDt;
        return sysShopStatusHist;
    }

//    @Builder(access = AccessLevel.PRIVATE)
//    public SysShopStatusHist(int shopId, ServiceStatus statusCd, LocalDate statusApplyDt) {
//        this.shopId = shopId;
//        this.statusCd = statusCd;
//        this.statusApplyDt = statusApplyDt;
//    }
}
