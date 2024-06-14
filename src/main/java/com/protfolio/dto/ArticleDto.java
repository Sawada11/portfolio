package com.protfolio.dto;

import com.protfolio.models.User;

import jakarta.validation.constraints.NotEmpty;

public class ArticleDto {
	
	 private User user;
	 @NotEmpty(message = "名前が入力してください") 
	 private String title;
	 
	 @NotEmpty(message = "名前が入力してください")
	 private String content;
	 
	 private String imageFileName;
}
