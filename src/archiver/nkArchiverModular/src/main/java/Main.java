//import java.io.IOException;

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
    // HuffmanCompressor compressor = new HuffmanCompressor(inFile, outFile);
    // HuffmanDeCompresor deCompressor = new HuffmanDeCompresor(outFile,
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
