package com.protfolio.profile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.protfolio.article.ArticleDto;
import com.protfolio.article.ArticleEntity;
import com.protfolio.article.ArticleService;
import com.protfolio.article.ArticlesRepository;
import com.protfolio.user.UserEntity;
import com.protfolio.user.UserRepository;
import com.protfolio.user.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ArticlesRepository articlesRepository;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserService userService;
	
	/*
	 * ログインユーザーの記事と情報を取得
	 */
	@GetMapping
	public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails,
			Principal principal) {
		 String username = userDetails.getUsername();
		 
		 UserEntity currentUser = userRepository.findByUsername(username);
//		ログインユーザーが投稿した記事を取得    
		List<ArticleEntity> currentArticles = articleService.getArticlesByUser(currentUser);
//		ログインユーザー情報を取得
		UserDetails user = userService.loadUserByUsername(principal.getName());
		model.addAttribute("user",user);
			model.addAttribute("articles", currentArticles);
			return "profile/profileIndex";
	}

	/*
	 * 編集画面
	 */
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id) {
		try {
			ArticleEntity article = articlesRepository.findById(id).get();
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
		return "articles/edit";
	}
	
	/*
	 * 編集機能
	 */
	@PostMapping("/edit")
	public String editArticle(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute ArticleDto articleDto,
			BindingResult result) {
		if(articleDto.getImageFileName().isEmpty()) {
			result.addError(new FieldError("articleDto", "imageFileName", "画像が挿入されていません"));
		}
		ArticleEntity article = articlesRepository.findById(id).get();
		
		if(result.hasErrors()) {
			model.addAttribute("article", article); //エラー処理後、入力データを保持
			return "articles/edit";
		}
	try {
		
		if(!articleDto.getImageFileName().isEmpty()) {
			String uploadDir = "public/images/";
			Path oldImagePath = Paths.get(uploadDir + article.getImageFileName());
//			画像データを削除
			try {
				Files.delete(oldImagePath);
			}catch(Exception ex) {
				System.out.println("例外:" + ex.getMessage());
			}
			
			MultipartFile image = articleDto.getImageFileName();
			Date createdAt = new Date();
			String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
			
//			画像データを指定のディレクトリにコピー
			try (InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
			}
			article.setImageFileName(storageFileName);
			article.setUser(articleDto.getUser());
			article.setTitle(articleDto.getTitle());
			article.setContent(articleDto.getContent());
			
			articlesRepository.save(article);
		}
		}catch(Exception ex) {
			System.out.println("例外:"+ ex.getMessage());
		}
		return "redirect:/profile";
	}
	
	/*
	 * 削除画面
	 */
	@GetMapping("/delete")
	public String showDeletePage(
			Model model,
			@RequestParam int id) {
		try {
			ArticleEntity article = articlesRepository.findById(id).get();
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
		return "articles/delete";
	}
	
	/*
	 * 削除機能
	 */
	@PostMapping("/delete")
	public String deleteArticle(
			@RequestParam int id
			) {
		try {
			ArticleEntity article = articlesRepository.findById(id).get();
			
			Path imagePath = Paths.get("public/images/" + article.getImageFileName());
			
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("例外:" + ex.getMessage());
			}
			articlesRepository.delete(article);
		}catch(Exception ex) {
			System.out.println("例外:" + ex.getMessage());
		}
		return "redirect:/profile";
	}
	
	/*
	 * プロフィール設定画面
	 */
	@GetMapping("setting")
	public String ProfileSettings() {
		return "profile/setting";
	}
}
