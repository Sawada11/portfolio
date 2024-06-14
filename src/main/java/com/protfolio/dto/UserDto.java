package com.protfolio.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
	
	@NotEmpty(message = "名前が入力してください")
	 private String name;
	
	@NotEmpty(message = "名前が入力してください")
	 private String email;
	
	@NotEmpty(message = "名前が入力してください")
	 private String password;
}
