package com.ichat.user.service.impl;

import com.ichat.chat.mapper.ChatMsgMapper;
import com.ichat.chat.mapper.ChatMsgMapperCustom;
import com.ichat.enums.MsgActionEnum;
import com.ichat.enums.MsgSignFlagEnum;
import com.ichat.enums.SearchFriendsStatusEnum;
import com.ichat.fastdfs.FastDFSClient;
import com.ichat.friends.mapper.FriendsRequestMapper;
import com.ichat.friends.mapper.MyFriendsMapper;
import com.ichat.friends.mapper.MyFriendsMapperCustom;
import com.ichat.netty.vo.ChatMsg;
import com.ichat.netty.vo.DataContent;
import com.ichat.netty.service.UserChannelRelationship;
import com.ichat.friends.entity.FriendsRequest;
import com.ichat.friends.entity.MyFriends;
import com.ichat.user.entity.Users;
import com.ichat.friends.entity.vo.FriendRequestVO;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.user.mapper.UsersMapper;
import com.ichat.user.mapper.UsersMapperCustom;
import com.ichat.user.service.UserService;
import com.ichat.utils.*;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author glw
 * @date 2018/11/14 17:10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Autowired
    private MyFriendsMapperCustom myFriendsMapperCustom;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private ChatMsgMapperCustom chatMsgMapperCustom;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    // 查询用户是否存在
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        user.setUsername(username);

        Users result = userMapper.selectOne(user);

        return result != null ? true : false;
    }

    // 查询用户是否登录
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

        // 利用逆向工具类查询用户登陆,省去了SQL语句的编写
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);

        Users result = userMapper.selectOneByExample(userExample);

        return result;
    }

    // 保存用户名
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {

        String userId = sid.nextShort();

        // 为每个用户生成一个唯一的二维码

//        String qrCodePath = "\\fastdfs\\tmp\\" + userId + "qrcode.png";
        String qrCodePath = "G:\\tmp\\" + userId + "qrcode.png";
        // ichat_qrcode:[username]  (商用需要加解密，如base64)
        qrCodeUtils.createQRCode(qrCodePath, "ichat_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "" ;
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodeUrl);

        user.setId(userId);
        userMapper.insert(user);
        return user;
    }

    // 更新用户名
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }
    //通过id查询user
    @Transactional(propagation = Propagation.SUPPORTS)
    private Users queryUserById(String userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    // 添加好友前置条件
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preConditionSearchFriends(String myUserId, String friendUsername) {

        Users user = queryUserInfoByUsername(friendUsername);

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
        Criteria myFriendsCondition = myFriends.createCriteria();
        myFriendsCondition.andEqualTo("myUserId", myUserId);    // 查询自己的id
        myFriendsCondition.andEqualTo("myFriendUserId", user.getId());  // 查询好友的id
        MyFriends myFriendsRelationShip = myFriendsMapper.selectOneByExample(myFriends);
        if (myFriendsRelationShip != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    // 根据用户名查询用户
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username) {
        Example user = new Example(Users.class);
        Criteria userCondition = user.createCriteria();
        userCondition.andEqualTo("username", username);
        return userMapper.selectOneByExample(user);
    }

    // 发送好友请求（注意：连续点击只能发送一次好友请求）
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

        // 根据用户名把朋友信息查询出来
        Users friend = queryUserInfoByUsername(friendUsername);

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

    // 查询好友申请列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return usersMapperCustom.queryFriendRequestList(acceptUserId);
    }

    // 忽略好友，删除好友请求
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteFriendRequest(String sendUserId, String accpetUserId) {
        // 1. 查询发送好友请求记录表
        Example friendExample = new Example(FriendsRequest.class);
        Criteria friendCondition = friendExample.createCriteria();
        friendCondition.andEqualTo("sendUserId", sendUserId);
        friendCondition.andEqualTo("acceptUserId", accpetUserId);
        friendsRequestMapper.deleteByExample(friendExample);
    }

    // 通过好友请求
    @Transactional(propagation = Propagation.REQUIRED)
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

    // 保存添加的好友关联关系到数据库
    @Transactional(propagation = Propagation.REQUIRED)
    private void saveFriends(String sendUserId, String acceptUserId){
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    // 查询用户好友列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        List<MyFriendsVO> myFriends = myFriendsMapperCustom.queryMyFriends(userId);
        return myFriends;
    }

    // 保存聊天消息到数据库
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {
        com.ichat.chat.entity.ChatMsg msgDB = new com.ichat.chat.entity.ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    // 批量签收消息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    // 获取已读消息列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<com.ichat.chat.entity.ChatMsg> getUnReadMsgList(String acceptUserId) {
        Example chatExample = new Example(com.ichat.chat.entity.ChatMsg.class);
        Criteria chatCondition = chatExample.createCriteria();
        chatCondition.andEqualTo("signFlag", 0);
        chatCondition.andEqualTo("acceptUserId", acceptUserId);

        List<com.ichat.chat.entity.ChatMsg> result = chatMsgMapper.selectByExample(chatExample);

        return result;
    }
}
