package com.workspace.crm.settings.web.interceptor;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.settings.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class loginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SYSTEM_USER_SESSION);
        if(user == null){
            //拦截成功后，返回登录页面
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }
}
