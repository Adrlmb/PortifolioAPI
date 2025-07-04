package com.example.portfolioAPI.exceptions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse{
  private int status;
  private String message;
  private LocalDateTime timestamp;

  public ErrorResponse(String message){
    this.message = message;
  }
}
