package com.jecarm.calculator.command;

import com.jecarm.calculator.RpnStack;

public class NumeralCommand implements Command {
  protected RpnStack<String> dataStack;
  protected String value;

  public NumeralCommand(RpnStack<String> dataStack, String value) {
    this.dataStack = dataStack;
    this.value = value;
  }

  @Override
  public void execute() {
    dataStack.push(value);
  }

  public void undo() {
    dataStack.pop();
  }

  @Override
  public String describe() {
    return "literal";
  }
}
