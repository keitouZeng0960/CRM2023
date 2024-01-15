package com.workspace.crm.settings.service;

import com.workspace.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> selectAllUsers();
}
