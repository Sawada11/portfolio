package com.protfolio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.protfolio.article.ArticlesRepository;
import com.protfolio.comment.CommentRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ArticlesRepository articleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * ログインユーザー情報を取得
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserCustomDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole())
            );
    }
    
    /**
     * 新しいユーザーを作成し、データベースに保存
     * パスワードは自動的にエンコード
     *
     */
    public void createUser(UserEntity user) {
//    	ユーザー名の重複チェック
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("ユーザー名は既に使用されています");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    
    /**
     * 既存のユーザー情報を更新
     * ユーザー名は常に更新され、パスワードは新しい値が提供された場合のみ更新
     * 更新されたパスワードは自動的にエンコード
     *
     */
    public void updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));
        existingUser.setUsername(user.getUsername());
        if (!user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }
    
    /*
     * ユーザーを削除
     */
    public void deleteUser(String username) {
    	UserEntity user = userRepository.findByUsername(username);
    	if(user != null) {
    		userRepository.delete(user);
    	} else {
    		throw new UsernameNotFoundException("ユーザーが存在しません。");
    	}
    }
 
}