package com.example.adminreference.config;

import com.example.adminreference.config.security.CustomAuthenticationHandler;
import com.example.adminreference.config.security.JwtAuthenticationFilter;
import com.example.adminreference.config.security.JwtProvider;
import com.example.adminreference.exception.CustomExceptionCode;
import com.example.adminreference.vo.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationHandler customAuthenticationHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorResponse(CustomExceptionCode.ACCESS_DENIED));
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorResponse(CustomExceptionCode.UNAUTHORIZED));
        };
    }

    @Bean
    public LogoutHandler customLogoutHandler() {
        return (request, response, authentication) -> {
            log.info("[BEG] customLogoutHandler");
            log.info(request.getHeader("Authorization"));
            log.info("[END] customLogoutHandler");
        };
    }

    public static final String[] NON_AUTH_LIST = {
            "/api-docs/**", "/swagger-ui/**", "/.well-known/jwks.json", "/h2-console/**",
            "/swagger-ui.html", "/login/**", "/logout/**", "/health/checker", "/",
            "/signup"
    };

    public static final String[] NON_GRANT_LIST = {
            "/common/**"
    };

    public static final String[] URL_WHITE_LIST = new ArrayList<String>() {{
        addAll(Arrays.asList(NON_GRANT_LIST));
        addAll(Arrays.asList(NON_AUTH_LIST));
    }}.toArray(new String[0]);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(URL_WHITE_LIST).permitAll()
                .anyRequest().access("@authorizationChecker.checker(request, authentication)");

        http.formLogin()
                .successHandler(customAuthenticationHandler)
                .failureHandler(customAuthenticationHandler)
                .loginProcessingUrl("/loginProcess")
                .usernameParameter("uid")
                .passwordParameter("password");

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/success")
                .invalidateHttpSession(true)
                .addLogoutHandler(customLogoutHandler());

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        //TODO: Header 선별해서 리스트 정하기.
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));

        corsConfiguration.setExposedHeaders(Collections.singletonList("*"));

        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
