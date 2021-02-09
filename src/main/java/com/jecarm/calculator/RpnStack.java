package com.jecarm.calculator;

import java.util.LinkedList;

public class RpnStack<T> {
  private final LinkedList<T> storage;

  public RpnStack() {
    storage = new LinkedList<>();
  }

  /** push to stack */
  public void push(T v) {
    storage.addFirst(v);
  }

  /** check from stack */
  public T peek() {
    return storage.getFirst();
  }

  /** pop from stack */
  public T pop() {
    return storage.removeFirst();
  }

  public void clear() {
    storage.clear();
  }

  public boolean isEmpty() {
    return storage.isEmpty();
  }

  public boolean nonEmpty() {
    return !storage.isEmpty();
  }

  public int size() {
    return storage.size();
  }

  public String showString() {
    LinkedList<String> reversedList = new LinkedList<>();
    for (T element: storage) {
      reversedList.addFirst(element.toString());
    }
    return "stack:" + String.join(" ", reversedList);
  }

  public String toString() {
    return storage.toString();
  }
}
