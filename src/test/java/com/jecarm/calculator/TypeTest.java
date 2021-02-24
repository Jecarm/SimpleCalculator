package com.jecarm.calculator;

import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.Literal;
import com.jecarm.calculator.command.Numerals;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.*;

public class TypeTest {

  @Test
  public void bigDecimalTest() {
    BigDecimal decimal = new BigDecimal("22");
    assertEquals("22", new BigDecimal("22").toString());
    assertEquals("0.123", new BigDecimal("0.123").toString());
    assertEquals("100000000000000", new BigDecimal("100000000000000").toString());
    assertEquals("0.123456789123456789", new BigDecimal("0.123456789123456789").toString());
    assertEquals("44", (decimal.add(new BigDecimal("22"))).toString());
    assertEquals("44.2222", (decimal.add(new BigDecimal("22.2222"))).toString());
    assertEquals("488.84", (decimal.multiply(new BigDecimal("22.22"))).toString());
    BigDecimal d1 = decimal.divide(new BigDecimal("22.22"), 15, RoundingMode.HALF_UP);
    assertEquals("0.990099009900990", d1.toString());

    BigDecimal d2 = decimal.divide(new BigDecimal("2"), 15, RoundingMode.UNNECESSARY);
    assertEquals("11.000000000000000", d2.toString());
  }

  @Test
  public void numeralTypeEqualsTest() {
      assertEquals(Numerals.from(1), Numerals.from(1));
      assertNotEquals(Numerals.from(1), Numerals.from(1L));
      assertEquals(Numerals.from(1L), Numerals.from(1L));
      assertEquals(Numerals.from(1.1f), Numerals.from(1.1f));
      assertEquals(Numerals.from(1.000), Numerals.from(1.000));
      assertEquals(Numerals.from(new BigDecimal(15)), Numerals.from(new BigDecimal(15)));
  }
}
