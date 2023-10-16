package com.example.adminreference.config.security;

import com.example.adminreference.annotation.OptionalGrant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class GrantAspect {

    @Before("execution(* com.example.adminreference.controller..*(..))")
    public void grantCheck(JoinPoint jp) throws Throwable {
        log.info("[BEG] GrantAspect");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        GrantType requestGrantType = (GrantType) request.getAttribute("OptionalGrant");
        OptionalGrant declared = ((MethodSignature) jp.getSignature()).getMethod().getDeclaredAnnotation(OptionalGrant.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = request.getRequestURI();

        // OptionalGrant request, annotation 둘 중 하나라도 존재하면 권한 추가체크 진행
        if (requestGrantType != null || declared != null) {
            // OptionalGrant request, annotation 둘 중 하나만 NULL인 경우 ACCESS DENIED
            if (requestGrantType == null || declared == null) {
                log.info("{} - [{}:{}] OptionalGrant IS NOT GRANTED", authentication.getName(), requestGrantType, requestURI);
                throw new AccessDeniedException("ACCESS DENIED");
            } else {    //
                boolean isGranted = false;
                if (!"anonymousUser".equals(authentication.getPrincipal().toString())) {
                    GrantType annotatedGrantType = declared.requiredGrant();
                    if (annotatedGrantType.equals(requestGrantType)) {
                        for (GrantedAuthority authority : authentication.getAuthorities()) {
                            RequestGrantedAuthority requestGrantedAuthority = (RequestGrantedAuthority) authority;
                            if (requestURI.startsWith(authority.getAuthority()) && requestGrantedAuthority.getGrant().getOrDefault(requestGrantType, false)) {
                                log.info("{} - [{}:{}] OptionalGrant IS GRANTED", authentication.getName(), requestGrantType, requestURI);
                                isGranted = true;
                                break;
                            }
                        }
                    }
                } else {
                    log.info("GRANT_ASPECT UNAUTHORIZED");
                    throw new InsufficientAuthenticationException("UNAUTHORIZED");
                }
                if (!isGranted) {
                    log.info("{} - [{}:{}] OptionalGrant IS NOT GRANTED", authentication.getName(), requestGrantType, requestURI);
                    throw new AccessDeniedException("ACCESS DENIED");
                }
            }
        }
    }
}
