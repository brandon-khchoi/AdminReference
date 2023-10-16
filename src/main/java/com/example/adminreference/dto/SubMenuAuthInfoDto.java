package com.example.adminreference.dto;

import com.example.adminreference.config.security.GrantType;
import com.example.adminreference.enumeration.UseYn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@ToString
@Slf4j
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SubMenuAuthInfoDto {
    @JsonIgnore
    @Schema(description = "상위 메뉴 아이디")
    private Long parentMenuId;
    @JsonIgnore
    @Schema(description = "상위 메뉴 명")
    private String parentMenuName;
    @Schema(description = "메뉴 아이디")
    private long menuId;
    @Schema(description = "메뉴 명")
    private String menuName;
    @Schema(description = "메뉴 URL")
    private String menuUrl;
    @Schema(description = "노출 여부(Y/N)")
    private UseYn showYn;
    @Schema(description = "부여 권한 리스트")
    private List<GrantType> permissionNames;

}
