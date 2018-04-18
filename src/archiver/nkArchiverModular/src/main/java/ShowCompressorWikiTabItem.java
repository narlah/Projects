import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

class ShowCompressorWikiTabItem {
    private CTabFolder ctabFolderBase; // add item with info here
    private String compressorName;
    private CTabItem compressorInfoTabItem;
    private Text label;

    ShowCompressorWikiTabItem(CTabFolder tabFolder, String compressorName) {
        this.ctabFolderBase = tabFolder;
        this.compressorName = compressorName;
        initUI();
    }

    private void initUI() {
        compressorInfoTabItem = new CTabItem(ctabFolderBase, SWT.BORDER);
        compressorInfoTabItem.setText("Compressor Wiki");

        Composite compositeWiki = new Composite(ctabFolderBase, SWT.BORDER);
        compositeWiki.setLayout(null);
        label = new Text(compositeWiki, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        label.setEditable(false);
        changeTo(compressorName);

        label.setBounds(0, 21, 590, 470);
        label.setEnabled(true);
        compressorInfoTabItem.setControl(compositeWiki);
    }

    void changeTo(String selectedMethod) {
        switch (selectedMethod) {
            case "Huffman":
                label.setText("Huffman Coding\n\nIn computer science and information theory, a Huffman code is an optimal prefix code found using the algorithm developed by David A. Huffman while he was a Ph.D. student at MIT, and published in the 1952 paper \"A Method for the Construction of Minimum-Redundancy Codes\".[1] The process of finding and/or using such a code is called Huffman coding and is a common technique in entropy encoding, including in lossless data compression. The algorithm's output can be viewed as a variable-length code table for encoding a source symbol (such as a character in a file). Huffman's algorithm derives this table based on the estimated probability or frequency of occurrence (weight) for each possible value of the source symbol. As in other entropy encoding methods, more common symbols are generally represented using fewer bits than less common symbols. Huffman's method can be efficiently implemented, finding a code in linear time to the number of input weights if these weights are sorted.[2] However, although optimal among methods encoding symbols separately, Huffman coding is not always optimal among all compression methods."
                        + "\n\nHistory.\n"
                        + " In 1951, David A. Huffman and his MIT information theory classmates were given the choice of a term paper or a final exam. The professor, Robert M. Fano, assigned a term paper on the problem of finding the most efficient binary code. Huffman, unable to prove any codes were the most efficient, was about to give up and start studying for the final when he hit upon the idea of using a frequency-sorted binary tree and quickly proved this method the most efficient.[3]"
                        + "In doing so, the student outdid his professor, who had worked with information theory inventor Claude Shannon to develop a similar code. By building the tree from the bottom up instead of the top down, Huffman avoided the major flaw of the suboptimal Shannon-Fano coding."
                        + "\n\nTerminology\n"
                        + "Huffman coding uses a specific method for choosing the representation for each symbol, resulting in a prefix code (sometimes called \"prefix-free codes\", that is, the bit string representing some particular symbol is never a prefix of the bit string representing any other symbol). Huffman coding is such a widespread method for creating prefix codes that the term \"Huffman code\" is widely used as a synonym for \"prefix code\" even when such a code is not produced by Huffman's algorithm.");
                break;
            case "GZiPStream":
                label.setText("Zip (file format)\n\nZIP is an archive file format that supports lossless data compression. A .ZIP file may contain one or more files or folders that may have been compressed. The .ZIP file format permits a number of compression algorithms. The format was originally created in 1989 by Phil Katz, and was first implemented in PKWARE, Inc.'s PKZIP utility, as a replacement for the previous ARC compression format by Thom Henderson. The .ZIP format is now supported by many software utilities other than PKZIP. Microsoft has included built-in .ZIP support (under the name \"compressed folders\") in versions of Microsoft Windows since 1998. Apple has included built-in .ZIP support in Mac OS X 10.3 (via BOMArchiveHelper, now Archive Utility) and later. Most free operating systems have built in support for .ZIP in similar manners to Windows and Mac OS X."
                        + ".ZIP files generally use the file extensions \".zip\" or \".ZIP\" and the MIME media type application/zip.ZIP is used as a base file format by many programs, usually under a different name. When navigating a file system via a user interface, graphical icons representing .ZIP files often appear as a document or other object prominently featuring a zipper."
                        + "\n\nHistory\n"
                        + "The .ZIP file format was created by Phil Katz of PKWARE. He created the format after his company had lawsuits filed against him by Systems Enhancement Associates (SEA) claiming that his archiving products were derivatives of SEA's ARC archiving system. The name \"zip\" (meaning \"move at high speed\") was suggested by Katz's friend, Robert Mahoney. They wanted to imply that their product would be faster than ARC and other compression formats of the time. The earliest known version of .ZIP File Format Specification was first published as part of PKZIP 0.9 package under the file APPNOTE.TXT in 1989."
                        + "The .ZIP file format was released into the public domain, but some ZIP features are covered by patents or pending patents.");
                break;
            case "LZ77":
                label.setText("LZ77\n\n LZ77 and LZ78 are the two lossless data compression algorithms published in papers by Abraham Lempel and Jacob Ziv in 1977 and 1978. They are also known as LZ1 and LZ2 respectively. These two algorithms form the basis for many variations including LZW, LZSS, LZMA and others. Besides their academic influence, these algorithms formed the basis of several ubiquitous compression schemes, including GIF and the DEFLATE algorithm used in PNG. They are both theoretically dictionary coders. LZ77 maintains a sliding window during compression. This was later shown to be equivalent to the explicit dictionary constructed by LZ78�however, they are only equivalent when the entire data is intended to be decompressed. LZ78 decompression allows random access to the input as long as the entire dictionary is available, while LZ77 decompression must always start at the beginning of the input.The algorithms were named an IEEE Milestone in 2004.\n\nTheoretical\n\n efficiencyIn the second of the two papers that introduced these algorithms they are analyzed as encoders defined by finite-state machines. A measure analogous to information entropy is developed for individual sequences (as opposed to probabilistic ensembles). This measure gives a bound on the compression ratio that can be achieved. It is then shown that there exist finite lossless encoders for every sequence that achieve this bound as the length of the sequence grows to infinity. In this sense an algorithm based on this scheme produces asymptotically optimal encodings. This result can be proved more directly, as for example in notes by Peter Shor.");
                break;
            default:
                label.setText("Empty");
        }
    }
}
