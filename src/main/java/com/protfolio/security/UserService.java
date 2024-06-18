package com.protfolio.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.protfolio.dto.UserDto;
import com.protfolio.models.User;

@Repository
public interface UserService  extends JpaRepository<User, Integer>{
	User save(UserDto userDto);
}
