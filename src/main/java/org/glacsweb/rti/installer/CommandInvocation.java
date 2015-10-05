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
  public String passwordTrigger;

  public ArrayList<CommandInvocation> cleanup = new ArrayList<CommandInvocation>();

  public int expectedExitValue = 0;

  public transient String stdout = "";
  public transient String stderr = "";

	public transient int exitValue = 0;

  public CommandInvocation(String command) {
    this.command = command;
  }

  private String substituteVariables(String text) {

    text = text.replaceAll("\\$HOME", System.getProperty("user.home"));
    text = text.replaceAll("\\$CWD", System.getProperty("user.dir"));
    text = text.replaceAll("\\$FILES", "files");

    return text;
  }

	public void run() throws Exception {

    UserInterface.setTitle(label);

    String[] bits = StringUtils.split(command, " ");

    for (int i = 0; i < bits.length; i++)
      bits[i] = substituteVariables(bits[i]);

System.out.println("RUNNING COMMAND: " + substituteVariables(command));

for (String bit : bits)
  System.out.println("bit: " + bit);

		ProcessBuilder processBuilder = new ProcessBuilder(bits);

    if (directory != null) {
      System.out.println("Using directory: " + substituteVariables(directory));
      processBuilder.directory(new File(substituteVariables(directory)));
    }

    if (pathExtra != null) {
      Map<String, String> env = processBuilder.environment();
      env.put("PATH", substituteVariables(pathExtra) + File.pathSeparator + env.get("PATH"));
    }

		Process process = processBuilder.start();

    if (passwordTrigger != null) {
      new TextInject(process.getInputStream(), process.getOutputStream(), passwordTrigger, UserInterface.getStoredPassword()).run();
      new TextInject(process.getErrorStream(), process.getOutputStream(), passwordTrigger, UserInterface.getStoredPassword()).run();
    }

		exitValue = process.waitFor();

    stdout = IOUtils.toString(process.getInputStream());
    stderr = IOUtils.toString(process.getErrorStream());
System.out.println("STDOUT: " + stdout);
System.out.println("STDERR: " + stderr);

    if (exitValue != expectedExitValue)
      throw new FailedStepException(this);
	}

  public int commandCount() {

    int count = 1;

    if (cleanup != null)
      count += cleanup.size();

    return count;
  }
}
