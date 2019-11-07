package com.ichat.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Users {
    /**
     * 用户唯一id，不需要自增长，不利于分布式切割表
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 预览类小头像
     */
    @Column(name = "head_image")
    private String headImage;

    /**
     * 头像
     */
    @Column(name = "head_image_big")
    private String headImageBig;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 用户自身二维码
     */
    @Column(name = "qrcode")
    private String qrcode;

    /**
     * 用户设备id
     */
    @Column(name = "cid")
    private String cid;
}