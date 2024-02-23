package com.adevaldo.dscomerce.controllers.handlers;

import com.adevaldo.dscomerce.dto.CustomError;
import com.adevaldo.dscomerce.dto.ValidationError;
import com.adevaldo.dscomerce.services.exceptions.DatabaseException;
import com.adevaldo.dscomerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        var status = HttpStatus.NOT_FOUND.value();
        var err = new CustomError(Instant.now(),status,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(err);

    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> datbase(DatabaseException e, HttpServletRequest request){
        var status = HttpStatus.BAD_REQUEST.value();
        var err = new CustomError(Instant.now(),status,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(err);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request){
        var status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        var err = new ValidationError(Instant.now(),status, "Dados Inválidos", request.getRequestURI());
        for(FieldError f: e.getBindingResult().getFieldErrors()){
             err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }
}
