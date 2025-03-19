package pl.hanysdev.largefilereader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CityNotFoundException.class)
  public ResponseEntity<String> handleCityNotFound(CityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(FileReadingException.class)
  public ResponseEntity<String> handleFileReadingException(FileReadingException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error reading file: " + ex.getMessage());
  }
}
