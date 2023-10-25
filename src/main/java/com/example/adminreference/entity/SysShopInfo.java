package com.example.adminreference.entity;

import com.example.adminreference.common.converter.UseYnConverter;
import com.example.adminreference.dto.ShopManageDto;
import com.example.adminreference.enumeration.BizType;
import com.example.adminreference.enumeration.UseYn;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sys_shop_info")
public class SysShopInfo {

    @Id
    private Integer shopId;
    @Enumerated(EnumType.STRING)
    private BizType businessDiv;
    private String corpNm;
    private String reprNm;
    private String corpRegNo;
    @Convert(converter = UseYnConverter.class)
    @Enumerated(EnumType.STRING)
    private UseYn regDuplYn;
    private String tel;
    private String fax;
    private String addr1;
    private String addr2;
    private String mgrNm;
    private String mgrCell;
    private String mgrEmail;
    private String billEmail;
    private String regFileNm;
    @Column(name = "reg_file_s3_path")
    private String regFileS3Path;

    @CreatedBy
    @Column(updatable = false)
    private Long regId;
    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;

    @LastModifiedBy
    @Column(insertable = false)
    private Long updId;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updDt;

//    public void changeRegFile(S3UploadResponse regFileUploadResult) {
//        this.regFileNm = regFileUploadResult.getOriginFileName();
//        this.regFileS3Path = regFileUploadResult.getImageUrl();
//    }


    public static SysShopInfo of(Integer shopId, BizType businessDiv, String corpNm, String reprNm, String corpRegNo,
                       UseYn regDuplYn, String tel, String fax, String addr1, String addr2, String mgrNm,
                       String mgrCell, String mgrEmail, String billEmail) {
        SysShopInfo sysShopInfo = new SysShopInfo();

        sysShopInfo.shopId = shopId;
        sysShopInfo.businessDiv = businessDiv;
        sysShopInfo.corpNm = corpNm;
        sysShopInfo.reprNm = reprNm;
        sysShopInfo.corpRegNo = corpRegNo;
        sysShopInfo.regDuplYn = regDuplYn;
        sysShopInfo.tel = tel;
        sysShopInfo.fax = fax;
        sysShopInfo.addr1 = addr1;
        sysShopInfo.addr2 = addr2;
        sysShopInfo.mgrNm = mgrNm;
        sysShopInfo.mgrCell = mgrCell;
        sysShopInfo.mgrEmail = mgrEmail;
        sysShopInfo.billEmail = billEmail;

        return sysShopInfo;
    }

    @Builder(access = AccessLevel.PROTECTED)
    public SysShopInfo(Integer shopId, String corpNm, String reprNm, String corpRegNo,
                       UseYn regDuplYn, String tel, String fax, String addr1, String addr2, String mgrNm, String mgrCell,
                       String mgrEmail, String billEmail, String regFileNm, String regFileS3Path) {

        this.shopId = shopId;
        this.corpNm = corpNm;
        this.reprNm = reprNm;
        this.corpRegNo = corpRegNo;
        this.regDuplYn = regDuplYn;
        this.tel = tel;
        this.fax = fax;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.mgrNm = mgrNm;
        this.mgrCell = mgrCell;
        this.mgrEmail = mgrEmail;
        this.billEmail = billEmail;
    }

    public void updateByShopManageInfo(ShopManageDto.Request shopManageRequest) {

        this.businessDiv = shopManageRequest.getBusinessDiv();
        this.corpNm = shopManageRequest.getCorpNm();
        this.reprNm = shopManageRequest.getReprNm();
        this.corpRegNo = shopManageRequest.getCorpRegNo();
        this.regDuplYn = shopManageRequest.getRegDuplYn();
        this.tel = shopManageRequest.getTel();
        this.fax = shopManageRequest.getFax();
        this.addr1 = shopManageRequest.getAddr1();
        this.addr2 = shopManageRequest.getAddr2();
        this.mgrNm = shopManageRequest.getMgrNm();
        this.mgrCell = shopManageRequest.getMgrCell();
        this.mgrEmail = shopManageRequest.getMgrEmail();
        this.billEmail = shopManageRequest.getBillEmail();
        this.regFileNm = shopManageRequest.getRegFileNm();
        this.regFileS3Path = shopManageRequest.getRegFileS3Path();
    }

}
