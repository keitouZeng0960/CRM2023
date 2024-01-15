package com.workspace.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/workbench/main/toMainIndex.do")
    public String toMainIndex(){
        return "workbench/main/index";
    }
}
