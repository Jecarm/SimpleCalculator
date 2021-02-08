package com.jecarm.calculator.exception;

public class OperationNotSupportException extends RuntimeException {
  public OperationNotSupportException(String message, Object... args) {
    super(String.format(message, args));
  }

  public OperationNotSupportException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause);
  }
}
