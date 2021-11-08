package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent() {
		return studentRepository.findAll();
	}

    public void addNewStudent(Student student) {
        Optional<Student> studenOptional = studentRepository.findStudentByEmail(student.getEmail());

        if (studenOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        
        if (!exists) {
            throw new IllegalStateException("student with id " + studentId + " doesn't exists");
        }

        studentRepository.deleteById(studentId);
    }

    @Transactional
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
}
