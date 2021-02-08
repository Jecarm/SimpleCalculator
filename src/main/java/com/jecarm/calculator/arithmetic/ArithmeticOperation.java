package com.jecarm.calculator.arithmetic;

import com.jecarm.calculator.command.Numeral;

public interface ArithmeticOperation<T> {
  default Numeral<T> plus(Numeral<T> numeral) {
    throw new RuntimeException("Operation not support.");
  }

  default Numeral<T> minus(Numeral<T> numeral) {
    throw new RuntimeException("Operation not support.");
  }

  default Numeral<T> multiply(Numeral<T> numeral) {
    throw new RuntimeException("Operation not support.");
  }

  default Numeral<T> divide(Numeral<T> numeral) {
    throw new RuntimeException("Operation not support.");
  }

  default <N> Numeral<N> sqrt() {
    throw new RuntimeException("Operation not support.");
  }
}
