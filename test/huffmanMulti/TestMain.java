package huffmanMulti;


import archiver.huffmanMulti.Main;
import junit.framework.TestCase;

import java.io.File;

public class TestMain extends TestCase {
    public void testCompressDecompress() {
        File source = new File("Shakespeare.txt");
        File compressed = new File("Shakespeare.out");
        File compressedParallel = new File("Shakespeare.p.out");
        File decompressed = new File("Shakespeare_out.txt");
        File decompressedParallel = new File("Shakespeare_out.p.txt");

        assertTrue(source.exists());
        compressed.delete();
        decompressed.delete();
        compressedParallel.delete();            //TODO see why the assertion error on testCompressDecompress
        decompressedParallel.delete();
        try {
            Main.compress(source, compressed);
            Main.compressParallel(2, source, compressedParallel);
            Main.compressParallel(3, source, compressedParallel);
            Main.compressParallel(4, source, compressedParallel);
            Main.compressParallel(8, source, compressedParallel);
            Main.decompress(compressed, decompressed);
            Main.decompress(compressedParallel, decompressedParallel);

            // check content of files
        } finally {
            compressed.delete();
            decompressed.delete();
            compressedParallel.delete();
            decompressedParallel.delete();
        }
    }
}
