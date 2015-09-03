package org.glacsweb.rti.installer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;

public class CommandInvocation {

	public String command;
  public String label;
  public String directory;
  public String pathExtra;

  public ArrayList<CommandInvocation> cleanup = new ArrayList<CommandInvocation>();

  public int expectedExitValue = 0;

  public transient String stdout = "";
  public transient String stderr = "";

	public transient int exitValue = 0;

  public CommandInvocation(String command) {
    this.command = command;
  }

	public boolean run() throws Exception {
System.out.println("RUNNING COMMAND: " + command);

    UserInterface.setTitle(label);

    String commandString = command;

    commandString = commandString.replaceAll("\\$HOME", System.getProperty("user.home"));
    commandString = commandString.replaceAll("\\$CWD", System.getProperty("user.dir"));

    String[] bits = StringUtils.split(commandString, " ");

		ProcessBuilder processBuilder = new ProcessBuilder(bits);

    if (directory != null)
      processBuilder.directory(new File(directory));

    if (pathExtra != null) {
      Map<String, String> env = processBuilder.environment();
      String pathAddition = pathExtra;

      pathAddition = pathAddition.replaceAll("\\$HOME", System.getProperty("user.home"));
      pathAddition = pathAddition.replaceAll("\\$CWD", System.getProperty("user.dir"));

      env.put("PATH", pathAddition + ":" + env.get("PATH"));
    }

		Process process = processBuilder.start();

		exitValue = process.waitFor();

    stdout = IOUtils.toString(process.getInputStream());
    stderr = IOUtils.toString(process.getErrorStream());
System.out.println("STDOUT: " + stdout);
System.out.println("STDERR: " + stderr);
		return exitValue == expectedExitValue;
	}

  public int commandCount() {

    int count = 1;

    if (cleanup != null)
      count += cleanup.size();

    return count;
  }
}
