package org.glacsweb.rti.installer;

import java.util.ArrayList;
import java.util.HashMap;

public class Script {

  public String introMessage;
  public String finishMessage;

  public HashMap<String,Dependency> dependencies;
  public ArrayList<Option> options;

  public transient ArrayList<String> selectedOptions;

  public void setSelectedOptions(ArrayList<String> selectedOptions) {
    this.selectedOptions = selectedOptions;
  }

  public int commandCount() throws Exception {

    int count = 0;

    for (Option option : options)
      if (selectedOptions.indexOf(option.label) != -1)
        count += option.commandCount();

    return count;
  }

  public void selectAll() {

    selectedOptions = new ArrayList<String>();

    for (Option option : options) {
      selectedOptions.add(option.label);
    }
  }

  public void run() throws Exception {

    int total = commandCount();
    int current = 0;

    UserInterface.setMaximumStep(total);

    try {

      for (Option option : options) {

        if (selectedOptions.indexOf(option.label) != -1) {

          ArrayList<CommandInvocation> successfulCommands = new ArrayList<CommandInvocation>();

          try {

            if ((option.dependencies != null) && (dependencies != null)) {

              for (String dependencyKey : option.dependencies) {

                Dependency dependency = dependencies.get(dependencyKey);

                if (dependency == null)
                  throw new Exception("Unrecognised dependency: " + dependencyKey);

                if (dependency.check() == false) {

                  if (dependency.downloads != null) {
                    for (Download download : dependency.downloads) {
                      if (download.exists() == false) {
                        download.download();
                      }
                    }
                  }

                  if (dependency.installCommands != null) {

                    for (CommandInvocation command : dependency.installCommands) {

                      current++;

                      UserInterface.setStep(current);

                      System.out.println(current + "/" + total + ": " + command.command);

                      command.run();
                    }
                  }
                }
              }
            }

            for (CommandInvocation command : option.commands) {

              current++;

              UserInterface.setStep(current);

              command.run();

              successfulCommands.add(0, command);
            }

          } finally {

            // Run cleanup commands

            for (CommandInvocation command : successfulCommands) {
              if (command.cleanup != null) {
                for (CommandInvocation cleanupCommand : command.cleanup) {
                  current++;
                  UserInterface.setStep(current);

                  cleanupCommand.run();
                }
              }
            }
          }
        }
      }

      UserInterface.selectCard("finish");

    } catch (FailedStepException e) {

      UserInterface.showError(e.command.stderr + "\n" + e.command.stdout);
    }
  }
}
