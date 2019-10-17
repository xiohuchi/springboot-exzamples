package com.dianmi.auth.config;

import com.dianmi.auth.filter.AuthenticationTokenFilter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author yangbin
 * @date 2019年10月14日
 * 认证相关配置
 */
@Primary
@Order(90)
@Configuration
@AllArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http
                .formLogin()
                .loginPage("/token/login")
                .loginProcessingUrl("/token/form")
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/token/**",
                        "/actuator/**",
                        "/mobile/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();

        http.addFilterBefore(new AuthenticationTokenFilter(redisTokenStore()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }

    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix("iam_dianmi:sso_oauth:");
        return tokenStore;
    }

}
