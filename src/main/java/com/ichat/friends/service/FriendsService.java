package com.ichat.friends.service;

import com.ichat.friends.entity.vo.FriendRequestVO;
import com.ichat.friends.entity.vo.MyFriendsVO;

import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 14:51
 * @Description : 好友业务层接口
 */
public interface FriendsService {

    /**
     * @Description: 搜索朋友的前置条件
     */
    Integer preConditionSearchFriends(String myUserId, String friendUsername);

    /**
     * @Description: 添加好友请求记录，保存到数据库
     */
    void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * @Description: 查询好友请求
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * @Description: 删除好友记录
     */
    void deleteFriendRequest(String sendUserId, String accpetUserId);

    /**
     * @Description: 通过好友记录
     *                1. 保存好友
     *                2. 逆向保存好友
     *                3. 删除好友请求记录
     */
    void passFriendRequest(String sendUserId, String accpetUserId);

    /**
     * @Description: 查询好友列表
     */
    List<MyFriendsVO> queryMyFriends(String userId);

}
