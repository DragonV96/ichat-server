package com.ichat.friends.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by glw
 * 2018/12/2 19:16
 * 用来对前端展示用的实体类（用户信息），可扩展
 */
@Data
@ApiModel(value = "前端展示我的好友的传输实体")
public class MyFriendsVO {
    @ApiModelProperty(value = "我的好友用户id")
    private String friendUserId;

    @ApiModelProperty(value = "我的好友用户名")
    private String friendUsername;

    @ApiModelProperty(value = "我的好友头像")
    private String friendHeadImage;

    @ApiModelProperty(value = "我的好友昵称")
    private String friendNickname;
}
