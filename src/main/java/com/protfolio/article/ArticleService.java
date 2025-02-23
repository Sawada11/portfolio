package com.protfolio.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protfolio.user.UserEntity;

@Service
public class ArticleService {

	@Autowired
	private ArticlesRepository articleRepository;
//	ログインユーザー情報を取得
	public List<ArticleEntity> getArticlesByUser(UserEntity user){
		return articleRepository.findByUserOrderByCreatedAtDesc(user);
	}
}
