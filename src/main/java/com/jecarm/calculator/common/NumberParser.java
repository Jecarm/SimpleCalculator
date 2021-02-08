package com.jecarm.calculator.common;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberParser<N extends Number> implements Parser<N> {
  static final List<Class<? extends Number>> supportedClasses = Arrays.<Class<? extends Number>>asList(Integer.class, Float.class, Long.class, Double.class);

  private static final Map<Class<?>, Parser<?>> parsers = new HashMap<>(supportedClasses.size());

  static {
    for (Class<? extends Number> supportedClass : supportedClasses) {
      parsers.put(supportedClass, new NumberParser<>(supportedClass));
    }
  }

  private final Class<? extends N> clazz;

  private NumberParser(Class<? extends N> clazz) {
    this.clazz = clazz;
  }

  public static <N extends Number> Parser<N> of(Class<N> clazz) {
    Preconditions.checkArgument(supportedClasses.contains(clazz),
      "Class " + clazz + " is not supported by " + NumberParser.class);
    return (Parser<N>) parsers.get(clazz);
  }

  @Override
  public N parseValue(String value) throws IllegalArgumentException {
    N result;

    if (value != null) {
      if (this.clazz.equals(Integer.class)) {
        result = (N) Integer.valueOf(value);
      } else if (this.clazz.equals(Float.class)) {
        result = (N) Float.valueOf(value);
      } else if (this.clazz.equals(Long.class)) {
        result = (N) Long.valueOf(value);
      } else if (this.clazz.equals(Double.class)) {
        result = (N) Double.valueOf(value);
      } else {
        throw new UnsupportedOperationException(this.clazz + " is not supported!");
      }
    } else {
      result = null;
    }

    return result;
  }
}
