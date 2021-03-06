package archiver.huffmanMulti;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A bit-oriented input stream.
 */
public class BitInputStream extends InputStream {
    private InputStream inputStream;
    private int currentByte;
    private int currentBit = 8;

    /**
     * Constructs a BitInputStream with a given inputStream.
     *
     * @param inputStream The underlying input stream to read bits from.
     */
    public BitInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Reads a single bit from the input stream.
     *
     * @return true, if the bit read from the input stream is '1'
     * @throws IOException If an underlying IOException occurs while reading from the
     *                     stream
     */
    public boolean readBit() throws IOException {
        if (currentBit == 8) {
            currentBit = 0;
            currentByte = inputStream.read();

            if (currentByte == -1) throw new EOFException();
        }

        boolean returnValue = (currentByte & (1 << (7 - currentBit))) != 0;

        currentBit++;

        return returnValue;
    }

    /**
     * Reads a unary-coded value from the input stream.
     *
     * @return A unary-coded value read from the input stream
     * @throws IOException If an underlying IOException occurs while reading from the
     *                     stream
     */
    public int readUnary() throws IOException {
        int value = 0;

        while (!readBit())
            value++;

        return value;
    }

    /**
     * Reads a Rice-coded value from the input stream.
     *
     * @param numFixedBits The number of bits used for the M parameter
     * @return The Rice-coded value read from the input stream.
     * @throws IOException If an underlying IOException occurs while reading from the
     *                     stream
     */
    public int readRice(int numFixedBits) throws IOException {
        int q = readUnary();
        int r = readBinary(numFixedBits);
        int m = 1 << numFixedBits;

        return 1 + (q << numFixedBits) + r;
    }

    /**
     * Reads a binary value from the input stream.
     *
     * @param numBits The number of bits to read
     * @return The value for the numBits read
     * @throws IOException If an underlying IOException occurs while reading from the
     *                     stream
     */
    public int readBinary(int numBits) throws IOException {
        if (numBits + currentBit < 8) {
            int offset = 8 - currentBit - numBits;
            currentBit += numBits;
            return (currentByte >> offset) & ((1 << numBits) - 1);
        } else {
            // Read remaining bits in current byte
            int value = 0;
            int bitsRemaining = numBits;
            int bitsRead = 8 - currentBit;
            value = currentByte & ((1 << bitsRead) - 1);
            bitsRemaining -= bitsRead;
            currentBit += bitsRead;

            // Read whole bytes
            int bytesToRead = bitsRemaining / 8;
            for (int i = 0; i < bytesToRead; ++i) {
                currentByte = inputStream.read();

                if (currentByte == -1) throw new EOFException();

                value <<= 8;
                value += currentByte;
            }

            // Read remaining bits
            bitsRemaining = bitsRemaining % 8;
            if (bitsRemaining > 0) {
                currentByte = inputStream.read();
                value <<= bitsRemaining;
                int offset = 8 - bitsRemaining;
                currentBit = bitsRemaining;
                value += (currentByte >> offset) & ((1 << bitsRemaining) - 1);
            }

            return value;
        }
    }

    @Override
    public int read() throws IOException {
        try {
            return readBinary(8);
        } catch (EOFException ex) {
            return -1;
        }
    }
}
