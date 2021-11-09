package com.example.demo.student;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "api/v1/student")
public class StudentController {
    
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("students", studentService.getStudent());
        return "home";
    }

    @GetMapping("/{studentId}")
    public String getStudentById(@PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("students", studentService.getStudentById(studentId));
        return "home";
    }

    @PostMapping
    public String registerNewStudent(@RequestBody Student student, Model model) {
        studentService.addNewStudent(student);
        model.addAttribute("appName", appName);
        model.addAttribute("students", studentService.getStudent());
        return "home";
    }

    @DeleteMapping(path = "{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId, Model model) {
        studentService.deleteStudent(studentId);
        model.addAttribute("appName", appName);
        model.addAttribute("students", studentService.getStudent());
        return "home";
    }

    @PutMapping(path = "{studentId}")
    public String updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            Model model) {
        studentService.updateStudent(studentId, name, email);
        model.addAttribute("appName", appName);
        model.addAttribute("students", studentService.getStudent());
        return "home";
    }
}
