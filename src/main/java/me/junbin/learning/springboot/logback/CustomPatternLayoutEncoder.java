package me.junbin.learning.springboot.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/26 21:29
 * @description : 参考 {@link PatternLayoutEncoder}，使用自定义的 PatternLayout。
 * 在 logback.xml 中，可以将 encoder#class 字段修改为 me.junbin.learning.springboot.logback.CustomPatternLayoutEncoder
 * 即可使用我们自定义的 Encoder 而不是默认的 ch.qos.logback.classic.encoder.PatternLayoutEncoder
 */
public class CustomPatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        // 使用自定义 PatternLayout
        PatternLayout patternLayout = new CustomPatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

}
