package org.glacsweb.rti;

import java.util.ArrayList;

public class Option {

  public String label;

  public ArrayList<CommandInvocation> commands = new ArrayList<CommandInvocation>();

  public int commandCount() {

    int count = 0;

    for (CommandInvocation command : commands)
      count += command.commandCount();

    return count;
  }
}
