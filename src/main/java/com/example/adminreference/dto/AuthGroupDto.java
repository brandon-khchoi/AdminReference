package com.example.adminreference.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthGroupDto {
    private Long authGroupId;
    private String authGroupName;

    @Builder
    public AuthGroupDto(Long authGroupId, String authGroupName) {
        this.authGroupId = authGroupId;
        this.authGroupName = authGroupName;
    }
}
