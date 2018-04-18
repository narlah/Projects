public abstract class Base_xpressor_abstract {
    String inFile;
    String outFile;

    String getInFileName() {
        return String.valueOf(inFile); // defensive copy
    }

    String getOutFileName() {
        return String.valueOf(outFile); // defensive copy
    }

    public abstract String toString(); // provide more information about the
    // type of the compression used
}
