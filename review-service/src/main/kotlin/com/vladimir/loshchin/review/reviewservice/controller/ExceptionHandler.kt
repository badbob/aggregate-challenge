package com.vladimir.loshchin.review.reviewservice.controller

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.ResponseEntity
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ExceptionHandler {
    
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun constraintViolationHandler(ex: ConstraintViolationException) : ResponseEntity<List<String>> {
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.constraintViolations.stream().map{ it.getMessage() }.toList());
    }
}