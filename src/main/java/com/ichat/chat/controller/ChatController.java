package com.ichat.chat.controller;

import com.ichat.chat.entity.ChatMsg;
import com.ichat.chat.service.ChatService;
import com.ichat.common.utils.IChatJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 14:39
 * @Description : 聊天接口
 */
@Slf4j
@RestController
@RequestMapping("chat")
@Api(tags = "聊天接口")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptUserId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取未签收消息", notes = "获取未签收消息")
    @PostMapping("/getUnReadMsgList")
    public IChatJSONResult getUnReadMsgList(String acceptUserId) throws Exception {
        // 1. userId判断不能为空
        if (StringUtils.isBlank(acceptUserId)) {
            return IChatJSONResult.errorMsg("");
        }

        // 2. 查询列表
        List<ChatMsg> unreadList = chatService.getUnReadMsgList(acceptUserId);
        return IChatJSONResult.ok(unreadList);
    }
}
