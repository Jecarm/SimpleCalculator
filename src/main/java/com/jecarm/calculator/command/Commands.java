package com.jecarm.calculator.command;

import com.jecarm.calculator.RpnStack;
import com.jecarm.calculator.exception.OperationNotSupportException;
import com.jecarm.calculator.types.Type;
import com.jecarm.calculator.util.TypeUtil;

public class Commands {

  public abstract static class BinaryOperator implements Command {
    protected RpnStack<Numeral> dataStack;
    protected Numeral left;
    protected Numeral right;

    public BinaryOperator(RpnStack<Numeral> dataStack) {
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
        result = exec(left, right);
      } else {
        // check type
        Type widestType = TypeUtil.findWiderCommonType(left.dataType(), right.dataType());
        result = exec(left.to(widestType), right.to(widestType));
      }
      dataStack.push(result);
    }

    @Override
    public void undo() {
      dataStack.pop();
      dataStack.push(left);
      dataStack.push(right);
    }

  }

  public abstract static class UnaryOperator implements Command {
    protected RpnStack<Numeral> dataStack;
    protected Numeral value;

    public UnaryOperator(RpnStack<Numeral> dataStack) {
      this.dataStack = dataStack;
    }

    @Override
    public void execute() {
      value = dataStack.pop();
      Numeral result = exec(value);
      dataStack.push(result);
    }

    @Override
    public void undo() {
      dataStack.pop();
      dataStack.push(value);
    }

    abstract public Numeral exec(Numeral value);

  }

  public abstract static class HelperCommand<T> implements Command {
    protected RpnStack<T> dataStack;

    public HelperCommand(RpnStack<T> dataStack) {
      this.dataStack = dataStack;
    }
  }

  public static class Plus extends BinaryOperator {

    public Plus(RpnStack<Numeral> dataStack) {
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

  public static class Subtract extends BinaryOperator {

    public Subtract(RpnStack<Numeral> dataStack) {
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

  public static class Multiply extends BinaryOperator {

    public Multiply(RpnStack<Numeral> dataStack) {
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

  public static class Divide extends BinaryOperator {

    public Divide(RpnStack<Numeral> dataStack) {
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

  public static class Sqrt extends UnaryOperator {

    public Sqrt(RpnStack<Numeral> dataStack) {
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

    public Undo(RpnStack<Numeral> dataStack) {
      super(dataStack);
    }

    @Override
    public void execute() {
      this.dataStack.pop();
    }

    @Override
    public void undo() {
      throw new OperationNotSupportException("Unsupported operation in Undo");
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

    public Clear(RpnStack<Numeral> dataStack) {
      super(dataStack);
    }

    @Override
    public void execute() {
      this.dataStack.clear();
    }

    @Override
    public void undo() {
      throw new OperationNotSupportException("Unsupported operation in Clear");
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
