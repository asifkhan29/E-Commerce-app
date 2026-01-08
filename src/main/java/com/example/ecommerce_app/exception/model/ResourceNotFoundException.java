package com.example.ecommerce_app.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName1, Object fieldValue1,
                                     String fieldName2, Object fieldValue2) {
        super(String.format("%s not found with %s: '%s' and %s: '%s'", 
            resourceName, fieldName1, fieldValue1, fieldName2, fieldValue2));
    }
}