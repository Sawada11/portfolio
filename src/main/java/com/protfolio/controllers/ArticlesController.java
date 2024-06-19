package com.protfolio.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticlesController {

	@Autowired
	private ArticlesRepository repo;
	
	/*
	 * 	記事を表示
	 *  記事はIDの降順でソートされる。
	 */
	@GetMapping({"", "/"})
	public String showArticleList(Model model) {
		List<Article> articles = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("articles", articles);
		return "articles/index";
	}
	
	/*
	 * 投稿ページを表示
	 */
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ArticleDto articleDto = new ArticleDto();
		model.addAttribute("articleDto", articleDto);

		return "articles/create";
	}
	
	/*
	 * 投稿機能
	 */
	@PostMapping("/create")
	public String createProduct(
			@Valid @ModelAttribute ArticleDto articleDto,
			BindingResult result) {

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
		
		Article article = new Article();
		article.setUser(articleDto.getUser());
		article.setTitle(articleDto.getTitle());
		article.setContent(articleDto.getContent());
		article.setImageFileName(storageFileName);
		article.setCreatedAt(createAt);
		
		repo.save(article);
		
		return "redirect:/articles";
	}
}
