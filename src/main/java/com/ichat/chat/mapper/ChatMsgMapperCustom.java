package com.ichat.chat.mapper;

import com.ichat.user.entity.Users;
import com.ichat.utils.MyMapper;

import java.util.List;

public interface ChatMsgMapperCustom extends MyMapper<Users> {

    public void batchUpdateMsgSigned(List<String> msgIdList);

}