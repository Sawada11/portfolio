package com.protfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protfolio.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	List<Comment> findByArticleId(int articleId);
}
