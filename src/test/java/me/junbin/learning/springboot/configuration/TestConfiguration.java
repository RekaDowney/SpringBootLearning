package me.junbin.learning.springboot.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static me.junbin.commons.ansi.ColorfulPrinter.green;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/23 9:38
 * @description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestConfiguration {

    @Autowired
    private PersonProperties personProperties;
    @Autowired
    private GoodProperties goodProperties;
    @Autowired
    private PersonPropertiesByValue personPropertiesByValue;

    @Test
    public void test01() throws Exception {
        assert personProperties.getAge() == 24;
        assert "Reka".equals(personProperties.getFirstName());
        assert "Downey".equals(personProperties.getLastName());
        assert new ArrayList<>(Arrays.asList("reading", "gaming")).equals(personProperties.getHobby());
    }

    @Test
    public void test02() throws Exception {
        assert personPropertiesByValue.getAge() == 24;
        assert "Reka".equals(personPropertiesByValue.getFirstName());
        assert "Downey".equals(personPropertiesByValue.getLastName());
        // 特别注意：List 不会自动分隔
        assert Collections.singletonList("reading,gaming").equals(personPropertiesByValue.getHobby());
    }

    @Test
    public void test03() throws Exception {
        assert goodProperties.getId() == 1L;
        assert "Currency".equals(goodProperties.getName());
        assert goodProperties.getPrice() == 9999;
        green(goodProperties);
    }

}