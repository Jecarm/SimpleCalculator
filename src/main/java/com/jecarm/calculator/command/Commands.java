package com.jecarm.calculator.command;

import com.jecarm.calculator.RpnStack;
import com.jecarm.calculator.exception.OperationNotSupportException;
import com.jecarm.calculator.types.Type;
import com.jecarm.calculator.util.TypeUtil;

public class Commands {

  public abstract static class BinaryCommand implements Command {
    protected RpnStack<String> dataStack;
    protected String left;
    protected String right;

    public BinaryCommand(RpnStack<String> dataStack) {
      this.dataStack = dataStack;
    }

    abstract public Numeral exec(Numeral left, Numeral right);

    @Override
    public void execute() {
      right = dataStack.pop();
      left = dataStack.pop();
      Numeral result;
      //TODO load from configuration
      if(isScience()){
        Numeral leftNumeral = TypeUtil.inferNumeralTypeOfScience(left);
        Numeral rightNumeral = TypeUtil.inferNumeralTypeOfScience(right);
        result = exec(leftNumeral, rightNumeral);
      } else {
        Numeral leftNumeral = TypeUtil.inferValueType(left);
        Numeral rightNumeral = TypeUtil.inferValueType(right);
        // check type
        Type widestType = TypeUtil.findWiderCommonType(leftNumeral.dataType(), rightNumeral.dataType());
        result = exec(leftNumeral.to(widestType), rightNumeral.to(widestType));
      }
      dataStack.push(result.value().toString());
    }

    @Override
    public void undo() {
      dataStack.pop();
      dataStack.push(right);
      dataStack.push(left);
    }

  }

  public abstract static class UnaryCommand implements Command {
    protected RpnStack<String> dataStack;
    protected String value;

    public UnaryCommand(RpnStack<String> dataStack) {
      this.dataStack = dataStack;
    }

    @Override
    public void execute() {
      value = dataStack.pop();
      Numeral result = exec(TypeUtil.inferValueType(value));
      dataStack.push(result.value().toString());
    }

    @Override
    public void undo() {
      dataStack.pop();
      dataStack.push(value);
    }

    abstract public Numeral exec(Numeral value);

  }

  public abstract static class HelperCommand implements Command {
    protected RpnStack<String> dataStack;

    public HelperCommand(RpnStack<String> dataStack) {
      this.dataStack = dataStack;
    }
  }

  public static class AddCommand extends BinaryCommand {

    public AddCommand(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public Numeral exec(Numeral left, Numeral right) {
      return left.plus(right);
    }

    @Override
    public String describe() {
      return "+";
    }
  }

  public static class Subtract extends BinaryCommand {

    public Subtract(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public Numeral exec(Numeral left, Numeral right) {
      return left.minus(right);
    }

    @Override
    public String describe() {
      return "-";
    }
  }

  public static class Multiply extends BinaryCommand {

    public Multiply(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public Numeral exec(Numeral left, Numeral right) {
      return left.multiply(right);
    }

    @Override
    public String describe() {
      return "*";
    }
  }

  public static class Divide extends BinaryCommand {

    public Divide(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public Numeral exec(Numeral left, Numeral right) {
      return left.divide(right);
    }

    @Override
    public String describe() {
      return "/";
    }
  }

  public static class Sqrt extends UnaryCommand {

    public Sqrt(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public Numeral exec(Numeral value) {
      return value.sqrt();
    }

    @Override
    public String describe() {
      return "sqrt";
    }
  }

  public static class Undo extends HelperCommand {

    public Undo(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public void execute() {
      this.dataStack.pop();
    }

    @Override
    public void undo() {
      throw new OperationNotSupportException("Not support");
    }

    @Override
    public String describe() {
      return "undo";
    }

    @Override
    public String toString() {
      return describe();
    }
  }

  public static class Clear extends HelperCommand {

    public Clear(RpnStack<String> dataStack) {
      super(dataStack);
    }

    @Override
    public void execute() {
      this.dataStack.clear();
    }

    @Override
    public void undo() {
      throw new OperationNotSupportException("Not support");
    }

    @Override
    public String describe() {
      return "clear";
    }

    @Override
    public String toString() {
      return describe();
    }
  }
}
