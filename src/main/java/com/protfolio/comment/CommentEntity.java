package com.protfolio.comment;

import java.util.Date;

import com.protfolio.article.ArticleEntity;
import com.protfolio.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private ArticleEntity article;
	
	@ManyToOne
	private UserEntity user;
	
	private String content;
	
	private Date createdAt;
}
