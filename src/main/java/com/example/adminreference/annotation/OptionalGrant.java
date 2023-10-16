package com.example.adminreference.annotation;

import com.example.adminreference.config.security.GrantType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 권한 관리용 어노테이션
 * Controller 내 메소드에 선언하여 사용
 * SpringSecurity AuthorizationChecker 권한체크 통과 후 GrantAspect에서 본 어노테이션을 체크한다.
 * 본 어노테이션이 걸려있는 경우 RequestMethod를 사용하여 권한체크를 하지 않고 선택된 GrantType에 따라 권한 체크를 진행한다.
 * 또한 RequestHeader 에 OptionalGrant 항목을 추가해  추가해야함.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalGrant {
    GrantType requiredGrant();
}
