import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

public final class GZipStreamCompresor extends Compressor {
    private final static int BUFFER_SIZE = 2048;

    GZipStreamCompresor(String inFile, String outFile) {
        super(inFile, outFile);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void encode() throws IOException {

        try {
            InputStream fis = new FileInputStream(new File(getInFileName()));
            GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(new File(getOutFileName())));

            byte[] b = new byte[BUFFER_SIZE];
            int count;
            while ((count = fis.read(b)) > 0) {
                gzip.write(b, 0, count);
            }
            fis.close();
            gzip.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getInFileName() + " -> " + "GZIP Stream Compressor" + " -> " + getOutFileName();
    }

    @Override
    public HashMap<?, ?> getDataStructure() {
        // TODO Auto-generated method stub
        return null;
    }

}
