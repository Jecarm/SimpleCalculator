package com.jecarm.calculator;

import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.command.Literal;
import com.jecarm.calculator.command.Numeral;

import com.jecarm.calculator.command.Numerals;
import com.jecarm.calculator.command.StringLiteral;
import com.jecarm.calculator.common.GeneralParser;
import com.jecarm.calculator.types.Types;
import com.jecarm.calculator.util.NumeralUtil;
import com.jecarm.calculator.util.TypeUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.junit.Assert.*;

public class CalculatorTest {
  Calculator calculator;
  @Before
  public void before() {
    Properties properties = new Properties();
    calculator = new Calculator(properties);
  }

  @Test
  public void evaluateExample1Test() {
    assertEquals("stack:5 2", calculator.parseInputs("5 2").compute().getStackInfo());
  }

  @Test
  public void evaluateExample2Test() {
    //precision: 15, scale max: 10
    //1.4142135623731
    assertEquals("stack:1.4142135623", calculator.parseInputs("2 sqrt").compute().getStackInfo());
    assertEquals("stack:", calculator.parseInputs("clear").compute().getStackInfo());
    assertEquals("stack:2", calculator.parseInputs("4 sqrt").compute().getStackInfo());

  }

  @Test
  public void evaluateExample3Test() {
    assertEquals("stack:3", calculator.parseInputs("5 2 -").compute().getStackInfo());
    assertEquals("stack:0", calculator.parseInputs("3 -").compute().getStackInfo());
    assertEquals("stack:", calculator.parseInputs("clear").compute().getStackInfo());
  }

  @Test
  public void evaluateExample4Test() {
    assertEquals("stack:5 4 3 2", calculator.parseInputs("5 4 3 2").compute().getStackInfo());
    assertEquals("stack:20", calculator.parseInputs("undo undo *").compute().getStackInfo());
    assertEquals("stack:100", calculator.parseInputs("5 *").compute().getStackInfo());
    assertEquals("stack:20 5", calculator.parseInputs("undo").compute().getStackInfo());
  }

  @Test
  public void evaluateExample5Test() {
    assertEquals("stack:7 6", calculator.parseInputs("7 12 2 /").compute().getStackInfo());
    assertEquals("stack:42", calculator.parseInputs("*").compute().getStackInfo());
    assertEquals("stack:10.5", calculator.parseInputs("4 /").compute().getStackInfo());
  }

  @Test
  public void evaluateExample6Test() {
    assertEquals("stack:1 2 3 4 5", calculator.parseInputs("1 2 3 4 5").compute().getStackInfo());
    assertEquals("stack:1 2 3 20", calculator.parseInputs("*").compute().getStackInfo());
    assertEquals("stack:-1", calculator.parseInputs("clear 3 4 -").compute().getStackInfo());
  }

  @Test
  public void evaluateExample7Test() {
    assertEquals("stack:1 2 3 4 5", calculator.parseInputs("1 2 3 4 5").compute().getStackInfo());
    assertEquals("stack:120", calculator.parseInputs("* * * *").compute().getStackInfo());
  }

  @Test
  public void evaluateExample8Test() {
    assertEquals("stack:11", calculator.parseInputs("1 2 3 * 5 + * * 6 5").compute().getStackInfo());
  }

  @Test
  public void inferValueTypeTest() {
    calculator.parseInputs("5 2");
    Numeral literal = TypeUtil.inferValueType("5");
    Numeral expected = Numerals.from(5);
    assertTrue(expected.equals(literal));
    assertTrue(Numerals.from(5.2d).equals(TypeUtil.inferValueType("5.2")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void inferValueTypeExceptionTest() {
    TypeUtil.inferValueType("5.f6");
  }

  @Test
  public void isNumeralTest() {
    assertTrue(NumeralUtil.isNumeral("2233"));
    assertTrue(NumeralUtil.isNumeral("1.234"));
    assertTrue(NumeralUtil.isNumeral("0.1"));
    assertFalse(NumeralUtil.isNumeral("abc"));
    assertFalse(NumeralUtil.isNumeral("0...2"));
  }

  @Test
  public void parserTest() {
    GeneralParser parser = new GeneralParser(new Properties());
    List<Literal> literals = parser.parse("1 2 3 *");
    List<Literal> expected = ImmutableList.of(
      Numerals.from(new BigDecimal(1)),
      Numerals.from(new BigDecimal(2)),
      Numerals.from(new BigDecimal(3)),
      new StringLiteral("*", Types.StringType.get(), 7));
    assertTrue(Objects.equals(literals, expected));
  }
}