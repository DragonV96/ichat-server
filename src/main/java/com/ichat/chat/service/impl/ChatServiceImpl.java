package com.ichat.chat.service.impl;

import com.ichat.chat.entity.ChatMsg;
import com.ichat.chat.mapper.ChatMsgMapper;
import com.ichat.chat.mapper.ChatMsgMapperCustom;
import com.ichat.chat.service.ChatService;
import com.ichat.common.enums.MsgSignFlagEnum;
import com.ichat.netty.vo.ChatMessage;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 15:13
 * @Description : 聊天业务层实现类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private ChatMsgMapperCustom chatMsgMapperCustom;

    @Autowired
    private Sid sid;

    /**
     * 保存聊天消息到数据库
     * @param chatMessage
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMessage chatMessage) {
        ChatMsg msgDB = new ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMessage.getReceiverId());
        msgDB.setSendUserId(chatMessage.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMessage.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    /**
     * 批量签收消息
     * @param msgIdList
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    /**
     * 获取已读消息列表
     * @param acceptUserId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatMsg> getUnReadMsgList(String acceptUserId) {
        Example chatExample = new Example(ChatMsg.class);
        Example.Criteria chatCondition = chatExample.createCriteria();
        chatCondition.andEqualTo("signFlag", 0);
        chatCondition.andEqualTo("acceptUserId", acceptUserId);

        List<ChatMsg> result = chatMsgMapper.selectByExample(chatExample);

        return result;
    }
}
