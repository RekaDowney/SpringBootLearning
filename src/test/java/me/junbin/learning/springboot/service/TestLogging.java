package me.junbin.learning.springboot.service;

import me.junbin.learning.springboot.constant.AppConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Object> newUser(int id, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", name);
        return user;
    }

    @Test
    public void test01() throws Exception {
        Map<String, Object> reka = newUser(1, "Reka");
        userService.insert(reka);
        Map<String, Object> lily = newUser(2, "Lily");
        userService.insert(lily);
        Map<String, Object> rachel = newUser(3, "Rachel");
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