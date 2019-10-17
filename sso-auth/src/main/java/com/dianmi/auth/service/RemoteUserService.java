package com.dianmi.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yangbin
 * @date 2019年10月14日
 * <p>
 * iam-dianmi
 */
public interface RemoteUserService extends UserDetailsService {
    /**
     * 根据社交登录code 登录
     *
     * @param code TYPE@CODE
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserBySocial(String code) throws UsernameNotFoundException;
}
