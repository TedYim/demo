package com.example.demo.cache.service.impl;

import com.example.demo.cache.entity.Student;
import com.example.demo.cache.service.StudentService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 测试Guava Cache的实现类
 */
@Service("studentGuavaCache")
public class StudentGuavaCacheImpl implements StudentService {


    @Cacheable(value = "redissonCache", key = "'id_'+#id", condition = "#id<3")
    public Student getStudent(Integer id) {
        Student stu = new Student();
        stu.setId(id);
        stu.setName("apple");
        return stu;
    }

    @CachePut(value = "topscoreEhCache", key = "'id_'+#stu.getId()")
    public Student updateStudent(Student stu) {
        System.out.println("update stu");
        return stu;
    }


    @CacheEvict(value = "guavaCache", key = "'id_'+#id")
    public void deleteStudent(Integer id) {
        System.out.println("delete student " + id);
    }

    public void myDelete(Integer id) {
        try {
            StudentService ss = (StudentService) AopContext.currentProxy();
            ss.deleteStudent(id);
            return;
        } catch (Exception e) {
            e.printStackTrace();

        }
        this.deleteStudent(id);
    }

    @CacheEvict(value = "guavaCache", allEntries = true)
    public void deleteAllStudent() {
        System.out.println("delete all student ");
    }
}

