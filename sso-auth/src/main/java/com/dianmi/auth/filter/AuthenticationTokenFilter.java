package com.dianmi.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yangbin
 * @date 2019年09月23日
 */
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private TokenStore tokenStore;

    public AuthenticationTokenFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenValue = resolveToken(request);

        log.info("当前线程名称：" + Thread.currentThread().getName());

        if (!StringUtils.isEmpty(tokenValue) && SecurityContextHolder.getContext().getAuthentication() == null) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            if (accessToken != null) {
                OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
                if (auth2Authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth2Authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return bearerToken == null ? null : bearerToken.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();
    }
}
