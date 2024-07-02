//package com.protfolio.models;
//
//import java.util.Date;
//import java.util.List;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Data
//@Entity
//public class User {
//	@OneToMany(mappedBy = "user")
//	private List<Article> article;
//	 
//	@OneToMany(mappedBy = "user")
//	private List<Comment> comment;
//	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	private int id;
//	  
//	 private String name;
//	 
//	 private String email;
//	 
//	 private String password;
//	 
//	 private Date createdAt;
//	 
//	
//}
