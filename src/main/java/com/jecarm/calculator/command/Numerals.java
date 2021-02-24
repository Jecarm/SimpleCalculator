package com.jecarm.calculator.command;

import com.google.common.base.Preconditions;
import com.jecarm.calculator.types.Type;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.TypeUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class Numerals {
  private Numerals() {
  }

  /**
   * Create a {@link Numeral} from an Object.
   *
   * @param value a value
   * @param <T>   Java type of value
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
    } else if (value instanceof BigInteger) {
      return (Numeral<T>) new Numerals.BigIntegerNumeral((BigInteger) value);
    } else if (value instanceof BigDecimal) {
      return (Numeral<T>) new Numerals.DecimalNumeral((BigDecimal) value);
    }

    throw new IllegalArgumentException(String.format(
      "Cannot create expression Numeral from %s: %s", value.getClass().getName(), value));
  }

  private abstract static class BaseNumeral<T> implements Numeral<T> {
    private final T value;
    private final Type dataType;

    BaseNumeral(T value, Type dataType) {
      Preconditions.checkNotNull(value, "Numeral values cannot be null");
      this.value = value;
      this.dataType = dataType;
    }

    @Override
    public T value() {
      return value;
    }

    @Override
    public Type dataType() {
      return dataType;
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

      return Objects.equals(value, that.value) && Objects.equals(dataType, that.dataType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, dataType);
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
    public Type dataType() {
      throw new UnsupportedOperationException("Cannot get the type of AboveMax");
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
    public Type dataType() {
      throw new UnsupportedOperationException("Cannot get the type of AboveMax");
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

  public static class IntegerNumeral extends BaseNumeral<Integer> {
    public IntegerNumeral(Integer value) {
      super(value, Types.IntegerType.get());
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
      super(value, Types.LongType.get());
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
      return new LongNumeral(this.value() + right.value());
    }

    @Override
    public Numeral<Long> minus(Numeral<Long> right) {
      return new LongNumeral(this.value() - right.value());
    }

    @Override
    public Numeral<Long> multiply(Numeral<Long> right) {
      return new LongNumeral(this.value() * right.value());
    }

    @Override
    public Numeral<Long> divide(Numeral<Long> right) {
      return new LongNumeral(this.value() / right.value());
    }

    @Override
    public Numeral sqrt() {
      return new DoubleNumeral(Math.sqrt(this.value().doubleValue()));
    }
  }

  public static class FloatNumeral extends BaseNumeral<Float> {
    public FloatNumeral(Float value) {
      super(value, Types.FloatType.get());
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
      super(value, Types.DoubleType.get());
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

    @Override
    public Numeral<Double> plus(Numeral<Double> right) {
      return new DoubleNumeral(this.value() + right.value());
    }

    @Override
    public Numeral<Double> minus(Numeral<Double> right) {
      return new DoubleNumeral(this.value() - right.value());
    }

    @Override
    public Numeral<Double> multiply(Numeral<Double> right) {
      return new DoubleNumeral(this.value() * right.value());
    }

    @Override
    public Numeral<Double> divide(Numeral<Double> right) {
      return new DoubleNumeral(this.value() / right.value());
    }

    @Override
    public Numeral sqrt() {
      return new DoubleNumeral(Math.sqrt(this.value().doubleValue()));
    }
  }

  public static class DecimalNumeral extends BaseNumeral<BigDecimal> {
    public DecimalNumeral(BigDecimal value) {
      super(value, Types.DecimalType.of(15, 10));
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

    @Override
    public Numeral<BigDecimal> plus(Numeral<BigDecimal> right) {
      return new DecimalNumeral(this.value().add(right.value()));
    }

    @Override
    public Numeral<BigDecimal> minus(Numeral<BigDecimal> right) {
      return new DecimalNumeral(this.value().subtract(right.value()));
    }

    @Override
    public Numeral<BigDecimal> multiply(Numeral<BigDecimal> right) {
      return new DecimalNumeral(this.value().multiply(right.value()));
    }

    @Override
    public Numeral<BigDecimal> divide(Numeral<BigDecimal> right) {
      BigDecimal result;
      try{
        result = this.value().divide(right.value());
      } catch (ArithmeticException e) {
        //TODO Has it a better implement intends of handling in exception?
        //TODO Check Rounding mode is OK ???
        result = this.value().divide(right.value(),
            ((Types.DecimalType)this.dataType()).scale(), RoundingMode.FLOOR);
      }
      return new DecimalNumeral(result);
    }

    @Override
    public Numeral sqrt() {
      //TODO Check Rounding mode is OK ???

      BigDecimal result;
      try {
        MathContext mathContext = new MathContext(((Types.DecimalType)this.dataType()).precision(),
          RoundingMode.UNNECESSARY);
        result = this.value().sqrt(mathContext);
      } catch (ArithmeticException e) {
        MathContext mathContext = new MathContext(((Types.DecimalType)this.dataType()).precision(),
          RoundingMode.FLOOR);
        result = this.value().sqrt(mathContext)
          .setScale(((Types.DecimalType)this.dataType()).scale(), RoundingMode.FLOOR);
      }
      return new DecimalNumeral(result);
    }
  }

  public static class BigIntegerNumeral extends BaseNumeral<BigInteger> {
    public BigIntegerNumeral(BigInteger value) {
      super(value, Types.BigIntegerType.get());
    }

    @Override
    public <T> Numeral<T> to(Type type) {
      switch (type.typeId()) {
        case BIG_INTEGER:
          return (Numeral<T>) this;
        default:
          return null;
      }
    }

    @Override
    public Numeral<BigInteger> plus(Numeral<BigInteger> right) {
      return new BigIntegerNumeral(this.value().add(right.value()));
    }

    @Override
    public Numeral<BigInteger> minus(Numeral<BigInteger> right) {
      return new BigIntegerNumeral(this.value().subtract(right.value()));
    }

    @Override
    public Numeral<BigInteger> multiply(Numeral<BigInteger> right) {
      return new BigIntegerNumeral(this.value().multiply(right.value()));
    }

    @Override
    public Numeral<BigInteger> divide(Numeral<BigInteger> right) {
      return new BigIntegerNumeral(this.value().divide(right.value()));
    }

    @Override
    public Numeral sqrt() {
      return null;
    }
  }
}
