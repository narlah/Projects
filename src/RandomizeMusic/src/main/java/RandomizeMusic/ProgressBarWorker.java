package RandomizeMusic;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

class ProgressBarWorker extends SwingWorker<Void, Integer> {

    private JProgressBar progressBar;
    private int max;
    private JLabel label;
    private RandomizeEngine randEngine;
    private boolean isSelected = false;
    private JList<String> original_list;

    ProgressBarWorker(JProgressBar jpb, int max, JLabel label, RandomizeEngine randEngine, boolean isSelected, JList<String> original_list) throws IOException {
        this.progressBar = jpb;
        this.max = max;
        this.label = label;
        this.randEngine = randEngine;
        this.isSelected = isSelected;
        this.original_list = original_list;

    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size() - 1);
        progressBar.setValue(i); // The last value in this array is all we care about.
        System.out.println(i);
        label.setText("Loading " + i + " of " + max);
    }

    @Override
    protected Void doInBackground() throws Exception {
        randEngine.randomizeDir(isSelected, progressBar);
        randEngine.changeDirectory(randEngine.exposeStartDirectory());
        Vector<String> newVector = randEngine.exposeMusicFileList();
        original_list.setListData(newVector);
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            JOptionPane.showMessageDialog(progressBar.getParent(), "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}