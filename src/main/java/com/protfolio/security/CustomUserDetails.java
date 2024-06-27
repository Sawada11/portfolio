package com.protfolio.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/*
 *	 ログインユーザー情報を格納するためのオブジェクト作成
 */
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final Long id;


    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}