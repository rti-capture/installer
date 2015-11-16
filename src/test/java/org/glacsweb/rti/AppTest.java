package org.glacsweb.rti.installer;

import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;

/**
 * Unit test for RTI-VIPS
 */
public class AppTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */

  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */

  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  public void resetTestDir() throws java.io.IOException {
    FileUtils.deleteDirectory(new File("testdir"));
    new File("testdir").mkdir();
  }

  public void testEmptyConfiguration() throws Exception
  {
    App app = new App();

    app.loadConfiguration("{ 'options' : [ ] }");
    app.script.run();
  }

  public void testBasicStep() throws Exception {

    resetTestDir();

    App app = new App();

    app.loadConfiguration("{ 'options' : [ { 'label' : 'foo', 'commands' : [ { 'command' : 'touch testdir/foo' } ] } ] }");

    app.script.selectAll();
    app.script.run();

    assertEquals(0, new File("testdir/foo").length());
  }

  public void testScriptIsValidJSON() throws Exception {
    String json = FileUtils.readFileToString(new File("script.json"));
    new Gson().fromJson(json, Script.class);
  }
}
