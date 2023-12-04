package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;

// Represents a screen printer for printing event log to screen
public class ScreenPrinter extends JInternalFrame implements LogPrinter {
    private JTextArea logArea;

    // constructor sets up window which log will be printed on screen
    public ScreenPrinter(Component parent) {
        super("Event log", false, true, false, false);
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane);
        setSize(400, 300);
        setPosition(parent);
        setVisible(true);
    }

    @Override
    public void printLog(EventLog el) {
        for (Event next : el) {
            logArea.setText(logArea.getText() + next.toString() + "\n\n");
        }

        repaint();
    }

    // sets specific position of window to parent window
    private void setPosition(Component parent) {
        setLocation(600, 450);
    }
}
