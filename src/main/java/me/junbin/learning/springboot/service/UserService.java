package me.junbin.learning.springboot.service;

import me.junbin.commons.gson.Gsonor;
import me.junbin.learning.springboot.domain.LogbackUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/27 22:43
 * @description :
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final Map<Long, LogbackUser> DB = new ConcurrentHashMap<>();

    public void insert(LogbackUser user) {
        LOGGER.info("入库：{}", Gsonor.SIMPLE.toJson(user));
        DB.put(user.getId(), user);
    }

    public LogbackUser randomGet() {
        int index = new Random().nextInt(DB.size());
        LOGGER.debug("随机获取索引为 {} 的用户", index);
        return new ArrayList<>(DB.values()).get(index);
    }

    public void delete(LogbackUser user) {
        LOGGER.info("删除：{}", Gsonor.SIMPLE.toJson(user));
        DB.remove(user.getId());
    }

    public List<LogbackUser> findAll() {
        return new ArrayList<>(DB.values());
    }

}