package org.glacsweb.rti.installer;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class PasswordRequest {

  public static void main(String[] args) {

    final JPasswordField password = new JPasswordField();

    JOptionPane optionPane = new JOptionPane(password,
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.OK_CANCEL_OPTION);

    JDialog dialog = optionPane.createDialog("Password Request");

    dialog.addComponentListener(new ComponentAdapter() {

        public void componentShown(ComponentEvent e) {

          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              password.requestFocusInWindow();
            }
          });

        }
      }
    );

    dialog.setVisible(true);
    dialog.dispose();

    System.out.println(password.getPassword());
  }
}
