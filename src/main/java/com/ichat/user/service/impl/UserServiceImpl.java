package com.ichat.user.service.impl;

import com.ichat.common.fastdfs.FastDFSClient;
import com.ichat.user.entity.Users;
import com.ichat.user.mapper.UsersMapper;
import com.ichat.common.utils.*;
import com.ichat.user.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;

/**
 * @author glw
 * @date 2018/11/14 17:10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = userMapper.selectOne(user);

        return result != null ? true : false;
    }

    /**
     * 查询用户是否登录
     * @param username
     * @param password
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
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

    /**
     * 保存用户名
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Users saveUser(Users user) {
        String userId = sid.nextShort();

        // 为每个用户生成一个唯一的二维码
//        String qrCodePath = "\\usr\\local\\fastdfs\\tmp\\" + userId + "qrcode.png";
        String qrCodePath = "G:\\tmp\\" + userId + "qrcode.png";
        // ichat_qrcode:[username]  (为安全考虑需要加解密，如base64)
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

    /**
     * 更新用户名
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Users updateUserInfo(Users user) {
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    /**
     * 通过id查询用户
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    protected Users queryUserById(String userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Users queryUserInfoByUsername(String username) {
        Example user = new Example(Users.class);
        Criteria userCondition = user.createCriteria();
        userCondition.andEqualTo("username", username);
        return userMapper.selectOneByExample(user);
    }
}
