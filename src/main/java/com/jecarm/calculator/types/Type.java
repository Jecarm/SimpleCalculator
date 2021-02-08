package com.jecarm.calculator.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Type {
  enum TypeID {
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DECIMAL(BigDecimal.class),
    BIG_INTEGER(BigInteger.class),
    STRING(CharSequence.class);

    private final Class<?> javaClass;
    TypeID(Class<?> javaClass) {
      this.javaClass = javaClass;
    }

    public Class<?> javaClass() {return this.javaClass;}
  }

  TypeID typeId();

}

