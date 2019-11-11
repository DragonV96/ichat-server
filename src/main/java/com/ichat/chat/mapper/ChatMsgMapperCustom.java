package com.ichat.chat.mapper;

import com.ichat.user.entity.Users;
import com.ichat.common.utils.MyMapper;

import java.util.List;

public interface ChatMsgMapperCustom extends MyMapper<Users> {

    void batchUpdateMsgSigned(List<String> msgIdList);

}