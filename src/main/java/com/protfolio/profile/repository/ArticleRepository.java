package com.protfolio.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protfolio.models.Article;
import com.protfolio.security.User;

public interface ArticleRepository extends JpaRepository<Article, Integer>{
	List<Article> findByUserOrderByCreatedAtDesc(User user);

}
