package com.ichat.mapper;

import com.ichat.pojo.Users;
import com.ichat.pojo.vo.FriendRequestVO;
import com.ichat.utils.MyMapper;

import java.util.List;

public interface UsersMapperCustom extends MyMapper<Users> {

    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

}