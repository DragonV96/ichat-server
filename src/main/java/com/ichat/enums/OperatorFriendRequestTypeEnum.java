package com.ichat.enums;

/**
 * Create by glw
 * 2018/12/1 20:35
 */
public enum OperatorFriendRequestTypeEnum {

    IGNORE(0, "忽略"),
    PASS(1, "通过");

    public final Integer type;
    public final String msg;

    OperatorFriendRequestTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public static String getMsgByType(Integer type) {
        for (OperatorFriendRequestTypeEnum operatorFriendRequestTypeEnum : OperatorFriendRequestTypeEnum.values()){
            if(operatorFriendRequestTypeEnum.getType() == type){
                return operatorFriendRequestTypeEnum.msg;
            }
        }
        return null;
    }
}
