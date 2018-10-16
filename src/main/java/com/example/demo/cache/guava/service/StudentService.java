package com.example.demo.cache.guava.service;

import com.example.demo.cache.guava.entity.Student;

public interface StudentService {

    public Student getStudent(Integer id);

    public Student updateStudent(Student stu);

    public void deleteStudent(Integer id);

    public void deleteAllStudent();

    public void myDelete(Integer id);
}


