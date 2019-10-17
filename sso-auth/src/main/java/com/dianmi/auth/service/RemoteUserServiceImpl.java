package com.dianmi.auth.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author yangbin
 * @date 2019年10月14日
 * <p>
 * iam-dianmi
 */
@Service
@AllArgsConstructor
public class RemoteUserServiceImpl implements RemoteUserService {
    /**
     * 根据社交登录code 登录
     *
     * @param inStr TYPE@CODE
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserBySocial(String inStr) throws UsernameNotFoundException {
        return new User("user_1", new BCryptPasswordEncoder().encode("123456"), new ArrayList<>());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("user_1", new BCryptPasswordEncoder().encode("123456"), new ArrayList<>());
    }
}
