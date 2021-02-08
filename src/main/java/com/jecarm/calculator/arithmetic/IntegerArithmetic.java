package com.jecarm.calculator.arithmetic;

import com.jecarm.calculator.command.Numeral;
import com.jecarm.calculator.command.Numerals;

public class IntegerArithmetic implements ArithmeticOperation<Integer> {
  private Numeral<Integer> numeral;

  public IntegerArithmetic(Numeral<Integer> numeral) {
    this.numeral = numeral;
  }

  @Override
  public Numeral<Integer> plus(Numeral<Integer> right) {
    return new Numerals.IntegerNumeral(numeral.value() + right.value());
  }

  @Override
  public Numeral<Integer> minus(Numeral<Integer> right) {
    return new Numerals.IntegerNumeral(numeral.value() - right.value());
  }

  @Override
  public Numeral<Integer> multiply(Numeral<Integer> right) {
    return new Numerals.IntegerNumeral(numeral.value() * right.value());
  }

  @Override
  public Numeral<Integer> divide(Numeral<Integer> right) {
    return new Numerals.IntegerNumeral(numeral.value() / right.value());
  }

  @Override
  public Numeral<Double> sqrt() {
    return new Numerals.DoubleNumeral(Math.sqrt(numeral.value().doubleValue()));
  }
}
