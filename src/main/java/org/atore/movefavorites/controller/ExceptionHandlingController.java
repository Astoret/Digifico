package org.atore.movefavorites.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atore.movefavorites.exception.BadRequestException;
import org.atore.movefavorites.model.ExceptionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController {

    private final Log logger = LogFactory.getLog(ExceptionHandlingController.class);

    @ExceptionHandler(Exception.class)
    public HttpEntity<ExceptionModel> serverError(HttpServletRequest req, Exception e) {
        ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        exceptionModel.setMessage(e.getMessage());
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(exceptionModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public HttpEntity<ExceptionModel> badRequest(HttpServletRequest req, Exception e) {
        ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionModel.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
    }

}
