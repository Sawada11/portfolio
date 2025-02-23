package com.protfolio.admin;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.protfolio.article.ArticleDto;
import com.protfolio.article.ArticleEntity;
import com.protfolio.article.ArticlesRepository;
import com.protfolio.comment.CommentDto;
import com.protfolio.comment.CommentEntity;
import com.protfolio.comment.CommentRepository;
import com.protfolio.user.UserEntity;
import com.protfolio.user.UserRepository;
import com.protfolio.user.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ArticlesRepository articleRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	/*
	 * 記事とユーザーを全データ表示
	 */
	@GetMapping({"","/"})
	public String Admin(Model model,
			@PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<ArticleEntity> articlesPage = articleRepository.findAll(pageable);
		model.addAttribute("articlesPage", articlesPage);

		List<UserEntity> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
			ArticleEntity article = articleRepository.findById(id).get();
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
			ArticleEntity article = articleRepository.findById(id).get();
			
			Path imagePath = Paths.get("public/images/" + article.getImageFileName());
			
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("例外：" + ex.getMessage());
			}
			articleRepository.delete(article);
			
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
		List<CommentEntity> comments = commentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("comments",comments);
//		ログインユーザー取得
		UserDetails user = userService.loadUserByUsername(principal.getName());
		
		model.addAttribute("user",user);
		try {
			ArticleEntity article = articleRepository.findById(id).get();
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
		CommentEntity commnent = commentRepository.findById(commentId).get();
		commentRepository.delete(commnent);
		return "redirect:/admin/comment?id=" + articleId ;
	}
	
    /*
     * ユーザー削除確認ページ
     */
    @GetMapping("/deleteUser")
    public String showDeleteUser(Model model, @RequestParam Long id) {
    	try {
    		UserEntity user = userRepository.findById(id).get();

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
    	UserEntity user = userRepository.findById(id).get();
    	userRepository.delete(user);
    	return "redirect:/admin";
    }
    
    /*
     * 管理者ユーザー登録ホーム
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
    	model.addAttribute("user", new UserEntity());
    	return "admin/register";
    }
    
    /*
     * 管理者ユーザーを登録
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserEntity user,
    			BindingResult bindingResult, Model model) {
    	if(bindingResult.hasErrors()) {
    		return "admin/register";
    	}
    	
    	try {
    		userService.createUser(user);
    	} catch(RuntimeException e) {
    		model.addAttribute("error", e.getMessage());
    		return "admin/register";
    	}
    	
    	return "redirect:/admin";
    }
    
    /*
     * 管理者ユーザーの設定画面
     */
    @GetMapping("/setting")
    public String setting() {
    	return "admin/adminSetting";
    }
    
    /*
     * 管理者のユーザーの編集画面
     */
    @GetMapping("/setting/edit")
    public String settingEdit(Model model, Principal principal) {
    	UserDetails user = userService.loadUserByUsername(principal.getName());
    	model.addAttribute("user",user);
    	return "admin/adminEdit";
    }
    
    /*
     * 管理者ユーザーの編集
     */
    @PostMapping("/setting/edit")
    public String settingUserEdit(@Valid @ModelAttribute("user") UserEntity user,
    		BindingResult bindingResult, Model model) {
    	if(bindingResult.hasErrors()) {
    		return "admin/adminEdit";
    	}
    	
    	try {
    		userService.updateUser(user);
    	} catch(RuntimeException e) {
    		model.addAttribute("error", e.getMessage());
    		return "admin/adminEdit";
    	}
    	return "redirect:/admin";
    }
}