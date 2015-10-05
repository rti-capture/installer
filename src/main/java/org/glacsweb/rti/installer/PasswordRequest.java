package org.glacsweb.rti.installer;

import javax.swing.JOptionPane;

public class PasswordRequest {

  public static void main(String[] args) {

    String s = (String) JOptionPane.showInputDialog(null,
        "Please enter your password:", "Password Request",
        JOptionPane.PLAIN_MESSAGE, null, null, "");

    System.out.println(s);
  }
}
