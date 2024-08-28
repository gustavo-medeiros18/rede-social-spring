package authproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidDataInputException extends RuntimeException {
  public InvalidDataInputException(String message) {
    super(message);
  }
}
