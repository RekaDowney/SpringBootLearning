package me.junbin.learning.springboot.service;

import me.junbin.commons.gson.Gsonor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/27 22:43
 * @description :
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final List<Object> DB = new ArrayList<>();

    public void insert(Object user) {
        LOGGER.info("入库：{}", Gsonor.SIMPLE.toJson(user));
        DB.add(user);
    }

    public Object randomGet() {
        int index = new Random().nextInt(DB.size());
        LOGGER.debug("随机获取索引为 {} 的用户", index);
        return DB.get(index);
    }

    public void delete(Object user) {
        LOGGER.info("删除：{}", Gsonor.SIMPLE.toJson(user));
        DB.remove(user);
    }

    public List<Object> findAll() {
        return new ArrayList<>(DB);
    }

}