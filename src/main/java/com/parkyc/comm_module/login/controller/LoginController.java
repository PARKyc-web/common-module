package com.parkyc.comm_module.login.controller;

import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import com.parkyc.comm_module.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/lgn")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // /api/v1/lgn/login
    @PostMapping("/login")
    public LoginDTO.Response login(@RequestBody LoginDTO.Request loginInfo){
        LoginDTO.Response response = loginService.login(loginInfo);

        return response;
    }


}
