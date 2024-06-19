package com.protfolio.dto;

import org.springframework.web.multipart.MultipartFile;

import com.protfolio.models.User;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ArticleDto {
	
	 @ManyToOne
	 private User user;
	 
//	 @NotEmpty(message = "名前が入力してください") 
	 private String title;
	 
//	 @NotEmpty(message = "名前が入力してください")
	 private String content;
	 
	 private MultipartFile imageFileName;
}
