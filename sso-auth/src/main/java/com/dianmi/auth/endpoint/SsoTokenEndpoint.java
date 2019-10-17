package com.dianmi.auth.endpoint;

import com.dianmi.auth.SecurityUtils;
import com.dianmi.auth.contant.CacheConstants;
import com.dianmi.auth.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangbin
 * @date 2019年10月16日
 * <p>
 * iam-dianmi
 */
@RestController
@RequestMapping("/token")
@Slf4j
public class SsoTokenEndpoint {
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("ftl/login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }


    @DeleteMapping("/logout")
    public R logout(HttpServletRequest request, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isEmpty(authHeader)) {
            return R.ok(Boolean.FALSE, "退出失败，token 为空");
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getValue())) {
            return R.ok(Boolean.TRUE, "退出失败，token 无效");
        }

        OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
        // 清空用户信息
        cacheManager.getCache(CacheConstants.USER_DETAILS)
                .evict(auth2Authentication.getName());

        // 清空access token
        tokenStore.removeAccessToken(accessToken);

        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);

        securityLogout(request);

        return R.ok(Boolean.TRUE);
    }

    private void securityLogout(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest required");
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.debug("Invalidating session: " + session.getId());
            session.invalidate();
        }

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    @GetMapping("/confirm_access")
    public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
        Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
        modelAndView.addObject("scopeList", scopeList.keySet());

        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
            modelAndView.addObject("app", new HashMap<>());
            modelAndView.addObject("user", SecurityUtils.getUser());
        }

        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }

}
