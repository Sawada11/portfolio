package com.protfolio.security;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    /*
     * ユーザー登録ホーム
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "security/register";
    }
    
	/*
	 * 新規ユーザーを登録
	 */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
    		BindingResult bindingResult, Model model) {
    	if(bindingResult.hasErrors()) {
    		return "security/register";
    	}
    	
    	try {
    		 userService.createUser(user);
    	} catch(RuntimeException e) {
    		//ユーザー名やメールアドレスが既に存在する場合はエラー文を表示
    		model.addAttribute("error",e.getMessage());
    		return "security/register";
    	}
       
        return "redirect:/register";
    }

    /*
     * ユーザー情報編集フォームを表示
     */
    @GetMapping("/edit")
    public String showEditForm(Model model, Principal principal) {
        UserDetails user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "/security/edit";
    }
    
    /*
     * ユーザー情報を更新
     */
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/articles";
    }

    /*
     * ユーザー削除確認ページ
     */
    @GetMapping("/delete")
    public String showDeleteUser(Model model, Principal principal) {
    	UserDetails user = userService.loadUserByUsername(principal.getName());
    	model.addAttribute("user",user);
    	return "/security/delete";
    }
    
    /*
     * ユーザーを削除
     */
    @PostMapping("/delete")
    public String deleteUser(Principal principal) {
    	userService.deleteUser(principal.getName());
    	return "redirect:/login";
    }
    
    /*
     * ログインページを表示
     */
    @GetMapping("/login")
    public String login() {
        return "/security/login";
    }
    
    /*
     * ログイン入力チェック
     */
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("user") User user, 
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "security/login";
        }
        
        // ここで実際の認証を行う
        if (userService.authenticate(user.getUsername(), user.getPassword())) {
            return "redirect:/dashboard"; // 認証成功時のリダイレクト先
        } else {
            model.addAttribute("error", "ユーザー名またはパスワードが間違っています。");
            return "security/login";
        }
    }
}