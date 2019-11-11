package com.ichat.netty.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Create by glw
 * 2018/12/8 15:32
 * 数据包内容
 */
@Data
@ApiModel(value = "数据包内容")
public class DataContent implements Serializable {

    private static final long serialVersionUID = -7865167970939580448L;

    @ApiModelProperty(value = "操作类型")
    private Integer action;

    @ApiModelProperty(value = "用户的聊天内容entity")
    private ChatMessage chatMessage;

    @ApiModelProperty(value = "扩展字段")
    private String extand;
}
