# SpringBoot 学习笔记

## SpringBoot 配置相关文件

- 基本配置
    - spring-boot 依赖 /META-INF/spring-configuration-metadata.json
    - spring-boot 依赖 /META-INF/additional-spring-configuration-metadata.json
    - spring-boot 依赖 /META-INF/spring.factories
    - spring-boot-autoconfigure 依赖 /META-INF/spring-configuration-metadata.json
    - spring-boot-autoconfigure 依赖 /META-INF/additional-spring-configuration-metadata.json
    - spring-boot-autoconfigure 依赖 /META-INF/spring.factories
- 日志
    - logback
        - spring-boot 依赖下的整个 org.springframework.boot.logging.logback 包（defaults.xml）
        - DefaultLogbackConfiguration#base 提供了 clr、wex、wEx 等 conversionRule 转换器配置

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

如果`@ConfigurationProperties`没有与`@Configuration`一起使用，那么在需要注入的类上可以通过添加注解`@EnableConfigurationProperties(XxxProperties.class)`来启动该配置加载。参考：`org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration`和`org.springframework.boot.autoconfigure.http.HttpEncodingProperties`。

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

`SpringBoot`自动加载过程：

通过 @EnableAutoConfiguration 开启自动配置有以下基本流程：
    org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.selectImports#96
        org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.getCandidateConfigurations#156
            org.springframework.core.io.support.SpringFactoriesLoader.loadFactoryNames 加载 SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION（扫描 classpath 下所有 jar 包中的 META-INF/spring.factories 文件）
                加载 org.springframework.boot.autoconfigure.EnableAutoConfiguration 对应的配置项（M:/Software/Env/Apache/Maven/repository/org/springframework/boot/spring-boot-autoconfigure/2.0.2.RELEASE/spring-boot-autoconfigure-2.0.2.RELEASE.jar!/META-INF/spring.factories:18）
                    org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.fireAutoConfigurationImportEvents 加载 XxxAutoConfiguration 配置

以`org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration`介绍自动配置：
    加载`org.springframework.boot.autoconfigure.http.HttpEncodingProperties`配置类，从配置文件中读取`spring.http.encoding`前缀的配置项到该属性配置类中。
    首先通过三个条件注解`ConditionalOnWebApplication`、`ConditionalOnClass`、`ConditionalOnProperty`判定是否可以加载该配置，是则执行下一步；
    判断是否已经配置了`haracterEncodingFilter`，没有则执行`HttpEncodingAutoConfiguration#characterEncodingFilter`方法生成配置类

## SpringBoot 日志

### slf4j

`slf4j`是一款`Java`简单日志门面，其遵循的基本思维为：由`slf4j`提供日志接口规范（整体抽象），用户可以使用不同的具体日志实现框架，最终实现日志功能整体汇总。
 
 常见的日志规范与具体日志实现框架如下：

| 日志规范                                  | 日志具体实现框架    |
| ------------------------------------- | ------- |
| SLF4J（Simple Logging Facade for Java）<br/> Jakarta Commons Logging（JCL） <br/> JBoss Logging | Logback <br/> log4j <br/> log4j2 <br/> JUL（java.util.logging） |

开发中通常是选定一种日志规范以及一种日志具体实现框架，然后通过桥接器将其他依赖框架中的日志实现框架桥接到所使用的日志规范中。

推荐使用`slf4j`和`logback`组合，这也是`SpringBoot`的默认日志框架，我们可以借助`IDEA`的依赖分析工具（在`pom.xml`文件中右键`Diagrams > Show Dependencies`进行分析）

只要选择了`slf4j`日志门面，那么`slf4j-api.jar`依赖就是必须的

`slf4j`支持以下几种日志实现框架

|  日志具体实现框架                                 | 依赖    |
| ------------------------------------- | ------- |
| logback | 原生支持，只需要引入 logback-classic 和 logback-core，不需要适配器层 |
| jul | 需要引入 slf4j-jdk14 适配器层 |
| log4j | 除了 log4j 依赖外，还需要 slf4j-log4j12 适配器层 |
| log4j2 | 除了 log4j-api 和 log4j-core 依赖外，还需要 log4j-slf4j-impl 适配器层 |

特别注意：只能选择一种日志具体实现框架，选定后，其余的日志实现框架依赖不能出现在项目依赖中，同时建议将其余日志实现框架通过桥接器桥接到`slf4j`做统一管理（这是为了避免项目依赖库中使用了其他日志实现框架）

`slf4j`提供了以下几种桥接器：

| 桥接器依赖                                 | 桥接功能    |
| ------------------------------------- | ------- |
| jul-to-slf4j | 将 java.util.logging 日志桥接到 slf4j |
| log4j-over-slf4j | 将 log4j 日志桥接到 slf4j |
| log4j-to-slf4j | 将 log4j2 日志桥接到 slf4j |
| jcl-over-slf4j | 将 jcl 日志规范桥接到 slf4j |

针对`jcl`日志规范，`slf4j`还提供了相应的委托器和桥接器，委托器`slf4j-jcl`可以将`slf4j`日志委托给`jcl`；桥接器`jcl-over-slf4j`可以将`jcl`日志桥接到`slf4j`。
特别注意：`slf4j-jcl`和`jcl-over-slf4j`依赖不可以同时出现，否则会形成环形，造成日志记录出问题。
同理：如果使用了`log4j`日志实现框架（`slf4j-log4j12`依赖），那么就不能添加`log4j-over-slf4j`桥接器依赖，如果添加了`log4j-over-slf4j`桥接器依赖，就不能选择`log4j`作为日志实现框架。对于其他日志实现框架也有相同的限制。

`spring-boot-starter-web`依赖会优先检测`classpath`下是否存在`log4j2`依赖，存在则使用`log4j2`作为日志实现，否则尝试加载`slf4j`作为日志实现，如果都没有，则使用`jul`作为日志实现。
这一块的配置可以参考：`org.apache.commons.logging.LogFactory`的静态初始化块
 
### SpringBoot 日志配置

默认`SpringBoot`采用 slf4j 和`logback`的组合，关于日志的配置项有：

```properties

## 指定日志实现框架的配置文件路径，可以使用 classpath: 前缀或者 file: 文件协议，默认为空串
# LoggingApplicationListener#initializeSystem:262
logging.config=
# 指定日志文件存储路径，比如 /var/log
##  SpringBoot 会将这个配置项转成 LOG_PATH 写入到系统属性中，用于 logback 的默认配置中
# 参考：spring-boot 依赖中的 /META-INF/additional-spring-configuration-metadata.json:71 或者 /META-INF/spring-configuration-metadata.json:204
logging.path=
# 指定日志文件存储名称，比如 springboot.log
##  SpringBoot 会将这个配置项转成 LOG_FILE 写入到系统属性中，用于 logback 的默认配置中
# 参考：spring-boot 依赖中的 /META-INF/additional-spring-configuration-metadata.json:97 或者 /META-INF/spring-configuration-metadata.json:230
logging.file=

## 文件滚动策略为 10MB 一个文件
# DefaultLogbackConfiguration#setRollingPolicy:145
##  SpringBoot 会将这个配置项转成 LOG_FILE_MAX_SIZE 写入到系统属性中，用于 logback 的默认配置中
logging.file.max-size=10MB
## 最多保留多少个归档文件（0 表示不限制归档文件数量），归档文件默认格式为 ${logFile}.%d{yyyy-MM-dd}.%i.gz
##  SpringBoot 会将这个配置项转成 LOG_FILE_MAX_HISTORY 写入到系统属性中，用于 logback 的默认配置中
# DefaultLogbackConfiguration#setRollingPolicy:146
logging.file.max-history=0
## 日志级别的格式，默认为 %5p
# LogbackLoggingSystem#loadDefaults:133
##  SpringBoot 会将这个配置项转成 LOG_LEVEL_PATTERN 写入到系统属性中，用于 logback 的默认配置中
logging.pattern.level=%-5p
## 日志记录时间的格式，默认为 yyyy-MM-dd HH:mm:ss.SSS
##  SpringBoot 会将这个配置项转成 LOG_DATEFORMAT_PATTERN 写入到系统属性中，用于 logback 的默认配置中
# LogbackLoggingSystem#loadDefaults:136
logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS
## 控制台日志记录格式，默认值参考：DefaultLogbackConfiguration#CONSOLE_LOG_PATTERN
##  SpringBoot 会将这个配置项转成 CONSOLE_LOG_PATTERN 写入到系统属性中，用于 logback 的默认配置中
## DefaultLogbackConfiguration#consoleAppender:116
#logging.pattern.console=
## 文件日志记录格式，默认值参考：DefaultLogbackConfiguration#FILE_LOG_PATTERN
##  SpringBoot 会将这个配置项转成 FILE_LOG_PATTERN 写入到系统属性中，用于 logback 的默认配置中
## DefaultLogbackConfiguration#fileAppender:129
#logging.pattern.file=
## 是否注册当 JVM 退出时关闭日志系统的钩子方法，默认为 false
# LoggingApplicationListener#registerShutdownHookIfNecessary:331
logging.register-shutdown-hook=true
## logging.level.root 指定根日志记录器的日志记录级别，默认为 info
# 参考：spring-boot 依赖中的 org/springframework/boot/logging/logback/base.xml:12
logging.level.root=info
## logging.level.packageOrClass 配置指定包或者类的日志记录级别
logging.level.org.hibernate=error

## 强制打开 ansi 彩色输出，默认值为 detect，会自动检测终端是否支持 ansi
spring.output.ansi.enabled=always

```
 
#### logging.file 和 logging.path 配置项

| logging.file | logging.path | 示例             | 描述                                       |
| ------------ | ------------ | -------------- | ---------------------------------------- |
| 没配置          | 没配置          |                | 日志只会记录到控制台                               |
| 指定文件         | 不管有没有配置      | springboot.log | 日志除了记录到控制台，也会记录到指定日志文件（可以是绝对路径或者相对于当前目录的文件） |
| 没配置          | 指定目录         | /var/log       | 日志除了记录到控制台，也会记录到指定日志文件                   |

默认文件记录器的文件存储路径为：`${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}`，其中`:-`表示默认值（具体可以参考：`ch.qos.logback.core.subst.Parser`）。
解析这个配置项可以得到如下的日志文件存储路径

```text

    ${LOG_FILE:-
        ${LOG_PATH:-
            ${LOG_TEMP:-
                ${java.io.tmpdir:-
                    /tmp
                }
            }
        }/spring.log
    }

```

### 自定义 SpringBoot 日志配置

只要在 classpath 下添加一个日志实现框架的配置文件，那么这个配置文件就会作为 SpringBoot 日志配置文件使用，此时不再使用 SpringBoot 的默认日志配置。
具体实现框架及其配置文件名称关系如下：

| 日志实现框架 | 配置文件名称 |
| ------------ | ------------ |
| logback | logback-spring.xml, logback-spring.groovy, logback.xml, 或者 logback.groovy |
| log4j | log4j.xml 或者 log4j.properties |
| log4j2 | log4j2-spring.xml 或者 log4j2.xml |
| jul | logging.properties |

可能你也注意到了，部分配置文件中包含了`-spring`字样。如果配置文件不包含`-spring`字样，那么这个配置文件就会直接交给日志具体实现框架加载，而加了`-spring`字样，那么这个配置文件就会由`SpringBoot`加载，可以完全控制日志的初始化，此时可以使用`SpringBoot`所提供的一些增强性配置。

以`logback.xml`和`logback-spring.xml`为例，讲解这两个文件的区别

`logback.xml`由`logback`日志框架直接加载，其加载时机非常早，`SpringBoot`无法控制它的初始化。

`logback-spring.xml`由`SpringBoot`加载，支持使用`springProfile`、`springProperty`标签来增强`logback`的配置功能。

`springProfile`标签的主要功能为：根据当前激活的`SpringProfiles`有选择地加载或者排除某些`logback`配置。它提供了`name`标签属性来配置`SpringProfiles`，比如
`<springProfile name="dev"> logback 部分配置 </springProfile>`表示当前激活的`SpringProfiles`中包含`dev`的话就执行该`logback`配置，
`<springProfile name="dev, test"> logback 部分配置 </springProfile>`表示当前激活的`SpringProfiles`中包含`dev`或者`test`的话就执行该`logback`配置，
`<springProfile name="!dev"> logback 部分配置 </springProfile>`表示当前激活的`SpringProfiles`中不包含`dev`的话就执行该`logback`配置
 
`springProperty`标签的主要功能为：读取在`application.properties`配置文件中配置的属性并用于`logback`配置。它有以下几个属性：
-`source`指定`application.properties`中的配置项名称，即定义当前声明的`springProperty`要使用哪个配置项，支持松散绑定
-`name`指定当前`springProperty`的名称，后续在`logback`中可以直接通过 ${name} 方式来获取该`springProperty`的值
-`defaultValue`指定当`application.properties`不存在`source`指定的配置项时，则将用当前属性值作为`springProperty`的值
-`scope`指定该`springProperty`的作用域，默认为`local`（读写，简单理解为：写入和读取都是针对当前`springProperty`所在的配置文件），其他可选的有`context`（读写，简单理解为写入读取`logback`的属性，`logback`提供了`include`标签用于引入部分配置，此时引入的配置文件中`scope`为`context`的`springProperty`可以直接在当前配置文件中使用）和`system`（只读，从系统环境变量中读取），具体参考：[logback的scope介绍][logback-scope]

`springProperty`标签类似于`logback`的`property`标签。当`logback`的`property`标签还支持`resource`属性或者`file`属性引入`properties`文件

### 超链接

[external-config-priority]: https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#boot-features-external-config "配置项优先级"
[logback-scope]: https://logback.qos.ch/manual/configuration.html#scopes "logback scope 属性介绍"

