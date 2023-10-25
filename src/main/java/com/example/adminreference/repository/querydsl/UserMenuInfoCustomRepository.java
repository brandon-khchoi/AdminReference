package com.example.adminreference.repository.querydsl;

import com.example.adminreference.config.security.GrantType;
import com.example.adminreference.dto.AuthGroupInfoDto;
import com.example.adminreference.dto.MenuAuthInfoDto;
import com.example.adminreference.dto.SubMenuAuthInfoDto;
import com.example.adminreference.dto.UserInfoDto;
import com.example.adminreference.entity.QTbMenuInfo;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.UseYn;
import com.example.adminreference.enumeration.UserSearchType;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.adminreference.entity.QTbAuthGroup.tbAuthGroup;
import static com.example.adminreference.entity.QTbAuthGroupPermission.tbAuthGroupPermission;
import static com.example.adminreference.entity.QTbAuthPermission.tbAuthPermission;
import static com.example.adminreference.entity.QTbMenuInfo.tbMenuInfo;
import static com.example.adminreference.entity.QTbUserInfo.tbUserInfo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;


@Slf4j
@Repository
@Transactional
public class UserMenuInfoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserMenuInfoCustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public PageImpl<AuthGroupInfoDto> selectAuthGroupInfoList(Long groupId, PageRequest pageRequest) {

        BooleanExpression authGroupIdCheck = groupId != null ? tbAuthGroup.authGroupId.eq(groupId) : null;

        JPAQuery<AuthGroupInfoDto> query = jpaQueryFactory.select(
                        Projections.fields(
                                AuthGroupInfoDto.class,
                                tbAuthGroup.authGroupId,
                                tbAuthGroup.authGroupName,
                                tbAuthGroup.updDt,
                                tbAuthGroup.regDt,
                                tbUserInfo.count().castToNum(Integer.class).as("userCount")))
                .from(tbAuthGroup)
                .leftJoin(tbUserInfo)
                .on(tbUserInfo.authGroupId.eq(tbAuthGroup.authGroupId))
                .where(authGroupIdCheck)
                .groupBy(tbAuthGroup);

        long count = query.fetch().size();

        List<AuthGroupInfoDto> authGroupInfoList = query
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(authGroupInfoList, pageRequest, count);
    }


    public List<UserInfoDto> selectNonAuthGroupingUser(UserSearchType searchType, String searchText) {

        BooleanExpression textSearch = null;

        if (Strings.isNotEmpty(searchText)) {
            if (UserSearchType.ID.equals(searchType)) {
                textSearch = tbUserInfo.adminId.contains(searchText);
            } else if (UserSearchType.NAME.equals(searchType)) {
                textSearch = tbUserInfo.username.contains(searchText);
            }
        }

        return jpaQueryFactory.select(
                        Projections.fields(
                                UserInfoDto.class,
                                tbUserInfo.id,
                                tbUserInfo.adminId,
                                tbUserInfo.username,
                                tbUserInfo.department,
                                tbUserInfo.position,
                                tbUserInfo.email,
                                tbUserInfo.phone_number
                        ))
                .from(tbUserInfo)
                .where(tbUserInfo.tbAuthGroup.isNull(), textSearch)
                .fetch();
    }

    public PageImpl<UserInfoDto.Response> selectAllUserAuthInfo(UserSearchType searchType, String searchText, Long authGroupId, PageRequest pageRequest) {

        BooleanExpression textSearch = null;

        if (Strings.isNotEmpty(searchText)) {
            if (UserSearchType.ID.equals(searchType)) {
                textSearch = tbUserInfo.adminId.contains(searchText);
            } else if (UserSearchType.NAME.equals(searchType)) {
                textSearch = tbUserInfo.username.contains(searchText);
            }
        }

        BooleanExpression authGroupIdCheck = null;

        if (authGroupId != null) {
            authGroupIdCheck = tbUserInfo.authGroupId.eq(authGroupId);
        }

        JPAQuery<UserInfoDto.Response> query = jpaQueryFactory
                .select(Projections.bean(
                        UserInfoDto.Response.class,
                        tbUserInfo.id,
                        tbUserInfo.adminId,
                        tbUserInfo.username,
                        tbUserInfo.phone_number,
                        tbUserInfo.email,
                        tbUserInfo.department,
                        tbUserInfo.position,
                        tbUserInfo.tbAuthGroup.authGroupId,
                        tbUserInfo.tbAuthGroup.authGroupName.coalesce("-").as("authGroupName"),
                        tbUserInfo.lastLoginDt.stringValue().coalesce("-").as("lastLoginDt"),
                        tbUserInfo.regDt)
                )
                .from(tbUserInfo)
                .leftJoin(tbUserInfo.tbAuthGroup, tbAuthGroup)
                .where(textSearch, authGroupIdCheck);

        long count = query.fetch().size();

        List<UserInfoDto.Response> tbUserInfoList = query
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(tbUserInfoList, pageRequest, count);
    }

    public List<MenuAuthInfoDto> selectMenuAuthByAdminId2(String adminId) {
        QTbMenuInfo parentMenuInfo = new QTbMenuInfo("parentMenuInfo");

        List<SubMenuAuthInfoDto> subMenuAuthInfoDtoList = jpaQueryFactory
                .from(tbUserInfo)
                .join(tbAuthGroupPermission)
                .on(tbUserInfo.authGroupId.eq(tbAuthGroupPermission.tbAuthGroupPermissionId.authGroupId).and(tbUserInfo.adminId.eq(adminId)))
                .join(tbAuthGroupPermission.tbAuthPermission, tbAuthPermission)
                .rightJoin(tbMenuInfo)
                .on(tbAuthPermission.tbMenuInfo.menuId.eq(tbMenuInfo.menuId))
                .leftJoin(parentMenuInfo)
                .on(parentMenuInfo.menuId.eq(tbMenuInfo.parentMenuId))
                .where(tbMenuInfo.isMenuUse.eq(UseYn.Y))
                .orderBy(parentMenuInfo.displaySort.asc(), tbMenuInfo.displaySort.asc())
                .transform(groupBy(tbMenuInfo.menuId)
                        .list(Projections.fields(
                                SubMenuAuthInfoDto.class,
                                tbMenuInfo.menuId.as("menuId"),
                                tbMenuInfo.parentMenuId.as("parentMenuId"),
                                parentMenuInfo.menuName.as("parentMenuName"),
                                tbMenuInfo.menuName.as("menuName"),
                                Expressions.asString("/").concat(parentMenuInfo.menuUrl).concat("/").concat(tbMenuInfo.menuUrl).coalesce(tbMenuInfo.menuUrl).as("menuUrl"),
                                list(tbAuthGroupPermission.tbAuthPermission.permissionName).as("permissionNames"))
                        )
                );
        return getMenuAuthInfoDtoList(subMenuAuthInfoDtoList);
    }

    public TbUserInfo selectTbUserInfo(String adminId) {

        return jpaQueryFactory
                .select(tbUserInfo)
                .from(tbUserInfo)
                .leftJoin(tbUserInfo.tbAuthGroup, tbAuthGroup)
                .where(tbUserInfo.adminId.eq(adminId))
                .fetchFirst();
    }

    public UserInfoDto.Response selectUserAuthGroupInfo(long id) {
        return jpaQueryFactory.select(
                        Projections.fields(
                                UserInfoDto.Response.class,
                                tbUserInfo.id,
                                tbUserInfo.adminId,
                                tbUserInfo.username,
                                tbUserInfo.phone_number,
                                tbUserInfo.email,
                                tbUserInfo.department,
                                tbUserInfo.position,
                                tbUserInfo.lastLoginDt.stringValue().coalesce("-").as("lastLoginDt"),
                                tbUserInfo.regDt,
                                tbUserInfo.tbAuthGroup.authGroupId,
                                tbUserInfo.tbAuthGroup.authGroupName
                        ))
                .from(tbUserInfo)
                .leftJoin(tbUserInfo.tbAuthGroup, tbAuthGroup)
                .where(tbUserInfo.id.eq(id))
                .fetchFirst();
    }

    public int deleteAuthPermissionInBatch(long authGroupId) {
        long deleteCount = jpaQueryFactory.delete(tbAuthGroupPermission)
                .where(tbAuthGroupPermission.tbAuthGroupPermissionId.authGroupId.eq(authGroupId))
                .execute();

        return (int) deleteCount;
    }

    public List<MenuAuthInfoDto> selectMenuAuthByAuthGroupId(Long groupId) {
        QTbMenuInfo parentMenuInfo = new QTbMenuInfo("parentMenuInfo");

        List<SubMenuAuthInfoDto> subMenuAuthInfoDtoList = jpaQueryFactory
                .from(tbAuthGroupPermission)
                .join(tbAuthGroupPermission.tbAuthPermission, tbAuthPermission)
                .on(tbAuthGroupPermission.tbAuthGroupPermissionId.authGroupId.eq(groupId))
                .rightJoin(tbMenuInfo)
                .on(tbAuthPermission.tbMenuInfo.menuId.eq(tbMenuInfo.menuId))
                .leftJoin(parentMenuInfo)
                .on(parentMenuInfo.menuId.eq(tbMenuInfo.parentMenuId))
                .where(tbMenuInfo.isMenuUse.eq(UseYn.Y))
                .orderBy(parentMenuInfo.displaySort.asc(), tbMenuInfo.displaySort.asc())
                .transform(groupBy(tbMenuInfo.menuId)
                        .list(Projections.fields(
                                SubMenuAuthInfoDto.class,
                                tbMenuInfo.menuId.as("menuId"),
                                tbMenuInfo.parentMenuId.as("parentMenuId"),
                                parentMenuInfo.menuName.as("parentMenuName"),
                                tbMenuInfo.menuName.as("menuName"),
                                Expressions.asString("/").concat(parentMenuInfo.menuUrl).concat("/").concat(tbMenuInfo.menuUrl).coalesce(tbMenuInfo.menuUrl).as("menuUrl"),
                                list(tbAuthGroupPermission.tbAuthPermission.permissionName).as("permissionNames"))
                        )
                );

        log.info("{}", subMenuAuthInfoDtoList);
        return getMenuAuthInfoDtoList(subMenuAuthInfoDtoList);
    }

    public List<MenuAuthInfoDto> selectMenuAuthByAdminId(String adminId) {
        QTbMenuInfo parentMenuInfo = new QTbMenuInfo("parentMenuInfo");

        List<SubMenuAuthInfoDto> subMenuAuthInfoDtoList = jpaQueryFactory
                .from(tbUserInfo)
                .join(tbAuthGroupPermission)
                .on(tbUserInfo.authGroupId.eq(tbAuthGroupPermission.tbAuthGroupPermissionId.authGroupId).and(tbUserInfo.adminId.eq(adminId)))
                .join(tbAuthGroupPermission.tbAuthPermission, tbAuthPermission)
                .rightJoin(tbMenuInfo)
                .on(tbAuthPermission.tbMenuInfo.menuId.eq(tbMenuInfo.menuId))
                .leftJoin(parentMenuInfo)
                .on(parentMenuInfo.menuId.eq(tbMenuInfo.parentMenuId))
                .where(tbMenuInfo.isMenuUse.eq(UseYn.Y))
                .orderBy(parentMenuInfo.displaySort.asc(), tbMenuInfo.displaySort.asc())
                .transform(groupBy(tbMenuInfo.menuId)
                        .list(Projections.fields(
                                SubMenuAuthInfoDto.class,
                                tbMenuInfo.menuId.as("menuId"),
                                tbMenuInfo.parentMenuId.as("parentMenuId"),
                                parentMenuInfo.menuName.as("parentMenuName"),
                                tbMenuInfo.menuName.as("menuName"),
                                Expressions.asString("/").concat(parentMenuInfo.menuUrl).concat("/").concat(tbMenuInfo.menuUrl).coalesce(tbMenuInfo.menuUrl).as("menuUrl"),
                                list(tbAuthGroupPermission.tbAuthPermission.permissionName).as("permissionNames"))
                        )
                );
        return getMenuAuthInfoDtoList(subMenuAuthInfoDtoList);
    }

    public List<MenuAuthInfoDto> getMenuAuthInfoDtoList(List<SubMenuAuthInfoDto> subMenuAuthInfoDtoList) {
        LinkedHashMap<Long, MenuAuthInfoDto> meLongListHashMap = new LinkedHashMap<>();
        for (SubMenuAuthInfoDto subMenuAuthInfoDto : subMenuAuthInfoDtoList) {
            if (subMenuAuthInfoDto.getParentMenuId() == null) {
                MenuAuthInfoDto menuAuthInfoDto = meLongListHashMap.getOrDefault(subMenuAuthInfoDto.getMenuId(), new MenuAuthInfoDto());
                menuAuthInfoDto.setMenuId(subMenuAuthInfoDto.getMenuId());
                menuAuthInfoDto.setMenuName(subMenuAuthInfoDto.getMenuName());
                menuAuthInfoDto.setMenuCode(subMenuAuthInfoDto.getMenuUrl());
                menuAuthInfoDto.setShowYn(subMenuAuthInfoDto.getPermissionNames().contains(GrantType.GET) ? UseYn.Y : UseYn.N);
                meLongListHashMap.put(subMenuAuthInfoDto.getMenuId(), menuAuthInfoDto);
            } else {
                MenuAuthInfoDto temp = meLongListHashMap.getOrDefault(subMenuAuthInfoDto.getParentMenuId(), new MenuAuthInfoDto());
                temp.getSubMenuAuthInfoList().add(subMenuAuthInfoDto);
                subMenuAuthInfoDto.setShowYn(subMenuAuthInfoDto.getPermissionNames().contains(GrantType.GET) ? UseYn.Y : UseYn.N);
                meLongListHashMap.put(subMenuAuthInfoDto.getParentMenuId(), temp);
            }
        }

        return meLongListHashMap.values().stream().filter(e -> e.getMenuId() != null).collect(Collectors.toList());
    }

}
