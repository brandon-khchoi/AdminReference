package com.example.adminreference.controller;

import com.example.adminreference.dto.AuthGroupDto;
import com.example.adminreference.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/common")
@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @Operation(summary = "권한 코드 리스트")
    @RequestMapping(value = "/auth-group-code-list", method = RequestMethod.GET)
    public ResponseEntity<List<AuthGroupDto>> getAuthGroupCodeList() {
        return ResponseEntity.ok(commonService.getAuthGroupCodeList());
    }

}
