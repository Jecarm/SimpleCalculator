package com.jecarm.calculator;

import com.jecarm.calculator.command.*;
import com.jecarm.calculator.common.GeneralParser;
import com.jecarm.calculator.util.Printer;

import java.util.List;
import java.util.Properties;


public class Calculator {

  RpnStack<Numeral> dataStack = new RpnStack<>();
  private List<Literal> inputLiterals;
  private Printer printer = new Printer();
  private CommandManager manager;
  private GeneralParser parser;
  private Properties props;

  public Calculator(Properties properties) {
    this.props = properties;
    manager = new CommandManager(properties);
    parser = new GeneralParser(properties);
  }

  public Calculator parseInputs(String inputs) {
    this.inputLiterals = parser.parse(inputs);
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
    Command cmd = parseCommand(value);
    // fast failure
    if (!isValidCommand(cmd, value)) return false;
    manager.executeCommand(cmd);
    return true;
  }

  private boolean isValidCommand(Command cmd, Literal value) {
    if (!checkCommandValid(cmd)) {
      printer.printConsole(String.format("operator %s (position: %d): insucient parameters",
        value.value().toString(), ((StringLiteral) value).getPosition()));
      return false;
    }
    return true;
  }

  private boolean checkCommandValid(Command cmd) {
    boolean isValid = true;
    if (cmd instanceof Commands.BinaryOperator && dataStack.size() < 2) {
      isValid = false;
    } else if (cmd instanceof Commands.UnaryOperator && dataStack.size() < 1) {
      isValid = false;
    }
    return isValid;
  }

  private Command parseCommand(Literal value) {
    if (value instanceof Numeral) {
      return new NumeralCommand(dataStack, (Numeral)value);
    } else if (value instanceof StringLiteral) {
      switch (value.toString()) {
        case "+":
          return new Commands.Plus(dataStack);
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
    } else {
      throw new UnsupportedOperationException("Unsupported literal: " + value);
    }
  }

  public String getStackInfo() {
    return dataStack.showString();
  }

}


