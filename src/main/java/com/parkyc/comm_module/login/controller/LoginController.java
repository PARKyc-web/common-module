package com.parkyc.comm_module.login.controller;

import com.parkyc.comm_module.common.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/lgn")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtProvider jwtProvider;

    // /api/v1/lgn/login
    @PostMapping("/login")
    public String login(){
        return "";
    }


}
