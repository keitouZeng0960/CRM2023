package com.workspace.crm.workbench.web.controller;

import com.workspace.crm.settings.pojo.DictionaryValue;
import com.workspace.crm.settings.pojo.User;
import com.workspace.crm.settings.service.DictionaryValueService;
import com.workspace.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ClueController {
    @Autowired
    DictionaryValueService dictionaryValueService;
    @Autowired
    UserService userService;

    @RequestMapping("/workbench/clue/toClueIndex.do")
    public ModelAndView toClueIndex(){
        ModelAndView mv=new ModelAndView();
        List<User> userList=userService.selectAllUsers();
        List<DictionaryValue> clueStateList = dictionaryValueService.queryValueByType("clueState");
        List<DictionaryValue> sourceList = dictionaryValueService.queryValueByType("source");
        List<DictionaryValue> appellationList = dictionaryValueService.queryValueByType("appellation");
        mv.addObject("clueStateList",clueStateList);
        mv.addObject("sourceList",sourceList);
        mv.addObject("appellationList",appellationList);
        mv.addObject("userList",userList);
        mv.setViewName("workbench/clue/index");
        return mv;
    }

}
