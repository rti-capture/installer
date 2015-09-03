package org.glacsweb.rti.installer;

import java.util.ArrayList;

public class Dependency {

  private CommandInvocation checkCommand;
  private String label;
  private String type;
  public ArrayList<CommandInvocation> installCommands;

  public boolean check() {

    boolean status;

    try {
      status = checkCommand.run();
    } catch (Exception e) {
      status = false;
    }

    System.out.println("Status = " + checkCommand.exitValue);
    System.out.println("Expected = " + checkCommand.expectedExitValue);
    System.out.println("Type = " + type);

    if (type != null) {
      if (type.equals("confirmation")) {
        if (status == false) {
          System.out.println("Showing WAIT dialog.");

          UserInterface.showWaitDialog(label);
        }
      }
    }

    return status;
  }

  public int commandCount() {

    if (installCommands == null) {
      return 0;
    } else {
      if (check() == false) {
        return installCommands.size();
      } else {
        return 0;
      }
    }
  }
}
