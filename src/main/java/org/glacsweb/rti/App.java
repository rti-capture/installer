package org.glacsweb.rti;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * RTI-VIPS Installer
 *
 */
public class App 
{
  public static void main(String[] args) throws Exception {

    Script script = new Script();

    Option op1 = new Option("RTI Viewer");

    op1.commands.add(new CommandInvocation("mkdir -p build/image"));
    op1.commands.add(new CommandInvocation("mkdir -p build/files"));
    op1.commands.add(new CommandInvocation("cp -r /Users/dgc/rti_files/osx/RTIViewer-1.1.0.dmg build/files"));
  
    CommandInvocation hdiCommand1 =
      new CommandInvocation("hdiutil attach -mountpoint build/image build/files/RTIViewer-1.1.0.dmg");
  
    hdiCommand1.cleanup.add(new CommandInvocation("hdiutil detach build/image"));
  
    op1.commands.add(hdiCommand1);
  
    op1.commands.add(new CommandInvocation("cp -r build/image/RTIViewer.app /Applications"));

    script.options.add(op1);

    for (Option option : script.options) {

      ArrayList<CommandInvocation> successfulCommands = new ArrayList<CommandInvocation>();

      for (CommandInvocation command : option.commands) {

        System.out.println("Running: " + command.command);

        boolean success = command.run();

        if (success) {
          successfulCommands.add(command);
        }

        System.out.println("Output:");

        for (String line : command.stdout) {
          System.out.println(line);
        }

        System.out.println("Error:");

        for (String line : command.stderr) {
          System.out.println(line);
        }
      }

      // Run cleanup commands

      for (CommandInvocation command : successfulCommands) {
        for (CommandInvocation cleanupCommand : command.cleanup) {
          cleanupCommand.run();
        }
      }
    }
  }
}
