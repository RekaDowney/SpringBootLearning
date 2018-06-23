# SpringBoot 学习笔记

## SpringBoot 自动配置

`SpringBoot`自动配置会根据`classpath`（类路径）下的`jar`依赖自动配置`Spring`应用，比如`classpath`下存在`SpringWebMvc`依赖，那么`SpringBoot`会自动配置`org.springframework.web.filter.CharacterEncodingFilter`和`org.springframework.web.servlet.DispatcherServlet`等容器`Bean`。

开启SpringBoot自动配置很简单，直接将`org.springframework.boot.autoconfigure.EnableAutoConfiguration`和`org.springframework.context.annotation.Configuration`一起使用即可开启自动配置。（`org.springframework.boot.autoconfigure.SpringBootApplication`是前面两个注解的组合增强版，可以直接使用`@SpringBootApplication`开启自动配置）

注意：`@EnableAutoConfiguration`注解在一个`SpringBoot`应用中应当只出现一次（显式标注或者隐式标注）

`SpringBoot`自动配置是非侵入式的（主要通过`@Condition`注解条件判断是否自动配置`Bean`），我们可以自己通过`@Configuration`配置类自己定义相关的`Bean，`结合`SpringBoot`的全局配置文件`application.properties`或者`application.yml`修改默认配置。

可以通过`@EnableAutoConfiguration#exclude(Class<?>[])`、`@EnableAutoConfiguration#exclude(String[])`、`@SpringBootApplication#exclude(Class<?>[])`、`@SpringBootApplication#exclude(String[])`、`application.properties`或`者application.yml`的`spring.autoconfigure.exclude`配置项禁用某些自动配置，比如在`SpringBoot`全局配置文件中添加`spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`配置项，那么将会禁用数据源的自动配置。
