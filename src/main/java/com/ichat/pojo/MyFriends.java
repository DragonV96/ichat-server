package com.ichat.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "my_friends")
public class MyFriends {
    /**
     * 好友关系表唯一id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "my_user_id")
    private String myUserId;

    /**
     * 用户关联的好友id
     */
    @Column(name = "my_friend_user_id")
    private String myFriendUserId;

    /**
     * 获取好友关系表唯一id
     *
     * @return id - 好友关系表唯一id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置好友关系表唯一id
     *
     * @param id 好友关系表唯一id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return my_user_id - 用户id
     */
    public String getMyUserId() {
        return myUserId;
    }

    /**
     * 设置用户id
     *
     * @param myUserId 用户id
     */
    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    /**
     * 获取用户关联的好友id
     *
     * @return my_friend_user_id - 用户关联的好友id
     */
    public String getMyFriendUserId() {
        return myFriendUserId;
    }

    /**
     * 设置用户关联的好友id
     *
     * @param myFriendUserId 用户关联的好友id
     */
    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId;
    }
}