package com.projetopraticobackend.servicocatalogo.infrastructure.api.controllers;

import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.NotFoundException;
import com.projetopraticobackend.servicocatalogo.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/* Nessa classe, definiremos todos os handlers de exceptions. */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class) //Quando uma "DomainException" ocorrer, esse método será chamado.
    public ResponseEntity<?> handleDomainException(final DomainException ex, //O Spring injetará a exception, a request e a response se necessário.
                                                   final HttpServletRequest httpServletRequest,
                                                   final HttpServletResponse httpServletResponse) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class) //Quando uma "NotFoundException" ocorrer, esse método será chamado.
    public ResponseEntity<?> handleDomainException(final NotFoundException ex, //O Spring injetará a exception, a request e a response se necessário.
                                                   final HttpServletRequest httpServletRequest,
                                                   final HttpServletResponse httpServletResponse) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors){

        static ApiError from(DomainException ex){
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}