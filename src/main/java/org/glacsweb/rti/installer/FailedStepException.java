package org.glacsweb.rti.installer;

public class FailedStepException extends Exception {

  public CommandInvocation command;

  public FailedStepException(CommandInvocation command) {
    this.command = command;
  }
}
