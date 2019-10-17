package com.dianmi.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author yangbin
 * @date 2019年10月14日
 * <p>
 * iam-dianmi
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public Authentication userInfo(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
}
