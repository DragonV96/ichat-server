package com.ichat.friends.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  用来对前端展示用的实体类(好友请求发送方信息)，可扩展
 */
@Data
@ApiModel(value = "前端展示好友的传输实体")
public class FriendRequestVO {
    @ApiModelProperty(value = "发送者用户id")
    private String sendUserId;

    @ApiModelProperty(value = "发送者用户名")
    private String sendUsername;

    @ApiModelProperty(value = "发送者头像")
    private String sendheadImage;

    @ApiModelProperty(value = "发送者昵称")
    private String sendNickname;
}