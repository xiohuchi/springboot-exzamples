package com.dianmi.auth.config;

import com.dianmi.auth.service.RemoteUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author yangbin
 * @date 2019年10月14日
 * <p>
 * iam-dianmi
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManagerBean;
    private final RemoteUserService userDetailsService;
    private static final String DEMO_RESOURCE_ID = "order";
    private final RedisTokenStore redisTokenStore;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String finalSecret = new BCryptPasswordEncoder().encode("123456");
        clients.inMemory().withClient("client1")
                .resourceIds(DEMO_RESOURCE_ID)
                .scopes("app")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .secret(finalSecret)

                .and().withClient("client2")
                .resourceIds(DEMO_RESOURCE_ID)
                .scopes("app")
                .authorizedGrantTypes("password", "refresh_token")
                .secret(finalSecret)

                .and()
                .withClient("client3")
                .authorities("oauth2")
                .scopes("hro")
                .resourceIds(DEMO_RESOURCE_ID)
                .authorizedGrantTypes("authorization_code", "password", "refresh_token","implicit")
                .secret(finalSecret)
                .redirectUris("https://ssl.jxufe.edu.cn/cas/login", "https://www.baidu.com")
//                .autoApprove(true)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(redisTokenStore)
                .authenticationManager(authenticationManagerBean)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .pathMapping("/oauth/confirm_access", "/token/confirm_access");
    }

}