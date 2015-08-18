package org.glacsweb.rti;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;

public class CommandInvocation {

	public String command;

  public ArrayList<CommandInvocation> cleanup = new ArrayList<CommandInvocation>();

  public int expectedExitValue = 0;

  public transient String stdout = "";
  public transient String stderr = "";

	public transient int exitValue = 0;

	public boolean run() throws Exception {

    String[] bits = StringUtils.split(command, " ");

		Process process = new ProcessBuilder(bits).start();

		exitValue = process.waitFor();

    stdout = IOUtils.toString(process.getInputStream());
    stderr = IOUtils.toString(process.getErrorStream());

		return exitValue == expectedExitValue;
	}

  public int commandCount() {
    return 1 + cleanup.size();
  }
}
