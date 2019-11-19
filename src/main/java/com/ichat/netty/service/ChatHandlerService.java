package com.ichat.netty.service;

import com.ichat.chat.service.ChatService;
import com.ichat.common.enums.MsgActionEnum;
import com.ichat.common.utils.AESUtils;
import com.ichat.common.utils.JsonUtils;
import com.ichat.common.utils.SpringUtil;
import com.ichat.netty.handler.ChatHandler;
import com.ichat.netty.vo.ChatMessage;
import com.ichat.netty.vo.DataContent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/19
 * @time : 23:42
 * @Description : 聊天业务层
 */
@Slf4j
@Service
public class ChatHandlerService {

    /**
     * 发送消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void sendMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //获取客户端传输过来的消息
        String content = msg.text();

        //进行客户端消息的解密
        content = AESUtils.aesDecrypt(content);
        Channel currentChannel = ctx.channel();

        // 1. 获取客户端发来的消息
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();

        // 2. 判断消息类型，根据不同的类型来处理不同的业务

        if (action == MsgActionEnum.CONNECT.type) {
            // 2.1 当websocket第一次open的时候，初始化channel，把用户的channel和userId关联起来
            String sendUserId = dataContent.getChatMessage().getSenderId();
            UserChannelRelationship.put(sendUserId, currentChannel);

//            // 测试
//            for (Channel c : users) {
//                log.info(c.id().asLongText());
//            }
            UserChannelRelationship.outPut();

        } else if (action == MsgActionEnum.CHAT.type) {
            // 2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
            ChatMessage chatMessage = dataContent.getChatMessage();
            String msgText = chatMessage.getMsg();
            String receiveUserId = chatMessage.getReceiverId();
            String sendUserId = chatMessage.getSenderId();

            // 保存消息到数据库，并且标记为未签收
            ChatService chatService = (ChatService)SpringUtil.getBean("chatServiceImpl");
            String msgId = chatService.saveMsg(chatMessage);
            chatMessage.setMsgId(msgId);

            // 将chatMsg装入dataContent中
            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMessage(chatMessage);

            // 发送消息
            // 从全局用户Channel关系中获取接收方的channel
            Channel receiverChannel = UserChannelRelationship.get(receiveUserId);
            if (receiverChannel == null) {
                // channel为空代表用户离线，推送消息(JPush，个推，小米推送)

            } else {
                // 当receiverChannel不为空的时候，从ChannelGroup中去查找对应的channel是否存在
                Channel findChannel = ChatHandler.users.find(receiverChannel.id());
                if (findChannel != null) {
                    // 用户在线
                    // 将消息加密发送
                    String contentMsg = JsonUtils.objectToJson(dataContentMsg);
                    contentMsg = AESUtils.aesEncrypt(contentMsg);
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(contentMsg));
                } else {
                    // 用户离线 ，推送消息
                }
            }

        } else if (action == MsgActionEnum.SIGNED.type) {
            // 2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            ChatService chatService = (ChatService) SpringUtil.getBean("chatServiceImpl");
            // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdsStr = dataContent.getExtand();
            String msgIds[] = msgIdsStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String mid : msgIds) {
                if (StringUtils.isNotBlank(mid)) {
                    msgIdList.add(mid);
                }
            }
//            log.info(msgIdList.toString());

            if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
                // 批量签收
                chatService.updateMsgSigned(msgIdList);
            }

        } else if (action == MsgActionEnum.KEEPALIVE.type) {
            // 2.4 心跳类型的消息
            log.info("收到来自channel为：{}的心跳包", currentChannel);
        }
    }
}
