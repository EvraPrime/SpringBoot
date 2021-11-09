package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.tools.DocumentationTool.Location;

import com.example.demo.student.Student;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest
class DemoApplicationTests {
    private static final String API_ROOT
    = "http://localhost:8080/api/v1/student";

    private Student createRandomStudent() {
		Random random = new Random();
        Student student = new Student();
        student.setName(RandomStringUtils.randomAlphabetic(10));
        student.setEmail(RandomStringUtils.randomAlphabetic(15));
        student.setDob(LocalDate.of(random.nextInt(121) + 1900, random.nextInt(12) + 1, random.nextInt(28) + 1));
        return student;
    }

    private String createStudentAsUri(Student student) {
		return API_ROOT + "/" + 1;
	}

	@Test
	public void whenGetAllStudents_thenOK() {
		Response response = RestAssured.get(API_ROOT);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}
	
	@Test
	public void whenGetStudentsById_thenOK() {
		Student student = createRandomStudent();
		String location = createStudentAsUri(student);
		Response response = RestAssured.get(location);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	public void whenCreateNewBook_thenOK() {
		Student student = createRandomStudent();
		Response response = RestAssured.given()
		  .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
		  .body(student)
		  .post(API_ROOT);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	public void whenDeleteCreatedBook_thenOk() {
		Student student = createRandomStudent();
		String location = createStudentAsUri(student);
		Response response = RestAssured.delete(location);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = RestAssured.get(location);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
	}
}
