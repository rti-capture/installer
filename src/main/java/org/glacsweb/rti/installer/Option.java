package org.glacsweb.rti.installer;

import java.util.ArrayList;

public class Option {

  public String label;
  public String[] dependencies;
  public ArrayList<CommandInvocation> commands = new ArrayList<CommandInvocation>();
  public Script script;

  public int commandCount() throws Exception {

    int count = 0;

    if ((dependencies != null) && (script.dependencies != null)) {

      for (String dependencyKey : dependencies) {
        Dependency dependency = script.dependencies.get(dependencyKey);

        if (dependency == null)
          throw new Exception("Unrecognised dependency: " + dependencyKey);

        count += dependency.commandCount();
      }
    }

    for (CommandInvocation command : commands)
      count += command.commandCount();

    return count;
  }
}
