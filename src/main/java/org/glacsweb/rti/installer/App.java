package org.glacsweb.rti.installer;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;

/**
 * RTI-VIPS Installer
 *
 */
public class App 
{
  public static void main(String[] args) throws Exception {

    String json = FileUtils.readFileToString(new File("script.json"));

    Script script = new Gson().fromJson(json, Script.class);

    script.run();
  }
}
