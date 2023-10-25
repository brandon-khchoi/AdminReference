package com.example.adminreference.controller;

import com.example.adminreference.common.PagingUtils;
import com.example.adminreference.dto.*;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.UserSearchType;
import com.example.adminreference.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Tag(name = "권한관리 API", description = "사용자 -> 권한관리 메뉴 관련 API")
@RequestMapping("/admin/auth")
public class UserAuthController {

    private final UserAuthService userManageService;

    @Operation(summary = "권한 그룹 미지정 유저 목록")
    @RequestMapping(value = "/non-auth-group-user", method = RequestMethod.GET)
    public ResponseEntity<List<UserInfoDto>> getNonAuthGroupUserList(
            @RequestParam(name = "searchType", required = false) UserSearchType searchType,
            @RequestParam(name = "searchText", required = false) String searchText
    ) {
        return ResponseEntity.ok().body(userManageService.getNonAuthGroupingUser(searchType, searchText));
    }

    @Operation(summary = "권한그룹 리스트 조회")
    @RequestMapping(value = "/auth-group-list", method = RequestMethod.GET)
    public ResponseEntity<List<AuthGroupInfoDto>> getAuthGroupList(
            @RequestParam(name = "authGroupId", required = false) Long authGroupId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @Max(100) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        PageImpl<AuthGroupInfoDto> pagingData = userManageService.getAuthGroupList(authGroupId, page, pageSize);
        HttpHeaders httpHeaders = PagingUtils.getListHeader(pagingData);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(pagingData.getContent());
    }

    @Operation(summary = "권한 그룹 관리 조회")
    @RequestMapping(value = "/auth-group/{authGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthGroupManageDto.Response> getAuthGroupManageInfo(
            @PathVariable(name = "authGroupId") Long authGroupId
    ) {
        return ResponseEntity.ok(userManageService.getAuthGroupManageInfo(authGroupId));
    }

    @Operation(summary = "권한 그룹별 권한 등록(POST)")
    @RequestMapping(value = "/auth-group", method = RequestMethod.POST)
    public ResponseEntity<Void> registerAuthGroup(
            @RequestBody AuthGroupManageDto.Request authGroupManageRequest
    ) {

        int affectedRowCount = 1;
        long authGroupId = userManageService.createAuthGroup(authGroupManageRequest.getAuthGroupName());
        List<TbUserInfo> userInfoList = userManageService.modifyAuthGroupUser(authGroupId, authGroupManageRequest.getIdList());
        affectedRowCount += userManageService.updateAuthInfo(authGroupId, authGroupManageRequest.getMenuAuthInfoDtoList());

        return ResponseEntity.status(HttpStatus.CREATED).header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount)).build();
    }

    @Operation(summary = "권한 그룹별 권한 수정(PUT)")
    @RequestMapping(value = "/auth-group/{authGroupId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAuthGroup(
            @PathVariable(name = "authGroupId") long authGroupId,
            @RequestBody AuthGroupManageDto.Request authGroupManageRequest
    ) {
        int affectedRowCount = 1;
        userManageService.modifyAuthGroup(authGroupId, authGroupManageRequest.getAuthGroupName());
        List<TbUserInfo> userInfoList = userManageService.modifyAuthGroupUser(authGroupId, authGroupManageRequest.getIdList());
        affectedRowCount += userManageService.updateAuthInfo(authGroupId, authGroupManageRequest.getMenuAuthInfoDtoList());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount)).build();
    }

    @Operation(summary = "권한 그룹 삭제")
    @RequestMapping(value = "/auth-group/{authGroupId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAuthGroup(
            @PathVariable(name = "authGroupId") long authGroupId
    ) {
        int affectedRowCount = userManageService.deleteAuthGroup(authGroupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount))
                .build();
    }

//    @Operation(summary = "권한 그룹별 권한 등록(POST)/수정(PUT)")
//    @RequestMapping(value = "/auth-group-info/{authGroupId}", method = {RequestMethod.PUT, RequestMethod.POST})
//    public ResponseEntity<Void> updateAuthInfo(
//            @PathVariable(name = "authGroupId") long authGroupId,
//            @RequestBody List<MenuAuthInfoDto> menuAuthInfoDtoList
//    ) {
//        return userManageService.updateAuthInfo(authGroupId, menuAuthInfoDtoList);
//    }
//
//    @Operation(summary = "그룹별 권한 리스트 조회")
//    @RequestMapping(value = "/auth-list/{authGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<MenuAuthInfoDto>> getAuthListByAuthGroupId(
//            @PathVariable(name = "authGroupId") Long authGroupId
//    ) {
//        return ResponseEntity.ok(userManageService.getUserMenuAuthInfoByAuthGroupId(authGroupId));
//    }
//
//    @Operation(summary = "권한그룹별 유저 리스트 조회")
//    @RequestMapping(value = "/user-list/{authGroupId}", method = RequestMethod.GET)
//    public ResponseEntity<List<HashMap<String, Object>>> userListByAuthGroupId(
//            @PathVariable(name = "authGroupId") long authGroupId
//    ) {
//        return ResponseEntity.ok().body(userManageService.getUserListByAuthGroupId(authGroupId));
//    }
//
//    @Operation(summary = "권한 그룹별 사용자 삭제", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"luna_user_no_list\":[\"\",\"\"]}"))))
//    @RequestMapping(value = "/auth-group-user/{authGroupId}", method = RequestMethod.DELETE)
//    public ResponseEntity<Void> deleteAuthGroupUser(
//            @PathVariable(name = "authGroupId") long authGroupId,
//            @RequestBody HashMap<String, List<Long>> reqMap
//    ) {
//        List<Long> userNoList = reqMap.get("luna_user_no_list");
//        int affectedRowCount = userManageService.deleteAuthGroupUser(authGroupId, userNoList);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount))
//                .build();
//    }
//
//    @Operation(summary = "권한 그룹별 사용자 변경", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"luna_user_no_list\": []}"))))
//    @RequestMapping(value = "/auth-group-user/{authGroupId}", method = RequestMethod.POST)
//    public ResponseEntity<Void> modifyAuthGroupUser(
//            @PathVariable(name = "authGroupId") long authGroupId,
//            @RequestBody HashMap<String, List<Long>> reqMap
//    ) {
//        List<Long> lunaUserNoList = reqMap.get("luna_user_no_list");
//        int affectedRowCount = userManageService.modifyAuthGroupUser(authGroupId, lunaUserNoList);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount))
//                .build();
//    }
//
//
//    @Operation(summary = "권한 그룹 추가", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"auth_group_name\": \"\"}"))))
//    @RequestMapping(value = "/auth-group", method = RequestMethod.POST)
//    public ResponseEntity<AuthGroupDto> addAuthGroup(
//            @RequestBody AuthGroupDto authGroupDto
//    ) {
//        AuthGroupDto responseAuthGroupDto = userManageService.addAuthGroup(authGroupDto.getAuthGroupName());
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Admin-Affected-Row-Count", "1")
//                .body(responseAuthGroupDto);
//    }
//
//    @Operation(summary = "권한 그룹 수정", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"auth_group_name\": \"\"}"))))
//    @RequestMapping(value = "/auth-group/{authGroupId}", method = RequestMethod.PUT)
//    public ResponseEntity<Void> modifyAuthGroup(
//            @PathVariable(name = "authGroupId") long authGroupId,
//            @RequestBody AuthGroupDto authGroupDto
//    ) {
//
//        int affectedRowCount = userManageService.modifyAuthGroup(authGroupId, authGroupDto.getAuthGroupName());
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount))
//                .build();
//    }


}
