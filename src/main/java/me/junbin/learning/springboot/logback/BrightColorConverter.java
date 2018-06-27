package me.junbin.learning.springboot.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.boot.logging.logback.ColorConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/26 21:24
 * @description : 参考 {@link ColorConverter} 并将所有颜色都改成亮色调
 */
public class BrightColorConverter extends CompositeConverter<ILoggingEvent> {


    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        ansiElements.put("faint", AnsiStyle.FAINT);
        ansiElements.put("red", AnsiColor.BRIGHT_RED);
        ansiElements.put("green", AnsiColor.BRIGHT_GREEN);
        ansiElements.put("yellow", AnsiColor.BRIGHT_YELLOW);
        ansiElements.put("blue", AnsiColor.BRIGHT_BLUE);
        ansiElements.put("magenta", AnsiColor.BRIGHT_MAGENTA);
        ansiElements.put("cyan", AnsiColor.BRIGHT_CYAN);
        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    private static final Map<Integer, AnsiElement> LEVELS;

    static {
        Map<Integer, AnsiElement> ansiLevels = new HashMap<>();
        ansiLevels.put(Level.ERROR_INTEGER, AnsiColor.BRIGHT_RED);
        ansiLevels.put(Level.WARN_INTEGER, AnsiColor.BRIGHT_YELLOW);
        LEVELS = Collections.unmodifiableMap(ansiLevels);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        AnsiElement element = ELEMENTS.get(getFirstOption());
        if (element == null) {
            // Assume highlighting
            element = LEVELS.get(event.getLevel().toInteger());
            element = (element != null ? element : AnsiColor.BRIGHT_GREEN);
        }
        return toAnsiString(in, element);
    }

    protected String toAnsiString(String in, AnsiElement element) {
        return AnsiOutput.toString(element, in);
    }

}