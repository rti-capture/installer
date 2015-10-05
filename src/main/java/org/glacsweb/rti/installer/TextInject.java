package org.glacsweb.rti.installer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TextInject implements Runnable {

  private InputStream inputStream;
  private OutputStream outputStream;
  private String trigger;
  private String text;

  public TextInject(InputStream inputStream, OutputStream outputStream,
      String trigger, String text) {

    this.inputStream = inputStream;
    this.outputStream = outputStream;
    this.trigger = trigger;
    this.text = text;
  }

  public void inject() throws java.io.IOException {
    System.out.println("TRIGGER");
    outputStream.write(text.getBytes());
  }

  public void run() {

    InputStreamReader reader = new InputStreamReader(inputStream);
    String response = "";
    int lastPosition = -1;

    try {

      while (true) {

        char[] buf = new char[100];

        int charsRead = reader.read(buf, 0, 100);

        if (charsRead == -1)
          break;

        response += new String(buf);

System.out.println("Got piece: " + new String(buf));

        int thisPosition = response.lastIndexOf(trigger);

        if (thisPosition != lastPosition) {
          inject();
        }

        lastPosition = thisPosition;
      }

    } catch (java.io.IOException e) {

      // Ignore IO errors
    }
  }

  /*
  public void run() {

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    try {

      while (true) {

        String line = reader.readLine();

        if (line == null)
          break;

System.out.println("Considering: " + line);
        if (line.indexOf(trigger) != -1) {
          System.out.println("TRIGGER");
          outputStream.write(text.getBytes());
        }
      }

    } catch (java.io.IOException e) {

      // Ignore IO errors
    }
  }*/
}
