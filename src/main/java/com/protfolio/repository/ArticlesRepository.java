package com.protfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.protfolio.models.Article;

public interface ArticlesRepository extends JpaRepository<Article, Integer>{
	Page<Article> findAll(Pageable pageable);
}
