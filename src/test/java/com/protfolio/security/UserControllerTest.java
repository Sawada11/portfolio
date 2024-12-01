package com.protfolio.security;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.protfolio.user.UserController;
import com.protfolio.user.UserEntity;
import com.protfolio.user.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    @WithMockUser // Spring Securityを使用している場合、認証をバイパス
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
//               .andDo(print()) // 詳細な結果を出力
               .andExpect(status().isOk()) //HTTPレスポンスの確認
               .andExpect(view().name("security/register"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser 
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/register")
               .with(csrf())  // CSRF トークンを追加
               .param("username", "newuser")
               .param("password", "password"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register"));  // リダイレクト先を確認

        verify(userService).createUser(any(UserEntity.class));
    }


    @Test
    @WithMockUser(username = "testuser")
    public void testShowEditForm() throws Exception {
        when(userService.loadUserByUsername("testuser")).thenReturn(testUser);

        mockMvc.perform(get("/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("security/edit"))
               .andExpect(model().attributeExists("user"));
    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    public void testEditUser() throws Exception {
//        mockMvc.perform(post("/edit")
//               .param("username", "updateduser"))
//               .andExpect(status().is3xxRedirection())
//               .andExpect(redirectedUrl("/articles"));
//
//        verify(userService).updateUser(any(User.class));
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    public void testShowDeleteUser() throws Exception {
//        when(userService.loadUserByUsername("testuser")).thenReturn(testUser);
//
//        mockMvc.perform(get("/delete"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("/security/delete"))
//               .andExpect(model().attributeExists("user"));
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    public void testDeleteUser() throws Exception {
//        mockMvc.perform(post("/delete"))
//               .andExpect(status().is3xxRedirection())
//               .andExpect(redirectedUrl("/login"));
//
//        verify(userService).deleteUser("testuser");
//    }
//
//    @Test
//    public void testShowLoginForm() throws Exception {
//        mockMvc.perform(get("/login"))
//               .andExpect(status().isOk())
//               .andExpect(view().name("security/login"))
//               .andExpect(model().attributeExists("user"));
//    }
}