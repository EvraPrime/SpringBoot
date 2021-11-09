package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException() {
        super();
    }
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public StudentNotFoundException(String message) {
        super(message);
    }
    public StudentNotFoundException(Throwable cause) {
        super(cause);
    }
}
