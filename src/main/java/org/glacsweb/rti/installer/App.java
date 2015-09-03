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
  public static void main(String[] args) throws Exception {

    String json = FileUtils.readFileToString(new File("script.json"));

    // Lines that start with a # symbol are treated as comments.

    json = json.replaceAll("^#.*", "");

    Script script = new Gson().fromJson(json, Script.class);

    UserInterface ui = new UserInterface(script);

    // Schedules the application to be run at the correct time in the event queue.
    SwingUtilities.invokeLater(ui);

//   System.out.println(script.dependencies.get("git").check());
//   script.run();

    
  }
}
