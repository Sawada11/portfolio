package com.protfolio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		 http.formLogin(login -> login
	                .loginProcessingUrl("/login")
	                .loginPage("/security/login")
	                .defaultSuccessUrl("/articles",true)
	                .failureUrl("/login?error")
	                .permitAll()
	        ).logout(logout -> logout
	                .logoutSuccessUrl("/")
	        ).authorizeHttpRequests(authz -> authz
//	                 あとで削除参照権限
	                .requestMatchers("/articles")
	                .permitAll()
//	                あとで削除参照権限
	                .requestMatchers("/articles/create")
	                .permitAll()
	        );
	        return http.build();
		
	}
	
	@Autowired
	public void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
}
