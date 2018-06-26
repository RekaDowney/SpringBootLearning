package me.junbin.learning.springboot.logback;

import ch.qos.logback.classic.PatternLayout;
import org.springframework.boot.logging.logback.ColorConverter;
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter;
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:zhongjunbin@chinamaincloud.com">发送邮件</a>
 * @createDate : 2018/6/26 21:19
 * @description : 继承 {@link PatternLayout}，添加颜色转换器快捷键，颜色转换器的具体使用方式可以参考：
 * <a href="https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#boot-features-logging-color-coded-output">SpringBoot彩色日志</a>
 * <pre>
 * 使用方式一：
 *    %clr(${content})
 *    注意：%clr() 是格式要求，所以不会打印该括号。这是颜色是根据日志级别确定的，FATAL、ERROR 为红色、WARN 为黄色，其余为绿色
 * 使用方式二：（推荐）
 *    %clr(${content}){${colorValue}}
 *    通过 %clr(){red} 可以直接指定 ${content} 的内容为红色， %clr(){green} 可以直接指定 ${content} 的内容为绿色。
 * 由于 %clr() 占用了 ()，因此如果想要打印出 () 必须使用 \(\)，即 %brightClr(\(%msg\)){green} 会打印出绿色的 (${日志信息})
 * </pre>
 */
public class CustomPatternLayout extends PatternLayout {

    static {
        defaultConverterMap.put("clr", ColorConverter.class.getName());
        defaultConverterMap.put("brightClr", BrightColorConverter.class.getName());
        defaultConverterMap.put("wex", WhitespaceThrowableProxyConverter.class.getName());
        defaultConverterMap.put("wEx", ExtendedWhitespaceThrowableProxyConverter.class.getName());
    }

}