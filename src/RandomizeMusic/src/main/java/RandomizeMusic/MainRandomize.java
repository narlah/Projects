package RandomizeMusic;

import javax.swing.*;

public class MainRandomize {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingRandomiseMusic ex = new SwingRandomiseMusic();
            ex.setVisible(true);
        });
    }
}
