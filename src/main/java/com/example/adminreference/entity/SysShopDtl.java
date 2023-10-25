package com.example.adminreference.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sys_shop_dtl")
public class SysShopDtl {

    @Id
    private Integer shopId;

    private long subsCnt;

    private int shopScore;

    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updDt;

    public static SysShopDtl of(int shopId) {
        return SysShopDtl.builder()
                .shopId(shopId)
                .shopScore(0)
                .subsCnt(0)
                .build();
    }

    @Builder(access = AccessLevel.PROTECTED)
    public SysShopDtl(Integer shopId, long subsCnt, int shopScore) {
        this.shopId = shopId;
        this.subsCnt = subsCnt;
        this.shopScore = shopScore;
    }


}
