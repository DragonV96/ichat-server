package com.ichat.friends.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
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
}