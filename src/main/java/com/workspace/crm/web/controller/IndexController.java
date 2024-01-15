package com.workspace.crm.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
//   所有访问该项目的请求全部交由该方法进行处理
    @RequestMapping("/")
    public String index(HttpServletRequest request){return "index";}

}
