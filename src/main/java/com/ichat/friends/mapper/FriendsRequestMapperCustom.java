package com.ichat.friends.mapper;

import com.ichat.common.utils.MyMapper;
import com.ichat.friends.entity.FriendsRequest;
import com.ichat.friends.entity.vo.FriendRequestVO;

import java.util.List;

/**
 * Create by glw
 * 2018/12/2 19:20
 */
public interface FriendsRequestMapperCustom extends MyMapper<FriendsRequest> {

    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

}
