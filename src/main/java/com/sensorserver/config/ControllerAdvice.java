package com.sensorserver.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "One or more fields in the json could not be parsed")
  @ExceptionHandler({InvalidFormatException.class})
  public void handleInvalidFormatException() {
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One or more fields in the json could not be parsed")
  @ExceptionHandler({MismatchedInputException.class})
  public void handleHttpMessageNotReadableException() {
  }
}
