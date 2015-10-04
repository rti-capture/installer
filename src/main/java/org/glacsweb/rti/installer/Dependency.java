package org.glacsweb.rti.installer;

import java.util.ArrayList;

public class Dependency {

  private CommandInvocation checkCommand;
  private String label;
  private String type;
  public ArrayList<CommandInvocation> installCommands;
  public ArrayList<Download> downloads;

  public boolean check() {

    boolean success = true;

    try {
      checkCommand.run();
    } catch (Exception e) {
      success = false;
    }

    System.out.println("Status = " + checkCommand.exitValue);
    System.out.println("Expected = " + checkCommand.expectedExitValue);
    System.out.println("Type = " + type);

    if (type != null) {
      if (type.equals("confirmation")) {
        if (success == false) {
          System.out.println("Showing WAIT dialog.");

          UserInterface.showWaitDialog(label);
        }
      }
    }

    return success;
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
