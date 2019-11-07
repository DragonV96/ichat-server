package com.ichat.netty.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * Create by glw
 * 2018/12/8 15:32
 * 数据包内容
 */
@Data
public class DataContent implements Serializable {

    private static final long serialVersionUID = -7865167970939580448L;

    private Integer action;     // 动作类型
    private ChatMsg chatMsg;    // 用户的聊天内容entity
    private String extand;      // 扩展字段
}
