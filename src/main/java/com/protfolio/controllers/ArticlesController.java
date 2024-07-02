package com.protfolio.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import com.protfolio.dto.ArticleDto;
import com.protfolio.models.Article;
import com.protfolio.repository.ArticlesRepository;
import com.protfolio.security.User;
import com.protfolio.security.UserRepository;
import com.protfolio.security.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticlesController {

	@Autowired
	private ArticlesRepository repo;
	
    @Autowired
    private UserService userService;
	
    @Autowired
    private UserRepository userRepository;
	/*
	 * 	記事を表示
	 *  記事はIDの降順でソート
	 *  ページネーション12記事表示
	 */
    @GetMapping({"", "/"})
    public String showArticleList(Model model, 
                                  Principal principal, 
                                  @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Article> articlesPage = repo.findAll(pageable);
        model.addAttribute("articlesPage", articlesPage);
        
        UserDetails user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        
        return "articles/index";
    }
	/*
	 * 投稿ページを表示
	 */
	@GetMapping("/create")
	public String showCreatePage(Model model, @AuthenticationPrincipal UserDetails userDetails,Principal principal) {
	    ArticleDto articleDto = new ArticleDto();
	   
	    model.addAttribute("articleDto", articleDto);
	    return "articles/create";
	}
	
	/*
	 * 投稿機能
	 */
	@PostMapping("/create")
	public String createArticle(
			@Valid @ModelAttribute ArticleDto articleDto,
			BindingResult result,   
			@AuthenticationPrincipal UserDetails userDetails) {

		if(articleDto.getImageFileName().isEmpty()) {
			result.addError(new FieldError("articleDto","imageFileName","画像が挿入されていません。"));
		}
		
		if(result.hasErrors()) {
			return "articles/create";
		}

		MultipartFile image = articleDto.getImageFileName();
		Date createAt = new Date();
//		 作成時刻と元のファイル名を組み合わせて、一意の保存ファイル名を生成
		String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();
		
		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir); 
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
//			画像データを指定のディレクトリにコピー
			try (InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
			}
			
		}catch(Exception e){
			System.out.println("例外:" + e.getMessage());
		}
		
//	     ログインユーザーの取得
		User currentUser = null;
	    if (userDetails != null) {
	        String username = userDetails.getUsername();
	        currentUser = userRepository.findByUsername(username);  
	    }

		Article article = new Article();
		article.setUser(currentUser);
		article.setTitle(articleDto.getTitle());
		article.setContent(articleDto.getContent());
		article.setImageFileName(storageFileName);
		article.setCreatedAt(createAt);
		
		repo.save(article);
		
		return "redirect:/articles";
	}
	

	/*
	 * 編集画面
	 */
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id) {
		try {
			Article article = repo.findById(id).get();
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
	try {
		Article article = repo.findById(id).get();
		
		if(result.hasErrors()) {
			return "articles/edit";
		}
		
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
			
			repo.save(article);
		}
		}catch(Exception ex) {
			System.out.println("例外:"+ ex.getMessage());
		}
		return "redirect:/articles";
	}
	
	/*
	 * 削除画面
	 */
	@GetMapping("/delete")
	public String showDeletePage(
			Model model,
			@RequestParam int id) {
		try {
			Article article = repo.findById(id).get();
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
			Article article = repo.findById(id).get();
			
			Path imagePath = Paths.get("public/images/" + article.getImageFileName());
			
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("例外:" + ex.getMessage());
			}
			repo.delete(article);
		}catch(Exception ex) {
			System.out.println("例外:" + ex.getMessage());
		}
		return "redirect:/articles";
	}
}
