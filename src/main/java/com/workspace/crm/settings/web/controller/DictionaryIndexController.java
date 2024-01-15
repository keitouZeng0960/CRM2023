package com.workspace.crm.settings.web.controller;

import com.workspace.crm.settings.pojo.DictionaryType;
import com.workspace.crm.settings.service.DictionaryTypeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DictionaryIndexController {


    @RequestMapping("/settings/dictionary/index.do")
    public String index(){return "settings/dictionary/index";}
}


