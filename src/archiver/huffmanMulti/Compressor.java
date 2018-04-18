package archiver.huffmanMulti;

import java.io.*;

public class Compressor {

    private Compressor() {
    }

    public static void multipleCompress(byte[] value, OutputStream outputStream) throws IOException {
        ByteArrayOutputStream lastOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        compress(value, lastOutputStream);
        lastOutputStream.flush();
        compress(lastOutputStream.toByteArray(), tempOutputStream);

        int compressionCount = 0;

        while (lastOutputStream.toByteArray().length > tempOutputStream.toByteArray().length && compressionCount < 255) {
            lastOutputStream = tempOutputStream;
            tempOutputStream = new ByteArrayOutputStream();

            lastOutputStream.flush();
            compress(lastOutputStream.toByteArray(), tempOutputStream);
            compressionCount++;
        }

        outputStream.write(compressionCount);
        outputStream.write(lastOutputStream.toByteArray());
        outputStream.flush();
    }

    public static void multipleDecompress(InputStream inputStream, OutputStream outputStream) throws IOException {
        int compressCount = inputStream.read();

        ByteArrayOutputStream lastOutputStream = new ByteArrayOutputStream();

        int readLenght;
        byte[] buffer = new byte[1024];

        while ((readLenght = inputStream.read(buffer)) > 0) {
            lastOutputStream.write(buffer, 0, readLenght);
        }

        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        for (int i = 0; i < compressCount; i++) {
            lastOutputStream.flush();
            ByteArrayInputStream tempInputStream = new ByteArrayInputStream(lastOutputStream.toByteArray());
            DeCompress(tempInputStream, tempOutputStream);
            lastOutputStream = tempOutputStream;
        }

        lastOutputStream.flush();
        ByteArrayInputStream finalInputStream = new ByteArrayInputStream(lastOutputStream.toByteArray());
        DeCompress(finalInputStream, outputStream);
    }

    public static void compress(byte[] value, OutputStream outputStream) throws IOException {

        CharacterTree tree = new CharacterTree(getFrequenceTable(value));

        BitOutputStream finalBitOutputStream = new BitOutputStream(outputStream);
        // write signature
        finalBitOutputStream.writeBinary('H', 8);
        finalBitOutputStream.writeBinary('F', 8);
        // write tree
        tree.serializeTo(finalBitOutputStream);
        // compress content
        ByteArrayOutputStream rawOutputStream = new ByteArrayOutputStream();
        BitOutputStream bitOutputStream = new BitOutputStream(rawOutputStream);
        tree.write(new ByteArrayInputStream(value), bitOutputStream);
        // write trash length
        int trashLength = bitOutputStream.getTrashLength();
        finalBitOutputStream.writeBinary(trashLength, 8);
        // write the trash
        finalBitOutputStream.writeBinary(0xFF, trashLength);
        // write the content
        finalBitOutputStream.write(rawOutputStream.toByteArray());
        // prepare to close
        finalBitOutputStream.flush();
    }

    public static void DeCompress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream);
        {// check for signature
            int H = bitInputStream.readBinary(8);
            int F = bitInputStream.readBinary(8);
            if (H != 'H' || F != 'F') {
                System.out.println("ERROR: compressed file cannot be recognized!");
                System.exit(0);
            }
        }

        // read tree
        CharacterTree tree = new CharacterTree(bitInputStream);

        // trash used to end the content at a complete byte end.
        int trashLength = bitInputStream.readBinary(8);

        // trash content should be only ones.
        int trashContent = bitInputStream.readBinary(trashLength);

        assert (trashContent == (1 << trashLength) - 1);

        // decompress content
        tree.read(bitInputStream, outputStream);
    }

    private static long[] getFrequenceTable(byte[] value) {
        long time = System.currentTimeMillis();
        long[] frequenceTable = new long[256];

        for (byte letter : value) {
            int letterValue = letter & 0x00FF;
            frequenceTable[letterValue]++;
        }
        System.out.println("frequence count: " + (System.currentTimeMillis() - time));

        return frequenceTable;
    }

}
