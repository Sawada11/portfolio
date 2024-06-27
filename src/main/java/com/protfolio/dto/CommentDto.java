package com.protfolio.dto;

import com.protfolio.models.Article;
import com.protfolio.security.User;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {
	@ManyToOne
	private Article article;
	@ManyToOne
	private User user;
	
	 @NotEmpty(message = "名前が入力してください") 
	private String content;
}
