package com.jecarm.calculator.command;

import java.util.Stack;

public class CommandManager {
  private final int MAX_STACK_SIZE = 10;
  private Stack<Command> undoCommands = new Stack<>();

  public void executeCommand(Command cmd) {
    cmd.execute();
    undoCommands.push(cmd);
  }

  public void undo(){
    if (!undoCommands.empty()) {
      Command cmd = undoCommands.pop();
      cmd.undo();
    }
  }
}
