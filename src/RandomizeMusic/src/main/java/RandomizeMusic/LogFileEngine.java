package RandomizeMusic;

import java.io.*;
import java.util.ArrayList;

// -> A new class for handling the log file creation and operation.
class LogFileEngine {
    private File logFile = null;
    private PrintStream fileWriter = null;
    private ArrayList<String[]> logBuffer = new ArrayList<>();

    LogFileEngine(String path, String logFileName) throws IOException {
        logFile = new File(path + "//" + logFileName);
    }

    public LogFileEngine(String path) throws IOException {
        String defLogFileName = "logfile.txt";
        logFile = new File(path + "//" + defLogFileName);
    }

    Boolean logSingleLine(String[] logLine) throws IOException {
        return logBuffer.add(logLine);
    }

    public ArrayList<String[]> returnLogRecords() {
        return logBuffer;
    }

    /**
     * Print the entire buffered log, after that the log file is closed
     */
    void printTheLog(boolean logFileFlag) throws IOException {
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
            this.closeFile();
        } catch (FileNotFoundException f) {
            System.out.println("Could not write a log file. File not found " + logFile.getAbsolutePath());
        } catch (UnsupportedEncodingException unsupCode) {
            System.out.println("Unsupported encoding : " + encoding);
        }

    }

    private void closeFile() throws IOException {
        try {
            fileWriter.close();
        } catch (Exception e) {
            throw new IOException("Problem closing log file : " + logFile);
        }
    }
}

// <- End of log file creator