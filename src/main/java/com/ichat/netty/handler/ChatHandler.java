package com.ichat.netty.handler;

import com.ichat.chat.service.ChatService;
import com.ichat.netty.service.UserChannelRelationship;
import com.ichat.netty.vo.ChatMessage;
import com.ichat.netty.vo.DataContent;
import com.ichat.common.utils.SpringUtil;
import com.ichat.common.enums.MsgActionEnum;
import com.ichat.common.utils.AESUtils;
import com.ichat.common.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Create by glw
 * 2018/11/11 23:48
 * @Description ：处理消息的handler
 * TextWebSocketFrame： 在netty中，用于为websocket专门处理文本的对象，frame是消息的载体
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channel
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
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
                Channel findChannel = users.find(receiverChannel.id());
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
            ChatService chatService = (ChatService)SpringUtil.getBean("chatServiceImpl");
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

    /**
     * @param ctx
     * @throws Exception
     * 当客户的连接服务端之后（打开连接），获取客户端的channel并放到ChannelGroup中进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asShortText();
        log.info("客户端被移除，channelId为{}", channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生一场之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
