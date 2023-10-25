package com.example.adminreference.entity;

import com.example.adminreference.common.converter.ServiceStatusConverter;
import com.example.adminreference.dto.ShopManageDto;
import com.example.adminreference.enumeration.ServiceStatus;
import com.example.adminreference.enumeration.TagType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sys_shop_mgt")
public class SysShopMgt implements Serializable {

    //유입경로, 스마트스토어 스토어 명, 루나ID, (법인명, 상호명)<-조회

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shopId;

    private String shopNm;
    private String memberId;
    private String description;
    private String pcUrl;
    private String moUrl;
    @Column(name = "main_img_s3_path", nullable = false)
    private String mainImgS3Path;
    @Column(name = "logo_img_s3_path", nullable = false)
    private String logoImgS3Path;
    @Column(name = "share_img_s3_path", nullable = false)
    private String shareImgS3Path;
    @Convert(converter = ServiceStatusConverter.class)
    @Column(updatable = false, insertable = false)
    private ServiceStatus statusCd;
    private LocalDate svcOpenDt;
    private String tags;

    private Integer shopSort;

    private String smartStoreShopNm;
    private String smartStorePrdBaseUrl;

    @CreatedBy
    @Column(updatable = false)
    private Long regId;
    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;

    @LastModifiedBy
    @Column(insertable = false)
    private Long updId;

    @Column(insertable = false)     // 토탈리스트 관리에서는 변경되서는 안되고 스토어관리에서는 일부데이터만 수정되어도 업데이트 되어야해서 수동으로 관리
    private LocalDateTime updDt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "shopId")
    @ToString.Exclude
    private SysShopInfo sysShopInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shopId")
    private SysShopDtl sysShopDtl;

//    public void changeMainImg(S3UploadResponse mainImgUploadResult) {
//        this.mainImgNm = mainImgUploadResult.getOriginFileName();
//        this.mainImgS3Path = mainImgUploadResult.getImageUrl();
//    }
//
//    public void changeLogoImg(S3UploadResponse logoImgUploadResult) {
//        this.logoImgNm = logoImgUploadResult.getOriginFileName();
//        this.logoImgS3Path = logoImgUploadResult.getImageUrl();
//    }
//
//    public void changeShareImg(S3UploadResponse shareImgUploadResult) {
//        this.shareImgS3Path = shareImgUploadResult.getImageUrl();
//    }

    public void changeShopSort(Integer shopSort) {
        this.shopSort = shopSort;
    }

    public void removeShopSort() {
        this.shopSort = null;
    }

    public void changeUpdDt(LocalDateTime updDt) {
        this.updDt = updDt;
    }

    public static SysShopMgt of(String shopNm, String memberId, String description, String pcUrl, String moUrl,
                                LocalDate svcOpenDt, String tags,
                                String smartStoreShopNm, String smartStorePrdBaseUrl) {
        SysShopMgt sysShopMgt = new SysShopMgt();
        sysShopMgt.statusCd = ServiceStatus.READY;
        sysShopMgt.shopNm = shopNm;
        sysShopMgt.memberId = memberId;
        sysShopMgt.description = description;
        sysShopMgt.pcUrl = pcUrl;
        sysShopMgt.moUrl = moUrl;
        sysShopMgt.svcOpenDt = svcOpenDt;
        sysShopMgt.tags = tags;
        sysShopMgt.smartStoreShopNm = smartStoreShopNm;
        sysShopMgt.smartStorePrdBaseUrl = smartStorePrdBaseUrl;
        sysShopMgt.updDt = LocalDateTime.now();
        return sysShopMgt;
    }

    public void updateByShopManageInfo(ShopManageDto.Request shopManageRequest) {

        this.shopNm = shopManageRequest.getShopNm();
        this.description = shopManageRequest.getDescription();
        this.pcUrl = shopManageRequest.getPcUrl();
        this.moUrl = shopManageRequest.getMoUrl();
        this.tags = convertTags(shopManageRequest.getTags());
        this.smartStoreShopNm = shopManageRequest.getSmartStoreShopNm();
        this.smartStorePrdBaseUrl = shopManageRequest.getSmartStorePrdBaseUrl();
        this.updDt = LocalDateTime.now();
    }

    public static String convertTags(List<TagType> tags) {
        if (tags != null) {
            return tags.stream().map(TagType::getValue).collect(Collectors.joining(","));
        }
        return null;
    }


}
