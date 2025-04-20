package com.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/car")
public class Car {

    //http://localhost:8080/api/v1/car
    @PostMapping
    public String CarDetails(){
        return "added";
    }
}
