package com.protfolio.models;

import java.util.Date;
import java.util.List;

import com.protfolio.security.User;

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
public class Article {
	
	@OneToMany(mappedBy = "article")
	 private List<Comment> comment;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	 @ManyToOne
	 private User user;
	 
	 private String title;
	 
	 private String content;
	 
	 private String imageFileName;
	 
	 private Date createdAt;
	 
	 
}
