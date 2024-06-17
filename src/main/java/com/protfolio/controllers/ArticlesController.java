package com.protfolio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.protfolio.models.Article;
import com.protfolio.repository.ArticlesRepository;

@Controller
@RequestMapping("/articles")
public class ArticlesController {

	@Autowired
	private ArticlesRepository repo;
	
	@GetMapping({"", "/"})
	public String showArticleList(Model model) {
		List<Article> articles = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("articles", articles);
		return "articles/index";
	}
}
