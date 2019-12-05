package com.ichat.user.controlller;

import com.ichat.user.entity.Users;
import com.ichat.user.entity.vo.UsersBO;
import com.ichat.user.entity.vo.UsersVO;
import com.ichat.user.service.UserService;
import com.ichat.common.fastdfs.FastDFSClient;
import com.ichat.common.utils.FileUtils;
import com.ichat.common.utils.IChatJSONResult;
import com.ichat.common.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author glw
 * @date 2018/11/14 15:04
 * @Description : 用户接口
 */
@Slf4j
@RestController
@RequestMapping("user")
@Api(tags = "用户接口")
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
//        String userHeadPath = "\\usr\\local\\fastdfs\\tmp\\" + userBO.getUserId() + "userFace64.png";
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
        String[] arr = url.split("\\.");
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
}
