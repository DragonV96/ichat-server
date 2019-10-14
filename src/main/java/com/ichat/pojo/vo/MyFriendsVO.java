package com.ichat.pojo.vo;

import lombok.Data;

/**
 * Create by glw
 * 2018/12/2 19:16
 * 用来对前端展示用的实体类（用户信息），可扩展
 */
@Data
public class MyFriendsVO {
    private String friendUserId;
    private String friendUsername;
    private String friendHeadImage;
    private String friendNickname;
}
