package com.dianmi.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yangbin
 * @date 2019年10月14日
 */
@Slf4j
@Component
public class SsoAuthenticationSuccessEventHandler extends AbstractAuthenticationSuccessEventHandler {

    /**
     * 处理登录成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     * @param request        请求
     * @param response       返回
     */
    @Override
    public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        log.info("用户登录成功：{} ", authentication.getPrincipal());
    }
}
