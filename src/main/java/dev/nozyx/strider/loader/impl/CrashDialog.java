package dev.nozyx.strider.loader.impl;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

final class CrashDialog {
    static void showCrashDialog(Frame parent, String msg) {
        showCrashDialog(parent, msg, null);
    }

    static void showCrashDialog(Frame parent, Throwable th) {
        showCrashDialog(parent, null, th);
    }

    static void showCrashDialog(Frame parent, String msg, Throwable th) {
        JDialog dialog = new JDialog(parent, "StriderLoader Crash", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        URL iconURL = CrashDialog.class.getResource("/icon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            dialog.setIconImage(icon.getImage());
        }

        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));

        JLabel titleLabel = new JLabel("<html><h2>StriderLoader crashed!</h2></html>");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        dialog.add(headerPanel, BorderLayout.PAGE_START);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        dialog.add(mainPanel, BorderLayout.CENTER);

        StringBuilder labelHtml = new StringBuilder("<html>");
        if (msg != null && !msg.isEmpty()) labelHtml.append("<u>Message:</u><br><i>").append(msg.replaceAll("\n", "<br>")).append("</i><br><br>");
        if (th != null && th.getMessage() != null && !th.getMessage().isEmpty()) labelHtml.append("<u>Error message:</u><br><i>").append(th.getMessage()).append("</i><br><br>");
        labelHtml.append("<u>Error stacktrace:</u>").append("</html>");

        JLabel label = new JLabel(labelHtml.toString());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(label);

        if (th != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            String stackTrace = sw.toString();

            mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            JTextArea textArea = new JTextArea(stackTrace);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setMargin(new Insets(5, 5, 5, 5));
            textArea.setLineWrap(false);

            FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
            String[] lines = stackTrace.split("\n");
            int maxLineWidth = 0;
            for (String line : lines) {
                int width = fm.stringWidth(line);
                if (width > maxLineWidth) maxLineWidth = width;
            }

            int scrollbarWidth = (Integer) UIManager.get("ScrollBar.width");
            int padding = 20;
            int scrollPaneWidth = Math.min(maxLineWidth + scrollbarWidth + padding, 600);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, 180));
            scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(scrollPane);
        }

        dialog.pack();

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
