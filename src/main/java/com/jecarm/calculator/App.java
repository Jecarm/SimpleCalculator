package com.jecarm.calculator;

import java.util.*;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<String> stopped = Arrays.asList("quit", "exit");
    Calculator calculator = new Calculator();
    while(true){
      String value = sc.nextLine();
      if (stopped.contains(value.trim())) break;
      calculator.parseInputs(value);
      calculator.compute();
    }
    sc.close();
  }
}
