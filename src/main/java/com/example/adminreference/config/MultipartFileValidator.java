package com.example.adminreference.config;

import com.example.adminreference.annotation.MultipartFileValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Slf4j
@Component
public class MultipartFileValidator implements ConstraintValidator<MultipartFileValid, MultipartFile> {

    private long limitSize;

    private MultipartFileValid.DataSizeType dataSizeType;

    private String customMessage;

    private String[] allowFileExtensions;

    private String name;

    private boolean nullable;

    @Override
    public void initialize(MultipartFileValid constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.limitSize = constraintAnnotation.limitSize();
        this.dataSizeType = constraintAnnotation.dataSizeType();
        this.allowFileExtensions = constraintAnnotation.allowFileExtensions();
        this.customMessage = constraintAnnotation.message();
        this.name = constraintAnnotation.name();
    }

    @Override
    public boolean isValid(MultipartFile input, ConstraintValidatorContext context) {
        if (!nullable) {
            context.disableDefaultConstraintViolation();
            String prefix = !"".equals(name) ? name + (((name.charAt(name.length() - 1) - 0XAC00) / 28) >= 0 ? "는 " : "은 ") : "해당 파일은 ";
            if (input == null) {
                context.buildConstraintViolationWithTemplate(prefix + "필수 항목입니다.").addConstraintViolation();
                return false;
            }
            // limitSize 0보다 작거나 같은 경우 체크 안함
            boolean isFileSizeValid = limitSize <= 0 || input.getSize() <= dataSizeType.apply(limitSize);
            // allowFileExtensions 없는 경우 체크 안함
            boolean isFileExtensionValid = allowFileExtensions.length == 0 || Arrays.asList(allowFileExtensions).contains(FilenameUtils.getExtension(input.getOriginalFilename()));

            if ("".equals(customMessage)) {
                if (!isFileSizeValid && !isFileExtensionValid) {
                    context.buildConstraintViolationWithTemplate(prefix + limitSize + dataSizeType + " 이하 " + String.join(", ", allowFileExtensions) + " 타입만 허용됩니다.").addConstraintViolation();
                } else if (!isFileSizeValid) {
                    context.buildConstraintViolationWithTemplate(prefix + limitSize + dataSizeType + " 이하만 허용됩니다.").addConstraintViolation();
                } else if (!isFileExtensionValid) {
                    context.buildConstraintViolationWithTemplate(prefix + String.join(", ", allowFileExtensions) + " 타입만 허용됩니다.").addConstraintViolation();
                }
            } else {
                context.buildConstraintViolationWithTemplate(customMessage).addConstraintViolation();
            }

            log.info("File Size : {} Byte / File Size Limit : {}{} isFileSizeValid = {}", input.getSize(), limitSize, dataSizeType, isFileSizeValid);
            log.info("File Extension : {} / Allowed File Extension {} isFileExtensionValid = {}", FilenameUtils.getExtension(input.getOriginalFilename()), String.join(" / ", allowFileExtensions), isFileExtensionValid);

            return isFileSizeValid && isFileExtensionValid;
        } else {
            log.info("File is nullable");
            return true;
        }
    }
}
