package com.protfolio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.protfolio.dto.ArticleDto;
import com.protfolio.models.Article;
import com.protfolio.repository.ArticlesRepository;
import com.protfolio.repository.CommentRepository;

@Controller
@RequestMapping("/articles")
public class CommentController {

	@Autowired
	private ArticlesRepository repoArticle;
	
	@Autowired 
	private CommentRepository repoComment;
	
	/*
	 * 記事詳細
	 */
	@GetMapping("/comment")
	public String showCommentPage(Model model, @RequestParam int id) {
		try {
			Article article = repoArticle.findById(id).get();
			model.addAttribute("article", article);
			
			ArticleDto articleDto = new ArticleDto();
			articleDto.setUser(article.getUser());
			articleDto.setTitle(article.getTitle());
			articleDto.setContent(article.getContent());
			
			model.addAttribute("articleDto",articleDto);
		}catch(Exception ex) {
			System.out.println("例外：" + ex.getMessage());
			return "redirect:/articles";
		}
		return "comments/comment";
	}
	
	/*
	 * コメント送信
	 */
//	@PostMapping("/create")
//	public String showCreateComment(@Valid @ModelAttribute CommentDto commentDto
//			) {
//		Date createAt = new Date();
//		Comment comment = new Comment();
//		comment.setArticle(commentDto.getArticle());
//		comment.setUser(commentDto.getUser());
//		comment.setContent(commentDto.getContent());
//		comment.setCreatedAt(createAt);
//		
//		repoComment.save(comment);
//		
//		return "redirect:/comment";
//	}
}
