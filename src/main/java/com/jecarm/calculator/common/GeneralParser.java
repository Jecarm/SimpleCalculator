package com.jecarm.calculator.common;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.*;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.TypeUtil;

import java.util.*;

public class GeneralParser {
  private final List<String> COMMANDS = ImmutableList.of("+", "-", "*", "/", "sqrt", "undo", "clear");

  private List<Literal> inputLiterals;
  private Properties props;

  public GeneralParser(Properties props) {
    this.props = props;
  }

  public List<Literal> parse(String inputs) {
    Objects.requireNonNull(inputs, "Inputs is null");
    List<String> splits = Splitter.on(' ').splitToList(inputs);
    inputLiterals = new ArrayList<>();
    for (int i = 0; i < splits.size(); i++) {
      Literal literal = parseValue(splits.get(i), 2*i + 1);
      if (literal instanceof EmptyLiteral) continue;
      inputLiterals.add(literal);
    }

    return inputLiterals;
  }

  public List<Literal> parsedValues() {
    return inputLiterals;
  }

  public Literal parseValue(String value, int position) throws IllegalArgumentException {
    Objects.requireNonNull(value, "The value cannot be null");
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
    return Boolean.valueOf(props.getProperty("science.enable", "true"));
  }

  private boolean isCommand(String value) {
    return COMMANDS.contains(value.toLowerCase(Locale.ROOT));
  }
}
