package com.example.manage.service.imp;

import com.example.manage.bean.UserBean;
import com.example.manage.mapper.UserMapper;
import com.example.manage.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserServiceImp implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public boolean addUser(UserBean userBean) {
        int count = userMapper.addUser(userBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserByUid(UserBean userBean) {
        int count = userMapper.updateUserByUid(userBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserByUid(String uid) {
        int count = userMapper.deleteUserByUid(uid);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<UserBean> queryUser() {
        return userMapper.queryUser();
    }

    @Override
    public UserBean queryUserByPhone(String phone) {
        return userMapper.queryUserByPhone(phone);
    }
}
