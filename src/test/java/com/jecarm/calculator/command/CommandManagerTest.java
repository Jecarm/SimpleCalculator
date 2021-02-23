package com.jecarm.calculator.command;

import com.jecarm.calculator.RpnStack;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.Assert.assertEquals;


public class CommandManagerTest {

  RpnStack<Numeral> dataStack;
  CommandManager manager;

  @Before
  public void before() {
    dataStack = new RpnStack<>();
    Properties props = new Properties();
    props.setProperty("undo.stack.capacity", "2");
    manager = new CommandManager(props);
  }

  @Test
  public void stackCapacityTest() {
    manager.executeCommand(new NumeralCommand(dataStack, Numerals.from(new BigDecimal(2))));
    manager.executeCommand(new NumeralCommand(dataStack, Numerals.from(new BigDecimal(3))));
    manager.executeCommand(new NumeralCommand(dataStack, Numerals.from(new BigDecimal(5))));
    // original stack info
    assertEquals("stack:2 3 5", dataStack.showString());

    assertEquals(2, manager.currentSize());

    Command plus = new Commands.Plus(dataStack);
    manager.executeCommand(plus);
    // stack info after execute plus
    assertEquals("stack:2 8", dataStack.showString());

    manager.undo();  // except stack: 2 3 5
    assertEquals("stack:2 3 5", dataStack.showString());

    manager.undo();  // except stack: 2 3
    assertEquals("stack:2 3", dataStack.showString());
  }

}