package com.jecarm.calculator.command;

public interface Command {
  void execute();
  void undo();
  String describe();
  default boolean isScience() {
    return true;
  }
}
