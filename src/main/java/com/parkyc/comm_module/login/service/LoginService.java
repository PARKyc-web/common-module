package com.parkyc.comm_module.login.service;

import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

    JwtProvider.Response webLogin(LoginDTO.Request loginInfo);

    JwtProvider.Response appLogin(LoginDTO.Request loginInfo);

    JwtProvider.Response webRefresh(HttpServletRequest request);

    JwtProvider.Response appRefresh(HttpServletRequest request);
}
