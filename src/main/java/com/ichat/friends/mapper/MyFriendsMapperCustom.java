package com.ichat.friends.mapper;

import com.ichat.friends.entity.MyFriends;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.common.utils.MyMapper;

import java.util.List;

/**
 * Create by glw
 * 2018/12/2 19:20
 */
public interface MyFriendsMapperCustom  extends MyMapper<MyFriends> {

    List<MyFriendsVO> queryMyFriends(String userId);

}
