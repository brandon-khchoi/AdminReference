package com.example.adminreference.config.security;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
public class RequestGrantedAuthority implements GrantedAuthority {

    private final String grantedDomain;

    private final HashMap<GrantType, Boolean> grant = new HashMap<>();

    public HashMap<GrantType, Boolean> getGrant() {
        return grant;
    }

    public void putGrant(String grantTypeName) {
        try {
            grant.put(GrantType.valueOf(grantTypeName), true);
        } catch (Exception e) {
            log.error(grantTypeName + " : This GrantType is not support");
        }
    }

    public void putGrant(GrantType grantType) {
        grant.put(grantType, true);
    }

    public RequestGrantedAuthority(String grantedDomain) {
        this.grantedDomain = grantedDomain;
    }

    public RequestGrantedAuthority(String grantedDomain, String grantTypeName) {
        this.grantedDomain = grantedDomain;
        this.grant.put(GrantType.valueOf(grantTypeName), true);
    }

    public RequestGrantedAuthority(String grantedDomain, List<GrantType> grantTypeNames) {
        this.grantedDomain = grantedDomain;
        for (GrantType grantType : grantTypeNames) {
            this.grant.put(GrantType.valueOf(grantType.name()), true);
        }
    }

    @Override
    public String getAuthority() {
        return grantedDomain;
    }

}
