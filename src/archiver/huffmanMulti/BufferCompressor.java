package archiver.huffmanMulti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferCompressor extends Thread {
    private static int id = 0;
    private final CharacterTree tree;
    private final byte[] buffer;
    private final ByteArrayOutputStream rawOutputStream;
    private final BitOutputStream bitOutputStream;
    public int bitLength;
    private boolean finished = false;

    public BufferCompressor(CharacterTree tree, byte[] buffer) {
        this.tree = tree;
        this.buffer = buffer;
        rawOutputStream = new ByteArrayOutputStream(buffer.length);
        bitOutputStream = new BitOutputStream(rawOutputStream);
        setName("BufferCompressor" + (id++));
    }

    public void run() {
        try {
            tree.write(new ByteArrayInputStream(buffer), bitOutputStream);
            bitOutputStream.flush();
            bitLength = rawOutputStream.size() * 8 + bitOutputStream.getBitsCount();
            bitOutputStream.writeBinary(0xFF, 8);
            bitOutputStream.flush();
            finished = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void write(BitOutputStream outBitOutputStream) {
        if (finished) {
            try {
                byte[] result = rawOutputStream.toByteArray();
                int bytes = bitLength / 8;
                for (int i = 0; i < bytes; i++) {
                    outBitOutputStream.writeBinary(result[i], 8);
                }
                // write remaining bits
                int remainigBits = bitLength % 8;
                if (remainigBits > 0) {
                    outBitOutputStream.writeBinary(result[bytes], remainigBits);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
