package com.alejandro.cruddemo.dao;

import com.alejandro.cruddemo.entity.Course;
import com.alejandro.cruddemo.entity.Instructor;
import com.alejandro.cruddemo.entity.InstructorDetail;
import com.alejandro.cruddemo.entity.Student;

import java.util.List;

public interface AppDAO {

    void save(Instructor theInstructor);

    Instructor findInstructorById(Integer theId);

    Instructor findInstructorByIdCriteria(Integer theId);

    void deleteInstructorById(Integer theId);

    InstructorDetail findInstructorDetailById(Integer theId);

    void deleteInstructorDetailById(Integer theId);

    List<Course> findCoursesByInstructorId(Integer theId);

    Instructor findInstructorByIdJoinFetch(Integer theId);

    void update(Instructor tempInstructor);

    void update(Course tempCourse);

    Course findCourseById(Integer theId);

    void deleteCourseById(Integer theId);

    void save(Course theCourse);

    Course findCourseAndReviewsByCourseId(Integer theId);

    Course findCourseAndStudentsByCourseId(Integer theId);

    Student findStudentAndCoursesByStudentId(Integer theId);

    void update(Student tempStudent);

    void deleteStudentById(Integer theId);
}
