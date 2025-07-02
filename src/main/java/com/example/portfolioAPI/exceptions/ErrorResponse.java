package com.example.portfolioAPI.exceptions;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse{
  private int status;
  private String message;
  private LocalDateTime timestamp;
}
