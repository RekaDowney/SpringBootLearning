package me.junbin.learning.springboot.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:52
 * @description :
 */
@SpringBootTest
//@WebAppConfiguration
@RunWith(SpringRunner.class)
public class TestDataSource {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test01() throws Exception {
        assert dataSource.getClass() == DruidDataSource.class;
    }

}