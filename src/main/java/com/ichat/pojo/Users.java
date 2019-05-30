package com.ichat.pojo;

import javax.persistence.Column;
import javax.persistence.Id;

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

    /**
     * 获取用户唯一id，不需要自增长，不利于分布式切割表
     *
     * @return id - 用户唯一id，不需要自增长，不利于分布式切割表
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户唯一id，不需要自增长，不利于分布式切割表
     *
     * @param id 用户唯一id，不需要自增长，不利于分布式切割表
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取预览类小头像
     *
     * @return head_image - 预览类小头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 设置预览类小头像
     *
     * @param headImage 预览类小头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * 获取头像
     *
     * @return head_image_big - 头像
     */
    public String getHeadImageBig() {
        return headImageBig;
    }

    /**
     * 设置头像
     *
     * @param headImageBig 头像
     */
    public void setHeadImageBig(String headImageBig) {
        this.headImageBig = headImageBig;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取用户自身二维码
     *
     * @return qrcode - 用户自身二维码
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置用户自身二维码
     *
     * @param qrcode 用户自身二维码
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * 获取用户设备id
     *
     * @return cid - 用户设备id
     */
    public String getCid() {
        return cid;
    }

    /**
     * 设置用户设备id
     *
     * @param cid 用户设备id
     */
    public void setCid(String cid) {
        this.cid = cid;
    }
}