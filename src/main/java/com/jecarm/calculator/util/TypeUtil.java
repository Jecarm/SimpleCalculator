package com.jecarm.calculator.util;

import com.jecarm.calculator.command.Numeral;
import com.jecarm.calculator.command.Numerals;
import com.jecarm.calculator.types.Type;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.jecarm.calculator.types.Types.*;

public class TypeUtil {
  private TypeUtil() {
  }

  public static List<Type> numericPrecedence = Arrays.asList(IntegerType.get(),
    LongType.get(), FloatType.get(), DoubleType.get());

  public static boolean isNaN(Object value) {
    if (value == null) {
      return false;
    }

    if (value instanceof Double) {
      return Double.isNaN((Double) value);
    } else if (value instanceof Float) {
      return Float.isNaN((Float) value);
    } else {
      return false;
    }
  }

  public static Numeral inferValueType(String value) {
    Numeral Numeral;
    try {
      Numeral = Numerals.from(Integer.parseInt(value));
    } catch (NumberFormatException e) {
      try {
        Numeral = Numerals.from(Long.parseLong(value));
      } catch (NumberFormatException w) {
        try {
          Numeral = Numerals.from(Double.parseDouble(value));
        } catch (NumberFormatException n) {
          throw new IllegalArgumentException("invalid parameter: " + value);
        }
      }
    }
    return Numeral;
  }

  public static Numeral inferNumeralTypeOfScience(String value) {
    if (NumeralUtil.isNumeral(value)){
      return Numerals.from(new BigDecimal(value));
    } else {
      throw new IllegalArgumentException("invalid parameter: " + value);
    }
  }

  public static Type findWiderCommonType(Type left, Type right) {

    if (left == right) {
      return left;
    } else if (left instanceof IntegerType && right instanceof DecimalType
      && ((DecimalType) right).isWiderThan(left)) {
      return right;
    } else if (left instanceof DecimalType && right instanceof IntegerType
      && ((DecimalType) left).isWiderThan(left)) {
      return left;
    } else {
      int leftIndex = numericPrecedence.lastIndexOf(left);
      int rightIndex = numericPrecedence.lastIndexOf(right);
      return leftIndex > rightIndex ? numericPrecedence.get(leftIndex): numericPrecedence.get(rightIndex);
    }
  }
}
