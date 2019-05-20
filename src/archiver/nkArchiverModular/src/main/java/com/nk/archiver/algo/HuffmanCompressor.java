package com.nk.archiver.algo;

import com.nk.archiver.tools.BinaryStdOut;
import com.nk.archiver.tools.Node;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public final class HuffmanCompressor extends Compressor {

  private HashMap<Character, String> codes = new HashMap<>();
  private LinkedHashMap<Character, Integer> treeToFile = new LinkedHashMap<>();

  public HuffmanCompressor(String inFile, String outFile) {
    super(inFile, outFile);
  }

  public LinkedHashMap<Character, Integer> getTreeToFile() {
    return treeToFile;
  }

  @Override
  public void encode() throws IOException {
    getFrequenciesFromFile();
    Node treeRoot = buildTree();
    buildCode(treeRoot, "");

    BufferedReader reader = new BufferedReader(new FileReader(getInFileName()));
    String line = reader.readLine();

    BinaryStdOut writer = new BinaryStdOut(
        new BufferedOutputStream(new FileOutputStream(new File(getOutFileName()))));
    writeTreeToFile(writer);
    while (line != null) {
      line += "\r";
      for (char c : line.toCharArray()) {
        for (char codeX : codes.get(c).toCharArray()) {
          if (codeX == '0') {
            writer.write(false);
          } else if (codeX == '1') {
            writer.write(true);
          } else {
            reader.close();
            throw new IllegalStateException("Illegal state");
          }
        }
      }
      line = reader.readLine();
    }
    reader.close();
    writer.close();
  }

  @Override
  public String toString() {
    return getInFileName() + " -> " + "Huffman com.nk.archiver.algo.Compressor" + " -> " + getOutFileName();
  }

  @SuppressWarnings("unchecked")
  public HashMap<Character, String> getDataStructure() {
    return (HashMap<Character, String>) codes.clone();
  }

  private void getFrequenciesFromFile() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(getInFileName()));
    String line = reader.readLine();
    try {
      char hashCodeForNewLine = (char) 13;
      while (line != null) {
        increaseCountInHashTable(hashCodeForNewLine);
        for (char current : line.toCharArray()) {
          try {
            increaseCountInHashTable(current);
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("WRONG " + current + " " + Character.getNumericValue(current));
          }
        }
        line = reader.readLine();
      }
    } finally {
      reader.close();
    }
  }

  private Node buildTree() {
    PriorityQueue<Node> queue = treeToFile.entrySet().stream()
        .map(entry -> new Node(entry.getValue(), entry.getKey(), null, null))
        .collect(Collectors.toCollection(PriorityQueue::new));
    while (queue.size() > 1) {
      Node min1 = queue.remove();
      Node min2 = queue.remove();
      queue.add(new Node(min1.getFreq() + min2.getFreq(), '\1', min1, min2));
    }
    return queue.remove();
  }

  private void buildCode(Node x, String s) {
    if (!x.isLeaf()) {
      buildCode(x.left, s + '0');
      buildCode(x.right, s + '1');
    } else {
      codes.put(x.getLetter(), s);
      // testDebugBuildCode(x, s);
    }
  }

  // in treeToFile
  private void increaseCountInHashTable(char c) {
    if (treeToFile.containsKey(c)) {
      treeToFile.put(c, (treeToFile.get(c) + 1));
    } else {
      treeToFile.put(c, 1);
    }
  }

  private void writeTreeToFile(BinaryStdOut writer) {
    int treeRecordSize = treeToFile.size();
    writer.write(treeRecordSize);
    for (Entry<Character, Integer> c : treeToFile.entrySet()) {
      writer.write((int) c.getKey());
      writer.write(c.getValue());
    }
    // testDebugWriteCodetoFile();
  }
  // // ************************************
  // private void testDebugBuildCode(com.nk.archiver.tools.Node x, String s) {
  // if (Character.valueOf(x.getLetter()).equals('\r')) {
  // System.out.println("cr" + "            " + x.getFreq() + "          " + s);
  // } else {
  // System.out.println("code :" + (int) x.getLetter() + " - '" + x.getLetter() + "' " + x.getFreq() + " " + s);
  // }
  // }
  //
  // private void testDebugWriteCodetoFile() {
  // System.out.println("Size:" + treeToFile.size());
  // for (Entry<Character, Integer> c : treeToFile.entrySet()) {
  // if (Character.valueOf(c.getKey()).equals('\r')) {
  // System.out.println("cr" + " " + c.getValue());
  // } else
  // System.out.println("'" + c.getKey() + "'" + " " + c.getValue());
  // }
  // }
  // // ************************************
}
