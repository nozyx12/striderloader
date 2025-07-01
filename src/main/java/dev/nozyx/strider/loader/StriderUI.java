package dev.nozyx.strider.loader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

final class StriderUI {
    private JFrame frame;

    private JProgressBar progressBar;

    private Point mouseClickPoint;

    private boolean started = false;

    void start() {
        UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
        UIManager.put("ProgressBar.selectionBackground", Color.DARK_GRAY);

        frame = new JFrame("StriderLoader v" + StriderLoader.LOADER_VERSION);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 400, 200, 20, 20));
        frame.setLocationRelativeTo(null);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point current = e.getLocationOnScreen();
                frame.setLocation(current.x - mouseClickPoint.x, current.y - mouseClickPoint.y);
            }
        });

        URL iconURL = CrashDialog.class.getResource("/icon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage logoImg = ImageIO.read(StriderUI.class.getResource("/logo.png"));
            Image scaledLogo = logoImg.getScaledInstance(250, 110, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (IOException e) {
            logoLabel.setText("Logo not found");
        }

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(logoLabel);

        JLabel versionLabel = new JLabel("v" + StriderLoader.LOADER_VERSION);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionLabel.setForeground(Color.DARK_GRAY);
        versionLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(versionLabel);

        progressBar = new JProgressBar();
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(true);
        progressBar.setForeground(new Color(123, 0, 21));
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressBar.setStringPainted(true);
        progressBar.setString("Starting");
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(progressBar);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.NORTH);

        frame.setVisible(true);

        started = true;
    }

    void setStatus(String status) {
        if (started) progressBar.setString(status);
    }

    void close() {
        frame.dispose();
    }

    JFrame getFrame() {
        return frame;
    }
}
