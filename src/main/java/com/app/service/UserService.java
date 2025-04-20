package com.app.service;
import com.app.entity.User;
import com.app.payload.LoginDto;
import com.app.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private JWTservice jwTservice;

    public UserService(UserRepository userRepository, JWTservice jwTservice) {
        this.userRepository = userRepository;
        this.jwTservice = jwTservice;
    }

    public User createUsers(User user){
      //  user.setPassword(passwordEncoder.encode(user.getPassword()));
      //  String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
//        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5)));
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(hashpw);
        User save = userRepository.save(user);
        return save;
    }

    public String userLoggin(LoginDto dto){
        Optional<User> byUsername = userRepository.findByUsername(dto.getUsername());
        if(byUsername.isPresent()){
            User user = byUsername.get();
            if(BCrypt.checkpw(dto.getPassword(), user.getPassword())){
                String token = jwTservice.generateToken(user.getUsername());
                return token;
            }
        }
        return null;
    }
}
