package me.junbin.learning.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/23 14:36
 * @description :
 */
@Configuration
@ConfigurationProperties(prefix = "good")
@PropertySource("classpath:bundle/good.properties")
public class GoodProperties {

    private Long id;
    private String name;
    private Integer price;

/*
    @Value("${good.id}")
    private Long id;
    @Value("${good.name}")
    private String name;
    @Value("${good.price}")
    private Integer price;
*/

    public GoodProperties() {
    }

    @Override
    public String toString() {
        return "GoodProperties{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}