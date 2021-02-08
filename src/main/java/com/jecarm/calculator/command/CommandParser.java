package com.jecarm.calculator.command;

import com.google.common.collect.ImmutableList;
import com.jecarm.calculator.common.Parser;

import java.util.List;
import java.util.Locale;

public class CommandParser implements Parser<Command> {

  private final List<String> commands = ImmutableList.of( "undo", "clear");

  @Override
  public Command parseValue(String value) throws IllegalArgumentException {
    if (value == null) return null;
    switch (value.toLowerCase(Locale.ROOT)) {
//      case "undo": return Commands.UndoCommand.get();
//      case "clear": return Commands.ClearCommand.get();
      default: return null;
    }
  }

  public boolean isCommand(String value) {
    return commands.contains(value.toLowerCase(Locale.ROOT));
  }
}
