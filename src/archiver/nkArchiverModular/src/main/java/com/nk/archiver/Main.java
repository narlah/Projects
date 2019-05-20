package com.nk.archiver;

import com.nk.archiver.ui.VisualInterface;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

  public static void main(String[] args) {
    // testMethod();
    VisualInterface visual = new VisualInterface();
    Shell shell = visual.initUI();
    shell.open();
    Display display = shell.getDisplay();

    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // void testMethod() {
  // String inFile = "C:\\Programing\\forArchive.txt";
  // String outFile = "C:\\Programing\\fromArchive.txt";
  // String decompressedFile = "C:\\Programing\\decompressedFile.txt";
  // com.nk.archiver.ui.HuffmanCompressor compressor = new com.nk.archiver.ui.HuffmanCompressor(inFile, outFile);
  // com.nk.archiver.algo.HuffmanDeCompresor deCompressor = new com.nk.archiver.algo.HuffmanDeCompresor(outFile,
  // decompressedFile);
  // try {
  // compressor.encode();
  // deCompressor.decode();
  // } catch (IOException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  //
  // }
}
