package me.junbin.learning.springboot.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:zhongjunbin@chinamaincloud.com">发送邮件</a>
 * @createDate : 2018/6/25 18:17
 * @description : {@link TestPropertySource#properties()} 的优先级高于 {@link SpringBootTest#properties()}
 */
@TestPropertySource(properties = {"key1:value1", "key2=value2", "key3=xxx"})
@SpringBootTest(properties = {"key3:value3", "key4=value4", "key5:value5"})
@RunWith(SpringRunner.class)
public class TestPropertiesPriority {

    @Autowired
    private Environment environment;

    @Test
    public void test01() throws Exception {
        assert "value1".equals(environment.getProperty("key1"));
        assert "value2".equals(environment.getProperty("key2"));
        assert "xxx".equals(environment.getProperty("key3"));
        assert "value4".equals(environment.getProperty("key4"));
        assert "value5".equals(environment.getProperty("key5"));
    }

}