package org.glacsweb.rti.installer;

public class Dependency {

  private CommandInvocation command;
  private String type;

  public boolean check() throws Exception {
    boolean status = command.run();

    System.out.println("Status = " + command.exitValue);
    System.out.println("Type = " + type);

    if (type.equals("osx installation request")) {
      if (command.stderr.indexOf("version") != -1) {
        System.out.println("Showing WAIT dialog.");
        // Show wait dialog for OS X 
      }
    }

    return status;
  }
}
