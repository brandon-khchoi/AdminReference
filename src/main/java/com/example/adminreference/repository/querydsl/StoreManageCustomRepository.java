package com.example.adminreference.repository.querydsl;

import com.example.adminreference.dto.ShopInfoDto;
import com.example.adminreference.dto.ShopManageDto;
import com.example.adminreference.dto.ShopSearchRequest;
import com.example.adminreference.dto.ShopStatusHistDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.adminreference.entity.QSysShopDtl.sysShopDtl;
import static com.example.adminreference.entity.QSysShopInfo.sysShopInfo;
import static com.example.adminreference.entity.QSysShopMgt.sysShopMgt;
import static com.example.adminreference.entity.QSysShopStatusHist.sysShopStatusHist;
import static com.example.adminreference.entity.QTbUserInfo.tbUserInfo;
//import static com.example.adminreference.entity.subquery.QProductCountSubQuery.productCountSubQuery;
//import static com.example.adminreference.entity.subquery.QSubCountSubQuery.subCountSubQuery;

@Slf4j
@Repository
@Transactional
public class StoreManageCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public StoreManageCustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Map<String, Object>> selectShopCodeList(Integer shopId) {
        List<Predicate> conditions = new ArrayList<>();
        if (shopId != null) {
            conditions.add(sysShopMgt.shopId.eq(shopId));
        }

        return jpaQueryFactory
                .select(sysShopMgt)
                .from(sysShopMgt)
                .leftJoin(sysShopMgt.sysShopDtl, sysShopDtl)
                .where(conditions.toArray(new Predicate[]{}))
                .fetchJoin()
                .fetch()
                .stream()
                .map(item -> new HashMap<String, Object>() {{
                    put("shop_id", item.getShopId());
                    put("shop_nm", item.getShopNm());
                    put("member_id", item.getMemberId());
                    put("status_cd", item.getStatusCd());
                    put("status_cd_desc", item.getStatusCd() != null ? item.getStatusCd().getDesc() : "-");
                    put("main_img_s3_path", item.getMainImgS3Path());
                    put("logo_img_s3_path", item.getLogoImgS3Path());
                    put("description", item.getDescription());
                    put("subs_cnt", item.getSysShopDtl() != null ? item.getSysShopDtl().getSubsCnt() : "-");
                    put("smart_store_prd_base_url", item.getSmartStorePrdBaseUrl());
                    put("reg_dt", item.getRegDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }}).collect(Collectors.toList());

    }


    public List<ShopInfoDto> selectAllShopList(ShopSearchRequest shopSearch) {

        return jpaQueryFactory.select(
                        Projections.bean(
                                ShopInfoDto.class,
                                sysShopMgt.shopId,
                                sysShopMgt.shopNm,
                                sysShopMgt.mainImgS3Path.as("mainImgUrl"),
                                sysShopMgt.logoImgS3Path.as("logoImgUrl"),
                                sysShopMgt.statusCd,
                                sysShopMgt.description,
                                sysShopMgt.shopSort,
                                sysShopMgt.svcOpenDt,
                                sysShopStatusHist.statusApplyDt.max().as("latestStatusApplyDt"),
//                                subCountSubQuery.subCnt.as("subsCnt"),
//                                productCountSubQuery.productCnt.as("productCnt"),
                                sysShopMgt.regDt,
                                sysShopMgt.updDt
                        ))
                .from(sysShopMgt)
                .leftJoin(sysShopStatusHist)
                .on(sysShopMgt.shopId.eq(sysShopStatusHist.shopId).and(sysShopStatusHist.statusApplyDt.before(LocalDate.now())))
//                .leftJoin(subCountSubQuery)
//                .on(sysShopMgt.shopId.eq(subCountSubQuery.shopId))
//                .leftJoin(productCountSubQuery)
//                .on(sysShopMgt.shopId.eq(productCountSubQuery.shopId))
                .groupBy(sysShopMgt)
                .where(getShopSearchCondition(shopSearch))
//                .orderBy(shopSearch.getOrderType().getOrder(), sysShopMgt.shopId.desc())
                .fetch();
    }

    public List<ShopStatusHistDto> selectShopStatusHist(int shopId) {
        return jpaQueryFactory.select(
                        Projections.bean(
                                ShopStatusHistDto.class,
                                sysShopStatusHist.shopId,
                                sysShopStatusHist.statusCd,
                                sysShopStatusHist.statusApplyDt,
                                tbUserInfo.username,
                                sysShopStatusHist.regDt
                        ))
                .from(sysShopStatusHist)
                .leftJoin(tbUserInfo)
                .on(sysShopStatusHist.regId.eq(tbUserInfo.id))
                .where(sysShopStatusHist.shopId.eq(shopId))
                .orderBy(sysShopStatusHist.regDt.desc())
                .fetch();
    }


    public ShopManageDto.Response selectShopManageInfo(int shopId) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                ShopManageDto.Response.class,
                                sysShopMgt,
                                sysShopMgt.sysShopInfo
                        ))
                .from(sysShopMgt)
                .join(sysShopMgt.sysShopInfo, sysShopInfo)
                .where(sysShopMgt.shopId.eq(shopId))
                .fetchFirst();
    }

    public PageImpl<ShopInfoDto> selectShopList(ShopSearchRequest shopSearch) {

        PageRequest pageRequest = PageRequest.of(shopSearch.getPage() - 1, shopSearch.getPageSize());

        Predicate[] conditions = getShopSearchCondition(shopSearch);
        //count 영향 안주는 조인 분리
        int count = jpaQueryFactory
                .select(sysShopInfo.shopId)
                .from(sysShopMgt)
                .where(conditions)
                .fetch().size();

        List<ShopInfoDto> sysShopMgtList = jpaQueryFactory.select(
                        Projections.bean(
                                ShopInfoDto.class,
                                sysShopMgt.shopId,
                                sysShopMgt.shopNm,
                                sysShopMgt.mainImgS3Path.as("mainImgUrl"),
                                sysShopMgt.logoImgS3Path.as("logoImgUrl"),
                                sysShopMgt.statusCd,
                                sysShopMgt.description,
                                sysShopMgt.shopSort,
                                sysShopMgt.svcOpenDt,
                                sysShopStatusHist.statusApplyDt.max().as("latestStatusApplyDt"),
//                                subCountSubQuery.subCnt.as("subsCnt"),
//                                productCountSubQuery.productCnt.as("productCnt"),
                                sysShopMgt.regDt,
                                sysShopMgt.updDt
                        ))
                .from(sysShopMgt)
                .leftJoin(sysShopStatusHist)
                .on(sysShopMgt.shopId.eq(sysShopStatusHist.shopId).and(sysShopStatusHist.statusApplyDt.before(LocalDate.now())))
//                .leftJoin(subCountSubQuery)
//                .on(sysShopMgt.shopId.eq(subCountSubQuery.shopId))
//                .leftJoin(productCountSubQuery)
//                .on(sysShopMgt.shopId.eq(productCountSubQuery.shopId))
                .groupBy(sysShopMgt)
                .where(conditions)
                .orderBy(shopSearch.getOrderType().getOrder(), sysShopMgt.shopId.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(sysShopMgtList, pageRequest, count);
    }

    public Predicate[] getShopSearchCondition(ShopSearchRequest shopSearch) {
        List<Predicate> conditions = new ArrayList<>();

        if (shopSearch.getShopId() != null) {
            conditions.add(sysShopMgt.shopId.eq(shopSearch.getShopId()));
        }
        if (shopSearch.getDateType() != null && shopSearch.getStartDate() != null && shopSearch.getEndDate() != null) {
            if (ShopSearchRequest.DateType.REG_DT.equals(shopSearch.getDateType())) {
                conditions.add(sysShopMgt.regDt.between(shopSearch.getStartDate().atStartOfDay(), shopSearch.getEndDate().atTime(23, 59, 59)));
            } else if (ShopSearchRequest.DateType.UPD_DT.equals(shopSearch.getDateType())) {
                conditions.add(sysShopMgt.updDt.between(shopSearch.getStartDate().atStartOfDay(), shopSearch.getEndDate().atTime(23, 59, 59)));
            } else if (ShopSearchRequest.DateType.SVC_OPEN_DT.equals(shopSearch.getDateType())) {
                conditions.add(sysShopMgt.svcOpenDt.between(shopSearch.getStartDate(), shopSearch.getEndDate()));
            }
        }

        if (!Collections.isEmpty(shopSearch.getServiceStatus())) {
            conditions.add(sysShopMgt.statusCd.in(shopSearch.getServiceStatus()));
        }

        if (Strings.isNotEmpty(shopSearch.getSearchText())) {
            if (shopSearch.getSearchType().equals(ShopSearchRequest.ShopSearchType.SHOP_NAME)) {
                conditions.add(sysShopMgt.shopNm.contains(shopSearch.getSearchText()));
            } else if (shopSearch.getSearchType().equals(ShopSearchRequest.ShopSearchType.MEMBER_ID)) {
                conditions.add(sysShopMgt.memberId.contains(shopSearch.getSearchText()));
            } else if (shopSearch.getSearchType().equals(ShopSearchRequest.ShopSearchType.SHOP_ID)) {
                conditions.add(sysShopMgt.shopId.like("%" + shopSearch.getSearchText() + "%"));
            }
        }

        if (!Collections.isEmpty(shopSearch.getTags())) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (int i = 0; i < shopSearch.getTags().size(); i++) {
                booleanBuilder.or(sysShopMgt.tags.contains(shopSearch.getTags().get(i).getValue()));
            }
            conditions.add(booleanBuilder);
        }

        return conditions.toArray(new Predicate[]{});
    }
}
