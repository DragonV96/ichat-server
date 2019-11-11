package com.ichat.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  用来对前端展示用的实体类（用户信息），可扩展
 */
@Data
@ApiModel(value = "前端展示用户的传输实体")
public class UsersVO {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "头像缩略图")
    private String headImage;

    @ApiModelProperty(value = "头像")
    private String headImageBig;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户二维码")
    private String qrcode;
}