package me.junbin.learning.springboot.Interceptor;

import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.domain.LogbackUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/28 9:24
 * @description : 将 IP 或者当前登陆的用户名称写入到 MDC 的 user 变量中，logback 配置文件中可以使用 %X{user} 来获取变量值。
 * 同时将 request_id 也写入到 logback 中，方便跟踪某次请求的所有跟踪日志
 */
public class LogbackMDCInterceptor implements HandlerInterceptor {

    private static final String USER = "user";
    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LogbackUser user = (LogbackUser) request.getSession().getAttribute(AppConstant.Web.SESSION_USER);
        if (user == null) {
            MDC.put(USER, getIp(request));
        } else {
            MDC.put(USER, user.getName());
        }
        MDC.put(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        MDC.remove(USER);
        // 请求结束后将当前线程对应的 MDC 变量清空
        MDC.clear();
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

}