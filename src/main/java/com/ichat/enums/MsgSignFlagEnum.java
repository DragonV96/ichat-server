package com.ichat.enums;

import lombok.Getter;

/**
 * Create by glw
 * 2018/12/8 23:39
 * @Description: 消息签收状态 枚举
 */
@Getter
public enum MsgSignFlagEnum {
	
	unsign(0, "未签收"),
	signed(1, "已签收");	
	
	public final Integer type;
	public final String content;
	
	MsgSignFlagEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
}
