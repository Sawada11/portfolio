package com.protfolio.user;

import java.util.List;

import com.protfolio.article.ArticleEntity;
import com.protfolio.comment.CommentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArticleEntity> article;
	 
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comment;
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 
	 	@NotBlank(message = "名前が入力してください")
	    private String username;
	 	@NotBlank(message = "パスワードを入力してください")
	    private String password;
	    private String role;
}
