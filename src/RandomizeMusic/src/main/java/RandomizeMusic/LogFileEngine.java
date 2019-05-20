package RandomizeMusic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

// handling the log file creation and operation.
class LogFileEngine {

  private File logFile;
  private PrintStream fileWriter = null;
  private ArrayList<String[]> logBuffer = new ArrayList<>();

  LogFileEngine(String path, String logFileName) {
    logFile = new File(path + "//" + logFileName);
  }

  public LogFileEngine(String path) {
    String defLogFileName = "logfile.txt";
    logFile = new File(path + "//" + defLogFileName);
  }

  Boolean logText(String[] text) {
    return logBuffer.add(text);
  }

  ArrayList<String[]> returnLogRecords() {
    return logBuffer;
  }

  /**
   * Print the entire buffered log, after that the buffer is cleared
   */
  void dumpTheLog(boolean logFileFlag) throws IOException {
    if (!logFileFlag) {
      return;
    }
    String encoding = "utf-8";
    try {
      logFile.createNewFile();
      fileWriter = new PrintStream(logFile, encoding);
      int longestSong = 0;
      int len;
      for (String[] strArray : logBuffer) {
        len = strArray[0].length();
        if (longestSong < len) {
          longestSong = len;
        }
      }
      String temp;
      for (String[] strPrint : logBuffer) {
        int currentSize = Integer.parseInt(strPrint[1]);
        int spacesAdded = longestSong - currentSize;
        temp = String.format("%-" + spacesAdded + "s", strPrint[0]);
        fileWriter.println(temp + " ---> " + strPrint[2]);
      }

      logBuffer.clear();
    } catch (FileNotFoundException f) {
      System.out.println("Could not write a log file. File not found " + logFile.getAbsolutePath());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      System.out.println(
          "Unsupported encoding : " + encoding + unsupportedEncodingException.getMessage());
    }

  }

  void closeFile() throws IOException {
    if (fileWriter == null) {
      dumpTheLog(true);
    }
    try {
      fileWriter.close();
    } catch (Exception e) {
      throw new IOException("Problem closing log file : " + logFile);
    }
  }
}

// <- End of log file creator