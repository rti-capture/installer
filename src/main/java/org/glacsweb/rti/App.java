package org.glacsweb.rti;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;

/**
 * RTI-VIPS Installer
 *
 */
public class App 
{
  public static void main(String[] args) throws Exception {

    String json = FileUtils.readFileToString(new File("script.json"));

    Script script = new Gson().fromJson(json, Script.class);

    for (Option option : script.options) {

      ArrayList<CommandInvocation> successfulCommands = new ArrayList<CommandInvocation>();

      for (CommandInvocation command : option.commands) {

        boolean success = command.run();

        if (success) {
          successfulCommands.add(command);
        }
      }

      // Run cleanup commands

      for (CommandInvocation command : successfulCommands) {
        if (command.cleanup != null) {
          for (CommandInvocation cleanupCommand : command.cleanup) {
            cleanupCommand.run();
          }
        }
      }
    }
  }
}
