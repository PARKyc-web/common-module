package com.parkyc.comm_module.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/page")
@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage(){
        return "/login/login";
    }
}
