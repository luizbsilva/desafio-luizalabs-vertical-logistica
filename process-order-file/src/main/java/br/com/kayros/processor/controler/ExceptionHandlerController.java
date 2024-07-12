package br.com.kayros.processor.controler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import br.com.kayros.model.exceptions.ResourceNotFoundException;
import br.com.kayros.model.exceptions.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
  private ResponseEntity<Object> handleFileAlreadyImported(
      JobInstanceAlreadyCompleteException exception,
      final HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            StandardError.builder()
                .timestamp(now())
                .status(CONFLICT.value())
                .error(CONFLICT.getReasonPhrase())
                .message("O arquivo informado já foi importado no sistema!")
                .path(request.getRequestURI())
                .build()
        );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  ResponseEntity<StandardError> handleNotFoundException(
      final ResourceNotFoundException ex, final HttpServletRequest request) {
    return ResponseEntity.status(NOT_FOUND).body(
        StandardError.builder()
            .timestamp(now())
            .status(NOT_FOUND.value())
            .error(NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build()
    );
  }
}