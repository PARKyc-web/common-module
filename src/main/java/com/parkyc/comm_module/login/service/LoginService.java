package com.parkyc.comm_module.login.service;

import com.parkyc.comm_module.login.dto.LoginDTO;

public interface LoginService {

    LoginDTO.Response login(LoginDTO.Request loginInfo);
}
