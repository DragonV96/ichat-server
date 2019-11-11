package com.ichat.chat.mapper;

import com.ichat.chat.entity.ChatMsg;
import com.ichat.common.utils.MyMapper;

import java.util.List;

public interface ChatMsgMapperCustom extends MyMapper<ChatMsg> {

    void batchUpdateMsgSigned(List<String> msgIdList);

}