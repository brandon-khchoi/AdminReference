package com.example.adminreference.service;

import com.example.adminreference.common.exception.CustomException;
import com.example.adminreference.common.exception.CustomExceptionCode;
import com.example.adminreference.config.security.GrantType;
import com.example.adminreference.dto.*;
import com.example.adminreference.entity.TbAuthGroup;
import com.example.adminreference.entity.TbAuthPermission;
import com.example.adminreference.entity.TbMenuInfo;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.UserSearchType;
import com.example.adminreference.repository.AuthGroupRepository;
import com.example.adminreference.repository.AuthPermissionRepository;
import com.example.adminreference.repository.MenuInfoRepository;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.repository.querydsl.UserMenuInfoCustomRepository;
import com.example.adminreference.vo.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserAuthService {

    private final MenuInfoRepository menuInfoRepository;

    private final UserMenuInfoCustomRepository userMenuInfoCustomRepository;
    //
    private final AuthPermissionRepository authPermissionRepository;
    //
    private final AuthGroupRepository authGroupRepository;
    //
    private final UserInfoRepository userInfoRepository;

//    private final ElasticLoggingUtil elasticLoggingUtil;

    public List<UserInfoDto> getNonAuthGroupingUser(UserSearchType searchType, String searchText) {
        return userMenuInfoCustomRepository.selectNonAuthGroupingUser(searchType, searchText);
    }

    @Transactional
    public Long createAuthGroup(String authGroupName) {
        if (authGroupRepository.existsByAuthGroupName(authGroupName)) {
            throw new CustomException(CustomExceptionCode.INVALID_RESOURCE_DUPLICATE);
        } else {
            TbAuthGroup tbAuthGroup = authGroupRepository.save(TbAuthGroup.newInstance(authGroupName));
            return tbAuthGroup.getAuthGroupId();
        }
    }


    @Transactional
    public List<TbUserInfo> modifyAuthGroupUser(Long authGroupId, List<Long> lunaUserNoList) {

        int affectedRowCount = 0;
        TbAuthGroup tbAuthGroup = authGroupRepository.findById(authGroupId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));

//            // 현재 그룹 유저
        List<TbUserInfo> userInfoList = userInfoRepository.findByAuthGroupId(authGroupId);
        // 요청 유저
        List<TbUserInfo> newUserInfoList = userInfoRepository.findAllById(lunaUserNoList);

        for (TbUserInfo newUserInfo : newUserInfoList) {
            newUserInfo.changeAuthGroup(tbAuthGroup);
        }

        for (TbUserInfo tbUserInfo : userInfoList) {
            if (!newUserInfoList.contains(tbUserInfo)) {
                tbUserInfo.removeAuthGroup();
            }
        }

        return newUserInfoList;
    }

    @Transactional
    public int deleteAuthGroupUser(long authGroupId, @NotEmpty List<Long> lunaUserNoList) {
        int count = 0;
        for (TbUserInfo tbUserInfo : userInfoRepository.findAllByAuthGroupIdAndIdIn(authGroupId, lunaUserNoList)) {
            if (tbUserInfo.getTbAuthGroup() != null) {
                tbUserInfo.removeAuthGroup();
                count++;
            }
        }
        return count;
    }

    @Transactional
    public void modifyAuthGroup(long authGroupId, @NotBlank String authGroupName) {
        if (!authGroupRepository.existsByAuthGroupIdNotAndAuthGroupName(authGroupId, authGroupName)) {
            TbAuthGroup tbAuthGroup = authGroupRepository.findById(authGroupId)
                    .orElseThrow(() -> new CustomException(new ErrorResponse(CustomExceptionCode.NOT_FOUND_DATA)));
            tbAuthGroup.changeAuthGroupName(authGroupName);
        } else {
            //그룹네임이 중복될때
            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
            errorResponse.setMessage("중복된 권한 그룹 명 입니다.");
            throw new CustomException(errorResponse);
        }
    }

    @Transactional
    public int deleteAuthGroup(long authGroupId) {

        if (authGroupRepository.existsByAuthGroupId(authGroupId)) {
            authGroupRepository.deleteById(authGroupId);

            List<TbUserInfo> tbUserInfoList = userInfoRepository.findByAuthGroupId(authGroupId);
            tbUserInfoList.forEach(TbUserInfo::removeAuthGroup);

            int deletedPermissionCount = userMenuInfoCustomRepository.deleteAuthPermissionInBatch(authGroupId);
            return tbUserInfoList.size() + deletedPermissionCount + 1;

        } else {
            throw new CustomException(new ErrorResponse(CustomExceptionCode.NOT_FOUND_DATA));
        }
    }

    public AuthGroupDto addAuthGroup(@NotBlank String authGroupName) {
        if (!authGroupRepository.existsByAuthGroupName(authGroupName)) {
            TbAuthGroup tbAuthGroup = TbAuthGroup.newInstance(authGroupName);
            tbAuthGroup = authGroupRepository.save(tbAuthGroup);

            return AuthGroupDto.builder()
                    .authGroupId(tbAuthGroup.getAuthGroupId())
                    .authGroupName(tbAuthGroup.getAuthGroupName())
                    .build();
        } else {
            ErrorResponse errorResponse = CustomExceptionCode.INVALID_RESOURCE_DUPLICATE.getErrorResponse();
            errorResponse.setMessage("중복된 권한 그룹 명 입니다.");
            throw new CustomException(errorResponse);
        }
    }


    @Transactional
    public int updateAuthInfo(long authGroupId, List<MenuAuthInfoDto> requestMenuAuthInfoDtoList) {

        int affectedRowCount = 0;

        TbAuthGroup tbAuthGroup = authGroupRepository.findById(authGroupId).orElseThrow();

        Map<Pair<Long, GrantType>, TbAuthPermission> permissionMap = authPermissionRepository.findAll().stream()
                .collect(Collectors.toMap(item -> Pair.of(item.getTbMenuInfo().getMenuId(), item.getPermissionName()), item -> item, (r1, r2) -> r1));

        for (MenuAuthInfoDto menuAuthInfoDto : requestMenuAuthInfoDtoList) {
            if (menuAuthInfoDto.getShowYn().isUsable()) {
                Pair<Long, GrantType> authKey = Pair.of(menuAuthInfoDto.getMenuId(), GrantType.GET);
                if (!permissionMap.containsKey(authKey)) {
                    Optional<TbMenuInfo> tbMenuInfoOptional = menuInfoRepository.findById(menuAuthInfoDto.getMenuId());
                    if (tbMenuInfoOptional.isPresent()) {
                        TbMenuInfo tbMenuInfo = tbMenuInfoOptional.get();
                        TbAuthPermission tbAuthPermission = TbAuthPermission.builder().permissionName(GrantType.GET).tbMenuInfo(tbMenuInfo).build();
                        affectedRowCount += tbAuthGroup.addAuthPermission(tbAuthPermission);
                    }
                } else {
                    affectedRowCount += tbAuthGroup.addAuthPermission(permissionMap.get(authKey));
                }
            } else {
                //기존 권한 중 제거된 대메뉴 권한 제거
                if (tbAuthGroup.getTbAuthPermissionList().stream().anyMatch(item -> item.getTbMenuInfo().getMenuId() == menuAuthInfoDto.getMenuId())) {
                    affectedRowCount += tbAuthGroup.removeAuthPermission(menuAuthInfoDto.getMenuId(), GrantType.GET);
                }
            }
            for (SubMenuAuthInfoDto subMenuAuthInfoDto : menuAuthInfoDto.getSubMenuAuthInfoList()) {
                //기존 권한 중 제거된 중메뉴 권한 제거
                List<TbAuthPermission> deletedPermissionList = tbAuthGroup.getTbAuthPermissionList().stream()
                        .filter(item -> item.getTbMenuInfo().getMenuId() == subMenuAuthInfoDto.getMenuId() && !subMenuAuthInfoDto.getPermissionNames().contains(item.getPermissionName()))
                        .collect(Collectors.toList());
                for (TbAuthPermission deletedPermission : deletedPermissionList) {
                    affectedRowCount += tbAuthGroup.removeAuthPermission(deletedPermission.getTbMenuInfo().getMenuId(), deletedPermission.getPermissionName());
                }

                for (GrantType permissionName : subMenuAuthInfoDto.getPermissionNames()) {
                    Pair<Long, GrantType> subAuthKey = Pair.of(subMenuAuthInfoDto.getMenuId(), permissionName);
                    if (!permissionMap.containsKey(subAuthKey)) {
                        Optional<TbMenuInfo> tbMenuInfoOptional = menuInfoRepository.findById(subMenuAuthInfoDto.getMenuId());
                        if (tbMenuInfoOptional.isPresent()) {
                            TbMenuInfo tbMenuInfo = tbMenuInfoOptional.get();
                            TbAuthPermission tbAuthPermission = TbAuthPermission.builder().permissionName(permissionName).tbMenuInfo(tbMenuInfo).build();
                            affectedRowCount += tbAuthGroup.addAuthPermission(tbAuthPermission);
                        }
                    } else {
                        affectedRowCount += tbAuthGroup.addAuthPermission(permissionMap.get(subAuthKey));
                    }
                }
            }
        }

        // 엘라스틱 로그 저장
//        elasticLoggingUtil.addCellookAdminLog(authGroupId, CellookAdminLog.ServiceType.STORE_MANAGE, GsonUtils.toJson(requestMenuAuthInfoDtoList));

        return affectedRowCount;
    }

//    @Transactional
//    public ResponseEntity<Void> updateAuthInfo(long authGroupId, List<MenuAuthInfoDto> requestMenuAuthInfoDtoList) {
//
//        int affectedRowCount = 0;
//
//        TbAuthGroup tbAuthGroup = authGroupRepository.findById(authGroupId).orElseThrow();
//
//        Map<Pair<Long, GrantType>, TbAuthPermission> permissionMap = authPermissionRepository.findAll().stream()
//                .collect(Collectors.toMap(item -> Pair.of(item.getTbMenuInfo().getMenuId(), item.getPermissionName()), item -> item, (r1, r2) -> r1));
//
//        for (MenuAuthInfoDto menuAuthInfoDto : requestMenuAuthInfoDtoList) {
//            if (menuAuthInfoDto.getShowYn().isUsable()) {
//                Pair<Long, GrantType> authKey = Pair.of(menuAuthInfoDto.getMenuId(), GrantType.GET);
//                if (!permissionMap.containsKey(authKey)) {
//                    Optional<TbMenuInfo> tbMenuInfoOptional = menuInfoRepository.findById(menuAuthInfoDto.getMenuId());
//                    if (tbMenuInfoOptional.isPresent()) {
//                        TbMenuInfo tbMenuInfo = tbMenuInfoOptional.get();
//                        TbAuthPermission tbAuthPermission = TbAuthPermission.builder().permissionName(GrantType.GET).tbMenuInfo(tbMenuInfo).build();
//                        affectedRowCount += tbAuthGroup.addAuthPermission(tbAuthPermission);
//                    }
//                } else {
//                    affectedRowCount += tbAuthGroup.addAuthPermission(permissionMap.get(authKey));
//                }
//            } else {
//                //기존 권한 중 제거된 대메뉴 권한 제거
//                if (tbAuthGroup.getTbAuthPermissionList().stream().anyMatch(item -> item.getTbMenuInfo().getMenuId() == menuAuthInfoDto.getMenuId())) {
//                    affectedRowCount += tbAuthGroup.removeAuthPermission(menuAuthInfoDto.getMenuId(), GrantType.GET);
//                }
//            }
//            for (SubMenuAuthInfoDto subMenuAuthInfoDto : menuAuthInfoDto.getSubMenuAuthInfoList()) {
//                //기존 권한 중 제거된 중메뉴 권한 제거
//                List<TbAuthPermission> deletedPermissionList = tbAuthGroup.getTbAuthPermissionList().stream()
//                        .filter(item -> item.getTbMenuInfo().getMenuId() == subMenuAuthInfoDto.getMenuId() && !subMenuAuthInfoDto.getPermissionNames().contains(item.getPermissionName()))
//                        .collect(Collectors.toList());
//                for (TbAuthPermission deletedPermission : deletedPermissionList) {
//                    affectedRowCount += tbAuthGroup.removeAuthPermission(deletedPermission.getTbMenuInfo().getMenuId(), deletedPermission.getPermissionName());
//                }
//
//                for (GrantType permissionName : subMenuAuthInfoDto.getPermissionNames()) {
//                    Pair<Long, GrantType> subAuthKey = Pair.of(subMenuAuthInfoDto.getMenuId(), permissionName);
//                    if (!permissionMap.containsKey(subAuthKey)) {
//                        Optional<TbMenuInfo> tbMenuInfoOptional = menuInfoRepository.findById(subMenuAuthInfoDto.getMenuId());
//                        if (tbMenuInfoOptional.isPresent()) {
//                            TbMenuInfo tbMenuInfo = tbMenuInfoOptional.get();
//                            TbAuthPermission tbAuthPermission = TbAuthPermission.builder().permissionName(permissionName).tbMenuInfo(tbMenuInfo).build();
//                            affectedRowCount += tbAuthGroup.addAuthPermission(tbAuthPermission);
//                        }
//                    } else {
//                        affectedRowCount += tbAuthGroup.addAuthPermission(permissionMap.get(subAuthKey));
//                    }
//                }
//            }
//        }
//
//        // 엘라스틱 로그 저장
////        elasticLoggingUtil.addCellookAdminLog(authGroupId, CellookAdminLog.ServiceType.STORE_MANAGE, GsonUtils.toJson(requestMenuAuthInfoDtoList));
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("X-Admin-Affected-Row-Count", String.valueOf(affectedRowCount));
//
//        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).build();
//    }

    @Transactional(readOnly = true)
    public AuthGroupManageDto.Response getAuthGroupManageInfo(Long authGroupId) {
        TbAuthGroup tbAuthGroup = authGroupRepository.findById(authGroupId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));
        List<TbUserInfo> tbUserInfoList = userInfoRepository.findByAuthGroupId(authGroupId);
        List<MenuAuthInfoDto> menuAuthInfoDtoList = userMenuInfoCustomRepository.selectMenuAuthByAuthGroupId(authGroupId);
        return AuthGroupManageDto.Response.of(tbAuthGroup, tbUserInfoList, menuAuthInfoDtoList);
    }

    @Transactional(readOnly = true)
    public List<MenuAuthInfoDto> getUserMenuAuthInfoByAuthGroupId(Long authGroupId) {
        return userMenuInfoCustomRepository.selectMenuAuthByAuthGroupId(authGroupId);
    }

    @Transactional(readOnly = true)
    public PageImpl<AuthGroupInfoDto> getAuthGroupList(Long authGroupId, int page, int pageSize) {
        return userMenuInfoCustomRepository.selectAuthGroupInfoList(authGroupId, PageRequest.of(page - 1, pageSize));
    }

    @Transactional(readOnly = true)
    public List<HashMap<String, Object>> getUserListByAuthGroupId(long authGroupId) {

        return userInfoRepository.findByAuthGroupId(authGroupId)
                .stream().map(tbUserInfo -> new HashMap<String, Object>() {{
                    put("admin_id", tbUserInfo.getAdminId());
                    put("username", tbUserInfo.getUsername());
                    put("auth_group_name", tbUserInfo.getTbAuthGroup().getAuthGroupName());
                    put("reg_dt", tbUserInfo.getRegDt());
                }}).collect(Collectors.toList());
    }

}
