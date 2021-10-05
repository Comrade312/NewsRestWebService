package com.example.demo.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, JOURNALIST, SUBSCRIBER;

    @Override
    public String getAuthority() {
        return name();
    }
}
