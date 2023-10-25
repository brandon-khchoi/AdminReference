//package com.example.adminreference.service;
//
//import com.example.adminreference.dto.ShopManageDto;
//import com.example.adminreference.repository.ShopDtlRepository;
//import com.example.adminreference.repository.ShopInfoRepository;
//import com.example.adminreference.repository.ShopManageRepository;
//import com.example.adminreference.repository.ShopStatusHistoryRepository;
//import com.example.adminreference.repository.querydsl.StoreManageCustomRepository;
//import kr.co.cellook.cellookadmin.common.GsonUtils;
//import kr.co.cellook.cellookadmin.config.exception.CustomException;
//import kr.co.cellook.cellookadmin.config.exception.CustomExceptionCode;
//import kr.co.cellook.cellookadmin.dto.ShopInfo;
//import kr.co.cellook.cellookadmin.dto.ShopStatusHistDto;
//import kr.co.cellook.cellookadmin.dto.elastic.CellookAdminLog;
//import kr.co.cellook.cellookadmin.entity.SysShopDtl;
//import kr.co.cellook.cellookadmin.entity.SysShopInfo;
//import kr.co.cellook.cellookadmin.entity.SysShopMgt;
//import kr.co.cellook.cellookadmin.entity.SysShopStatusHist;
//import kr.co.cellook.cellookadmin.repository.*;
//import kr.co.cellook.cellookadmin.vo.ErrorResponse;
//import kr.co.cellook.cellookadmin.vo.S3UploadResponse;
//import kr.co.cellook.cellookadmin.vo.ShopSearch;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class StoreManageService {
//
//    private final StoreManageCustomRepository storeManageCustomRepository;
//
//    private final ShopManageRepository shopManageRepository;
//
//    private final ShopInfoRepository shopInfoRepository;
//
//    private final ShopDtlRepository shopDtlRepository;
//
//    private final ShopStatusHistoryRepository shopStatusHistoryRepository;
//
////    private final FileUploadService fileUploadService;
//
//    private final static String SHOP_INFO_S3_SUB_PATH = "/images/shopInfo";
//
////    private final ElasticLoggingUtil elasticLoggingUtil;
//
//
//    @Transactional(readOnly = true)
//    public Map<String, Boolean> corpRegNoIsDuplicate(Integer shopId, String corpRegNo) {
//        boolean isDuplicate;
//        if (shopId != null) {
//            isDuplicate = shopInfoRepository.existsByShopIdNotAndCorpRegNo(shopId, corpRegNo);
//        } else {
//            isDuplicate = shopInfoRepository.existsByCorpRegNo(corpRegNo);
//        }
//        HashMap<String, Boolean> resultMap = new HashMap<>();
//        resultMap.put("is_duplicate", isDuplicate);
//
//        return resultMap;
//    }
//
////    @Transactional(readOnly = true)
////    public Workbook shopListExcelDown(ShopSearch shopSearch) {
////
////        List<ExcelCellInfo> excelCellInfoList = new ArrayList<>() {{
////            add(ExcelCellInfo.builder().cellName("스코어코드").cellKey("shopId").cellWidth(13).build());
////            add(ExcelCellInfo.builder().cellName("스토어명").cellKey("shopNm").cellWidth(15).build());
////            add(ExcelCellInfo.builder().cellName("유입경로").cellKey("channelDivDesc").cellWidth(13).build());
////            add(ExcelCellInfo.builder().cellName("mall_id").cellKey("lunaId").cellWidth(17).build());
////            add(ExcelCellInfo.builder().cellName("판매상품 수").cellKey("productCnt").cellWidth(13).build());
////            add(ExcelCellInfo.builder().cellName("구독 수").cellKey("subsCnt").cellWidth(13).build());
////            add(ExcelCellInfo.builder().cellName("서비스 사용 상태").cellKey("statusCdDesc").cellWidth(18).build());
////            add(ExcelCellInfo.builder().cellName("입점일").cellKey("regDt").cellWidth(20).isDateTime(true).build());
////            add(ExcelCellInfo.builder().cellName("오픈일").cellKey("svcOpenDt").cellWidth(15).isDate(true).build());
////            add(ExcelCellInfo.builder().cellName("최근 상태변경 일").cellKey("latestStatusApplyDt").cellWidth(20).isDate(true).build());
////        }};
////
////        List<ShopInfo> shopInfos = storeManageCustomRepository.selectAllShopList(shopSearch);
////
////        ExcelUtils excelUtils = new ExcelUtils();
////        return excelUtils.makeWorkbook("스토어 리스트", excelCellInfoList, shopInfos);
////    }
//
////    public List<LunaMallInfo> getShopInfo(MallSearchType searchType, String searchText) {
////        String url = "https://ladminapi.lunasoft.co.kr/mallInfoApi/searchMallInfo?searchType=" + searchType.name() + "&searchText=" + searchText;
////        ParameterizedTypeReference<List<LunaMallInfo>> listParameterizedTypeReference = new ParameterizedTypeReference<>() {
////        };
////        ResponseEntity<List<LunaMallInfo>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, listParameterizedTypeReference);
////
////        return responseEntity.getBody();
////    }
//
//    @Transactional(readOnly = true)
//    public List<ShopStatusHistDto> getShopStatusHistory(int shopId) {
//        return storeManageCustomRepository.selectShopStatusHist(shopId);
//    }
//
//    @Transactional
//    public ShopManageDto.Response updateShopInfo(int shopId, ShopManageDto.Request shopManageRequest) {
//
//        log.info("update SysShopInfo : {}", shopManageRequest);
//
//        if (Strings.isNotBlank(shopManageRequest.getCorpRegNo()) && !shopManageRequest.getRegDuplYn().isUsable() && shopInfoRepository.existsByShopIdNotAndCorpRegNo(shopId, shopManageRequest.getCorpRegNo())) {
//            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
//            errorResponse.setMessage("중복된 사업자번호 입니다.");
//            throw new CustomException(errorResponse);
//        }
//        // lunaId 중복체크
//        if (shopManageRepository.existsByShopIdNotAndLunaId(shopId, shopManageRequest.getLunaId())) {
//            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
//            errorResponse.setMessage("중복된 mall id 입니다. 다시 확인해주세요.");
//            throw new CustomException(errorResponse);
//        }
//
//        SysShopMgt sysShopMgt = shopManageRepository.findById(shopId)
//                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));
//
//        // SysShopMgt update
//        sysShopMgt.updateByShopManageInfo(shopManageRequest);
//
//        String yyyyMMddHHmmss = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//
//        if (shopManageRequest.getMainImg() != null && !shopManageRequest.getMainImg().isEmpty()) {
//            S3UploadResponse mainImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getMainImg());
//            sysShopMgt.changeMainImg(mainImgUploadResult);
//        }
//
//        if (shopManageRequest.getLogoImg() != null && !shopManageRequest.getLogoImg().isEmpty()) {
//            S3UploadResponse logoImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getLogoImg());
//            sysShopMgt.changeLogoImg(logoImgUploadResult);
//        }
//
//        if (shopManageRequest.getShareImg() != null && !shopManageRequest.getShareImg().isEmpty()) {
//            S3UploadResponse shareImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getShareImg());
//            sysShopMgt.changeShareImg(shareImgUploadResult);
//        }
//
//        SysShopInfo sysShopInfo = shopInfoRepository.findById(shopId)
//                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));
//
//        // SysShopInfo update
//        sysShopInfo.updateByShopManageInfo(shopManageRequest);
//
//        //사업자 등록증 업로드
//        if (shopManageRequest.getRegImg() != null && !shopManageRequest.getRegImg().isEmpty()) {
//            S3UploadResponse regFileUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + sysShopMgt.getShopId(), shopManageRequest.getRegImg());
//            sysShopInfo.changeRegFile(regFileUploadResult);
//        }
//
//        insertShopStatusHistory(shopId, shopManageRequest);
//
//        ShopManageDto.Response shopManageResponse = new ShopManageDto.Response(sysShopMgt, sysShopInfo);
//
//        // 엘라스틱 로그 저장
//        elasticLoggingUtil.addCellookAdminLog(shopId, CellookAdminLog.ServiceType.STORE_MANAGE, GsonUtils.toJson(shopManageResponse));
//
//        return shopManageResponse;
//    }
//
//    @Transactional
//    public ShopManageDto.Response insertShopInfo(ShopManageDto.Request shopManageRequest) {
//
//        log.info("insert SysShopInfo : {}", shopManageRequest);
//
//        if (Strings.isNotBlank(shopManageRequest.getCorpRegNo()) && !shopManageRequest.getRegDuplYn().isUsable() && shopInfoRepository.existsByCorpRegNo(shopManageRequest.getCorpRegNo())) {
//            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
//            errorResponse.setMessage("중복된 사업자번호 입니다.");
//            throw new CustomException(errorResponse);
//        }
//        // lunaId 중복체크
//        if (shopManageRepository.existsByLunaId(shopManageRequest.getLunaId())) {
//            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
//            errorResponse.setMessage("중복된 mall id 입니다. 다시 확인해주세요.");
//            throw new CustomException(errorResponse);
//        }
//
//        if (shopManageRequest.getMainImg() == null || shopManageRequest.getMainImg().isEmpty()) {
//            ErrorResponse errorResponse = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
//            errorResponse.setMessage("메인 이미지는 필수 값 입니다.");
//            throw new CustomException(errorResponse);
//        } else if (shopManageRequest.getLogoImg() == null || shopManageRequest.getLogoImg().isEmpty()) {
//            ErrorResponse errorResponse = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
//            errorResponse.setMessage("로고 이미지는 필수 값 입니다.");
//            throw new CustomException(errorResponse);
//        } else if (shopManageRequest.getShareImg() == null || shopManageRequest.getShareImg().isEmpty()) {
//            ErrorResponse errorResponse = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
//            errorResponse.setMessage("공유 이미지는 필수 값 입니다.");
//            throw new CustomException(errorResponse);
//        }
//
//        SysShopMgt sysShopMgt = shopManageRequest.toSysShopMgt();
//        sysShopMgt = shopManageRepository.save(sysShopMgt); //shopId 알아내기 위해 먼저 insert
//
//        int shopId = sysShopMgt.getShopId();
//
//        S3UploadResponse mainImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getMainImg());
//        S3UploadResponse logoImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getLogoImg());
//        S3UploadResponse shareImgUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getShareImg());
//
//        sysShopMgt.changeMainImg(mainImgUploadResult);
//        sysShopMgt.changeLogoImg(logoImgUploadResult);
//        sysShopMgt.changeShareImg(shareImgUploadResult);
//
//        SysShopInfo sysShopInfo = shopManageRequest.toSysShopInfo(shopId);
//
//        //사업자 등록증 업로드
//        if (shopManageRequest.getRegImg() != null && !shopManageRequest.getRegImg().isEmpty()) {
//            S3UploadResponse regFileUploadResult = fileUploadService.uploadFile(SHOP_INFO_S3_SUB_PATH + "/" + shopId, shopManageRequest.getRegImg());
//            sysShopInfo.changeRegFile(regFileUploadResult);
//        }
//
//        sysShopInfo = shopInfoRepository.save(sysShopInfo);
//
//        shopDtlRepository.save(SysShopDtl.of(shopId));
//
//        insertShopStatusHistory(shopId, shopManageRequest);
//
//        ShopManageDto.Response shopManageResponse = new ShopManageDto.Response(sysShopMgt, sysShopInfo);
//
//        // 엘라스틱 로그 저장
//        elasticLoggingUtil.addCellookAdminLog(shopId, CellookAdminLog.ServiceType.STORE_MANAGE, GsonUtils.toJson(shopManageResponse));
//
//        return shopManageResponse;
//    }
//
//    @Transactional
//    public void insertShopStatusHistory(int shopId, ShopManageDto.Request shopManageRequest) {
//        if (shopManageRequest.getChangeStatusCd() != null && shopManageRequest.getChangeStatusDate() != null) {
//            SysShopStatusHist sysShopStatusHist = SysShopStatusHist.newInstance(shopId, shopManageRequest.getChangeStatusCd(), shopManageRequest.getSvcOpenDt());
//            log.info("insert ShopStatusHistory : {}", sysShopStatusHist);
//            shopStatusHistoryRepository.save(sysShopStatusHist);
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public Page<ShopInfo> getShopList(ShopSearch shopSearch) {
//        return storeManageCustomRepository.selectShopList(shopSearch);
//    }
//
//    @Transactional(readOnly = true)
//    public ShopManageDto.Response getShopManageInfo(int shopId) {
//
//        return storeManageCustomRepository.selectShopManageInfo(shopId);
//    }
//
//}
