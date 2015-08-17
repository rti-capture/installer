package org.glacsweb.rti;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class CommandInvocation {

	public String[] command;

  public ArrayList<CommandInvocation> cleanup = new ArrayList<CommandInvocation>();

	public ArrayList<String> stdout = new ArrayList<String>();
	public ArrayList<String> stderr = new ArrayList<String>();

	public int exitValue = 0;
  public int expectedExitValue = 0;

	public CommandInvocation(String... command) {
		this.command = command;
	}

	public CommandInvocation(String command) {
		this.command = StringUtils.split(command, " ");
	}

	public boolean run() throws Exception {

System.out.println(StringUtils.join(command, " "));
		Process process = new ProcessBuilder(command).start();

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader ibr = new BufferedReader(isr);

		InputStream es = process.getErrorStream();
		InputStreamReader esr = new InputStreamReader(es);
		BufferedReader ebr = new BufferedReader(esr);

		exitValue = process.waitFor();

		String line;

		while ((line = ibr.readLine()) != null) {
			stdout.add(line);
		}

		while ((line = ebr.readLine()) != null) {
			stderr.add(line);
		}

		return exitValue == expectedExitValue;
	}
}
