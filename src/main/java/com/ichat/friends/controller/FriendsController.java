package com.ichat.friends.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : glw
 * @date : 2019/11/11
 * @time : 13:47
 * @Description : 好友接口
 */
@Slf4j
@RequestMapping
@RestController("friends")
@Api(tags = "好友接口", description = "好友接口")
public class FriendsController {

}
