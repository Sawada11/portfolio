package com.protfolio.admin.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.protfolio.security.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ArticlesRepository articleRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UserService userService;
	/*
	 * 記事とユーザーを全データ表示
	 */
	@GetMapping({"","/"})
	public String Admin(Model model,
			@PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Article> articlesPage = articleRepo.findAll(pageable);
		model.addAttribute("articlesPage", articlesPage);

		List<User> users = userRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("users",users);
		
		return "admin/index";
	}
	
	/*
	 * 削除画面
	 */
	@GetMapping("/delete")
	public String deleteAdmin(
			Model model,
			@RequestParam int id) {
		try {
			Article article = articleRepo.findById(id).get();
			model.addAttribute("article", article);
			
			ArticleDto articleDto = new ArticleDto();
			articleDto.setUser(article.getUser());
			articleDto.setTitle(article.getTitle());
			articleDto.setContent(article.getContent());
			
			model.addAttribute("articleDto",articleDto);
		}catch(Exception ex) {
			System.out.println("例外：" + ex.getMessage());
		}
		return "admin/delete";
	}
	
	/*
	 * 記事を削除
	 */
	@PostMapping("/delete")
	public String deleteArticle(@RequestParam int id) {
		try {
			Article article = articleRepo.findById(id).get();
			
			Path imagePath = Paths.get("public/images/" + article.getImageFileName());
			
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("例外：" + ex.getMessage());
			}
			articleRepo.delete(article);
			
		}catch(Exception ex) {
			System.out.println("例外：" + ex.getMessage());
		}
		return "redirect:/admin";
	}
	
	/*
	 * 記事コメントを削除
	 */
	@GetMapping("/comment")
	public String showCommentPage(Model model, @RequestParam int id,
			Principal principal) {
		CommentDto commentDto = new CommentDto();
		model.addAttribute("commentDto", commentDto);
		
//		コメント欄
		List<Comment> comments = commentRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("comments",comments);
//		ログインユーザー取得
		UserDetails user = userService.loadUserByUsername(principal.getName());
		
		model.addAttribute("user",user);
		try {
			Article article = articleRepo.findById(id).get();
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
		return "admin/comment";
	}
	
	/*
	 * コメント削除
	 */
	@PostMapping("/deleteComment")
	public String deleteComment(@RequestParam int commentId,
			@RequestParam int articleId) {
		Comment commnent = commentRepo.findById(commentId).get();
		commentRepo.delete(commnent);
		return "redirect:/admin/comment?id=" + articleId ;
	}
	
    /*
     * ユーザー削除確認ページ
     */
    @GetMapping("/deleteUser")
    public String showDeleteUser(Model model, @RequestParam Long id) {
    	try {
    		User user = userRepo.findById(id).get();

        	model.addAttribute("user",user);
    	}catch(Exception ex) {
    		System.out.println("例外：" + ex.getMessage());
    		return "redirect:/admin";
    	}
    	return "/admin/deleteUser";
    }
    
    /*
     * ユーザーを削除
     */
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
    	System.out.println("\\\\\6666&&&\\\\");
    	User user = userRepo.findById(id).get();
    	userRepo.delete(user);
    	return "redirect:/admin";
    }
    
}