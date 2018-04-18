import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public final class HuffmanDeCompresor extends DeCompressor {
    private LinkedHashMap<Character, Integer> treeFromFile;

    public HuffmanDeCompresor(String inFile, String outFile) {
        super(inFile, outFile);
    }

    /**
     * Reads frequencies from the beginning of a file, the first integer is the
     * number of pairs [character{as an integer},frequency]
     *
     * @throws IOException in case there is a problem reading the file
     */
    private void getFrequencesFromFile() throws IOException {
        BinaryStdIn reader = new BinaryStdIn(new BufferedInputStream(new FileInputStream(new File(getInFileName()))));
        treeFromFile = new LinkedHashMap<>();
        int treeLenght = reader.readInt();
        for (int i = 0; i < treeLenght; i++) {
            char currentChar = (char) reader.readInt();
            int currentFreq = reader.readInt();
            treeFromFile.put(currentChar, currentFreq);
            // System.out.println("'" + currentChar + "' " + currentFreq);
        }
        reader.close();
    }

    private Node buildTree() {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (Entry<Character, Integer> entry : treeFromFile.entrySet()) {
            queue.add(new Node(entry.getValue(), entry.getKey(), null, null));
        }
        treeFromFile = null; // freeing resources
        while (queue.size() > 1) {
            Node min1 = queue.remove();
            Node min2 = queue.remove();
            queue.add(new Node(min1.getFreq() + min2.getFreq(), '\1', min1, min2));
        }
        return queue.remove();
    }

    // // temporary printing the codes - debugging purposes
    // private void testDebugbuildCode(Node x, String s) {
    // if (!x.isLeaf()) {
    // buildCode(x.left, s + '0');
    // buildCode(x.right, s + '1');
    // } else {
    // // > debug test temp
    // if (Character.valueOf(x.getLetter()).equals('\r')) {
    // System.out.println("cr" + " " + x.getFreq() + " " + s);
    // } else {
    // System.out.println("'"+x.getLetter() + "' " + x.getFreq() + " " + s);
    // }
    // // < debug test temp
    // }
    // }

    /**
     * Skip the header info.Did not want to use the same reader in
     * getFrequencesFromFile as this would make possible to stale and block the
     * file handler
     *
     * @throws IOException IOException
     */
    @Override
    public void decode() throws IOException {
        getFrequencesFromFile();
        Node treeRoot = buildTree();
        // testDebugbuildCode(treeRoot,"");

        BinaryStdIn reader = new BinaryStdIn(new BufferedInputStream(new FileInputStream(new File(getInFileName()))));

        int treeLenght = reader.readInt();
        for (int i = 0; i < treeLenght * 2; i++) {
            reader.readInt();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getOutFileName())));
        Node tempNode = treeRoot;
        while (!reader.isEmpty()) {
            boolean tmp = reader.readBoolean();
            tempNode = (tmp) ? tempNode.right : tempNode.left;
            if (tempNode.isLeaf()) {
                char c = tempNode.getLetter();
                if (Character.valueOf(c).equals('\r')) {
                    writer.newLine();
                } else {
                    writer.write(c);
                }
                tempNode = treeRoot;
            }
        }
        reader.close();
        writer.close();
    }

    @Override
    public String toString() {
        return getOutFileName() + " -> " + "Huffman DE Compressor" + " -> " + getInFileName();
    }
}
