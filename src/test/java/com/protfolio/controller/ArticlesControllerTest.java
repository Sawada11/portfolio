package com.protfolio.controller;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.protfolio.article.ArticleEntity;
import com.protfolio.article.ArticlesController;
import com.protfolio.article.ArticlesRepository;
import com.protfolio.user.UserEntity;
import com.protfolio.user.UserService;

@WebMvcTest(ArticlesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticlesRepository repo;

    @MockBean
    private UserService userService;  // UserServiceをモック

    private ArticleEntity testArticle;

    @BeforeEach
    void setUp() {
        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testArticle = new ArticleEntity();
        testArticle.setId(1);
        testArticle.setUser(testUser);
        testArticle.setTitle("Test Title");
        testArticle.setContent("Test Content");
        testArticle.setImageFileName("test.jpg");
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testShowArticleList() throws Exception {
        when(repo.findAll(any(Sort.class))).thenReturn(Arrays.asList(testArticle));

        mockMvc.perform(get("/articles").with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(status().isOk())
               .andExpect(view().name("articles/index"))
               .andExpect(model().attributeExists("articles"));

        verify(repo).findAll(any(Sort.class));
    }

    // ログイン認証のテスト
    @Test
    public void testLoginAuthentication() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "password")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/articles"));
    }
}