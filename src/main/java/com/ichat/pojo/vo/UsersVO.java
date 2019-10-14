package com.ichat.pojo.vo;

import lombok.Data;

/**
 *  用来对前端展示用的实体类（用户信息），可扩展
 */
@Data
public class UsersVO {
    private String id;
    private String username;
    private String headImage;
    private String headImageBig;
    private String nickname;
    private String qrcode;
}