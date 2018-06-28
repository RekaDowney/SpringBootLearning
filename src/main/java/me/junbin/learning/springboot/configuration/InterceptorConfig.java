package me.junbin.learning.springboot.configuration;

import me.junbin.learning.springboot.Interceptor.LogbackMDCInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/28 9:22
 * @description :
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Bean
    public LogbackMDCInterceptor logbackMDCInterceptor() {
        return new LogbackMDCInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logbackMDCInterceptor())
                .addPathPatterns("/**");
    }

}