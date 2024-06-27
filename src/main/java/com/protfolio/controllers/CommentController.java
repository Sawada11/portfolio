package com.protfolio.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.protfolio.dto.ArticleDto;
import com.protfolio.dto.CommentDto;
import com.protfolio.models.Article;
import com.protfolio.models.Comment;
import com.protfolio.repository.ArticlesRepository;
import com.protfolio.repository.CommentRepository;
import com.protfolio.security.User;
import com.protfolio.security.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class CommentController {

	@Autowired
	private ArticlesRepository repoArticle;

	@Autowired 
	private CommentRepository repoComment;
	
	@Autowired
	private UserRepository userRepository;
	/*
	 * 記事詳細
	 */
	@GetMapping("/comment")
	public String showCommentPage(Model model, @RequestParam int id) {
		CommentDto commentDto = new CommentDto();
		model.addAttribute("commentDto", commentDto);
		
//		コメント欄
		List<Comment> comments = repoComment.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("comments",comments);
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
	@PostMapping("/post")
	public String showPostComment(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute CommentDto commentDto,
			BindingResult result,
			@AuthenticationPrincipal UserDetails userDetails
			) {
//		ログインユーザーを取得
		User currentUser = null;
		if(userDetails != null) {
			String username = userDetails.getUsername();
			currentUser = userRepository.findByUsername(username);
		}
		  // 記事を取得
	    Article article = repoArticle.findById(id)
	            .orElseThrow(() -> new RuntimeException("記事が見つかりません"));
		Date createAt = new Date();
		Comment comment = new Comment();
		comment.setArticle(article);
		comment.setUser(currentUser);
		comment.setContent(commentDto.getContent());
		comment.setCreatedAt(createAt);
		
		repoComment.save(comment);
		return "redirect:/articles/comment?id=" + id ;
	}
	
	/*
	 * コメント編集
	 */
	@PostMapping("/comments/edit")
	public String editComment(@RequestParam int commentId,
			@RequestParam int articleId,
			@Valid @ModelAttribute CommentDto commentDto) {
	    Comment comment = repoComment.findById(commentId).get();
	    comment.setContent(commentDto.getContent());
		repoComment.save(comment);

	    return "redirect:/articles/comment?id=" + articleId ;
	}
	/*
	 * コメントの削除
	 */
	@PostMapping("/comments/delete")
	public String deleteComment(@RequestParam int commentId,
			@RequestParam int articleId) {
	   Comment comment = repoComment.findById(commentId).get();
	   repoComment.delete(comment);
	    return "redirect:/articles/comment?id=" + articleId ;
	}

}
