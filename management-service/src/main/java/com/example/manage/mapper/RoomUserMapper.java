package com.example.manage.mapper;

import com.example.manage.bean.RoomUserBean;
import com.example.manage.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomUserMapper {
    int addRoomUser(RoomUserBean roomUserBean);

    List<RoomUserBean> queryRoomUser();

    int deleteRoomUserById(String id);

    RoomUserBean getRoomUserByPhone(String phone);

    int updateRoomUserById(RoomUserBean roomUserBean);

    int updatePwd(RoomUserBean roomUserBean);

    List<RoomUserBean> queryRoomUserByCondition(String condition);

    RoomUserBean getRoomUserById(String id);
}
