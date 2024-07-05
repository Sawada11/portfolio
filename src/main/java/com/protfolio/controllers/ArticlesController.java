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
	

}
