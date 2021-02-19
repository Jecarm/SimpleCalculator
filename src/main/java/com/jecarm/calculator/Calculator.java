package com.jecarm.calculator;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.*;
import com.jecarm.calculator.common.CommonParser;
import com.jecarm.calculator.common.Parser;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.Printer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class Calculator {

  RpnStack<Numeral> dataStack = new RpnStack<>();
  private List<Literal> inputLiterals;
  private Printer printer = new Printer();
  private CommandManager manager = new CommandManager();
  private CommonParser parser = new CommonParser();

  public Calculator() {

  }

  public Calculator(String inputs) {
    parseInputs(inputs);
  }

  public Calculator parseInputs(String inputs) {
    this.inputLiterals = parser.parseCommand(inputs);
    return this;
  }

  public Calculator compute() {
    for (Literal value : inputLiterals) {
      if (!evaluate(value)) break;
    }
    printer.printConsole(getStackInfo());
    return this;
  }

  public boolean evaluate(Literal value) {
    if (value instanceof StringLiteral) {
      Command cmd = parseCommand(value.value().toString());
      if (dataStack.isEmpty() || !checkCommandValid(cmd)) {
        printer.printConsole(String.format("operator %s (position: %d): insucient parameters",
                value.value().toString(), ((StringLiteral) value).getPosition()));
        return false;
      }
      if (cmd instanceof Commands.Undo) {
        manager.undo();
      } else {
        manager.executeCommand(cmd);
      }
    } else if (value instanceof Numeral) {
      Command numeralCommand = new NumeralCommand(dataStack, (Numeral)value);
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

  public String getStackInfo() {
    return dataStack.showString();
  }

}


