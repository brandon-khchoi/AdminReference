package com.example.adminreference.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class AuthGroupInfoDto {

    @Schema(description = "권한 그룹 아이디")
    private long authGroupId;
    @Schema(description = "권한 그룹 명")
    private String authGroupName;
    @Schema(description = "그룹 내 유저수")
    private int userCount;
    @Schema(description = "수정일")
    private LocalDateTime updDt;
    @Schema(description = "등록일")
    private LocalDateTime regDt;

}
