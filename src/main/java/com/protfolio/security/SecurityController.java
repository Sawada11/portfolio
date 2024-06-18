package com.protfolio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.protfolio.dto.UserDto;
import com.protfolio.repository.UserRepository;

@RequestMapping("/")
@Controller
public class SecurityController {
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	//新規作成画面を表示
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "security/register";
	}
	
	//ユーザーを保存
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
		userService.save(userDto);
		model.addAttribute("message", "ユーザーを作成しました");
		return "security/register";
	}
	
	@GetMapping("/login")
	public String login() {
		return "security/login";
	}
	
//	ユーザーネームを表示
//	@RequestMapping("/")
//	public String userPage (Model model, Principal principal) {
//		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
//		model.addAttribute("user", userDetails);
//		return "index";
//	}
//	

	
}
