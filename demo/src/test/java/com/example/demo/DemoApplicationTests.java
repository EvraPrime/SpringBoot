package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import com.example.demo.student.Student;

import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
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
        student.setDob(LocalDate.of(random.nextInt(2000) + 1000, random.nextInt(12) + 1, random.nextInt(28) + 1));
        return student;
    }

    private String createStudentAsUri(Student student) {
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(student)
            .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

	@Test
	public void whenGetAllStudentsThenOK() {
		Response response = RestAssured.get(API_ROOT);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}
	
	@Test
	public void whenCreateStudentThenOK() {
		Student student = createRandomStudent();
		createStudentAsUri(student);
		Response response = RestAssured.get(
		  API_ROOT + "/name/" + student.getName());
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertTrue(response.as(List.class)
		  .size() > 0);
	}

	@Test
	public void MultipleGetRequestsShouldWork() {
		new Thread(() -> {
			Response response = RestAssured.get(API_ROOT);
		}).start();
		new Thread(() -> {
			Student student = createRandomStudent();
			createStudentAsUri(student);
		}).start();
	}

	// @Test
	// public void whenGetCreatedBookById_thenOK() {
	// 	Book book = createRandomBook();
	// 	String location = createBookAsUri(book);
	// 	Response response = RestAssured.get(location);
		
	// 	assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	// 	assertEquals(book.getTitle(), response.jsonPath()
	// 	  .get("title"));
	// }
	
	// @Test
	// public void whenGetNotExistBookById_thenNotFound() {
	// 	Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
		
	// 	assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	// }

}
