import java.io.*;
import java.util.zip.GZIPInputStream;

public final class GZipStreamDeCompresor extends DeCompressor {
    private final static int BUFFER_SIZE = 2048;

    public GZipStreamDeCompresor(String inFile, String outFile) {
        super(inFile, outFile);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void decode() throws IOException {

        try {
            OutputStream fos = new FileOutputStream(new File(getInFileName()));
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(new File(getOutFileName())));

            byte[] b = new byte[BUFFER_SIZE];
            int count;
            while ((count = gzip.read(b)) > 0) {
                fos.write(b, 0, count);
            }
            fos.close();
            gzip.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getInFileName() + " -> " + "GZIP Stream Compressor" + " -> " + getOutFileName();
    }

}
