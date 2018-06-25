package me.junbin.learning.springboot.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static me.junbin.commons.ansi.ColorfulPrinter.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/24 22:57
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplicationFileLoadOrder {

    @Autowired
    private Environment environment;

    @Test
    public void test01() throws Exception {
        assert "ProjectBasePath/config/application.yml".equals(environment.getProperty("current"));
        assert "first-priority".equals(environment.getProperty("first"));
        assert "second-priority".equals(environment.getProperty("second"));
        assert "third-priority".equals(environment.getProperty("third"));
        assert "fourth-priority".equals(environment.getProperty("fourth"));

        green(environment.getProperty("current"));
        yellow(environment.getProperty("first"));
        magenta(environment.getProperty("second"));
        cyan(environment.getProperty("third"));
        red(environment.getProperty("fourth"));
    }

}