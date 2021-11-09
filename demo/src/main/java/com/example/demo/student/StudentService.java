package com.example.demo.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, timeout = 30)
public class StudentService {
    
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, timeout = 15)
    public List<Student> getStudent() {
        return studentRepository.findAll();
	}

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

//        try {
//            TimeUnit.SECONDS.sleep(5);
//        }
//        catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        
        if (!exists) {
            throw new IllegalStateException("student with id " + studentId + " doesn't exists");
        }

        studentRepository.deleteById(studentId);
    }

    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new IllegalStateException(
                            "student with id " + studentId + " doesn't exists"));
        
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }

            student.setEmail(email);
        }
    }

    public List<Student> getStudentById(Long studentId) {
        List<Student> sList = new ArrayList<>();
        sList.add(studentRepository.findById(studentId).orElseThrow());
        return sList;
    }
}
