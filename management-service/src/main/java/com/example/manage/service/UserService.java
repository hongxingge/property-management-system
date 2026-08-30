package com.example.manage.service;

import com.example.manage.bean.UserBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    boolean addUser(UserBean userBean);

    boolean updateUserByUid(UserBean userBean);

    boolean deleteUserByUid(String uid);

    List<UserBean> queryUser();

    UserBean queryUserByPhone(String phone);
}
