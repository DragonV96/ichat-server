package com.ichat.friends.service.impl;

import com.ichat.common.enums.MsgActionEnum;
import com.ichat.common.enums.SearchFriendsStatusEnum;
import com.ichat.common.utils.AESUtils;
import com.ichat.common.utils.JsonUtils;
import com.ichat.friends.entity.FriendsRequest;
import com.ichat.friends.entity.MyFriends;
import com.ichat.friends.entity.vo.FriendRequestVO;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.friends.mapper.FriendsRequestMapper;
import com.ichat.friends.mapper.FriendsRequestMapperCustom;
import com.ichat.friends.mapper.MyFriendsMapper;
import com.ichat.friends.mapper.MyFriendsMapperCustom;
import com.ichat.friends.service.FriendsService;
import com.ichat.netty.service.UserChannelRelationship;
import com.ichat.netty.vo.DataContent;
import com.ichat.user.entity.Users;
import com.ichat.user.service.UserService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 14:52
 * @Description : 好友业务层实现类
 */
@Service
public class FriendsServiceImpl implements FriendsService {

    @Autowired
    private UserService userService;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private FriendsRequestMapperCustom friendsRequestMapperCustom;

    @Autowired
    private MyFriendsMapperCustom myFriendsMapperCustom;

    @Autowired
    private Sid sid;

    /**
     * 添加好友前置条件
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Integer preConditionSearchFriends(String myUserId, String friendUsername) {

        Users user = userService.queryUserInfoByUsername(friendUsername);

        // 1. 搜索的用户不存在，返回 [无此用户]
        if (user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        // 2. 搜索的账号是自己，返回 [不能添加自己为好友]
        if (user.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        // 3. 搜索的用户已经是好友，返回 [好友已存在，不能重复添加好友]
        Example myFriends = new Example(MyFriends.class);
        Example.Criteria myFriendsCondition = myFriends.createCriteria();
        myFriendsCondition.andEqualTo("myUserId", myUserId);    // 查询自己的id
        myFriendsCondition.andEqualTo("myFriendUserId", user.getId());  // 查询好友的id
        MyFriends myFriendsRelationShip = myFriendsMapper.selectOneByExample(myFriends);
        if (myFriendsRelationShip != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    /**
     * 发送好友请求（注意：连续点击只能发送一次好友请求）
     * @param myUserId
     * @param friendUsername
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

        // 根据用户名把朋友信息查询出来
        Users friend = userService.queryUserInfoByUsername(friendUsername);

        // 1. 查询发送好友请求记录表
        Example friendExample = new Example(FriendsRequest.class);
        Criteria friendCondition = friendExample.createCriteria();
        friendCondition.andEqualTo("sendUserId", myUserId);
        friendCondition.andEqualTo("acceptUserId", friend.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(friendExample);

        if(friendsRequest == null) {
            // 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
            String requestId = sid.nextShort();

            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);
        }
    }

    /**
     * 查询好友申请列表
     * @param acceptUserId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return friendsRequestMapperCustom.queryFriendRequestList(acceptUserId);
    }

    /**
     * 忽略好友，删除好友请求
     * @param sendUserId
     * @param accpetUserId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteFriendRequest(String sendUserId, String accpetUserId) {
        // 1. 查询发送好友请求记录表
        Example friendExample = new Example(FriendsRequest.class);
        Criteria friendCondition = friendExample.createCriteria();
        friendCondition.andEqualTo("sendUserId", sendUserId);
        friendCondition.andEqualTo("acceptUserId", accpetUserId);
        friendsRequestMapper.deleteByExample(friendExample);
    }

    /**
     * 通过好友请求
     * @param sendUserId
     * @param accpetUserId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void passFriendRequest(String sendUserId, String accpetUserId) {
        saveFriends(sendUserId, accpetUserId);
        saveFriends(accpetUserId, sendUserId);
        deleteFriendRequest(sendUserId, accpetUserId);

        Channel sendChannel = UserChannelRelationship.get(sendUserId);
        try{
            if (sendChannel != null) {
                // 使用websocket主动推送消息到请求发起者，更新其通讯录列表为最新状态
                DataContent dataContent = new DataContent();
                dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

                // 将消息加密发送
                String contentMsg = JsonUtils.objectToJson(dataContent);
                contentMsg = AESUtils.aesEncrypt(contentMsg);

                sendChannel.writeAndFlush(new TextWebSocketFrame(contentMsg));
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    /**
     * 保存添加的好友关联关系到数据库
     * @param sendUserId
     * @param acceptUserId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    protected void saveFriends(String sendUserId, String acceptUserId){
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    /**
     * 查询用户好友列表
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        List<MyFriendsVO> myFriends = myFriendsMapperCustom.queryMyFriends(userId);
        return myFriends;
    }
}
