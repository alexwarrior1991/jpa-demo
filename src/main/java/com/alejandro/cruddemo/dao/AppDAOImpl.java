package com.alejandro.cruddemo.dao;

import com.alejandro.cruddemo.entity.Course;
import com.alejandro.cruddemo.entity.Instructor;
import com.alejandro.cruddemo.entity.InstructorDetail;
import com.alejandro.cruddemo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppDAOImpl implements AppDAO {

    // define field for entity manager
    private final EntityManager entityManager;

    // inject entity manager using constructor injection
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Instructor theInstructor) {
        entityManager.persist(theInstructor);
    }

    @Override
    public Instructor findInstructorById(Integer theId) {
        return entityManager.find(Instructor.class, theId);
    }

    @Override
    public Instructor findInstructorByIdCriteria(Integer theId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Instructor> query = cb.createQuery(Instructor.class);


        Root<Instructor> instructor = query.from(Instructor.class);

        // Eager fetch
        Fetch<Instructor, Course> f = instructor.fetch("courses", JoinType.LEFT);

        query.where(cb.equal(instructor.get("id"), theId));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    @Transactional
    public void deleteInstructorById(Integer theId) {

        // retrieve the instructor
        Instructor tempInstructor = entityManager.find(Instructor.class, theId);

        // get the courses
        List<Course> courses = tempInstructor.getCourses();

        // break association of all courses for the instructor
        courses.forEach(tempCourse -> tempCourse.setInstructor(null));

        // delete the instructor
        entityManager.remove(tempInstructor);
    }

    @Override
    public InstructorDetail findInstructorDetailById(Integer theId) {
        return entityManager.find(InstructorDetail.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorDetailById(Integer theId) {

        // retrieve the instructorDetail
        InstructorDetail tempInstructorDetail = entityManager.find(InstructorDetail.class, theId);

        // remove tha associated object reference
        //break bidirectional link
        tempInstructorDetail.getInstructor().setInstructorDetail(null);

        // delete the instructorDetail
        entityManager.remove(tempInstructorDetail);
    }

    @Override
    public List<Course> findCoursesByInstructorId(Integer theId) {
        // create query
        TypedQuery<Course> query = entityManager.createQuery(
                "from Course c WHERE c.instructor.id = :data",
                Course.class
        );

        query.setParameter("data", theId);

        // execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

    @Override
    public Instructor findInstructorByIdJoinFetch(Integer theId) {

        // create query
        TypedQuery<Instructor> query = entityManager.createQuery(
                "select i from Instructor i " +
                        "JOIN FETCH i.courses " +
                        "JOIN FETCH i.instructorDetail " +
                        "WHERE i.id = :data",
                Instructor.class
        );

        query.setParameter("data", theId);

        Instructor instructor = query.getSingleResult();

        return instructor;

    }

    @Override
    @Transactional
    public void update(Instructor tempInstructor) {
        entityManager.merge(tempInstructor);
    }

    @Override
    @Transactional
    public void update(Course tempCourse) {
        entityManager.merge(tempCourse);
    }

    @Override
    public Course findCourseById(Integer theId) {
        return entityManager.find(Course.class, theId);
    }

    @Override
    @Transactional
    public void deleteCourseById(Integer theId) {

        // retrieve the course
        Course tempCourse = entityManager.find(Course.class, theId);

        // delete the course
        entityManager.remove(tempCourse);
    }

    @Override
    @Transactional
    public void save(Course theCourse) {
        entityManager.persist(theCourse);
    }

    @Override
    public Course findCourseAndReviewsByCourseId(Integer theId) {
        // create query
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c from Course c "
                        + "JOIN FETCH c.reviews "
                        + "where c.id = :data"
                , Course.class
        );
        query.setParameter("data", theId);

        // execute query
        Course course = query.getSingleResult();

        return course;
    }

    @Override
    public Course findCourseAndStudentsByCourseId(Integer theId) {

        // create query
        TypedQuery<Course> query = entityManager.createQuery(
                "SELECT c from Course c "
                        + "JOIN FETCH c.students "
                        + "where c.id = :data"
                , Course.class
        );
        query.setParameter("data", theId);

        // execute query
        Course course = query.getSingleResult();

        return course;
    }

    @Override
    public Student findStudentAndCoursesByStudentId(Integer theId) {
        // create query
        TypedQuery<Student> query = entityManager.createQuery(
                "select s from Student s "
                + "JOIN FETCH s.courses "
                + "where s.id = :data", Student.class
        );
        query.setParameter("data", theId);

        // execute query
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void update(Student tempStudent) {
        entityManager.merge(tempStudent);
    }

    @Override
    @Transactional
    public void deleteStudentById(Integer theId) {
        // retrieve the student
        Student tempStudent = entityManager.find(Student.class, theId);

        // delete the student
        entityManager.remove(tempStudent);
    }
}
