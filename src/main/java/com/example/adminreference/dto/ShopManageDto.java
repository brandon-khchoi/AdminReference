package com.example.adminreference.dto;

import com.example.adminreference.annotation.MultipartFileValid;
import com.example.adminreference.common.InsertValidation;
import com.example.adminreference.common.UpdateValidation;
import com.example.adminreference.entity.SysShopInfo;
import com.example.adminreference.entity.SysShopMgt;
import com.example.adminreference.enumeration.BizType;
import com.example.adminreference.enumeration.ServiceStatus;
import com.example.adminreference.enumeration.TagType;
import com.example.adminreference.enumeration.UseYn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
public class ShopManageDto {

    @Setter
    @Getter
    public static class Request {

        @NotBlank
        @Schema(description = "스토어 명")
        private String shopNm;

        @NotBlank
        @Schema(description = "회원 아이디")
        private String memberId;

        @NotBlank
        @Schema(description = "스토어 소개")
        private String description;

        @NotBlank
        @Schema(description = "스마트스토어 PC URL")
        private String pcUrl;

        @NotBlank
        @Schema(description = "스마트스토어 MOBILE URL")
        private String moUrl;

        @MultipartFileValid(name = "카카오톡 공유하기 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg"}, groups = InsertValidation.class, nullable = false)
        @MultipartFileValid(name = "카카오톡 공유하기 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg"}, groups = UpdateValidation.class)
        @Schema(description = "카카오톡 공유하기 이미지")
        private MultipartFile shareImg;

        @MultipartFileValid(name = "스토어홈 대표 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg", "webp"}, groups = InsertValidation.class, nullable = false)
        @MultipartFileValid(name = "스토어홈 대표 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg", "webp"}, groups = UpdateValidation.class)
        @Schema(description = "스토어홈 대표 이미지")
        private MultipartFile mainImg;

        @MultipartFileValid(name = "로고 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg", "webp"}, groups = InsertValidation.class, nullable = false)
        @MultipartFileValid(name = "로고 이미지", limitSize = 10, dataSizeType = MultipartFileValid.DataSizeType.MB, allowFileExtensions = {"png", "jpg", "jpeg", "webp"}, groups = UpdateValidation.class)
        @Schema(description = "로고 이미지")
        private MultipartFile logoImg;

        @NotBlank
        @Schema(description = "루나 Mall ID")
        private String lunaId;

        @Schema(implementation = Integer.class, description = "서비스 사용상태 코드")
        private ServiceStatus changeStatusCd;

        @Schema(description = "서비스 사용상태 적용일")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate changeStatusDate;

        @Schema(description = "오픈일")
        private LocalDate svcOpenDt;

        @NotEmpty
        @Schema(description = "추천 그룹명")
        private List<TagType> tags;

        @Schema(description = "스마트스토어 스토어명")
        private String smartStoreShopNm;

        @NotBlank
        @Schema(description = "스마트스토어 상품상세 Base URL")
        private String smartStorePrdBaseUrl;

        @NotNull
        @Schema(description = "사업 구분 (법인 : C, 개인 : P)")
        private BizType businessDiv;

        @Schema(description = "법인명")
        private String corpNm;

        @Schema(description = "대표자 성명")
        private String reprNm;

        @Schema(description = "사업자 등록번호")
        private String corpRegNo;

        @Schema(defaultValue = "N", description = "사업자 등록번호 중복여부")
        private UseYn regDuplYn = UseYn.N;

        @Schema(description = "대표 번호")
        private String tel;

        @Schema(description = "팩스 번호")
        private String fax;

        @Schema(description = "사업자 주소")
        private String addr1;

        @Schema(description = "사업자 상세주소")
        private String addr2;

        @Schema(description = "담당자 명")
        private String mgrNm;

        @Schema(description = "담당자 연락처")
        private String mgrCell;

        @Schema(description = "담당자 이메일")
        private String mgrEmail;

        @Schema(description = "세금계산서 발행 이메일")
        private String billEmail;

        @Schema(description = "사업자 등록증 이미지 파일")
        private MultipartFile regImg;

        @Schema(description = "사업자 등록증 파일 명")
        private String regFileNm;

        @Schema(description = "사업자 등록증 URL")
        private String regFileS3Path;

        public void setChangeStatusCd(Integer changeStatusCd) {
            if (changeStatusCd != null) {
                this.changeStatusCd = Arrays.stream(ServiceStatus.values())
                        .filter(e -> e.getCode() == changeStatusCd)
                        .findAny()
                        .orElseThrow(() -> new EnumConstantNotPresentException(ServiceStatus.class, String.valueOf(changeStatusCd)));
            }
        }

        @JsonIgnore
        public SysShopInfo toSysShopInfo(int shopId) {
            return SysShopInfo.of(shopId, businessDiv,  corpNm, reprNm, corpRegNo,
                    regDuplYn, tel, fax, addr1, addr2, mgrNm, mgrCell, mgrEmail, billEmail);
        }

        @JsonIgnore
        public SysShopMgt toSysShopMgt() {
            return SysShopMgt.of(shopNm, memberId, description, pcUrl, moUrl,
                    svcOpenDt,
                    SysShopMgt.convertTags(tags),
                    smartStoreShopNm, smartStorePrdBaseUrl);
        }
    }

    @Getter
    @Setter
    public static class Response {

        @Schema(description = "스토어 명")
        private String shopNm;
        @Schema(description = "스토어 아이디")
        private Integer shopId;
        @Schema(description = "스토어 소개")
        private String description;
        @Schema(description = "스마트스토어 PC URL")
        private String pcUrl;
        @Schema(description = "스마트스토어 MOBILE URL")
        private String moUrl;
//        @Schema(description = "메인 이미지 파일명")
//        private String mainImgNm;
        @Schema(description = "메인 이미지 URL")
        private String mainImgS3Path;
//        @Schema(description = "로고 이미지 파일명")
//        private String logoImgNm;
        @Schema(description = "로고 이미지 URL")
        private String logoImgS3Path;
        @Schema(description = "카카오톡 공유하기 URL")
        private String shareImgS3Path;

        @Schema(description = "현재 서비스 사용상태")
        private Integer statusCd;
        @Schema(description = "현재 서비스 사용상태 Description")
        private String statusCdDesc;

        @Schema(description = "오픈일")
        private LocalDate svcOpenDt;
        @Schema(description = "태그 리스트")
        private List<TagType> tags;

        @Schema(description = "스마트스토어 스토어명")
        private String smartStoreShopNm;
        @Schema(description = "스마트스토어 상품상세 Base URL")
        private String smartStorePrdBaseUrl;

        @Schema(description = "사업 구분 (법인 : C, 개인 : P)")
        private BizType businessDiv;
        @Schema(description = "법인명")
        private String corpNm;
        @Schema(description = "대표자 성명")
        private String reprNm;
        @Schema(description = "사업자 등록번호")
        private String corpRegNo;
        @Schema(defaultValue = "N", description = "사업자 등록번호 중복여부")
        private UseYn regDuplYn;
        @Schema(description = "대표 번호")
        private String tel;
        @Schema(description = "팩스 번호")
        private String fax;
        @Schema(description = "사업자 주소")
        private String addr1;
        @Schema(description = "사업자 상세주소")
        private String addr2;
        @Schema(description = "담당자 명")
        private String mgrNm;
        @Schema(description = "담당자 연락처")
        private String mgrCell;
        @Schema(description = "담당자 이메일")
        private String mgrEmail;
        @Schema(description = "세금계산서 발행 이메일")
        private String billEmail;
        @Schema(description = "사업자 등록증 파일 명")
        private String regFileNm;
        @Schema(description = "사업자 등록증 URL")
        private String regFileS3Path;


        public Response(SysShopMgt sysShopMgt, SysShopInfo sysShopInfo) {
            this.shopId = sysShopMgt.getShopId();
            this.shopNm = sysShopMgt.getShopNm();
            this.description = sysShopMgt.getDescription();
            this.pcUrl = sysShopMgt.getPcUrl();
            this.moUrl = sysShopMgt.getMoUrl();
            this.mainImgS3Path = sysShopMgt.getMainImgS3Path();
            this.logoImgS3Path = sysShopMgt.getLogoImgS3Path();

            this.shareImgS3Path = sysShopMgt.getShareImgS3Path();

            this.statusCd = sysShopMgt.getStatusCd().getCode();
            this.statusCdDesc = sysShopMgt.getStatusCd().getDesc();

            this.svcOpenDt = sysShopMgt.getSvcOpenDt();

            this.tags = new ArrayList<>();
            if (sysShopMgt.getTags() != null) {
                for (String tag : sysShopMgt.getTags().split(",")) {
                    Arrays.stream(TagType.values())
                            .filter(e -> e.getValue().equals(tag))
                            .findAny()
                            .ifPresent(item -> this.tags.add(item));
                }
            }
            this.smartStoreShopNm = sysShopMgt.getSmartStoreShopNm();
            this.smartStorePrdBaseUrl = sysShopMgt.getSmartStorePrdBaseUrl();

            this.businessDiv = sysShopInfo.getBusinessDiv();
            this.corpNm = sysShopInfo.getCorpNm();
            this.reprNm = sysShopInfo.getReprNm();
            this.corpRegNo = sysShopInfo.getCorpRegNo();
            this.regDuplYn = sysShopInfo.getRegDuplYn();
            this.tel = sysShopInfo.getTel();
            this.fax = sysShopInfo.getFax();
            this.addr1 = sysShopInfo.getAddr1();
            this.addr2 = sysShopInfo.getAddr2();
            this.mgrNm = sysShopInfo.getMgrNm();
            this.mgrCell = sysShopInfo.getMgrCell();
            this.mgrEmail = sysShopInfo.getMgrEmail();
            this.billEmail = sysShopInfo.getBillEmail();
            this.regFileNm = sysShopInfo.getRegFileNm();
            this.regFileS3Path = sysShopInfo.getRegFileS3Path();
        }
    }

}
