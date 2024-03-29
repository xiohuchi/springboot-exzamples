package com.dianmi.auth.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yangbin
 * @date 2019年10月14日
 * 认证成功事件处理器
 */
public abstract class AbstractAuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        Authentication authentication = (Authentication) event.getSource();
        if (!StringUtils.isEmpty(authentication.getAuthorities())) {
            handle(authentication, request, response);
        }
    }

    /**
     * 处理登录成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     * @param request        请求
     * @param response       响应
     */
    public abstract void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response);
}
