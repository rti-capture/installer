package org.glacsweb.rti.installer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import org.apache.commons.lang.exception.ExceptionUtils;
import java.util.ArrayList;
import javax.swing.JOptionPane;
 
public class UserInterface implements Runnable {

  private static UserInterface ui;

  private JFrame frame;
  private JPanel cards;
  private JLabel title;
  private JProgressBar progressBar;
  private JEditorPane errorPane;
  private JPanel selectPanel;
  private JButton nextButton;

  private enum Card { WELCOME, OPTIONS, PROGRESS, ERROR, FINISHED };

  private Script script;

  private ArrayList<String> optionLabels = new ArrayList<String>();

  public UserInterface(Script script) {
    this.script = script;

    for (Option option : script.options) {
      optionLabels.add(option.label);
      System.out.println("Option: " + option.label);
    }

    this.ui = this;
  }

  @Override
  public void run() {

    frame = new JFrame();

    frame.setTitle("RTI-VIPS Installer");
    frame.setBounds(100, 100, 412, 303);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // FIXME

    frame.getContentPane().setLayout(new BorderLayout(0, 0));

    JPanel actionPanel = new JPanel();
    frame.getContentPane().add(actionPanel, BorderLayout.SOUTH);

    JButton cancelButton = new JButton("Cancel");
    actionPanel.add(cancelButton);

    nextButton = new JButton("Next");

    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        nextButtonPressed(ae);
      }
    });

    actionPanel.add(nextButton);

    JPanel titlePanel = new JPanel();
    frame.getContentPane().add(titlePanel, BorderLayout.NORTH);

    title = new JLabel("Select applications");
    titlePanel.add(title);

    cards = new JPanel();
    frame.getContentPane().add(cards, BorderLayout.CENTER);
    cards.setLayout(new CardLayout(8, 0));

    JPanel selectCard = new JPanel();
    cards.add(selectCard, "select");
    GridBagLayout gbl_selectCard = new GridBagLayout();
    gbl_selectCard.columnWidths = new int[] {500};
    gbl_selectCard.rowHeights = new int[]{46, 0};
    gbl_selectCard.columnWeights = new double[]{0.0, 1.0};
    gbl_selectCard.rowWeights = new double[]{1.0, Double.MIN_VALUE};
    selectCard.setLayout(gbl_selectCard);

    selectPanel = new JPanel();
    GridBagConstraints gbc_selectPanel = new GridBagConstraints();
    gbc_selectPanel.gridwidth = 2;
    gbc_selectPanel.gridx = 0;
    gbc_selectPanel.gridy = 0;
    selectCard.add(selectPanel, gbc_selectPanel);
    selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.PAGE_AXIS));

    for (String optionLabel : optionLabels) {
      JCheckBox chckbxNewCheckBox = new JCheckBox(optionLabel);
      chckbxNewCheckBox.setSelected(true);
      selectPanel.add(chckbxNewCheckBox);
    }

    JPanel progressCard = new JPanel();
    cards.add(progressCard, "progress");

    progressBar = new JProgressBar();
    progressCard.add(progressBar);

    JPanel errorCard = new JPanel();
    cards.add(errorCard, "error");
    errorCard.setLayout(new BoxLayout(errorCard, BoxLayout.X_AXIS));

    errorPane = new JEditorPane();
    errorPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
    errorPane.setEditable(false);
    errorCard.add(errorPane);

    JPanel finishCard = new JPanel();
    cards.add(finishCard, "finish");

    frame.setVisible(true);
  }

  public void nextButtonPressed(ActionEvent evt) {
        
    selectCard("progress");

    Thread worker = new Thread() {

      public void run() {
        try {
          nextButton.setEnabled(false);
          script.setSelectedOptions(getSelectedOptions());
          script.run();
        } catch (Exception e) {
          ui.showError(ExceptionUtils.getStackTrace(e));
        }
      }
    };

    worker.start();
  }

  public static void selectCard(String card) {
    CardLayout cl = (CardLayout) ui.cards.getLayout();
    cl.show(ui.cards, card);
  }

  public static void showError(String text) {
    selectCard("error");
    ui.errorPane.setText(text);
  }

  public static void setMaximumStep(int max) {
    ui.progressBar.setMaximum(max);
  }

  public static void setStep(int value) {
    ui.selectCard("progress");
    ui.progressBar.setValue(value);
  }

  public void setOptions(String[] options) {
    selectPanel.removeAll();

    for (String option : options) {
      JCheckBox checkbox = new JCheckBox(option);
      checkbox.setSelected(true);
      selectPanel.add(checkbox);
    }
  }

  public ArrayList<String> getSelectedOptions() {

    ArrayList<String> selectedOptions = new ArrayList<String>();

    for (Component component : selectPanel.getComponents()) {

      JCheckBox checkbox = (JCheckBox) component;

      if (checkbox.isSelected()) {
        selectedOptions.add(checkbox.getText());
      }
    }

    return selectedOptions;
  }

  public static void showWaitDialog(String label) {
    JOptionPane.showMessageDialog(null, "Please continue once '" + label +
        "' is installed.", "Installer", JOptionPane.INFORMATION_MESSAGE);
  }
}
