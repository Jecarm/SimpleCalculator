package com.jecarm.calculator.command;

import com.google.common.base.Preconditions;
import com.jecarm.calculator.types.Type;

import java.util.Objects;

public abstract class BaseLiteral<T> implements Literal<T> {
  private final T value;
  private final Type dataType;

  BaseLiteral(T value, Type dataType) {
    Preconditions.checkNotNull(value, "The value cannot be null");
    this.value = value;
    this.dataType = dataType;
  }

  @Override
  public T value() {
    return value;
  }

  @Override
  public Type dataType() {
    return dataType;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    BaseLiteral<T> that = (BaseLiteral<T>) other;

    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
