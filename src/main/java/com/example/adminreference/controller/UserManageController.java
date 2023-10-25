package com.example.adminreference.controller;

import com.example.adminreference.common.PagingUtils;
import com.example.adminreference.dto.UserInfoDto;
import com.example.adminreference.enumeration.UserSearchType;
import com.example.adminreference.service.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/manage")
public class UserManageController {

    private final UserManageService userManageService;

    @Operation(summary = "사용자 목록 검색 : searchType -> ID or NAME")
    @RequestMapping(value = "user-list", method = RequestMethod.GET)
    public ResponseEntity<List<UserInfoDto.Response>> getAdminUserList(
            @RequestParam(name = "searchType", required = false) UserSearchType searchType,
            @RequestParam(name = "searchText", required = false) String searchText,
            @RequestParam(name = "authGroupId", required = false) Long authGroupId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @Max(100) @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        PageImpl<UserInfoDto.Response> pagingData = userManageService.getAllUserList(searchType, searchText, authGroupId, page, pageSize);
        HttpHeaders httpHeaders = PagingUtils.getListHeader(pagingData);
        return ResponseEntity.ok().headers(httpHeaders).body(pagingData.getContent());
    }

    @Operation(summary = "계정별 정보 조회")
    @RequestMapping(value = "/user-info/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserInfoDto.Response> getUserInfo(
            @PathVariable long id
    ) {
        return ResponseEntity.ok(userManageService.getMenuList(id));
    }

    @PostMapping("/user-info")
    public ResponseEntity<Void> userRegister(
            @RequestBody UserInfoDto.Request request
    ) {
        userManageService.userRegister(request);
        return ResponseEntity.status(CREATED)
                .header("X-Admin-Affected-Row-Count", "1")
                .build();
    }

    @PutMapping("/user-info/{id}")
    public ResponseEntity<UserInfoDto.Response> userModify(
            @PathVariable long id,
            @RequestBody UserInfoDto.Request request
    ) {
        return ResponseEntity.status(CREATED)
                .header("X-Admin-Affected-Row-Count", "1")
                .body(userManageService.userModify(id, request));
    }

}
