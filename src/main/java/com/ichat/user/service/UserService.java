package com.ichat.user.service;

import com.ichat.netty.vo.ChatMsg;
import com.ichat.user.entity.Users;
import com.ichat.friends.entity.vo.FriendRequestVO;
import com.ichat.friends.entity.vo.MyFriendsVO;

import java.util.List;

/**
 * @author glw
 * @date 2018/11/14 17:08
 */
public interface UserService {

    /**
     * @Description: 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * @Description: 判断用户是否存在
     */
    public Users queryUserForLogin(String username, String password);

    /**
     * @Description: 用户注册
     */
    public Users saveUser(Users user);

    /**
     * @Description: 修改用户记录
     */
    public Users updateUserInfo(Users user);

    /**
     * @Description: 搜索朋友的前置条件
     */
    public Integer preConditionSearchFriends(String myUserId, String friendUsername);

    /**
     * @Description: 根据用户名查询用户对象
     */
    public Users queryUserInfoByUsername(String sername);

    /**
     * @Description: 添加好友请求记录，保存到数据库
     */
    public void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * @Description: 查询好友请求
     */
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

     /**
     * @Description: 删除好友记录
     */
    public void deleteFriendRequest(String sendUserId, String accpetUserId);

    /**
     * @Description: 通过好友记录
     *                1. 保存好友
     *                2. 逆向保存好友
     *                3. 删除好友请求记录
     */
    public void passFriendRequest(String sendUserId, String accpetUserId);

    /**
     * @Description: 查询好友列表
     */
    public List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * @Description: 保存聊天消息到数据库
     */
    public String saveMsg(ChatMsg chatMsg);

    /**
     * @Description: 批量签收消息
     */
    public void updateMsgSigned(List<String> msgIdList);

    /**
     * @Description: 获取未签收消息列表
     */
    public List<com.ichat.chat.entity.ChatMsg> getUnReadMsgList(String acceptUserId);
}
