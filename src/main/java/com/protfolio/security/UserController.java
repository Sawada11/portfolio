package com.protfolio.security;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
    	UserDetails user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
    	return "/security/index";
    }
    
    /*
     * ユーザー登録ホーム
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/security/register";
    }
    
	/*
	 * 新規ユーザーを登録
	 */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/login";
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
     * ログインページを表示
     */
    @GetMapping("/login")
    public String login() {
        return "/security/login";
    }
}