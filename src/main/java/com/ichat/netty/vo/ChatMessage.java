package com.ichat.netty.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Create by glw
 * 2018/12/8 15:32
 * 传输消息
 */
@Data
@ApiModel(value = "传输消息")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = -3464274653567766468L;

    @ApiModelProperty(value = "发送者的用户id")
    private String senderId;

    @ApiModelProperty(value = "接收者的用户id")
    private String receiverId;

    @ApiModelProperty(value = "聊天内容")
    private String msg;

    @ApiModelProperty(value = "用于消息的签收")
    private String msgId;
}
