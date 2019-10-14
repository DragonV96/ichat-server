package com.ichat.pojo;

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
    private String username;

    /**
     * 密码
     */
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
    private String nickname;

    /**
     * 用户自身二维码
     */
    private String qrcode;

    /**
     * 用户设备id
     */
    private String cid;
}