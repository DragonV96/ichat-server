package com.ichat.netty.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Create by glw
 * 2018/12/8 17:36
 * 用户id和Channel的关联关系处理
 */
@Slf4j
public class UserChannelRelationship {
    private static HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel){
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId){
        return manager.get(senderId);
    }

    public static void outPut() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            log.info("UserId：{} ，对应ChannelId：{}", entry.getKey(), entry.getValue().id().asLongText());
        }
    }
}
