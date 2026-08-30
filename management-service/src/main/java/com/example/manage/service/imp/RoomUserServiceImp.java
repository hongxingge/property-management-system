package com.example.manage.service.imp;

import com.example.manage.bean.RoomUserBean;
import com.example.manage.mapper.RoomUserMapper;
import com.example.manage.service.RoomUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RoomUserServiceImp implements RoomUserService {

    @Resource
    RoomUserMapper roomUserMapper;

    @Override
    public boolean addRoomUser(RoomUserBean roomUserBean) {
        int count = roomUserMapper.addRoomUser(roomUserBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<RoomUserBean> queryRoomUser() {
        return roomUserMapper.queryRoomUser();
    }

    @Override
    public boolean deleteRoomUserById(String id) {
        int count = roomUserMapper.deleteRoomUserById(id);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public RoomUserBean getRoomUserByPhone(String phone) {
        return roomUserMapper.getRoomUserByPhone(phone);
    }

    @Override
    public boolean updateRoomUserById(RoomUserBean roomUserBean) {
        int count = roomUserMapper.updateRoomUserById(roomUserBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePwd(RoomUserBean roomUserBean) {
        int count = roomUserMapper.updatePwd(roomUserBean);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<RoomUserBean> queryRoomUserByCondition(String condition) {
        return roomUserMapper.queryRoomUserByCondition(condition);
    }

    @Override
    public RoomUserBean getRoomUserById(String id) {
        return roomUserMapper.getRoomUserById(id);
    }
}
