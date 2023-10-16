package com.example.adminreference.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthorizationChecker {

    public boolean checker(HttpServletRequest request, Authentication authentication) {
        try {
            if (!"anonymousUser".equals(authentication.getPrincipal().toString())) {
                boolean isGranted = false;

                String requestMethod = request.getMethod();
                // header에 OptionalGrant 있고 정상적인 경우 권한체크를 AOP(GrantAspect)로 넘긴다.
                try {
                    String optionalGrant = request.getHeader("OptionalGrant");
                    if (optionalGrant != null && !"".equals(optionalGrant)) {
                        GrantType grantType = GrantType.valueOf(optionalGrant.toUpperCase());
                        log.info("Optional GrantType : {}", grantType);
                        request.setAttribute("OptionalGrant", grantType);
                        isGranted = true;
                    }
                } catch (Exception ignored) {
                    return false;
                }

                if (!isGranted) {
                    String requestURI = request.getRequestURI();
                    GrantType requestGrantType = GrantType.valueOf(requestMethod);
                    for (GrantedAuthority authority : authentication.getAuthorities()) {
                        RequestGrantedAuthority requestGrantedAuthority = (RequestGrantedAuthority) authority;
                        if (requestURI.startsWith(authority.getAuthority()) && requestGrantedAuthority.getGrant().getOrDefault(requestGrantType, false)) {
                            log.info("{} - [{}:{}] IS GRANTED", authentication.getName(), requestMethod, requestURI);
                            isGranted = true;
                            break;
                        }
                    }
                    if (!isGranted) {
                        log.info("{} - [{}:{}] IS NOT GRANTED", authentication.getName(), requestMethod, requestURI);
                    }
                }
                return isGranted;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }
}
