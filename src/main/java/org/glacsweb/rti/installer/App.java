package org.glacsweb.rti.installer;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import javax.swing.SwingUtilities;

/**
 * RTI-VIPS Installer
 *
 */
public class App 
{
  protected Script script;

  public void loadConfiguration(String json) {

    // Lines that start with a # symbol are treated as comments.
    json = json.replaceAll("^#.*", "");

    script = new Gson().fromJson(json, Script.class);

    for (Option option : script.options) {
      option.script = script;
    }
  }

  public static void main(String[] args) throws Exception {

    String json = FileUtils.readFileToString(new File("script.json"));

    App app = new App();

    app.loadConfiguration(json);

    UserInterface ui = new UserInterface(app.script);

    // Schedules the application to be run at the correct time in the event queue.
    SwingUtilities.invokeLater(ui);
  }
}
