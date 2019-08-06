package com.uyoqu.hello.docs.core.exception;

/**
 * @author: yoqu
 * @date: 2019-08-06
 * @email: yoqulin@qq.com
 **/
public class ParameterErrorException extends RuntimeException {
  public ParameterErrorException(String message) {
    super(message);
  }
}
