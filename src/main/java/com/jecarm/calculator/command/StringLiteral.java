package com.jecarm.calculator.command;

import com.jecarm.calculator.types.Type;

import java.util.Objects;

public class StringLiteral extends BaseLiteral<String> implements Position {
  private final int position;

  public StringLiteral(String value, Type dataType, int position) {
    super(value, dataType);
    this.position = position;
  }

  @Override
  public int getPosition() {
    return position;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    StringLiteral otherLiteral = (StringLiteral)other;
    return Objects.equals(this.value(), otherLiteral.value()) && dataType() == otherLiteral.dataType()
      && Objects.equals(position, otherLiteral.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value(), position);
  }
}
