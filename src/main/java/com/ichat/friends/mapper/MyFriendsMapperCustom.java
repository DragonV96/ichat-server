package com.ichat.friends.mapper;

import com.ichat.user.entity.Users;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.utils.MyMapper;

import java.util.List;

/**
 * Create by glw
 * 2018/12/2 19:20
 */
public interface MyFriendsMapperCustom  extends MyMapper<Users> {

    public List<MyFriendsVO> queryMyFriends(String userId);

}
