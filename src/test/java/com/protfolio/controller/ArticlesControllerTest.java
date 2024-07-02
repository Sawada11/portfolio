package com.protfolio.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import com.protfolio.controllers.ArticlesController;
import com.protfolio.models.Article;
import com.protfolio.models.User;
import com.protfolio.repository.ArticlesRepository;

@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {
/*
 * 
 * テスト用ユーザーを作成
 * ログイン認証のテストを作成
 * 
 * 
 */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticlesRepository repo;

    private Article testArticle;

    @BeforeEach
    void setUp() {
    	User testUser = new User();
        testArticle = new Article();
        testArticle.setId(1);
        testArticle.setUser(testUser);
        testArticle.setTitle("Test Title");
        testArticle.setContent("Test Content");
        testArticle.setImageFileName("test.jpg");
    }

    @Test
    public void testShowArticleList() throws Exception {
        when(repo.findAll(any(Sort.class))).thenReturn(Arrays.asList(testArticle));

        mockMvc.perform(get("/articles"))
               .andExpect(status().isOk())
               .andExpect(view().name("articles/index"))
               .andExpect(model().attributeExists("articles"));

        verify(repo).findAll(any(Sort.class));
    }

//    @Test
//    public void testShowCreatePage() throws Exception {
//        mockMvc.perform(get("/articles/create"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("articles/create"))
//               .andExpect(model().attributeExists("articleDto"));
//    }
//
//    @Test
//    public void testCreateArticle() throws Exception {
//        MockMultipartFile imageFile = new MockMultipartFile("imageFileName", "test.jpg", "image/jpeg", "test image content".getBytes());
//
//        mockMvc.perform(multipart("/articles/create")
//               .file(imageFile)
//               .param("user", "testUser")
//               .param("title", "Test Title")
//               .param("content", "Test Content"))
//               .andExpect(status().is3xxRedirection())
//               .andExpect(redirectedUrl("/articles"));
//
//        verify(repo).save(any(Article.class));
//    }
//
//    @Test
//    public void testShowEditPage() throws Exception {
//        when(repo.findById(1)).thenReturn(Optional.of(testArticle));
//
//        mockMvc.perform(get("/articles/edit").param("id", "1"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("articles/edit"))
//               .andExpect(model().attributeExists("article", "articleDto"));
//
//        verify(repo).findById(1);
//    }
//
//    @Test
//    public void testEditArticle() throws Exception {
//        when(repo.findById(1)).thenReturn(Optional.of(testArticle));
//
//        MockMultipartFile imageFile = new MockMultipartFile("imageFileName", "test.jpg", "image/jpeg", "test image content".getBytes());
//
//        mockMvc.perform(multipart("/articles/edit")
//               .file(imageFile)
//               .param("id", "1")
//               .param("user", "updatedUser")
//               .param("title", "Updated Title")
//               .param("content", "Updated Content"))
//               .andExpect(status().is3xxRedirection())
//               .andExpect(redirectedUrl("/articles"));
//
//        verify(repo).save(any(Article.class));
//    }
//
//    @Test
//    public void testShowDeletePage() throws Exception {
//        when(repo.findById(1)).thenReturn(Optional.of(testArticle));
//
//        mockMvc.perform(get("/articles/delete").param("id", "1"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("articles/delete"))
//               .andExpect(model().attributeExists("article", "articleDto"));
//
//        verify(repo).findById(1);
//    }
//
//    @Test
//    public void testDeleteArticle() throws Exception {
//        when(repo.findById(1)).thenReturn(Optional.of(testArticle));
//
//        mockMvc.perform(post("/articles/delete").param("id", "1"))
//               .andExpect(status().is3xxRedirection())
//               .andExpect(redirectedUrl("/articles"));
//
//        verify(repo).delete(any(Article.class));
//    }
}