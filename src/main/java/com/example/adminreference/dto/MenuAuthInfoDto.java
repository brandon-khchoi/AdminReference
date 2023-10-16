package com.example.adminreference.dto;

import com.example.adminreference.enumeration.UseYn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class MenuAuthInfoDto {

    private Long menuId;
    private String menuName;
    private UseYn showYn;
    private String menuCode;
    private List<SubMenuAuthInfoDto> subMenuAuthInfoList = new ArrayList<>();
}
