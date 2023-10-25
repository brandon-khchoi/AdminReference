package com.example.adminreference.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
public class JwtProvider {
    private static final String _AUTH_HEADER = "Authorization";

    private final ECPrivateKey privateKey;

    private final ECPublicKey publicKey;

    private final Gson _GSON = new Gson();

    private JwtProvider() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory kf = KeyFactory.getInstance("EC"); // Elliptic Curve
        privateKey = (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(new PemReader(new StringReader(StreamUtils.copyToString(new ClassPathResource("jwt/ec-private.pkcs8").getInputStream(), StandardCharsets.UTF_8))).readPemObject().getContent()));
        publicKey = (ECPublicKey) kf.generatePublic(new X509EncodedKeySpec(new PemReader(new StringReader(StreamUtils.copyToString(new ClassPathResource("jwt/ec-public.pem").getInputStream(), StandardCharsets.UTF_8))).readPemObject().getContent()));
    }

    public String constructJWK() {
        return String.format(
                "{\"kty\":\"EC\",\"crv\":\"P-256\",\"x\":\"%s\",\"y\":\"%s\"}",
                Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getW().getAffineX().toByteArray()),
                Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getW().getAffineY().toByteArray())
        );
    }

    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader(_AUTH_HEADER) != null) {
            return request.getHeader(_AUTH_HEADER).replaceAll("(?i)bearer", "").trim();
        } else {
            return null;
        }
    }

    public String createToken(AdminUser adminUser) {
        Claims claims = Jwts.claims();
        claims.put(ClaimType.ADMIN_ID.name, adminUser.getAdminId());
        claims.put(ClaimType.AUTHORITY.name, adminUser.getAuthorities());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("Admin-Auth")
                .setClaims(claims)
                .setIssuer("admin.co.kr")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 1시간
                .signWith(privateKey)
                .compact();
    }

    public Authentication getAuthentication(String token) throws JsonProcessingException {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String adminId = claims.get(ClaimType.ADMIN_ID.name, String.class);

        String authoritiesJson = _GSON.toJson(claims.get(ClaimType.AUTHORITY.name, List.class));
        List<RequestGrantedAuthority> authorities = _GSON.fromJson(authoritiesJson, new TypeToken<List<RequestGrantedAuthority>>() {
        }.getType());

        AdminUser principal = new AdminUser(adminId, authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
        return EnumSet.allOf(ClaimType.class)
                .stream()
                .noneMatch(claimType -> claims.get(claimType.name) == null);
    }

}
