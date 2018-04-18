import java.io.IOException;

public abstract class DeCompressor extends Base_xpressor_abstract {

    public DeCompressor(String inFile, String outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    public abstract void decode() throws IOException;
}
