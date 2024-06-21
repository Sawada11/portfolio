package com.protfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protfolio.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
