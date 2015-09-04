package org.glacsweb.rti.installer;

import java.net.URLConnection;
import java.net.URL;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.File;

public class Download {

  private String label;
  private String url;
  private String local;

  public boolean exists() throws SecurityException {
    return new File(local).isFile();
  }

  public void download() throws Exception {

    DataInputStream dis = null;
    FileOutputStream fos = null;

    boolean success = false;

    try {

      URLConnection conn = new URL(url).openConnection();

      String lengthString = conn.getHeaderField("Content-Length");

      int contentLength = Integer.parseInt(lengthString);

      System.out.println("Length = " + contentLength);

      dis = new DataInputStream(conn.getInputStream());
      fos = new FileOutputStream(local);

      byte[] buffer = new byte[1000000];
      int size = 0;
      int total = 0;

      while ((size = dis.read(buffer)) != -1) {
        total += size;
        System.out.println("Current = " + total);
        fos.write(buffer, 0, size);
      }

      success = true;

    } finally {

      if (dis != null)
        dis.close();

      if (fos != null)
        fos.close();
    }

    if (success == false) {
      // Perform garbage collection to ensure OS level lock is removed first.
      System.gc();
      new File(local).delete();
    }

    if (success == false) {
      throw new Exception("Couldn't download " + label);
    }
  }
}
