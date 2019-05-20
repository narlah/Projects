package com.nk.archiver;

import com.nk.archiver.algo.Compressor;
import com.nk.archiver.algo.GZipStreamCompresor;
import com.nk.archiver.algo.HuffmanCompressor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ArchiveController {


  private Compressor compressor;

  private String compressorName;
  private String inFile;
  private String outFile;

  public void changeCompressor(String compressorNameNew) {
    if (!this.compressorName.equals(compressorNameNew)) {
      this.compressorName = compressorNameNew;
      compressor = null;
    }
  }

  public void compress() throws IllegalArgumentException, IOException {
    if (compressorName != null && inFile != null && outFile != null) {
      switch (compressorName) {
        case "Huffman":
          compressor = new HuffmanCompressor(inFile, outFile);
          break;
        case "GZiPStream":
          compressor = new GZipStreamCompresor(inFile, outFile);
          break;
        default:
          compressor = new HuffmanCompressor(inFile, outFile);
          break;
      }
      compressor.encode();
    } else {
      throw new IllegalArgumentException(
          "One is null -> Name : " + compressorName + " in " + inFile + " or Out is " + outFile);
    }
  }

  public String getInFile() {
    return inFile;
  }

  /**
   * @param inFile the inFile to set
   */
  public void setInFile(String inFile) {
    this.inFile = inFile;
  }

  /**
   * @param extension File Extention
   */
  public String updateOutFileExtension(String extension) {
    if (outFile != null) {
      outFile = outFile + "." + extension;
    }
    return outFile;
  }

  public String getOutFile() {
    return outFile;
  }

  public String setOutFileFromIn(String inFileFullPath) {
    Path p = Paths.get(inFileFullPath);
    Path folder = p.getParent();
    String newName = folder + File.separator + p.getFileName().toString() + ".nik";
    this.outFile = newName;
    return newName;
  }

  public HashMap<?, ?> getDataStructure() {
    return compressor.getDataStructure();
  }

  public String getCompressorName() {
    return compressorName;
  }

  /**
   * @param compressorName the compressorName to set
   */
  public void setCompressorName(String compressorName) {
    this.compressorName = compressorName;
  }

  public Compressor getCompressor() {
    return compressor;
  }

}
