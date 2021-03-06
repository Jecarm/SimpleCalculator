package com.jecarm.calculator.types;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.Objects;

public class Types {
  private Types() {
  }

  public static final ImmutableMap<String, Type> PRIMITIVE_TYPES = ImmutableMap.<String, Type>builder()
    .put(IntegerType.get().toString(), IntegerType.get())
    .put(LongType.get().toString(), LongType.get())
    .put(FloatType.get().toString(), FloatType.get())
    .put(DoubleType.get().toString(), DoubleType.get())
    .build();

  public static Type fromTypeString(String typeString) {
    String lowerTypeString = typeString.toLowerCase(Locale.ROOT);
    if (PRIMITIVE_TYPES.containsKey(lowerTypeString)) {
      return PRIMITIVE_TYPES.get(lowerTypeString);
    }
    throw new IllegalArgumentException("Cannot parse type string to number: " + typeString);
  }

  public static abstract class BaseType implements Type {

    @Override
    public boolean equals(Object obj) {
      return this == obj;
    }

    @Override
    public int hashCode() {
      return getClass().hashCode();
    }
  }

  public static class IntegerType extends BaseType {
    private static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }

    public static IntegerType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.INTEGER;
    }

    @Override
    public String toString() {
      return "int";
    }
  }

  public static class LongType extends BaseType {
    private static final LongType INSTANCE = new LongType();

    private LongType() {
    }

    public static LongType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.LONG;
    }

    @Override
    public String toString() {
      return "long";
    }
  }

  public static class FloatType extends BaseType {
    private static final FloatType INSTANCE = new FloatType();

    private FloatType() {
    }

    public static FloatType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.FLOAT;
    }

    @Override
    public String toString() {
      return "float";
    }
  }

  public static class DoubleType extends BaseType {
    private static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {
    }

    public static DoubleType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.DOUBLE;
    }

    @Override
    public String toString() {
      return "double";
    }
  }

  public static class DecimalType extends BaseType {
    public static DecimalType of(int precision, int scale) {
      return new DecimalType(precision, scale);
    }

    private final int scale;
    private final int precision;

    private DecimalType(int precision, int scale) {
      Preconditions.checkArgument(precision <= 38,
        "Decimals with precision larger than 38 are not supported: %s", precision);
      this.scale = scale;
      this.precision = precision;
    }

    public int scale() {
      return scale;
    }

    public int precision() {
      return precision;
    }

    @Override
    public TypeID typeId() {
      return TypeID.DECIMAL;
    }

    public boolean isWiderThan(Type other) {
      if (other instanceof DecimalType) {
        DecimalType otherDecimal = (DecimalType) other;
        return (precision - scale) >= (otherDecimal.precision - otherDecimal.scale)
          && scale > otherDecimal.scale;
      } else if (other instanceof IntegerType) {
        isWiderThan(new DecimalType(10, 0));
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("decimal(%d, %d)", precision, scale);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o ==null || getClass() != o.getClass()) {
        return false;
      }
      DecimalType that = (DecimalType) o;
      return scale == that.scale && precision == that.precision;
    }

    @Override
    public int hashCode() {
      return Objects.hash(DecimalType.class, scale, precision);
    }
  }

  public static class BigIntegerType extends BaseType {
    private static final BigIntegerType INSTANCE = new BigIntegerType();

    private BigIntegerType() {
    }

    public static BigIntegerType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.BIG_INTEGER;
    }

    @Override
    public String toString() {
      return "BigInteger";
    }
  }

  public static class StringType extends BaseType {
    private static final StringType INSTANCE = new StringType();

    private StringType() {
    }

    public static StringType get() {
      return INSTANCE;
    }

    @Override
    public TypeID typeId() {
      return TypeID.BIG_INTEGER;
    }

    @Override
    public String toString() {
      return "String";
    }
  }
}
