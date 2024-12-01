package com.protfolio.article;

import java.util.Date;
import java.util.List;

import com.protfolio.comment.CommentEntity;
import com.protfolio.user.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "articles")
public class ArticleEntity {
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<CommentEntity> comment;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	 @ManyToOne
	 private UserEntity user;
	 
	 private String title;
	 
	 private String content;
	 
	 private String imageFileName;
	 
	 private Date createdAt;
	 
	 
}
