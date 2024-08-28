package authproject.exceptions.handler;

import authproject.exceptions.ExceptionResponse;
import authproject.exceptions.InvalidDataInputException;
import authproject.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ExceptionResponse> handleAllExceptions(
      Exception ex,
      WebRequest request
  ) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
      Exception ex,
      WebRequest request
  ) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidDataInputException.class)
  public final ResponseEntity<ExceptionResponse> handleInvalidDataInputExceptions(
      Exception ex,
      WebRequest request
  ) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        new Date(),
        ex.getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
