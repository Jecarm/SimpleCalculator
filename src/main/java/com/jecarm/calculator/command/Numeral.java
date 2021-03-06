package com.jecarm.calculator.command;

import com.jecarm.calculator.types.Type;

public interface Numeral<T> extends Literal<T> {

  /**
   * Converts this Numeral to a Numeral of the given type.
   *
   * @param type
   * @param <X>  The java type
   * @return A new Numeral of given type
   */
  <X> Numeral<X> to(Type type);

  default Numeral<T> plus(Numeral<T> value) {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getName());
  }

  default Numeral<T> minus(Numeral<T> value) {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getName());
  }

  default Numeral<T> multiply(Numeral<T> value) {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getName());
  }

  default Numeral<T> divide(Numeral<T> value) {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getName());
  }

  default Numeral sqrt() {
    throw new UnsupportedOperationException("Unsupported operation in " + getClass().getName());
  }
}
