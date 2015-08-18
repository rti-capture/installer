package org.glacsweb.rti;

import java.util.ArrayList;

public class Option {

  public String label;

  public ArrayList<CommandInvocation> commands = new ArrayList<CommandInvocation>();

  public Option(String label) {
    this.label = label;
  }
}
