package me.junbin.learning.springboot.repository;

import me.junbin.learning.springboot.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/29 9:29
 * @description :
 */
public interface UserRepo {

    int insert(User user);

    User findById(@Param("id") Long id);

}