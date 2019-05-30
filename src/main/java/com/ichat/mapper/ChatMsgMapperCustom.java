package com.ichat.mapper;

import com.ichat.pojo.Users;
import com.ichat.utils.MyMapper;

import java.util.List;

public interface ChatMsgMapperCustom extends MyMapper<Users> {

    public void batchUpdateMsgSigned(List<String> msgIdList);

}