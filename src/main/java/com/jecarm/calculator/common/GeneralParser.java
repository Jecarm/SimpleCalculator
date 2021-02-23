package com.jecarm.calculator.common;

import com.google.common.collect.ImmutableSet;
import com.jecarm.calculator.command.Literal;
import com.jecarm.calculator.command.EmptyLiteral;
import com.jecarm.calculator.command.Numeral;
import com.jecarm.calculator.command.StringLiteral;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.TypeUtil;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Locale;
import java.util.Properties;

public class GeneralParser {
  private final Set<String> COMMANDS = ImmutableSet.of("+", "-", "*", "/", "sqrt", "undo", "clear");

  private Properties props;

  public GeneralParser(Properties props) {
    this.props = props;
  }

  public List<Literal> parse(String input) {
    Objects.requireNonNull(input, "Inputs is null");
    List<Literal> inputLiterals = new ArrayList<>();
    int position = 0;
    for (int i=0; i< input.length(); i++) {
      if (input.charAt(i) == ' ') {
        position++;
      } else {
        StringBuilder builder = new StringBuilder();
        int firstPosition = position + 1;
        while (i < input.length() && input.charAt(i) != ' ') {
          builder.append(input.charAt(i));
          i++;
          position++;
        }
        String token = builder.toString();
        Literal literal = parseValue(token, firstPosition);
        inputLiterals.add(literal);
        if (i >= input.length()) break;
        i--;
      }
    }
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
