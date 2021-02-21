package com.jecarm.calculator;

import java.util.*;

/**
 * Application Context
 */
public class ApplicationContext {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<String> stopped = Arrays.asList("quit", "exit");
    Properties config = loadConfiguration();
    Calculator calculator = new Calculator(config);
    while(true){
      String value = sc.nextLine();
      if (stopped.contains(value.trim())) break;
      calculator.parseInputs(value).compute();
    }
    sc.close();
  }

  private static Properties loadConfiguration() {
    //TODO load properties
    return new Properties();
  }
}
