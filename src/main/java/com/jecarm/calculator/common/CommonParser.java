package com.jecarm.calculator.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.*;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.TypeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommonParser {
  private final List<String> commands = ImmutableList.of("+", "-", "*", "/", "sqrt", "undo", "clear");

  private List<Literal> inputLiterals;

  public CommonParser() {
  }

  public List<Literal> parseCommand(String inputs) {
    String[] inputStrings = inputs.split("\\s+");
    inputLiterals = new ArrayList<>();
    for (int i = 0; i < inputStrings.length; i++) {
      Literal literal = parseValue(inputStrings[i], i + 1);
      if (literal instanceof EmptyLiteral) continue;
      inputLiterals.add(literal);
    }

    return inputLiterals;
  }

  public List<Literal> getLiterals() {
    return inputLiterals;
  }

  public Literal parseValue(String value, int position) throws IllegalArgumentException {
    Preconditions.checkNotNull(value, "The value cannot be null");
    if (value.isEmpty()) return new EmptyLiteral();
    else if (NumeralUtil.isNumeral(value)) {
      Numeral numeral;
      if (isScience()) {
        numeral = TypeUtil.inferNumeralTypeOfScience(value);
      } else {
        numeral = TypeUtil.inferValueType(value);
      }
      return numeral;
    } else if (isCommand(value)) {
      return new StringLiteral(value, Types.StringType.get(), position);
    } else {
      throw new IllegalArgumentException(String.format("Invalid parameters: %s", value));
    }
  }

  private boolean isScience() {
    //TODO get from configuration
    return true;
  }

  private boolean isCommand(String value) {
    return commands.contains(value.toLowerCase(Locale.ROOT));
  }
}
