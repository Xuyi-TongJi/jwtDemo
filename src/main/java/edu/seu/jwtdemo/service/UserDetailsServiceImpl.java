package edu.seu.jwtdemo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsService自定义实现类
 * @author xuyitjuseu
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String encode = passwordEncoder.encode("123");
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin, ROLE_ADMIN");
        return new User("admin", encode, auths);
    }
}
