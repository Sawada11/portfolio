package com.protfolio.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.protfolio.user.UserEntity;

public interface ArticlesRepository extends JpaRepository<ArticleEntity, Integer>{
	Page<ArticleEntity> findAll(Pageable pageable);
	List<ArticleEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
}
