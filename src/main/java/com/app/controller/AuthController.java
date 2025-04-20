package com.app.controller;
import com.app.entity.User;
import com.app.payload.LoginDto;
import com.app.payload.TokenDto;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
//http://localhost:8080/api/v1/auth/signUp
    @PostMapping("/signUp")
    public ResponseEntity<?> createUser(@RequestBody User user){
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if(byUsername.isPresent()){
            return new ResponseEntity<>("Username Is Already Registered", HttpStatus.NOT_FOUND) ;
        }
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if(byEmail.isPresent()){
            return new ResponseEntity<>("Email Is Already Registered", HttpStatus.NOT_FOUND) ;
        }
        User users = userService.createUsers(user);
        return new ResponseEntity<>("User Registered Successfully", HttpStatus.CREATED);
    }
// http://localhost:8080/api/v1/auth/users-LogIN
    @PostMapping("/users-LogIN")
    public ResponseEntity<?>  userLogIN(@RequestBody LoginDto dto){
        String token = userService.userLoggin(dto);
        if(token != null){
            TokenDto tokensdto=new TokenDto();
            tokensdto.setToken(token);
            tokensdto.setType("JWT");
            return new ResponseEntity<>(tokensdto,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Invalid Credentials",HttpStatus.BAD_REQUEST);
        }
    }
    //http://localhost:8080//api/v1/auth/message
    @PostMapping("/message")
    public String demochecking(){
        return "message";
    }
}
