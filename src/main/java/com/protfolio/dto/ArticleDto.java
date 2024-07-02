package com.protfolio.dto;

import org.springframework.web.multipart.MultipartFile;

import com.protfolio.security.User;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleDto {
	
	 @ManyToOne
	 private User user;
	 
	 @NotEmpty(message = "名前が入力してください") 
	 private String title;
	 
	 @NotEmpty(message = "内容を入力してください")
	 private String content;
	 @NotNull(message = "画像を選択してください")
	 private MultipartFile imageFileName;
}
