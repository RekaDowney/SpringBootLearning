package me.junbin.learning.springboot.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.Properties;

import static me.junbin.commons.ansi.ColorfulPrinter.green;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:43
 * @description : 由于 {@link DruidDataSource}
 * <p>
 * 通过 JavaConfig 方式定义 MyBatis 配置时，MyBatis-plus 插件无法识别，从而导致类别名无法正常使用
 * <p>
 * {@link DruidDataSource} 这个 {@link Bean} 是被 {@link org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerPostProcessor} 执行实例化的。
 * 而 {@link org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerPostProcessor}（一个 {@link BeanPostProcessor}）处理的 Bean
 * 创建时间会比较早，因此此时会导致被 {@link org.springframework.context.annotation.Configuration} 标注的配置类尚未实例化就开始调用内部 {@link Bean} 的实例化。
 * 从而导致 {@link PropertySource} 或者 {@link ConfigurationProperties} 无法通过 {@link Value} 注入属性（同理，{@link Autowired} 也无法注入其他依赖）。
 * 因此，如果需要注入属性，必须通过实现 {@link EnvironmentAware} 接口来获取 {@link Environment} 对象，随后通过该对象来获取属性值；
 * 如果需要获取 Spring 容器中的其他 {@link Bean}，可以通过实现 {@link org.springframework.context.ApplicationContextAware} 接口来获取。
 * 正常情况下，包含有 {@link BeanPostProcessor} 的 {@link Bean} 都需要特殊配置一下，比如所有的 {@link BeanPostProcessor} 或者由该后处理器处理的 {@link Bean} 都要放置到一个配置类中。
 * <p>
 * <p>
 * 第二种解决方案是：
 * 通过将 {@link BeanPostProcessor} 所处理的 {@link Bean} 定义为 static，避免实例化该 {@link Bean} 的时候也将该 {@link Bean} 所在的
 * {@link org.springframework.context.annotation.Configuration} 一起实例化。
 */
@Configuration
@PropertySource(encoding = "UTF-8", value = "classpath:bundle/db.properties")
public class DataSourceConfig implements EnvironmentAware {

    @Value("${jdbc.url}")
    private String url;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean(value = "dataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {

        green("@Value 加载 --> " + this.url);

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driver.class.name"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        dataSource.setInitialSize(environment.getProperty("jdbc.druid.initialSize", int.class));
        dataSource.setMinIdle(environment.getProperty("jdbc.druid.minIdle", int.class));
        dataSource.setMaxActive(environment.getProperty("jdbc.druid.maxActive", int.class));
        dataSource.setMaxWait(environment.getProperty("jdbc.druid.maxWait", int.class));
        dataSource.setTimeBetweenEvictionRunsMillis(environment.getProperty("jdbc.druid.timeBetweenEvictionRunsMillis", int.class));
        dataSource.setMinEvictableIdleTimeMillis(environment.getProperty("jdbc.druid.minEvictableIdleTimeMillis", int.class));
        dataSource.setValidationQuery(environment.getProperty("jdbc.druid.validationQuery"));
        dataSource.setTestWhileIdle(environment.getProperty("jdbc.druid.testWhileIdle", boolean.class));
        dataSource.setTestOnBorrow(environment.getProperty("jdbc.druid.testOnBorrow", boolean.class));
        dataSource.setTestOnReturn(environment.getProperty("jdbc.druid.testOnReturn", boolean.class));
        dataSource.setPoolPreparedStatements(environment.getProperty("jdbc.druid.poolPreparedStatements", boolean.class));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(environment.getProperty("jdbc.druid.maxPoolPreparedStatementPerConnectionSize", int.class));
        dataSource.setUseGlobalDataSourceStat(environment.getProperty("jdbc.druid.useGlobalDataSourceStat", boolean.class));

        Properties properties = new Properties();
        String connProperties = environment.getProperty("jdbc.druid.connectionProperties");
        for (String property : connProperties.split(";")) {
            // 直接使用 split("=") 会导致 Base64 编码的 publicKey（结尾可能有一个或者两个 = ）出现问题
            // 解决方案一：直接通过 indexOf('=') 进行切割
            // 解决方案二：依旧使用 split("=")，检测 arr[0] 是否是 config.decrypt.key，是的话检测 arr[1] 的长度 % 4，如果结果为 0，则补上足够的 = 使得长度 % 4 == 0
            int idx = property.indexOf('=');
            properties.setProperty(property.substring(0, idx), property.substring(idx + 1));
        }
        dataSource.setConnectProperties(properties);

        // config 过滤器指向 com.alibaba.druid.filter.config.ConfigFilter
        dataSource.setFilters(environment.getProperty("jdbc.druid.filters"));
        return dataSource;
    }

}