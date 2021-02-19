package com.jecarm.calculator.command;

import com.jecarm.calculator.types.Type;

public interface Literal<T> {
  /**
   * @return The value wrapped by this Numeral
   */
  T value();

  Type dataType();
}
