package com.jecarm.calculator;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.*;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.Printer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class Calculator {
  private final List<String> commands = ImmutableList.of("+", "-", "*", "/", "sqrt", "undo", "clear");

  RpnStack<String> dataStack = new RpnStack<>();
  private List<String> inputList;
  private Printer printer = new Printer();
  private CommandManager manager = new CommandManager();

  public Calculator() {

  }

  public Calculator(String inputs) {
    parseInputs(inputs);
  }

  public Calculator parseInputs(String inputs) {
    this.inputList = Arrays.stream(inputs.split("\\s+"))
      .map(String::trim)
      .filter(s -> !s.isEmpty())
      .collect(Collectors.toList());
    return this;
  }

  public Calculator compute() {
    for (String value : inputList) {
      if (!evaluate(value)) break;
    }
    printer.printConsole("stack:" + dataStack.showString());
    return this;
  }

  public boolean evaluate(String value) {
    if (isCommand(value)) {
      Command cmd = parseCommand(value);
      if (dataStack.isEmpty() || !checkCommandValid(cmd)) {
        printer.printConsole(String.format("operator %s insucient parameters", value));
        return false;
      }
      if (cmd instanceof Commands.Undo) {
        manager.undo();
      } else {
        manager.executeCommand(cmd);
      }
    } else if (NumeralUtil.isNumeral(value)) {
      Command numeralCommand = new NumeralCommand(dataStack, value);
      manager.executeCommand(numeralCommand);
    } else {
      printer.printConsole(String.format("Invalid parameters: %s", value));
      return false;
    }
    return true;
  }

  private boolean checkCommandValid(Command cmd) {
    boolean isValid = true;
    if (cmd instanceof Commands.BinaryCommand && dataStack.size() < 2) {
      isValid = false;
    } else if (cmd instanceof Commands.UnaryCommand && dataStack.size() < 1) {
      isValid = false;
    }
    return isValid;
  }

  private Command parseCommand(String value) {
    if (value == null) return null;
    switch (value) {
      case "+":
        return new Commands.AddCommand(dataStack);
      case "-":
        return new Commands.Subtract(dataStack);
      case "*":
        return new Commands.Multiply(dataStack);
      case "/":
        return new Commands.Divide(dataStack);
      case "sqrt":
        return new Commands.Sqrt(dataStack);
      case "undo":
        return new Commands.Undo(dataStack);
      case "clear":
        return new Commands.Clear(dataStack);
      default:
        return null;
    }
  }

  @VisibleForTesting
  public String showStackInfo() {
    return dataStack.showString();
  }

  private boolean isCommand(String value) {
    return commands.contains(value.toLowerCase(Locale.ROOT));
  }
}


