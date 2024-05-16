package Bai2;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JLabel timeLabel;

    public TimeClient() {
        initializeGUI();
        connectToServer();
        startClockUpdater();
    }

    private void initializeGUI() {
        frame = new JFrame("Clock Client");
        frame.setSize(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        timeLabel = new JLabel("Connecting...", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 24));

        frame.add(timeLabel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            timeLabel.setText("Connection failed");
            e.printStackTrace();
        }
    }

    private void startClockUpdater() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (socket != null && socket.isConnected() && !socket.isClosed()) {
                    out.println("time");
                    try {
                        String serverTime = in.readLine();
                        if (serverTime != null) {
                            timeLabel.setText(serverTime);
                        }
                    } catch (IOException ex) {
                        timeLabel.setText("Error");
                        ex.printStackTrace();
                    }
                }
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TimeClient();
            }
        });
    }
}
