package archiver.huffmanAdaptive;

import java.io.*;
import java.util.Arrays;

public final class AdaptiveHuffmanCompress {

    public static void main(String[] args) throws IOException {
        // Show what command line arguments to use
        if (args.length == 0) {
            System.err.println("Usage: java AdaptiveHuffmanCompress InputFile OutputFile");
            System.exit(1);
            return;
        }

        // Otherwise, compress
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
        BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        try {
            compress(in, out);
        } finally {
            out.close();
            in.close();
        }
    }

    static void compress(InputStream in, BitOutputStream out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqTable = new FrequencyTable(initFreqs);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        // We don't need to make a canonical code since we don't transmit the
        // code tree
        enc.codeTree = freqTable.buildCodeTree();
        int count = 0;
        while (true) {
            int b = in.read();
            if (b == -1) break;
            enc.write(b);

            freqTable.increment(b);
            count++;
            // Update code tree
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)
                enc.codeTree = freqTable.buildCodeTree();
            if (count % 262144 == 0) // Reset frequency table
                freqTable = new FrequencyTable(initFreqs);
        }
        enc.write(256); // EOF
    }

    private static boolean isPowerOf2(int x) {
        return x > 0 && (x & -x) == x;
    }

}
