package me.junbin.learning.springboot.web;

import me.junbin.commons.gson.Gsonor;
import me.junbin.learning.springboot.constant.AppConstant;
import me.junbin.learning.springboot.domain.LogbackUser;
import me.junbin.learning.springboot.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/28 9:31
 * @description :
 */
@RestController
public class LogbackUserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackUserController.class);

    @GetMapping("/register")
    public Object register(LogbackUser logbackUser) {
        LOGGER.info("注册用户 --> {}", Gsonor.SIMPLE.toJson(logbackUser));
        if (StringUtils.isEmpty(logbackUser.getName())) {
            return "Error";
        }
        List<LogbackUser> users = userService.findAll();
        if (users.stream().anyMatch(u -> u.getName().equals(logbackUser.getName()))) {
            return "Exists";
        }
        logbackUser.setId(users.stream().mapToLong(LogbackUser::getId).max().orElse(0) + 1);
        userService.insert(logbackUser);
        return logbackUser;
    }

    @GetMapping("/login")
    public Object login(LogbackUser logbackUser, HttpServletRequest request) {
        LOGGER.info("登陆 --> {}", logbackUser);
        Optional<LogbackUser> user = userService.findAll().stream().filter(u -> u.getName().equals(logbackUser.getName())).findFirst();
        if (user.isPresent()) {
            request.getSession().setAttribute(AppConstant.Web.SESSION_USER, user.get());
            return "Ok";
        }
        return "Error";
    }

    @GetMapping("/current/user")
    public Object currentUser(HttpServletRequest request) {
        LOGGER.info("查询当前登陆的用户");
        Object user = request.getSession().getAttribute(AppConstant.Web.SESSION_USER);
        if (user == null) {
            return "No login";
        }
        return user;
    }

    @GetMapping("/logout")
    public Object logout(HttpServletRequest request) {
        LOGGER.info("退出当前登陆的用户");
        Object user = request.getSession().getAttribute(AppConstant.Web.SESSION_USER);
        if (user == null) {
            return "No login";
        }
        request.getSession().removeAttribute(AppConstant.Web.SESSION_USER);
        return user;
    }

}