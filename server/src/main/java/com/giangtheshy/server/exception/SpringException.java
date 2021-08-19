package com.giangtheshy.server.exception;

public class SpringException extends RuntimeException  {

  public SpringException(String message,Exception e) {
    super(message,e);
  }
  public SpringException(String exMessage) {
    super(exMessage);
  }
}
