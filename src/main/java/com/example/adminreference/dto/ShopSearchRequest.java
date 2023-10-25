package com.example.adminreference.dto;

import com.example.adminreference.enumeration.ServiceStatus;
import com.example.adminreference.enumeration.TagType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.OrderSpecifier;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.adminreference.entity.QSysShopMgt.sysShopMgt;
//import static kr.co.cellook.cellookadmin.entity.subquery.QProductCountSubQuery.productCountSubQuery;
//import static kr.co.cellook.cellookadmin.entity.subquery.QSubCountSubQuery.subCountSubQuery;

@Getter
@Setter
@ToString
public class ShopSearchRequest {

    public ShopSearchRequest() {
        //기본값 설정
        this.page = 1;
        this.pageSize = 10;
        this.orderType = OrderType.SVC_OPEN_DT;
    }

    @Parameter(description = "스토어 아이디")
    private Integer shopId;

    @Parameter(description = "조회 날짜 타입 (REG_DT : 등록일, UPD_DT : 수정일, SVC_OPEN_DT: 오픈일)")
    private DateType dateType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(description = "조회 시작일")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(description = "조회 종료일")
    private LocalDate endDate;

    @Parameter(description = "서비스 상태 (READY : 입점대기, OPEN : 오픈, STOP : 사용정지, SUSPEND : 일시정지)")
    private List<ServiceStatus> serviceStatus;

    @Parameter(description = "조회 타입 (SHOP_ID : 스토어 아이디, SHOP_NAME : 스토어 명, MALL_ID : 몰 아이디)")
    private ShopSearchType searchType;

    @Parameter(description = "조회 Text")
    private String searchText;

    @Parameter(description = "태그")
    private List<TagType> tags;

    @Parameter(description = "정렬 타입 (SVC_OPEN_DT : 오픈일, PRODUCT_COUNT : 상품 많은 순, SUBSCRIPT_COUNT : 구독 많은 순)")
    private OrderType orderType;

    @Min(10)
    @Max(100)
    @Parameter(description = "페이지 사이즈", example = "10")
    private int pageSize;

    @Min(1)
    @Parameter(description = "페이지 번호", example = "1")
    private int page;

    @JsonIgnore
    public List<Integer> getServiceStatusStr() {
        return serviceStatus.stream().map(ServiceStatus::getCode).collect(Collectors.toList());
    }

    public enum OrderType {
        SVC_OPEN_DT(sysShopMgt.svcOpenDt.desc());
//        PRODUCT_COUNT(productCountSubQuery.productCnt.desc()),
//        SUBSCRIPT_COUNT(subCountSubQuery.subCnt.desc());

        private final OrderSpecifier<?> order;

        OrderType(OrderSpecifier<?> order) {
            this.order = order;
        }

        public OrderSpecifier<?> getOrder() {
            return order;
        }
    }

    public enum DateType {
        REG_DT, UPD_DT, SVC_OPEN_DT
    }

    public enum ShopSearchType {
        SHOP_ID, SHOP_NAME, MEMBER_ID
    }


}
