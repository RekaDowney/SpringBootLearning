# SpringBoot 学习笔记

## SpringBoot 自动配置

`SpringBoot`自动配置会根据`classpath`（类路径）下的`jar`依赖自动配置`Spring`应用，比如`classpath`下存在`SpringWebMvc`依赖，那么`SpringBoot`会自动配置`org.springframework.web.filter.CharacterEncodingFilter`和`org.springframework.web.servlet.DispatcherServlet`等容器`Bean`。

开启`SpringBoot`自动配置很简单，直接将`org.springframework.boot.autoconfigure.EnableAutoConfiguration`和`org.springframework.context.annotation.Configuration`一起使用即可开启自动配置。（`org.springframework.boot.autoconfigure.SpringBootApplication`是前面两个注解的组合增强版，可以直接使用`@SpringBootApplication`开启自动配置）

注意：`@EnableAutoConfiguration`注解在一个`SpringBoot`应用中应当只出现一次（显式标注或者隐式标注）

`SpringBoot`自动配置是非侵入式的（主要通过`@Condition`注解条件判断是否自动配置`Bean`），我们可以自己通过`@Configuration`配置类自己定义相关的`Bean，`结合`SpringBoot`的全局配置文件`application.properties`或者`application.yml`修改默认配置。

可以通过`@EnableAutoConfiguration#exclude(Class<?>[])`、`@EnableAutoConfiguration#exclude(String[])`、`@SpringBootApplication#exclude(Class<?>[])`、`@SpringBootApplication#exclude(String[])`、`application.properties`或`者application.yml`的`spring.autoconfigure.exclude`配置项禁用某些自动配置，比如在`SpringBoot`全局配置文件中添加`spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`配置项，那么将会禁用数据源的自动配置。

### 配置文件配置项注入

`SpringBoot`支持通过`org.springframework.boot.context.properties.ConfigurationProperties`或者`org.springframework.beans.factory.annotation.Value`来从配置文件中获取配置项并注入到相关的属性中，两者区别如下。
 
 |          | @ConfigurationProperties                 | @Value                                   |
 | -------- | ---------------------------------------- | ---------------------------------------- |
 | 功能       | 批量注入（映射）配置项                              | 一个一个指定注入（映射）配置项                          |
 | 加载时机     | 由Spring容器加载，由ConfigurationPropertiesBindingPostProcessor后处理器增强功能 | 由Spring容器加载，等待相应@Component标注的类实例化后执行配置项映射 |
 | 使用方式     | 1、与@Configuration一起在类上标注，映射作用于成员变量；2、与@Bean一起标注在方法上，映射作用于方法返回类型的成员变量上；3、默认从SpringBoot全局配置文件中映射配置项（支持导航），可以通过@PropertySource或者@PropertySources加载指定配置文件进行映射（不支持导航） | 1、与任意@Component注解及其派生组件注解使用；2、可以标注在成员变量、方法、方法参数列表、注解上；3默认从SpringBoot全局配置文件中映射配置项（不支持导航），可以通过@PropertySource或者@PropertySources加载指定配置文件进行映射（支持导航） |
 | 松散映射     | 支持驼峰字段名与驼峰、下划线、短横线、全大写加下划线（常用于系统变量）配置项映射。比如：firstName字段可以映射到firstName、first-name、first_name这三种配置项 | 不支持，必须明确指定映射的配置项                         |
 | 复杂结构     | 支持List、Set、Map等复杂结构                      | 不支持                                      |
 | SpEL     | 不支持                                      | 支持                                       |
 | JSR303校验 | 支持，通过@Validated开启，由ConfigurationPropertiesBindingPostProcessor处理器执行校验 | 不支持                                      |

如果遇到`@ConfigurationProperties`注入失败的情况，可以考虑在需要注入的类上添加注解`@EnableConfigurationProperties(XxxProperties.class)`。

特别注意：`@PropertySource`不支持导入`yml`配置文件，只支持导入`properties`文件。

### 全局配置文件加载顺序与优先级

`SpringBoot`默认从以下四个路径加载全局配置文件`application.yml`或者`application.properties`。
 
 - `file:./config/`（项目根目录的`config`目录）
 - `file:./`（项目根目录）
 - `classpath:/config/`（`classpath`的`config`目录）
 - `classpath:/`（`classpath`根路径）

以上四种加载顺序优先级依次递减，`SpringBoot`会一次性加载以上路径下的所有全局配置文件，如果配置文件中存在相同键的配置项，则会使用高优先级的配置项覆盖低优先级的配置项。

可以通过`spring.config.location`决定只加载哪些配置文件（多个配置文件使用英文逗号分隔）。参考如下命令：

```bash

    java -jar springboot-xxx.jar --spring.config.location=classpath:/default.properties,file:./config/default.yml

```

此时的配置文件加载优先级为：`file:./config/default.yml` > `classpath:/default.properties`。即越靠后声明的配置文件优先级越高。

由于使用`spring.config.location`配置项后`SpringBoot`不会再从默认四个路径中加载配置文件。因此`SpringBoot`又提供了`spring.config.additional-location`配置项来增强默认加载路径，通过该配置项指定的配置文件优先级高于默认的四个路径下的配置文件。


注意：由于在项目运行初始化时就必须决定加载哪个配置文件，因此`spring.config.location`配置项必须通过命令行参数方式配置。
特别注意：使用`spring.config.location`配置项后`SpringBoot`不会再从默认四个路径中加载配置文件。

### Profile

`Spring Profiles`提供了一种隔离应用程序配置的方式，能够实现配置在特定环境下加载加载。`org.springframework.context.annotation.Profile`可以与任何`@Component`以及`@Component`的派生组件注解一起使用。

我们可以根据`@Profile`派生出相应的环境声明注解以方便配置声明。具体使用方式可以参考如下样例：

```java

    // 声明 @Dev 环境声明注解，等同于 @Profile(AppConstant.Profiles.DEV)
    @Profile(AppConstant.Profiles.DEV)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface Dev {
    
    }

    // 声明 @Prod 环境声明注解，等同于 @Profile(AppConstant.Profiles.PROD)
    @Profile(AppConstant.Profiles.PROD)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface Prod {
    
    }
    
    // 定义 Bean
    @Configuration
    public class AppConfig {
        @Dev
        @Bean("userService")
        public UserService devUserService() {
            return new UserService(){
                // ... dev 配置 
            };    
        }
    
        @Dev
        @Bean("userService")
        public UserService prodUserService() {
            return new UserService(){ 
                // ... prod 配置 
            };    
        }
    }
    
    // 使用 Bean，在全局配置文件中添加 spring.profiles.active=dev 可以激活所有 dev 的 Bean 配置
    @RestController
    public class UserController {
        
        @Autowired
        @Qualifier("userService")
        private UserService userService;
    
    }
    
```

在`AppConfig`配置类中可以看到我们声明了两个`UserService`，他们的采用了相同的`BeanName`，但在启动项目时通过`spring.profiles.active=dev`配置项指定激活`dev`环境，因此实际生成的`userService`时 testUserService。
如果某个配置使用了`@Profile`或者其派生注解，那么在对应`profile`没有激活的情况下，该配置将不会加载。
单元测试时，可以通过添加`JVM`参数`-Dspring.profiles.active=prod`或者在单元测试类上添加`@ActiveProfiles(AppConstant.Profiles.PROD)`注解激活指定的 profile，此时会忽略全局配置文件的`spring.profiles.active`配置项。关于这些外部配置项的优先级具体参考下一节介绍。

注意：所有没有使用`@Profile`或者其派生注解的配置表示不论激活哪种`profile`，这些配置都将加载并生效。

### 配置项（外部配置文件）加载顺序与优先级

参考：[配置项优先级][external-config-priority]

这里建议使用以下几种最常用的配置项优先级

- @TestPropertySource#properties() -- 数组形式，每个数组成员表示一个 KV 对，可以使用 Key=Value 格式或者 Key:Value 格式
- @SpringBootTest#properties() -- 数组形式，每个数组成员表示一个 KV 对，可以使用 Key=Value 格式或者 Key:Value 格式
- 命令行参数 -- 数组形式，通常放在其他配置项末尾，比如：java -jar xxx.jar --server.port=7890 --custom.key=customValue，格式为 --Key=Value --Key1=Value2
- javax.servlet.ServletConfig 的 init parameters 配置
- javax.servlet.ServletContext 的 init parameters 配置
- jar 包外的 application-{profile}.properties 和 application-{profile}.yml 配置文件
- jar 包内的 application-{profile}.properties 和 application-{profile}.yml 配置文件
- jar 包外的 application.properties 和 application.yml 配置文件
- jar 包内的 application.properties 和 application.yml 配置文件

注意：使用`mvn clean package`打包的时候，`${projectBasePath}/config`目录不会被打包到`jar`文件中。

[external-config-priority]: https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#boot-features-external-config "配置项优先级"

