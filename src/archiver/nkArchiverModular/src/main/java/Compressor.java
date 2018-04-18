import java.io.IOException;
import java.util.HashMap;

public abstract class Compressor extends Base_xpressor_abstract {

    Compressor(String inFile, String outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    public abstract void encode() throws IOException;

    public abstract HashMap<?, ?> getDataStructure();
}
