package com.protfolio.profile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.protfolio.models.Article;
import com.protfolio.profile.service.ArticleService;
import com.protfolio.security.User;
import com.protfolio.security.UserRepository;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping
	public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//		ログインユーザー取得
		 String username = userDetails.getUsername();
		 User currentUser = userRepository.findByUsername(username);
//		ログインユーザーが投稿した記事を取得    
		List<Article> currentArticles = articleService.getArticlesByUser(currentUser);
			
			model.addAttribute("user", username);
			model.addAttribute("articles", currentArticles);
			return "profile/profileIndex";
	}
}
