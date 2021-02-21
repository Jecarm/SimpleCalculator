package com.jecarm.calculator.command;

import java.util.Properties;
import java.util.Stack;

public class CommandManager {
  private final int MAX_STACK_SIZE = 10;
  private Stack<Command> undoCommands = new Stack<>();
  private Properties props;

  public CommandManager(Properties props) {
    this.props = props;
  }

  public void executeCommand(Command cmd) {
    cmd.execute();
    //TODO limit stack size
    undoCommands.push(cmd);
  }

  public void undo(){
    if (!undoCommands.empty()) {
      Command cmd = undoCommands.pop();
      cmd.undo();
    }
  }

  private int recoverStackSize() {
    return Integer.parseInt(props.getOrDefault("undo.size", MAX_STACK_SIZE).toString());
  }
}
