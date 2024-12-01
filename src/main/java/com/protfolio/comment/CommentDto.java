package com.protfolio.comment;

import com.protfolio.article.ArticleEntity;
import com.protfolio.user.UserEntity;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {
	@ManyToOne
	private ArticleEntity article;
	@ManyToOne
	private UserEntity user;
	
	 @NotEmpty(message = "コメントを入力してください") 
	private String content;
}
