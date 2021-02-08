package com.jecarm.calculator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumeralUtil {
  public static boolean isNumeral(String value) {
    // 检查value合法性
    Pattern p = Pattern.compile("\\d+||\\d*\\.\\d+||\\d*\\.?\\d+?e[+-]\\d*\\.?\\d+?||e[+-]\\d*\\.?\\d+?");
    Matcher m = p.matcher(value);
    return m.matches();
  }
}
