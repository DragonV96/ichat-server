package com.ichat.user.controlller;

import com.ichat.chat.entity.ChatMsg;
import com.ichat.enums.OperatorFriendRequestTypeEnum;
import com.ichat.enums.SearchFriendsStatusEnum;
import com.ichat.user.entity.Users;
import com.ichat.user.entity.vo.UsersBO;
import com.ichat.friends.entity.vo.MyFriendsVO;
import com.ichat.user.entity.vo.UsersVO;
import com.ichat.user.service.UserService;
import com.ichat.fastdfs.FastDFSClient;
import com.ichat.utils.FileUtils;
import com.ichat.utils.IChatJSONResult;
import com.ichat.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author glw
 * @date 2018/11/14 15:04
 */
@Slf4j
@RestController
@RequestMapping("u")
@Api(tags = "用户接口", description = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @ApiOperation(value = "注册or登陆", notes = "若用户存在则登陆，不存在则直接注册并自动登陆")
    @PostMapping("/registOrLogin")
    public IChatJSONResult registOrLogin(@RequestBody Users user) throws Exception {

        // 1. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return IChatJSONResult.errorMsg("用户名或密码不能为空！");
        }

        // 2. 判断用户名是否存在，存在则登陆，不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 登陆
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IChatJSONResult.errorMsg("用户名或密码不正确！");
            }

        } else {
            // 1.2 注册
            user.setNickname(user.getUsername());
            user.setHeadImage("");
            user.setHeadImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            userResult = userService.saveUser(user);
        }

        // UsersVO类，去掉Users类中一些不要的实体类信息，扩展性更好
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        return IChatJSONResult.ok(usersVO);
    }

    @ApiOperation(value = "上传头像", notes = "上传头像到文件服务器")
    @PostMapping("/uploadFaceBase64")
    public IChatJSONResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception {

        // 获取前端传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = userBO.getHeadData();
//        String userHeadPath = "\\fastdfs\\tmp\\" + userBO.getUserId() + "userFace64.png";
        String userHeadPath = "G:\\tmp\\" + userBO.getUserId() + "userFace64.png";
        FileUtils.base64ToFile(userHeadPath, base64Data);

        // 上传文件到fastdfs
        MultipartFile headFile = FileUtils.fileToMultipart(userHeadPath);
        String url = fastDFSClient.uploadBase64(headFile);
//        log.info(url);

        // "fasfaffasfaffsf.png"
        // "fasfaffasfaffsf_80x80.png"

        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String tumpImgUrl = arr[0] + thump + arr[1];    // 拼接头像缩略图的url地址

        // 更新用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setHeadImage(tumpImgUrl);
        user.setHeadImageBig(url);

        Users result = userService.updateUserInfo(user);

        return IChatJSONResult.ok(result);
    }

    @ApiOperation(value = "更改昵称", notes = "更改用户昵称")
    @PostMapping("/setNickname")
    public IChatJSONResult setNickname(@RequestBody UsersBO userBO) throws Exception {
        // 更新用户昵称
        // (完成后自己写个判断不为空或者长度太长的判断)
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        if (!StringUtils.isBlank(user.getId()) && !StringUtils.isBlank(user.getNickname())) {
            Users result = userService.updateUserInfo(user);
            return IChatJSONResult.ok(result);
        }
        return IChatJSONResult.errorMsg("出错啦！");
    }

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
        Integer status = userService.preConditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
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
        Integer status = userService.preConditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            userService.sendFriendRequest(myUserId, friendUsername);
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
        // 1. 判断myUserId和friendUsername不能为空
        if (StringUtils.isBlank(userId)) {
            return IChatJSONResult.errorMsg("");
        }
        // 2. 查询用户接受到的添加好友申请
        return IChatJSONResult.ok(userService.queryFriendRequestList(userId));
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

        if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
            // 3. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
            userService.deleteFriendRequest(sendUserId, accpetUserId);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            // 4. 判断如果通过好友请求，则互相增加好友记录到数据库好友表
            // 5. 然后删除好友请求的数据库表记录
            userService.passFriendRequest(sendUserId, accpetUserId);
        }
        // 6. 数据库查询好友列表（通过请求后更新）
        List<MyFriendsVO> myFriends = userService.queryMyFriends(accpetUserId);
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
        List<MyFriendsVO> myFriends = userService.queryMyFriends(userId);

        return IChatJSONResult.ok(myFriends);
    }

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptUserId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取未签收消息", notes = "获取未签收消息")
    @PostMapping("/getUnReadMsgList")
    public IChatJSONResult getUnReadMsgList(String acceptUserId) throws Exception {
        // 1. userId判断不能为空
        if (StringUtils.isBlank(acceptUserId)) {
            return IChatJSONResult.errorMsg("");
        }

        // 2. 查询列表
        List<ChatMsg> unreadList = userService.getUnReadMsgList(acceptUserId);
        return IChatJSONResult.ok(unreadList);
    }
}
