# SpringBoot 学习笔记

## SpringBoot 自动配置

`SpringBoot`自动配置会根据`classpath`（类路径）下的`jar`依赖自动配置`Spring`应用，比如`classpath`下存在`SpringWebMvc`依赖，那么`SpringBoot`会自动配置`org.springframework.web.filter.CharacterEncodingFilter`和`org.springframework.web.servlet.DispatcherServlet`等容器`Bean`。

开启SpringBoot自动配置很简单，直接将`org.springframework.boot.autoconfigure.EnableAutoConfiguration`和`org.springframework.context.annotation.Configuration`一起使用即可开启自动配置。（`org.springframework.boot.autoconfigure.SpringBootApplication`是前面两个注解的组合增强版，可以直接使用`@SpringBootApplication`开启自动配置）

注意：`@EnableAutoConfiguration`注解在一个`SpringBoot`应用中应当只出现一次（显式标注或者隐式标注）

`SpringBoot`自动配置是非侵入式的（主要通过`@Condition`注解条件判断是否自动配置`Bean`），我们可以自己通过`@Configuration`配置类自己定义相关的`Bean，`结合`SpringBoot`的全局配置文件`application.properties`或者`application.yml`修改默认配置。

可以通过`@EnableAutoConfiguration#exclude(Class<?>[])`、`@EnableAutoConfiguration#exclude(String[])`、`@SpringBootApplication#exclude(Class<?>[])`、`@SpringBootApplication#exclude(String[])`、`application.properties`或`者application.yml`的`spring.autoconfigure.exclude`配置项禁用某些自动配置，比如在`SpringBoot`全局配置文件中添加`spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`配置项，那么将会禁用数据源的自动配置。

### 配置文件配置项注入

 SpringBoot 支持通过 org.springframework.boot.context.properties.ConfigurationProperties 或者 org.springframework.beans.factory.annotation.Value 来从配置文件中获取配置项并注入到相关的属性中，两者区别如下。
 
 |          | @ConfigurationProperties                 | @Value                                   |
 | -------- | ---------------------------------------- | ---------------------------------------- |
 | 功能       | 批量注入（映射）配置项                              | 一个一个指定注入（映射）配置项                          |
 | 加载时机     | 由Spring容器加载，由ConfigurationPropertiesBindingPostProcessor后处理器增强功能 | 由Spring容器加载，等待相应@Component标注的类实例化后执行配置项映射 |
 | 使用方式     | 1、与@Configuration一起在类上标注，映射作用于成员变量；2、与@Bean一起标注在方法上，映射作用于方法返回类型的成员变量上；3、默认从SpringBoot全局配置文件中映射配置项（支持导航），可以通过@PropertySource或者@PropertySources加载指定配置文件进行映射（不支持导航） | 1、与任意@Component注解及其派生组件注解使用；2、可以标注在成员变量、方法、方法参数列表、注解上；3默认从SpringBoot全局配置文件中映射配置项（不支持导航），可以通过@PropertySource或者@PropertySources加载指定配置文件进行映射（支持导航） |
 | 松散映射     | 支持驼峰字段名与驼峰、下划线、短横线配置项映射。比如：firstName字段可以映射到firstName、first-name、first_name这三种配置项 | 不支持，必须明确指定映射的配置项                         |
 | 复杂结构     | 支持List、Set、Map等复杂结构                      | 不支持                                      |
 | SpEL     | 不支持                                      | 支持                                       |
 | JSR303校验 | 支持，通过@Validated开启，由ConfigurationPropertiesBindingPostProcessor处理器执行校验 | 不支持                                      |
