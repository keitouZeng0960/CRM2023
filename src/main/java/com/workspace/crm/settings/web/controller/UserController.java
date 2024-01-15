package com.workspace.crm.settings.web.controller;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.commons.pojo.ReturnObject;
import com.workspace.crm.commons.utils.DateUtils;
import com.workspace.crm.settings.pojo.User;
import com.workspace.crm.settings.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /*这个url要和controller方法访问的资源目录保持一致*/
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "/settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //销毁cookie
        Cookie c1 = new Cookie("loginAct","0");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","0");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //清除session
        session.invalidate();
        return "redirect:/";
    }


    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        //调用业务
        User user = (User) userService.queryUserByLoginActAndPwd(map);
        session.setAttribute(Constants.SYSTEM_USER_SESSION,user);


        //json字符串对象
        ReturnObject ro = new ReturnObject();

        //判断
        if (user == null) {
            //用户名或密码错误并给json字符串封装信息
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("输入的用户名或密码错误");
        } else {
            if (DateUtils.dateToString(new Date()).compareTo(user.getExpireTime()) > 0) {
                //用户已过期
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())) {
                //用户已被锁定
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("该账号已被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //ip地址无法登录
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("IP受限");
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                //登录成功，判断是否记住密码
                if("true".equals(isRemPwd)){
                    //如果记住，则写cookie,必须要调用user对象的属性
                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(60*60*24*10);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(60*60*24*10);
                    response.addCookie(c2);

                }else{
                    //如果没有勾选，则写一个过期时间为0的cookie,注意cookie的名称必须相同
                    Cookie c1 = new Cookie("loginAct","0");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd","0");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        //登录成功
        return ro;
    }
}
