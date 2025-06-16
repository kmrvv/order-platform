package com.example.platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailsService delegate;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return delegate.loadUserByUsername(username);
    }

    public Authentication getAuthentication(String username) {
        UserDetails userDetails = loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}