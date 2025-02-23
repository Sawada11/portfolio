package com.protfolio.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{
	List<CommentEntity> findByArticleId(int articleId);
}
