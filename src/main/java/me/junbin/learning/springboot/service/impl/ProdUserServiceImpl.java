package me.junbin.learning.springboot.service.impl;

import me.junbin.learning.springboot.annotation.Prod;
import me.junbin.learning.springboot.domain.User;
import me.junbin.learning.springboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:35
 * @description :
 */
@Prod
@Service("userService")
public class ProdUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdUserServiceImpl.class);

    @Override
    public void insert(User user) {
        LOGGER.info("添加用户：{}", user);
        LOGGER.info("添加用户：{} 成功", user);
    }

}
