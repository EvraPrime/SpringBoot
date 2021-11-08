package com.example.demo.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    //SELECT * FROM student WHERE email = ?
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.MANDATORY, readOnly = true)
    Optional<Student> findStudentByEmail(String email);
}
