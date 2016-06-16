import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;

public class ClientTTT {

    private JFrame window = new JFrame("Крестики-нолики");
    private JLabel labelStatus = new JLabel("");
    private String mark;
    private String opponentMark;

    private JButton[] gameField = new JButton[9];
    private JButton currentButton;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientTTT() throws Exception {

        socket = new Socket("192.168.0.16", 65500);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        labelStatus.setBackground(Color.lightGray);
        window.getContentPane().add(labelStatus, "South");

        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setBackground(Color.black);
        gameFieldPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < gameField.length; i++) {
            final int j = i;
            gameField[i] = new JButton();
            gameField[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentButton = gameField[j];
                    out.println("MOVE " + j);}});
            gameFieldPanel.add(gameField[i]);
        }
        window.getContentPane().add(gameFieldPanel, "Center");
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                mark = response.substring(8,9);
                System.out.println(mark.charAt(0));
                mark = (mark.charAt(0) == 'X' ? "X" : "O");
                opponentMark  = (mark.charAt(0) == 'X' ? "O" : "X");
                window.setTitle(mark);
            }
            while (true) {
                response = in.readLine();
                System.out.println(response);
                if (response != null) {
	                if (response.startsWith("VALID_MOVE")) {
	                    labelStatus.setText("Ход сделан. Ожидание соперника");
	                    currentButton.setText(mark);
	                    currentButton.repaint();
	                } else if (response.startsWith("OPPONENT_MOVED")) {
	                    int loc = Integer.parseInt(response.substring(15));
	                    gameField[loc].setText(opponentMark);
	                    gameField[loc].repaint();
	                    labelStatus.setText("Соперник совершил ход. Ваша очередь.");
	                } else if (response.startsWith("VICTORY")) {
	                    labelStatus.setText("Победа!");
	                    break;
	                } else if (response.startsWith("DEFEAT")) {
	                    labelStatus.setText("Поражение");
	                    break;
	                } else if (response.startsWith("TIE")) {
	                    labelStatus.setText("Ничья!");
	                    break;
	                } else if (response.startsWith("MESSAGE")) {
	                    labelStatus.setText(response.substring(8));
	                }
	            }
            }
            out.println("QUIT");
        }
        finally {
            socket.close();
        }
    }

    private boolean playAgain() {
        int response = JOptionPane.showConfirmDialog(window,
            "Ещё раз?","",JOptionPane.YES_NO_OPTION);
        window.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            ClientTTT client = new ClientTTT();
            client.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.window.setSize(240, 160);
            client.window.setVisible(true);
            client.window.setResizable(false);
            client.play();
            if (!client.playAgain()) {
                break;
            }
        }
    }
}