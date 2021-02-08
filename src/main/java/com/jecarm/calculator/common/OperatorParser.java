//package com.jecarm.calculator.common;
//
//import com.google.common.collect.ImmutableList;
//import com.jecarm.calculator.command.Command;
//import com.jecarm.calculator.command.Commands;
//import java.util.List;
//import java.util.Locale;
//
//public class OperatorParser implements Parser<Command> {
//
//  private static final List<String> operators = ImmutableList.of("+", "-", "*", "/", "sqrt");
//
//  @Override
//  public Command parseValue(String value) throws IllegalArgumentException {
//    if (value == null) return null;
//    switch (value) {
//      case "+": return Expressions.Add.get();
//      case "-": return Expressions.Subtract.get();
//      case "*": return Expressions.Multiply.get();
//      case "/": return Expressions.Divide.get();
//      case "sqrt": return Expressions.Sqrt.get();
//      default: return null;
//    }
//  }
//
//  public boolean isOperator(String value) {
//    return operators.contains(value.toLowerCase(Locale.ROOT));
//  }
//}
