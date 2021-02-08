package com.jecarm.calculator.command;

import com.google.common.base.Preconditions;
import com.jecarm.calculator.types.Type;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.TypeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Numerals {
  private Numerals() {
  }

  /**
   * Create a {@link Numeral} from an Object.
   *
   * @param value a value
   * @param <T> Java type of value
   * @return a Numeral for the given value
   */
  public static <T> Numeral<T> from(T value) {
    Preconditions.checkNotNull(value, "Cannot create expression Numeral from null");
    Preconditions.checkArgument(!TypeUtil.isNaN(value), "Cannot create expression Numeral from NaN");

    if (value instanceof Integer) {
      return (Numeral<T>) new IntegerNumeral((Integer) value);
    } else if (value instanceof Long) {
      return (Numeral<T>) new Numerals.LongNumeral((Long) value);
    } else if (value instanceof Float) {
      return (Numeral<T>) new Numerals.FloatNumeral((Float) value);
    } else if (value instanceof Double) {
      return (Numeral<T>) new Numerals.DoubleNumeral((Double) value);
    } else if (value instanceof CharSequence) {
      return (Numeral<T>) new Numerals.StringNumeral((CharSequence) value);
    } else if (value instanceof BigDecimal) {
      return (Numeral<T>) new Numerals.DecimalNumeral((BigDecimal) value);
    }

    throw new IllegalArgumentException(String.format(
      "Cannot create expression Numeral from %s: %s", value.getClass().getName(), value));
  }

  private abstract static class BaseNumeral<T> implements Numeral<T> {
    private final T value;

    BaseNumeral(T value) {
      Preconditions.checkNotNull(value, "Numeral values cannot be null");
      this.value = value;
    }

    @Override
    public T value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      BaseNumeral<T> that = (BaseNumeral<T>) other;

      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(value);
    }
  }

  static class AboveMax<T> implements Numeral<T> {
    private static final AboveMax INSTANCE = new AboveMax();

    private AboveMax() {
    }

    @Override
    public T value() {
      throw new UnsupportedOperationException("AboveMax has no value");
    }

    @Override
    public <X> Numeral<X> to(Type type) {
      throw new UnsupportedOperationException("Cannot change the type of AboveMax");
    }

    @Override
    public String toString() {
      return "aboveMax";
    }
  }

  static class BelowMin<T> implements Numeral<T> {
    private static final BelowMin INSTANCE = new BelowMin();

    private BelowMin() {
    }

    @Override
    public T value() {
      throw new UnsupportedOperationException("BelowMin has no value");
    }

    @Override
    public <X> Numeral<X> to(Type type) {
      throw new UnsupportedOperationException("Cannot change the type of BelowMin");
    }

    @Override
    public String toString() {
      return "belowMin";
    }
  }

  static class IntegerNumeral extends BaseNumeral<Integer>{
    IntegerNumeral(Integer value) {
      super(value);
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case INTEGER:
          return (Numeral<T>) this;
        case LONG:
          return (Numeral<T>) new LongNumeral(value().longValue());
        case FLOAT:
          return (Numeral<T>) new FloatNumeral(value().floatValue());
        case DOUBLE:
          return (Numeral<T>) new DoubleNumeral(value().doubleValue());
        case DECIMAL:
          int scale = ((Types.DecimalType) type).scale();
          // rounding mode isn't necessary, but pass one to avoid warnings
          return (Numeral<T>) new DecimalNumeral(
            BigDecimal.valueOf(value()).setScale(scale, RoundingMode.HALF_UP));
        default:
          return null;
      }
    }

    @Override
    public Numeral plus(Numeral<Integer> right) {
      return new IntegerNumeral(this.value() + right.value());
    }

    public Numeral minus(Numeral<Integer> right) {
      return new IntegerNumeral(this.value() - right.value());
    }

    public Numeral multiply(Numeral<Integer> right) {
      return new IntegerNumeral(this.value() * right.value());
    }

    public Numeral divide(Numeral<Integer> right) {
      return new IntegerNumeral(this.value() / right.value());
    }

    @Override
    public Numeral sqrt() {
      return new DoubleNumeral(Math.sqrt(this.value().doubleValue()));
    }
  }

  public static class LongNumeral extends BaseNumeral<Long> {
    public LongNumeral(Long value) {
      super(value);
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case INTEGER:
          if ((long) Integer.MAX_VALUE < value()) {
            return AboveMax.INSTANCE;
          } else if ((long) Integer.MIN_VALUE > value()) {
            return BelowMin.INSTANCE;
          }
          return (Numeral<T>) new IntegerNumeral(value().intValue());
        case LONG:
          return (Numeral<T>) this;
        case FLOAT:
          return (Numeral<T>) new FloatNumeral(value().floatValue());
        case DOUBLE:
          return (Numeral<T>) new DoubleNumeral(value().doubleValue());
        case DECIMAL:
          int scale = ((Types.DecimalType) type).scale();
          // rounding mode isn't necessary, but pass one to avoid warnings
          return (Numeral<T>) new DecimalNumeral(
            BigDecimal.valueOf(value()).setScale(scale, RoundingMode.HALF_UP));
        default:
          return null;
      }
    }

    @Override
    public Numeral plus(Numeral<Long> right) {
      return null;
    }
  }

  public static class FloatNumeral extends BaseNumeral<Float> {
    public FloatNumeral(Float value) {
      super(value);
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case FLOAT:
          return (Numeral<T>) this;
        case DOUBLE:
          return (Numeral<T>) new DoubleNumeral(value().doubleValue());
        case DECIMAL:
          int scale = ((Types.DecimalType) type).scale();
          return (Numeral<T>) new DecimalNumeral(
            BigDecimal.valueOf(value()).setScale(scale, RoundingMode.HALF_UP));
        default:
          return null;
      }
    }
  }

  public static class DoubleNumeral extends BaseNumeral<Double> {
    public DoubleNumeral(Double value) {
      super(value);
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case FLOAT:
          if ((double) Float.MAX_VALUE < value()) {
            return AboveMax.INSTANCE;
          } else if ((double) -Float.MAX_VALUE > value()) {
            // Compare with -Float.MAX_VALUE because it is the most negative float value.
            // Float.MIN_VALUE is the smallest non-negative floating point value.
            return BelowMin.INSTANCE;
          }
          return (Numeral<T>) new FloatNumeral(value().floatValue());
        case DOUBLE:
          return (Numeral<T>) this;
        case DECIMAL:
          int scale = ((Types.DecimalType) type).scale();
          return (Numeral<T>) new DecimalNumeral(
            BigDecimal.valueOf(value()).setScale(scale, RoundingMode.HALF_UP));
        default:
          return null;
      }
    }
  }

  public static class StringNumeral extends BaseNumeral<CharSequence> {
    public StringNumeral(CharSequence value) {
      super(value);
    }
    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case STRING:
          return (Numeral<T>) this;
        default:
          return null;
      }
    }
  }

  public static class DecimalNumeral extends BaseNumeral<BigDecimal> {
    public DecimalNumeral(BigDecimal value) {
      super(value);
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case DECIMAL:
          // do not change decimal scale
          return (Numeral<T>) this;
        default:
          return null;
      }
    }
  }
}
