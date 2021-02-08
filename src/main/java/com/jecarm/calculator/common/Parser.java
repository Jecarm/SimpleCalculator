package com.jecarm.calculator.common;

public interface Parser<T> {
  /**
   * @param value value string to be parse
   * @return parsed object
   * @throws IllegalArgumentException
   */
  T parseValue(String value) throws IllegalArgumentException;
}
