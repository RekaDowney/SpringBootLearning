package me.junbin.learning.springboot.configuration;

import me.junbin.learning.springboot.annotation.Dev;
import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/24 22:43
 * @description :
 */
@Configuration
public class AppConfig {

    @Dev
    @Bean("userService")
    public UserService devUserService() {
        return () -> AppConstant.Profiles.DEV;
    }

    //    @Prod
    @Bean("userService")
    @Profile(AppConstant.Profiles.PROD)
    public UserService prodUserService() {
        return () -> AppConstant.Profiles.PROD;
    }

}