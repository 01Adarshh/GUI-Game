import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Main game window. JFrame is inherited to create the GUI.
public class TicTacToeGame extends JFrame implements ActionListener, Restartable {
    private JButton[] cells = new JButton[9];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private Player playerX = new Player("Player X", 'X');
    private Player playerO = new Player("Player O", 'O');
    private Player currentPlayer = playerX;
    private boolean gameOver = false;
    private int moves = 0;

    public TicTacToeGame() {
        setTitle("Tic-Tac-Toe");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(18, 10, 8, 10));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 3, 7, 7));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        centerPanel.setBackground(new Color(225, 232, 240));

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new JButton("");
            cells[i].setFont(new Font("Arial", Font.BOLD, 44));
            cells[i].setFocusPainted(false);
            cells[i].setBackground(Color.WHITE);
            cells[i].addActionListener(this);
            centerPanel.add(cells[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        statusLabel = new JLabel("Player X's turn (X)", SwingConstants.CENTER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        scoreLabel = new JLabel("Score: Player X 0  |  Player O 0", SwingConstants.CENTER);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton restartButton = new JButton("New Round");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartGame());

        JButton resetScoreButton = new JButton("Reset Scores");
        resetScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetScoreButton.addActionListener(e -> {
            playerX.resetScore();
            playerO.resetScore();
            updateScore();
            restartGame();
        });

        bottomPanel.add(statusLabel);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(scoreLabel);
        bottomPanel.add(Box.createVerticalStrut(12));
        bottomPanel.add(restartButton);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(resetScoreButton);
        add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(245, 247, 250));
        setVisible(true);
    }

    // Polymorphism: this method overrides ActionListener's method.
    @Override
    public void actionPerformed(ActionEvent event) {
        if (gameOver) {
            return;
        }

        JButton clickedButton = (JButton) event.getSource();
        if (!clickedButton.getText().equals("")) {
            return;
        }

        clickedButton.setText(String.valueOf(currentPlayer.getSymbol()));
        if (currentPlayer.getSymbol() == 'X') {
            clickedButton.setForeground(new Color(35, 95, 180));
        } else {
            clickedButton.setForeground(new Color(210, 75, 65));
        }
        moves++;

        if (hasWon()) {
            gameOver = true;
            currentPlayer.increaseScore();
            statusLabel.setText(currentPlayer.getName() + " wins!");
            updateScore();
        } else if (moves == 9) {
            gameOver = true;
            statusLabel.setText("It's a draw!");
        } else {
            currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
            statusLabel.setText(currentPlayer.getName() + "'s turn (" +
                    currentPlayer.getSymbol() + ")");
        }
    }

    private boolean hasWon() {
        int[][] combinations = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        for (int[] combination : combinations) {
            String a = cells[combination[0]].getText();
            String b = cells[combination[1]].getText();
            String c = cells[combination[2]].getText();

            if (!a.equals("") && a.equals(b) && b.equals(c)) {
                return true;
            }
        }
        return false;
    }

    private void updateScore() {
        scoreLabel.setText("Score: Player X " + playerX.getScore() +
                "  |  Player O " + playerO.getScore());
    }

    @Override
    public void restartGame() {
        for (JButton cell : cells) {
            cell.setText("");
            cell.setForeground(Color.BLACK);
        }
        currentPlayer = playerX;
        gameOver = false;
        moves = 0;
        statusLabel.setText("Player X's turn (X)");
    }
}
