package com.ichat.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by glw
 * 2018/11/22 0:39
 */
@Data
@ApiModel(value = "用户传输实体")
public class UsersBO {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "头像数据")
    private String headData;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;
}
