package archiver.huffmanMulti;

public class Merger extends Thread {
    public long[] frequenceTable;
    int pos;
    Counter[] counters;

    public Merger(int pos, Counter[] counters) {
        this.pos = pos;
        this.counters = counters;
        setName("Merger " + pos);
    }

    public void run() {
        frequenceTable = counters[pos].frequenceTable;
        if (pos * 2 + 2 < counters.length) {
            // two nodes
            Merger merger1 = new Merger(pos * 2 + 1, counters);
            Merger merger2 = new Merger(pos * 2 + 2, counters);
            merger1.start();
            merger2.start();
            try {
                merger1.join();
                merger2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
            long[] frequence1 = merger1.frequenceTable;
            long[] frequence2 = merger2.frequenceTable;
            for (int i = 0; i < 256; i++) {
                frequenceTable[i] += frequence1[i] + frequence2[i];
            }
        } else if (pos * 2 + 2 == counters.length) {
            // one node
            long[] frequence1 = counters[pos * 2 + 1].frequenceTable;
            for (int i = 0; i < 256; i++) {
                frequenceTable[i] += frequence1[i];
            }
        } else {
            // no nodes
        }
    }
}
