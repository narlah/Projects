package testQR.src.main.java.testingQrcode.testQR;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WriteToQRFile {

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(400, 400));
        // ImageIcon icon = new ImageIcon("androidBook.jpg");

        final JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                BufferedImage image = null;
                super.paintComponent(g);
                try {
                    image = ImageIO.read(QRCode.from("Nikolai Kosevaaaaaaaaaaaaaaaaaaaaaaa").to(ImageType.PNG).file());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (image != null) {
                    g.drawImage(image, 0, 0, this);
                }
            }
        };

        frame.setBounds(new Rectangle(100, 100, 600, 600));

        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JTextField textField = new JTextField();
        textField.setBounds(10, 5, 364, 20);
        panel.add(textField);
        textField.setColumns(10);

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(arg0 -> {
            panel.repaint();
            panel.updateUI();

        });
        generateButton.setActionCommand("");
        generateButton.setBounds(146, 25, 89, 20);
        panel.add(generateButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}