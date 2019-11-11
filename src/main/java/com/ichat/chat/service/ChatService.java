package com.ichat.chat.service;

import com.ichat.chat.entity.ChatMsg;
import com.ichat.netty.vo.ChatMessage;

import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 15:13
 * @Description : 聊天接口
 */
public interface ChatService {

    /**
     * @Description: 保存聊天消息到数据库
     */
    String saveMsg(ChatMessage chatMessage);

    /**
     * @Description: 批量签收消息
     */
    void updateMsgSigned(List<String> msgIdList);

    /**
     * @Description: 获取未签收消息列表
     */
    List<ChatMsg> getUnReadMsgList(String acceptUserId);
}
