package br.com.kayros.model.exceptions;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StandardError {
  private LocalDateTime timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;

}
