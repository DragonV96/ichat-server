package com.ichat.user.service;

import com.ichat.user.entity.Users;

/**
 * @author glw
 * @date 2018/11/14 17:08
 */
public interface UserService {

    /**
     * @Description: 判断用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * @Description: 判断用户是否存在
     */
    Users queryUserForLogin(String username, String password);

    /**
     * @Description: 用户注册
     */
    Users saveUser(Users user);

    /**
     * @Description: 修改用户记录
     */
    Users updateUserInfo(Users user);

    /**
     * @Description: 根据用户名查询用户对象
     */
    Users queryUserInfoByUsername(String sername);
}
