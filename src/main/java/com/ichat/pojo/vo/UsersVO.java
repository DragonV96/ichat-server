package com.ichat.pojo.vo;

/**
 *  用来对前端展示用的实体类（用户信息），可扩展
 */
public class UsersVO {
    private String id;
    private String username;
    private String headImage;
    private String headImageBig;
    private String nickname;
    private String qrcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHeadImageBig() {
        return headImageBig;
    }

    public void setHeadImageBig(String headImageBig) {
        this.headImageBig = headImageBig;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}