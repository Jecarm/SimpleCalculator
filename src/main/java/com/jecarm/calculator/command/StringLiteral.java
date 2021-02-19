package com.jecarm.calculator.command;

import com.jecarm.calculator.types.Type;

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
}
