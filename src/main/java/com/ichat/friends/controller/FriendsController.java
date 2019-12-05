package com.ichat.friends.controller;

import com.ichat.common.enums.OperatorFriendRequestTypeEnum;
import com.ichat.common.enums.SearchFriendsStatusEnum;
import com.ichat.common.utils.IChatJSONResult;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.friends.service.FriendsService;
import com.ichat.user.entity.Users;
import com.ichat.user.entity.vo.UsersVO;
import com.ichat.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 13:47
 * @Description : 好友接口
 */
@Slf4j
@RestController
@RequestMapping("friends")
@Api(tags = "好友接口")
public class FriendsController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendsService friendsService;

    /**
     * 搜索好友接口,根据账号做精确匹配查询而不是模糊查询
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "搜索好友", notes = "搜索好友")
    @PostMapping("/search")
    public IChatJSONResult searchUser(String myUserId, String friendUsername) throws Exception {
        // 判断myUserId和friendUsername不能为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
            return IChatJSONResult.errorMsg("");
        }

        // 前置条件 - 1. 搜索的用户不存在，返回 [无此用户]
        // 前置条件 - 2. 搜索的账号是自己，返回 [不能添加自己为好友]
        // 前置条件 - 3. 搜索的用户已经是好友，返回 [好友已存在，不能重复添加好友]
        Integer status = friendsService.preConditionSearchFriends(myUserId, friendUsername);
        if (status.equals(SearchFriendsStatusEnum.SUCCESS.status)) {
            Users user = userService.queryUserInfoByUsername(friendUsername);

            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(user, usersVO);
            return IChatJSONResult.ok(usersVO);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IChatJSONResult.errorMsg(errorMsg);
        }
    }

    /**
     * 发送添加好友的请求
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "添加好友", notes = "添加好友")
    @PostMapping("/addFriendRequest")
    public IChatJSONResult addFriendRequest(String myUserId, String friendUsername) throws Exception {
        // 判断myUserId和friendUsername不能为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
            return IChatJSONResult.errorMsg("");
        }

        // 前置条件 - 1. 搜索的用户不存在，返回 [无此用户]
        // 前置条件 - 2. 搜索的账号是自己，返回 [不能添加自己为好友]
        // 前置条件 - 3. 搜索的用户已经是好友，返回 [好友已存在，不能重复添加好友]
        Integer status = friendsService.preConditionSearchFriends(myUserId, friendUsername);
        if (status.equals(SearchFriendsStatusEnum.SUCCESS.status)) {
            friendsService.sendFriendRequest(myUserId, friendUsername);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IChatJSONResult.errorMsg(errorMsg);
        }
        return IChatJSONResult.ok();
    }

    /**
     * 查询发送添加好友的请求
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询好友请求", notes = "查询好友请求")
    @PostMapping("/queryFriendRequests")
    public IChatJSONResult queryFriendRequests(String userId) throws Exception {
        log.info("***查询好友请求*** userId = {}", userId);
        // 1. 判断myUserId和friendUsername不能为空
        if (StringUtils.isBlank(userId)) {
            return IChatJSONResult.errorMsg("用户ID为空");
        }
        // 2. 查询用户接受到的添加好友申请
        return IChatJSONResult.ok(friendsService.queryFriendRequestList(userId));
    }

    /**
     * 接收方 通过或者忽略朋友的请求
     * @param accpetUserId
     * @param sendUserId
     * @param operType
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过好友请求", notes = "通过好友请求")
    @PostMapping("/opreFriendRequest")
    public IChatJSONResult opreFriendRequest(String accpetUserId, String sendUserId, Integer operType) throws Exception {
        // 1. 判断accpetUserId sendUserId operType不能为空
        if (StringUtils.isBlank(accpetUserId) || StringUtils.isBlank(sendUserId) || operType == null) {
            return IChatJSONResult.errorMsg("");
        }

        // 2. 如果operType 没有对应的枚举值，则直接抛出空错误信息
        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return IChatJSONResult.errorMsg("");
        }

        if (operType.equals(OperatorFriendRequestTypeEnum.IGNORE.type)) {
            // 3. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
            friendsService.deleteFriendRequest(sendUserId, accpetUserId);
        } else if (operType.equals(OperatorFriendRequestTypeEnum.PASS.type)) {
            // 4. 判断如果通过好友请求，则互相增加好友记录到数据库好友表
            // 5. 然后删除好友请求的数据库表记录
            friendsService.passFriendRequest(sendUserId, accpetUserId);
        }
        // 6. 数据库查询好友列表（通过请求后更新）
        List<MyFriendsVO> myFriends = friendsService.queryMyFriends(accpetUserId);
        return IChatJSONResult.ok(myFriends);
    }

    /**
     * 查询我的好友列表
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询好友列表", notes = "查询好友列表")
    @PostMapping("/myFriends")
    public IChatJSONResult myFriends(String userId) throws Exception {
        // 1. userId判断不能为空
        if (StringUtils.isBlank(userId)) {
            return IChatJSONResult.errorMsg("");
        }

        // 2. 数据库查询好友列表
        List<MyFriendsVO> myFriends = friendsService.queryMyFriends(userId);

        return IChatJSONResult.ok(myFriends);
    }

}
