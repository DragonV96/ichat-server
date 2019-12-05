package com.ichat.common.enums;

import lombok.Getter;

/**
 * Create by glw
 * 2018/11/26 21:51
 * 添加好友前置状态 枚举
 */
@Getter
public enum SearchFriendsStatusEnum {
    SUCCESS(0, "OK"),
    USER_NOT_EXIST(1, "无此用户..."),
    NOT_YOURSELF(2, "不能添加自己为好友..."),
    ALREADY_FRIENDS(3, "已经添加为好友了，不能重复添加...");

    public final Integer status;
    public final String msg;

    SearchFriendsStatusEnum(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public static String getMsgByKey(Integer status) {
        for (SearchFriendsStatusEnum type: SearchFriendsStatusEnum.values()) {
            if (type.getStatus().equals(status)) {
                return type.msg;
            }
        }
        return null;
    }
}
