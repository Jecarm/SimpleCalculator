package com.jecarm.calculator.command;

import com.jecarm.calculator.RpnStack;

import java.util.Properties;

public class CommandManager {
  private final int DEFAULT_STACK_CAPACITY = 20;
  private RpnStack<Command> undoCommands = new RpnStack<>();
  private Properties props;
  private int capacity;

  public CommandManager(Properties props) {
    this.props = props;
    this.capacity = Integer.parseInt(props.getProperty("undo.stack.capacity",
        String.valueOf(DEFAULT_STACK_CAPACITY)));
  }

  public void executeCommand(Command cmd) {
    if (cmd instanceof Commands.Undo) {
      undo();
    } else {
      cmd.execute();
      if (undoCommands.size() >= capacity) {
        // evict the oldest command
        undoCommands.pollOldestElement();
      }
      undoCommands.push(cmd);
    }
  }

  public void undo(){
    if (undoCommands.nonEmpty()) {
      Command cmd = undoCommands.pop();
      cmd.undo();
    }
  }

  public int currentSize() {
    return undoCommands.size();
  }
}
