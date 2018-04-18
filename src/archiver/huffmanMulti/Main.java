package archiver.huffmanMulti;

import java.io.*;

public class Main {
    public static final int BUFFER_SIZE = 512 * 1024;
    public static boolean dumpTable = false;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Use with parameters: [e|a|d|x|v] [-N] inFile outFile");
            System.out.println("                     a|e or nothing for compress");
            System.out.println("                     d|x for decompress");
            System.out.println("                     v dump fequency table");
            System.out.println("                     -N where N is the number of threads");
            return;
        }
        boolean compress = true;

        int threads = Runtime.getRuntime().availableProcessors();
        File inFile = null;
        File outFile = null;

        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            if ("a".equalsIgnoreCase(arg) || "e".equalsIgnoreCase(arg)) {
                compress = true;
            } else if ("d".equalsIgnoreCase(arg) || "x".equalsIgnoreCase(arg)) {
                compress = true;
            } else if ("v".equalsIgnoreCase(arg)) {
                dumpTable = true;
            } else if (arg.startsWith("-")) {
                int thr;
                try {
                    thr = Integer.parseInt(arg.substring(1), 10);

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: canot parse number of threads: " + arg.substring(1));
                    return;
                }
                if (thr <= 0) {
                    System.out.println("ERROR: illegal number of threads: " + arg.substring(1));
                    return;
                }
            } else {
                if (inFile == null) {
                    inFile = new File(arg);
                    if (!inFile.exists() || !inFile.isFile()) {
                        System.out.println("ERROR: inFile does not exists or not a file!");
                        return;
                    }
                } else {
                    outFile = new File(arg);
                }
            }
        }
        if (inFile == null) {
            System.out.println("ERROR: No inFile defined!");
            return;
        }
        if (outFile == null) {
            System.out.println("ERROR: No outFile defined!");
            return;
        }

        System.out.println("Mode              : " + (compress ? "comress" : "decompress"));
        System.out.println("Number of threads : " + threads);
        System.out.println("inFile            : " + inFile.getCanonicalPath());
        System.out.println("outFile           : " + outFile.getCanonicalPath());

        if (compress) {
            compressParallel(threads, inFile, outFile);
        } else {
            decompress(inFile, outFile);
        }
    }

    public static void decompress(File inputFile, File outputFile) {
        long time = System.currentTimeMillis();
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE);
            outStream = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);

            Compressor.DeCompress(inStream, outStream);

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }

                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("decompress: " + (System.currentTimeMillis() - time));
    }

    public static void compress(File inputFile, File outputFile) {
        long time = System.currentTimeMillis();
        long timeCompress = 0;
        long timeCompressE = 0;
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE);
            byte[] inputBuffer = new byte[(int) inputFile.length()];
            inStream.read(inputBuffer);

            outStream = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);
            timeCompress = System.currentTimeMillis();
            Compressor.compress(inputBuffer, outStream);
            timeCompressE = System.currentTimeMillis();
            outStream.flush();

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }

                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("compress: " + (System.currentTimeMillis() - time) + " -file read: "
                + (timeCompressE - timeCompress));
    }

    public static void compressParallel(int threads, File inputFile, File outputFile) {
        long time = System.currentTimeMillis();
        long timeCompress = 0;
        long timeCompressE = 0;
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE);
            long inFileSize = inputFile.length();
            long chunk = (inFileSize + threads) / threads;
            if (chunk > Integer.MAX_VALUE) {
                // if file is too big create threads that will match the size
                System.out.println("INFO: threads will be started to match the max buffer size.");
                chunk = Integer.MAX_VALUE;
                threads = (int) ((inFileSize / Integer.MAX_VALUE) + 1);
            }
            byte[][] buffers = new byte[threads][];

            // read buffers for each thread
            for (int i = 1; i <= threads; i++) {
                byte[] buffer;
                if (i == threads) {
                    buffer = new byte[(int) (inFileSize - chunk * (threads - 1))];
                } else {
                    buffer = new byte[(int) chunk];
                }
                int read = inStream.read(buffer);
                if (read != buffer.length) {
                    System.out.println("ERROR: cannot read required bytes from the file!");
                    System.exit(0);
                }
                buffers[i - 1] = buffer;
            }

            timeCompress = System.currentTimeMillis();
            timeCompressE = timeCompress;

            long timeF = System.currentTimeMillis();
            // start counters
            Counter[] counters = new Counter[threads];
            for (int i = 0; i < threads; i++) {
                counters[i] = new Counter(buffers[i]);
                counters[i].start();
            }
            // wait for them
            for (int i = 0; i < threads; i++) {
                counters[i].join();
            }
            // start merging sums
            Merger merger = new Merger(0, counters);
            merger.start();
            merger.join();
            counters = null;
            System.out.println("frequence count: " + (System.currentTimeMillis() - timeF));

            // get counts and build the huffman tree
            long[] frequenceTable = merger.frequenceTable;
            CharacterTree tree = new CharacterTree(frequenceTable);
            merger = null;

            // compress each buffer
            BufferCompressor[] compressors = new BufferCompressor[threads];
            for (int i = 0; i < threads; i++) {
                compressors[i] = new BufferCompressor(tree, buffers[i]);
                compressors[i].start();
            }
            // wait for them
            long totalCompressedLength = 0;
            for (int i = 0; i < threads; i++) {
                compressors[i].join();
                totalCompressedLength += compressors[i].bitLength;
            }
            int trashLength = (int) (8 - totalCompressedLength % 8);

            // merge results in the output file
            outStream = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);
            BitOutputStream finalBitOutputStream = new BitOutputStream(outStream);
            // write signature
            finalBitOutputStream.writeBinary('H', 8);
            finalBitOutputStream.writeBinary('F', 8);
            // serialize tree
            tree.serializeTo(finalBitOutputStream);
            // write trash length
            finalBitOutputStream.writeBinary(trashLength, 8);
            // and the trash itself
            finalBitOutputStream.writeBinary(0xFF, trashLength);
            // write each compressor into finalBitOutputStream
            for (int i = 0; i < threads; i++) {
                compressors[i].write(finalBitOutputStream);
            }
            // prepare to close
            finalBitOutputStream.flush();
            outStream.flush();
            timeCompressE = System.currentTimeMillis();

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }

                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("compress parallel(" + threads + "): " + (System.currentTimeMillis() - time)
                + " -file read: " + (timeCompressE - timeCompress));
    }
}
