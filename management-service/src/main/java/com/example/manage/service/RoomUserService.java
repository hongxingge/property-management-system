package com.example.manage.service;

import com.example.manage.bean.RoomUserBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomUserService {
    boolean addRoomUser(RoomUserBean roomUserBean);

    List<RoomUserBean> queryRoomUser();

    boolean deleteRoomUserById(String id);

    RoomUserBean getRoomUserByPhone(String phone);

    boolean updateRoomUserById(RoomUserBean roomUserBean);

    boolean updatePwd(RoomUserBean roomUserBean);

    List<RoomUserBean> queryRoomUserByCondition(String condition);

    RoomUserBean getRoomUserById(String id);
}
