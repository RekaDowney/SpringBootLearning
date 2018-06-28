package me.junbin.learning.springboot.service;

import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.domain.LogbackUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static me.junbin.commons.ansi.ColorfulPrinter.green;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/27 22:47
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(AppConstant.Profiles.DEV)
//@ActiveProfiles(AppConstant.Profiles.PROD)
public class TestLogging {

    @Autowired
    private UserService userService;

    @Test
    public void test01() throws Exception {
        LogbackUser reka = new LogbackUser(1L, "Reka");
        userService.insert(reka);
        LogbackUser lily = new LogbackUser(2L, "Lily");
        userService.insert(lily);
        LogbackUser rachel = new LogbackUser(3L, "Rachel");
        userService.insert(rachel);

        Object user = userService.randomGet();
        assert user == reka || user == lily || user == rachel;
        green(user);

        assert userService.findAll().size() == 3;

        userService.delete(lily);

        assert userService.findAll().size() == 2;

        assert userService.randomGet() != lily;
    }

}