package com.protfolio.dto;

import com.protfolio.models.Article;
import com.protfolio.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {
	
	private Article article;
	
	private User user;
	
	 @NotEmpty(message = "名前が入力してください") 
	private String content;
}
