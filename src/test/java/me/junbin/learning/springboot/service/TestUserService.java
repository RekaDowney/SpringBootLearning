package me.junbin.learning.springboot.service;

import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:36
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
//@ActiveProfiles(AppConstant.Profiles.DEV)
@ActiveProfiles(AppConstant.Profiles.PROD)
public class TestUserService {

    @Autowired
    private UserService userService;

    @Test
    public void test01() throws Exception {
        userService.insert(new User(1L, "Reka"));
    }

}