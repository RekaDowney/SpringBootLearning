package me.junbin.learning.springboot.configuration;

import me.junbin.learning.springboot.domain.User;
import me.junbin.learning.springboot.repository.UserRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018-06-30 21:52
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMyBatis {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void test01() throws Exception {
        User reka = new User("Reka");
        userRepo.insert(reka);

        assert reka.getId() == 1L;

        assert userRepo.findById(1L).equals(reka);
    }

}