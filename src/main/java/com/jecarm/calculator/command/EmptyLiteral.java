package com.jecarm.calculator.command;

import com.jecarm.calculator.types.Type;

public class EmptyLiteral implements Literal {
  @Override
  public Object value() {
    return null;
  }

  @Override
  public Type dataType() {
    return null;
  }
}
