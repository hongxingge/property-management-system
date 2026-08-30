package com.example.manage.mapper;

import com.example.manage.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserMapper {

    int addUser(UserBean userBean);

    int updateUserByUid(UserBean userBean);

    int deleteUserByUid(String uid);

    List<UserBean> queryUser();

    UserBean queryUserByPhone(String phone);

}
