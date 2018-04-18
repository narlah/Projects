package RandomizeMusic;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

class RandomizeEngine {
    private String startDirectory;
    private Vector<String> AllMusicFiles = new Vector<>();

    RandomizeEngine(String dir) {
        this.startDirectory = dir;
        getMusicFiles();
    }

    /**
     * Ensures we pick only the mp3 files
     */
    private void getMusicFiles() {
        File startDir = new File(startDirectory);
        File[] AllFiles = startDir.listFiles();
        if (AllFiles != null && AllFiles.length != 0) {
            Arrays.sort(AllFiles, (f1, f2) -> {
                Integer a = Integer.valueOf("0" + f1.getName().replaceAll("(\\d*).*", "$1"));
                Integer b = Integer.valueOf("0" + f2.getName().replaceAll("(\\d*).*", "$1"));
                return a.compareTo(b);
            });

            for (File file : AllFiles) {
                String path = file.getAbsolutePath();
                Boolean flag = path.endsWith(".mp3");
                if (flag & file.isFile()) {
                    AllMusicFiles.add(file.getName());
                }
            }
        }
    }

    Vector<String> exposeMusicFileList() {
        return this.AllMusicFiles;
    }

    String exposeStartDirectory() {
        return this.startDirectory;
    }

    String exposeStartDirectoryParent() {
        String parentDir;
        parentDir = new File(this.startDirectory).getParent();
        if (parentDir == null)
            parentDir = new File(this.startDirectory).getAbsolutePath();
        return parentDir;
    }

    void changeDirectory(String newDirectory) {
        this.startDirectory = newDirectory;
        AllMusicFiles = new Vector<>();
        getMusicFiles();
    }

    /**
     * Does the main work, shuffles the collected musical files and change names accordingly.
     *
     * @param logFileFlag Flag for the log file
     * @throws IOException in case we can't read things ...
     */
    void randomizeDir(boolean logFileFlag, final JProgressBar progressBar) throws IOException {
        Collections.shuffle(AllMusicFiles);
        int vectorLenght = AllMusicFiles.size();

        // Log file preparation
        Long time = new Date().getTime();
        String logFileName = "logFile_" + time + ".txt";
        LogFileEngine logFileEngine = new LogFileEngine(startDirectory, logFileName);
        String newName;
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(vectorLenght);
        // Main cycle , we shuffle , get the new name, and rename the file
        for (int i = 0; i < vectorLenght; i++) {

            String originalName = AllMusicFiles.elementAt(i);
            newName = getNewName(originalName, i);
            File newNamedFile = new File(startDirectory + "//" + newName);
            final int ii = i;
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> progressBar.setValue(ii + 1));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (newNamedFile.exists()) {
                System.out.println("This file already exists, will not be renamed " + newNamedFile.getAbsolutePath());
            } else {
                File origNamedFile = new File(startDirectory + "//" + originalName);
                // Logging each line with before and after names and paths
                String[] tempList = {origNamedFile.getAbsolutePath(), newNamedFile.length() + "", newNamedFile.getAbsolutePath()};
                // rename files , comment this for testing purposes
                boolean success = origNamedFile.renameTo(newNamedFile);
                if (success) {
                    logFileEngine.logSingleLine(tempList);
                } else {
                    String[] tempListErr = {"There was a problem with renaming the file  ", "0", originalName};
                    logFileEngine.logSingleLine(tempListErr);
                }
            }
        }
        logFileEngine.printTheLog(logFileFlag);
    }

    /**
     * Must remove the numbers in front of the song name to add our numbers. Gets the position of the first letter, ignores numbers and punctuation.
     * Returns new name
     *
     * @param oldName        Old Name
     * @param songNextNumber Next Number
     * @return New Name
     */
    private String getNewName(String oldName, int songNextNumber) {
        int c = 0;
        while (!Character.isLetter(oldName.charAt(c))) {
            if (oldName.charAt(c) == '.') {
                c++;
                break;
            }
            c++;
        }

        String strippedName = oldName.substring(c);
        String newName;
        if (strippedName.isEmpty() || strippedName.equals("mp3")) {
            newName = (songNextNumber + 1) + "." + oldName;
        } else {
            newName = (songNextNumber + 1) + "." + strippedName;
        }
        return newName;
    }
}
