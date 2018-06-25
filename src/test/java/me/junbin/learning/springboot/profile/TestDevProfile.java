package me.junbin.learning.springboot.profile;

import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/24 22:45
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDevProfile {

    @Autowired
    private Environment environment;
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Test
    public void test01() throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();
        assert activeProfiles.length == 1;
        assert AppConstant.Profiles.DEV.equals(activeProfiles[0]);
    }

    @Test
    public void test02() throws Exception {
        assert AppConstant.Profiles.DEV.equals(userService.getProfile());
    }

}