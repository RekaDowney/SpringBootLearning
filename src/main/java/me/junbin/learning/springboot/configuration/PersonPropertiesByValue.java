package me.junbin.learning.springboot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@gmail.com">发送邮件</a>
 * @createDate : 2018/6/23 14:35
 * @description :
 */
@Configuration
public class PersonPropertiesByValue {

    @Value("${person.first-name}")
    private String firstName;
    @Value("${person.last_name}")
    private String lastName;
    @Value("${person.age}")
    private int age;
    @Value("${person.hobby}")
    private List<String> hobby;

    @Override
    public String toString() {
        return "PersonPropertiesByValue{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", hobby=" + hobby +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobby() {
        return hobby;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

}