package com.workspace.crm.settings.service.impl;

import com.workspace.crm.settings.mapper.UserMapper;
import com.workspace.crm.settings.pojo.User;
import com.workspace.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return (User) userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> selectAllUsers(){
        return userMapper.selectAllUsers();
    }
}
