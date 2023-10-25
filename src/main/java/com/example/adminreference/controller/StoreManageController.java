//package com.example.adminreference.controller;
//
//import com.example.adminreference.annotation.OptionalGrant;
//import com.example.adminreference.config.security.GrantType;
//import com.example.adminreference.service.StoreManageService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.cellook.cellookadmin.common.InsertValidation;
//import kr.co.cellook.cellookadmin.common.PagingUtils;
//import kr.co.cellook.cellookadmin.common.UpdateValidation;
//import kr.co.cellook.cellookadmin.config.annotation.OptionalGrant;
//import kr.co.cellook.cellookadmin.config.security.GrantType;
//import kr.co.cellook.cellookadmin.dto.LunaMallInfo;
//import kr.co.cellook.cellookadmin.dto.ShopInfo;
//import kr.co.cellook.cellookadmin.dto.ShopManageDto;
//import kr.co.cellook.cellookadmin.dto.ShopStatusHistDto;
//import kr.co.cellook.cellookadmin.enumeration.MallSearchType;
//import kr.co.cellook.cellookadmin.service.StoreManageService;
//import kr.co.cellook.cellookadmin.vo.ShopSearch;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springdoc.api.annotations.ParameterObject;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Tag(name = "스토어 관리", description = "스토어 정보 조회 및 등록/수정")
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/shop/manage")
//public class StoreManageController {
//
//    private final StoreManageService storeManageService;
//
////    @Operation(summary = "사업자 등록 번호 중복 체크")
////    @RequestMapping(value = "corp-reg-no-dupl", method = RequestMethod.GET)
////    public ResponseEntity<Map<String, Boolean>> regNoDuplicateCheck(
////            @RequestParam(name = "shop_id", required = false) Integer shopId,
////            @RequestParam(name = "corp_reg_no") String corpRegNo
////    ) {
////        return ResponseEntity.ok(storeManageService.corpRegNoIsDuplicate(shopId, corpRegNo));
////    }
//
////    @Operation(summary = "스토어 리스트 엑셀 다운로드")
////    @OptionalGrant(requiredGrant = GrantType.EXCEL)
////    @RequestMapping(value = "/list-excel", method = RequestMethod.GET)
////    public void shopListExcelDown(
////            HttpServletResponse response,
////            @ParameterObject @ModelAttribute ShopSearch shopSearch
////    ) throws IOException {
////
////        String fileName = "SHOP_INFO_LIST_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
////
////        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
////        response.setContentType("ms-vnd/excel");
////        try (Workbook workbook = storeManageService.shopListExcelDown(shopSearch)) {
////            workbook.write(response.getOutputStream());
////        }
////    }
////
////    @Operation(summary = "루나 몰정보 조회")
////    @RequestMapping(value = "/mall-info", method = RequestMethod.GET)
////    public ResponseEntity<List<LunaMallInfo>> getLunaMallInfo(
////            @RequestParam(name = "searchType") MallSearchType searchType,
////            @RequestParam(name = "searchText") String searchText
////    ) {
////        return ResponseEntity.ok().body(storeManageService.getShopInfo(searchType, searchText));
////    }
////
////    @Operation(summary = "상태변경 사용이력")
////    @RequestMapping(value = "/status-history", method = RequestMethod.GET)
////    public ResponseEntity<List<ShopStatusHistDto>> getShopStatusHistory(
////            @RequestParam(name = "shopId") int shopId
////    ) {
////        return ResponseEntity.ok().body(storeManageService.getShopStatusHistory(shopId));
////    }
////
////    @Operation(summary = "스토어 관리 리스트")
////    @RequestMapping(value = "/list", method = RequestMethod.GET)
////    public ResponseEntity<List<ShopInfo>> getStoreList(
////            @ParameterObject @ModelAttribute ShopSearch shopSearch
////    ) {
////        log.info("{}", shopSearch);
////        Page<ShopInfo> pagingShopInfo = storeManageService.getShopList(shopSearch);
////        HttpHeaders httpHeaders = PagingUtils.getListHeader(pagingShopInfo);
////        return ResponseEntity.ok()
////                .headers(httpHeaders)
////                .body(pagingShopInfo.getContent());
////    }
////
////    @Operation(summary = "스토어 관리 상세")
////    @RequestMapping(value = "/info/{shopId}", method = RequestMethod.GET)
////    public ResponseEntity<ShopManageDto.Response> getShopManageInfo(
////            @PathVariable(name = "shopId") int shopId
////    ) {
////        return ResponseEntity.ok().body(storeManageService.getShopManageInfo(shopId));
////    }
////
////    @Operation(summary = "스토어 정보 생성")
////    @RequestMapping(value = "/info", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<ShopManageDto.Response> addStoreInfo(
////            @Validated(InsertValidation.class) ShopManageDto.Request shopManageRequest
////    ) {
////
////        ShopManageDto.Response shopManageResponse = storeManageService.insertShopInfo(shopManageRequest);
////        return ResponseEntity.status(HttpStatus.CREATED)
////                .header("X-Cellook-Affected-Row-Count", "1")
////                .body(shopManageResponse);
////    }
////
////    @Operation(summary = "스토어 정보 수정")
////    @RequestMapping(value = "/info/{shopId}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<ShopManageDto.Response> modifyStoreInfo(
////            @PathVariable(name = "shopId") int shopId,
////            @Validated(UpdateValidation.class) ShopManageDto.Request shopManageRequest
////    ) {
////        ShopManageDto.Response shopManageResponse = storeManageService.updateShopInfo(shopId, shopManageRequest);
////        return ResponseEntity.status(HttpStatus.CREATED)
////                .header("X-Cellook-Affected-Row-Count", "1")
////                .body(shopManageResponse);
////    }
//}
