package com.example.adminreference.service;

import com.example.adminreference.dto.AuthGroupDto;
import com.example.adminreference.repository.AuthGroupRepository;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommonService {

    private final AuthGroupRepository authGroupRepository;

    public List<AuthGroupDto> getAuthGroupCodeList() {
        return authGroupRepository.findAll().stream()
                .map(tbAuthGroup -> AuthGroupDto.builder()
                        .authGroupId(tbAuthGroup.getAuthGroupId())
                        .authGroupName(tbAuthGroup.getAuthGroupName())
                        .build())
                .collect(Collectors.toList());
    }


}
