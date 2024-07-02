package com.protfolio.profile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protfolio.models.Article;
import com.protfolio.profile.repository.ArticleRepository;
import com.protfolio.security.User;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
//	ログインユーザー情報を取得
	public List<Article> getArticlesByUser(User user){
		return articleRepository.findByUserOrderByCreatedAtDesc(user);
	}
}
