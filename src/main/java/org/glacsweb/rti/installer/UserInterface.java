package org.glacsweb.rti.installer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import org.apache.commons.lang.exception.ExceptionUtils;
 
public class UserInterface implements Runnable {

  private static UserInterface ui;

  private JFrame frame;
  private JPanel cards;
  private JLabel title;
  private JProgressBar progressBar;
  private JEditorPane errorPane;
  private JPanel selectPanel;
  private JButton nextButton;
  private JButton cancelButton;
  private JLabel stepLabel;

  public String storedPassword = null;

  private enum Card { WELCOME, OPTIONS, PROGRESS, ERROR, FINISHED };

  Card currentCard = Card.WELCOME;

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

    try {
      UIManager.setLookAndFeel(
          UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }

    frame = new JFrame();

    String introLabel = "<html><center><h2>Welcome to the installer</h2></center><p>Click \"Next\" to choose the components to install.</p></html>";
    
    String finishLabel = "<html><h2><center>Installation complete</center></h2><p>The requested options were installed successfully. Click \"Finish\" to quit the installer.</p></html>";

    // Start of paste from Eclipse

    frame.setTitle("RTI-VIPS Installer");
    frame.setBounds(100, 100, 412, 303);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    frame.getContentPane().setLayout(new BorderLayout(0, 0));
    
    JPanel actionPanel = new JPanel();
    frame.getContentPane().add(actionPanel, BorderLayout.SOUTH);
    
    cancelButton = new JButton("Cancel");

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        cancelButtonPressed(ae);
      }
    });
   
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
    
    JPanel introCard = new JPanel();
    cards.add(introCard, "intro");
    introCard.setLayout(new BoxLayout(introCard, BoxLayout.X_AXIS));
    
    Component verticalGlue = Box.createVerticalGlue();
    introCard.add(verticalGlue);

    if (script.introMessage != null)
      introLabel = script.introMessage;
    
    JLabel lblThisSoftwareInstaller = new JLabel(introLabel);
    introCard.add(lblThisSoftwareInstaller);
    
    Component verticalGlue_3 = Box.createVerticalGlue();
    introCard.add(verticalGlue_3);
    
    JPanel selectCard = new JPanel();
    cards.add(selectCard, "select");
    selectCard.setLayout(new BoxLayout(selectCard, BoxLayout.X_AXIS));
    
    Component horizontalGlue = Box.createHorizontalGlue();
    selectCard.add(horizontalGlue);
    
    selectPanel = new JPanel();
    selectCard.add(selectPanel);
    selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.PAGE_AXIS));

    JPanel progressCard = new JPanel();
    cards.add(progressCard, "progress");
    
    stepLabel = new JLabel("Step");
    stepLabel.setBounds(new Rectangle(0, 0, 0, 20));
    stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
    stepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    progressBar = new JProgressBar();
    progressCard.setLayout(new BoxLayout(progressCard, BoxLayout.Y_AXIS));
    
    Component verticalGlue_2 = Box.createVerticalGlue();
    progressCard.add(verticalGlue_2);
    progressCard.add(stepLabel);
    
    Component verticalStrut = Box.createVerticalStrut(8);
    progressCard.add(verticalStrut);
    progressCard.add(progressBar);
    
    Component verticalGlue_1 = Box.createVerticalGlue();
    progressCard.add(verticalGlue_1);
    
    JPanel errorCard = new JPanel();
    cards.add(errorCard, "error");
    errorCard.setLayout(new BoxLayout(errorCard, BoxLayout.Y_AXIS));
       
    errorPane = new JEditorPane();
    errorPane.setEditable(false);
    errorPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

    JScrollPane scrollPane = new JScrollPane(errorPane);
    errorCard.add(scrollPane);
    
    Component verticalStrut_1 = Box.createVerticalStrut(10);
    errorCard.add(verticalStrut_1);
    
    JButton copyErrorButton = new JButton("Copy message to clipboard");

    copyErrorButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		copyMessage();
    	}
    });

    copyErrorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    errorCard.add(copyErrorButton);
    
    JPanel finishCard = new JPanel();
    cards.add(finishCard, "finish");
    finishCard.setLayout(new BoxLayout(finishCard, BoxLayout.Y_AXIS));
    
    Component verticalGlue_4 = Box.createVerticalGlue();
    finishCard.add(verticalGlue_4);

    if (script.finishMessage != null)
      finishLabel = script.finishMessage;

    JLabel lblNewLabel = new JLabel(finishLabel);
    finishCard.add(lblNewLabel);
    
    Component verticalGlue_5 = Box.createVerticalGlue();
    finishCard.add(verticalGlue_5);
    
    Component horizontalGlue_1 = Box.createHorizontalGlue();
    selectCard.add(horizontalGlue_1);

    // End of paste from Eclipse

    for (String optionLabel : optionLabels) {
      JCheckBox chckbxNewCheckBox = new JCheckBox(optionLabel);
      chckbxNewCheckBox.setSelected(true);
      selectPanel.add(chckbxNewCheckBox);
    }

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    selectCard("intro");
  }

  public void nextButtonPressed(ActionEvent evt) {

    if (currentCard == Card.WELCOME) {

      selectCard("select");

    } else if (currentCard == Card.OPTIONS) {

      selectCard("progress");

      // ui.requestPassword();

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

    } else if (currentCard == Card.ERROR) {

      System.exit(0);

    } else if (currentCard == Card.FINISHED) {

      System.exit(0);
    }
  }

  public void cancelButtonPressed(ActionEvent evt) {

    Object[] options = { "Yes", "No" };

    int response = JOptionPane.showOptionDialog(null,
        "Are you sure you want to cancel the installation?",
        "Cancel installation",
        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
        null, options, options[0]);

    if (response == 0) {
      System.exit(1);
    }
  }

  public static void selectCard(String card) {

    CardLayout cl = (CardLayout) ui.cards.getLayout();
    cl.show(ui.cards, card);

    if (card.equals("intro")) {

      ui.currentCard = Card.WELCOME;

      ui.setTitle(" ");

      ui.nextButton.setLabel("Next");
      ui.nextButton.setEnabled(true);
      ui.cancelButton.setEnabled(true);

    } else if (card.equals("select")) {

      ui.currentCard = Card.OPTIONS;

      ui.setTitle("Select options");

      ui.nextButton.setLabel("Next");
      ui.nextButton.setEnabled(true);
      ui.cancelButton.setEnabled(true);

    } else if (card.equals("progress")) {

      ui.currentCard = Card.PROGRESS;

      ui.setTitle("Installation in progress");

      ui.nextButton.setLabel("Next");
      ui.nextButton.setEnabled(false);
      ui.cancelButton.setEnabled(false);

    } else if (card.equals("error")) {

      ui.currentCard = Card.ERROR;

      ui.setTitle("Error");

      ui.nextButton.setLabel("Finish");
      ui.nextButton.setEnabled(true);
      ui.cancelButton.setEnabled(false);

    } else if (card.equals("finish")) {

      ui.currentCard = Card.FINISHED;

      ui.setTitle(" ");

      ui.nextButton.setLabel("Finish");
      ui.nextButton.setEnabled(true);
      ui.cancelButton.setEnabled(false);
    }
  }

  public static void showError(String text) {
    selectCard("error");
    ui.errorPane.setText(text);
  }

  public static void copyMessage() {
    StringSelection selection = new StringSelection(ui.errorPane.getText());
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(selection, selection);

    JOptionPane.showMessageDialog(null, "Copied.", "Installer",
        JOptionPane.INFORMATION_MESSAGE);
  }

  public static void setMaximumStep(int max) {
    ui.progressBar.setMaximum(max);
  }

  public static void setStep(int value) {
    ui.selectCard("progress");
    ui.progressBar.setValue(value);
  }

  public static void setStepLabel(String label) {
    ui.stepLabel.setText(label);
  }

  public void setOptions(String[] options) {
    selectPanel.removeAll();

    for (String option : options) {
      JCheckBox checkbox = new JCheckBox(option);
      checkbox.setSelected(true);
      selectPanel.add(checkbox);
    }
  }

  public static void setTitle(String title) {
    ui.title.setText(title);
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

  public static void requestPassword() {

    String s = (String) JOptionPane.showInputDialog(ui.frame,
        "Please enter your password:", "Password Request",
        JOptionPane.PLAIN_MESSAGE, null, null, "");

    if (s != null) {
      ui.storedPassword = s;
    }
  }

  public static void showWaitDialog(String label) {
    JOptionPane.showMessageDialog(null, "Click OK once '" + label +
        "' is installed.", "Installer", JOptionPane.INFORMATION_MESSAGE);
  }

  public static String getStoredPassword() {
    return ui.storedPassword;
  }
}
