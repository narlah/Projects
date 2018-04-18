package RandomizeMusic;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

class SwingRandomiseMusic extends JFrame {
    public static final long serialVersionUID = 1;

    private RandomizeEngine randEngine;

    private JList<String> original_list;
    private JFileChooser fileChooser;
    private JButton btnPlay;
    private JButton btnStop;
    private JCheckBox ckBox;
    private JLabel currentDir;
    private JLabel statusBar;

    private Thread playerThread;
    private FileInputStream fis = null;
    private AdvancedPlayer player;
    private JProgressBar progressBar;

    SwingRandomiseMusic() {
        setResizable(false);

        setTitle("Randomize music");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(SwingRandomiseMusic.class.getResource("/ListView.gif")));
        setSize(760, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    /**
     * Initialises UI only
     */
    private void initUI() {
        String initialDir = File.separator;
        randEngine = new RandomizeEngine(initialDir);
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        // File chooser
        final JButton openButton = new JButton("Choose dir");
        openButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
        openButton.setIcon(new ImageIcon(SwingRandomiseMusic.class.getResource("/Directory.gif")));
        openButton.setBounds(20, 11, 105, 30);
        openButton.addActionListener(event -> {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(randEngine.exposeStartDirectoryParent()));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int returnVal = fileChooser.showOpenDialog(SwingRandomiseMusic.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File newSelectedDir = fileChooser.getSelectedFile();
                randEngine.changeDirectory(newSelectedDir.getAbsolutePath());
                Vector<String> newVector = randEngine.exposeMusicFileList();
                original_list.setListData(newVector);
                currentDir.setText(newSelectedDir.getAbsolutePath());
            }
        });
        Vector<String> fileList = randEngine.exposeMusicFileList();
        panel.setLayout(null);
        panel.add(openButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 52, 700, 728);
        panel.add(scrollPane);

        original_list = new JList<>(fileList);
        scrollPane.setViewportView(original_list);
        scrollPane.setViewportView(original_list);
        // Double click
        original_list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    stopPlay();
                    String selectedName = original_list.getSelectedValue();
                    File readyToPlay = new File(randEngine.exposeStartDirectory() + File.separator + selectedName);
                    playFile(readyToPlay);
                }
            }
        });
        // Shuffle
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.setBounds(640, 11, 100, 30);
        panel.add(shuffleButton);
        shuffleButton.addActionListener(event -> {
            try {
                new ProgressBarWorker(progressBar, randEngine.exposeMusicFileList().size(), new JLabel("Label"), randEngine, ckBox.isSelected(),
                        original_list).execute();
                // randEngine.randomizeDir(ckBox.isSelected(), progressBar);
                // randEngine.changeDirectory(randEngine.exposeStartDirectory());
                // Vector<String> newVector = randEngine.exposeMusicFileList();
                // original_list.setListData(newVector);
            } catch (IOException ioException) {
                statusBar.setText("!!! IO Error during shuffle!!!");
            }

        });

        statusBar = new JLabel("Change the directory or shuffle the selected files.");
        statusBar.setBounds(20, 779, 360, 22);
        panel.add(statusBar);

        currentDir = new JLabel(initialDir);
        currentDir.setBounds(186, 15, 366, 22);
        panel.add(currentDir);

        ckBox = new JCheckBox("Log file");
        ckBox.setBounds(558, 15, 76, 23);
        panel.add(ckBox);
        // Play
        btnPlay = new JButton("");
        btnPlay.setIcon(new ImageIcon(SwingRandomiseMusic.class.getResource("/maximize-pressed.gif")));
        btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnPlay.addActionListener(arg0 -> {
            String selectedName = original_list.getSelectedValue();
            File readyToPlay = new File(randEngine.exposeStartDirectory() + File.separator + selectedName);
            playFile(readyToPlay);
        });
        btnPlay.setBounds(135, 15, 24, 23);
        panel.add(btnPlay);
        // Stop
        btnStop = new JButton("");
        btnStop.addActionListener(e -> {
            stopPlay();
            btnPlay.setEnabled(true);
            btnStop.setEnabled(false);
        });
        btnStop.setIcon(new ImageIcon(SwingRandomiseMusic.class.getResource("/minimize-pressed.gif")));
        btnStop.setBounds(158, 15, 24, 23);
        panel.add(btnStop);

        progressBar = new JProgressBar();
        progressBar.setOrientation(SwingConstants.VERTICAL);
        progressBar.setBounds(727, 52, 17, 728);
        panel.add(progressBar);
    }

    // > ************* play music part //
    private void playFile(File readyToPlay) {
        if (readyToPlay.exists()) {
            try {
                fis = new FileInputStream(readyToPlay);
                player = new AdvancedPlayer(fis);
            } catch (JavaLayerException | FileNotFoundException e) {
                System.out.println("Problem opening the playback file!");
                e.printStackTrace();
            }
        }
        playerThread = new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    System.out.println("Java layer Exception!");
                    e.printStackTrace();
                }
            }
        };
        playerThread.start();
        btnPlay.setEnabled(false);
        btnStop.setEnabled(true);
    }

    @SuppressWarnings("all")
    private void stopPlay() {
        if (playerThread != null && fis != null) {
            playerThread.stop();
            try {
                fis.close();
            } catch (IOException e1) {
                System.out.println("Problem while closing the playback stream!");
                e1.printStackTrace();
            }
        }
    }
}
