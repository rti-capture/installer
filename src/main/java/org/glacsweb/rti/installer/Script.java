package org.glacsweb.rti.installer;

import java.util.ArrayList;

public class Script {

  public ArrayList<Option> options = new ArrayList<Option>();

  public int commandCount() {

    int count = 0;

    for (Option option : options)
      count += option.commandCount();

    return count;
  }

  public void run() throws Exception {

    int total = commandCount();
    int current = 0;

    for (Option option : options) {

      ArrayList<CommandInvocation> successfulCommands = new ArrayList<CommandInvocation>();

      for (CommandInvocation command : option.commands) {

        current++;

System.out.println(current + "/" + total + ": " + command.command);
        boolean success = command.run();

        if (success) {
          successfulCommands.add(command);
        }
      }

      // Run cleanup commands

      for (CommandInvocation command : successfulCommands) {
        if (command.cleanup != null) {
          for (CommandInvocation cleanupCommand : command.cleanup) {
            current++;
System.out.println(current + "/" + total + ": " + command.command);
            cleanupCommand.run();
          }
        }
      }
    }
  }
}
