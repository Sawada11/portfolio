package com.protfolio.article;

import org.springframework.web.multipart.MultipartFile;

import com.protfolio.user.UserEntity;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleDto {
	
	 @ManyToOne
	 private UserEntity user;
	 
	 @NotEmpty(message = "名前が入力してください") 
	 private String title;
	 
	 @NotEmpty(message = "内容を入力してください")
	 private String content;
	 @NotNull(message = "画像を選択してください")
	 private MultipartFile imageFileName;
}
