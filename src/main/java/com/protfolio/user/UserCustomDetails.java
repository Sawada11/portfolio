package com.protfolio.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/*
 *	 ログインユーザー情報を格納するためのオブジェクト作成
 */
public class UserCustomDetails extends org.springframework.security.core.userdetails.User {
    private final Long id;


    public UserCustomDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}