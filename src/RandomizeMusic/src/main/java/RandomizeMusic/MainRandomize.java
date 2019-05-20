package RandomizeMusic;

import javax.swing.SwingUtilities;

public class MainRandomize {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      SwingRandomiseMusic ex = new SwingRandomiseMusic();
      ex.setVisible(true);
    });
  }
}
