package me.junbin.learning.springboot.service.impl;

import me.junbin.commons.gson.Gsonor;
import me.junbin.learning.springboot.annotation.Dev;
import me.junbin.learning.springboot.domain.User;
import me.junbin.learning.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:30
 * @description :
 */
@Dev
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void insert(User user) {
        LOGGER.info("添加用户：{}", Gsonor.SIMPLE.toJson(user));
    }

}