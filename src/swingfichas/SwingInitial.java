package swingfichas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SwingInitial implements ActionListener {
    private JFrame mainWindowMenu;
    private JPanel mainPanelMenu;
    private JButton createGameButton;
    private JButton playGameButton;
    private JButton exitButton;
    private JLabel titleGame;
    private ImageIcon menuIcon;
    private Image imageMenuScaled;
    private ImageIcon iconMenuScaled;
    private JLabel imageMenu;

    private JFrame frameGameCreation;
    private JPanel panelGameCreation;
    private JButton[][] buttons;
    private int DEFAULT_ROWS = 3;
    private int DEFAULT_COLS = 3;
    private JSpinner rowSelector;
    private JSpinner colSelector;
    private JButton saveFile;
    private char[][] board;

    SwingInitial() {
        initializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2Panel();
        addEventListeners2Components();
        addComponents2Frame();
    }

    private void initializeComponents() {
        mainWindowMenu = new JFrame("Swing Fichas Iguales");
        mainPanelMenu = new JPanel();
        mainPanelMenu.setLayout(new BoxLayout(mainPanelMenu, BoxLayout.Y_AXIS));
        mainPanelMenu.setAlignmentX(0.5f);

        createGameButton = new JButton("CREATE GAME");
        playGameButton = new JButton("PLAY GAME");
        exitButton = new JButton("EXIT");

        titleGame = new JLabel("FICHAS IGUALES");
        titleGame.setAlignmentX(0.5f);

        menuIcon = new ImageIcon("etc/images/fichasroja,verde,azul.png");

        imageMenuScaled = menuIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        iconMenuScaled = new ImageIcon(imageMenuScaled);
        imageMenu = new JLabel(iconMenuScaled);
        imageMenu.setAlignmentX(0.5f);

        panelGameCreation = new JPanel();
        rowSelector = new JSpinner(new SpinnerNumberModel(DEFAULT_ROWS, 1, 20, 1));
        JFormattedTextField rowEditor = ((JSpinner.DefaultEditor) rowSelector.getEditor()).getTextField();
        rowEditor.setEditable(false);

        colSelector = new JSpinner(new SpinnerNumberModel(DEFAULT_COLS, 1, 20, 1));
        JFormattedTextField colEditor = ((JSpinner.DefaultEditor) colSelector.getEditor()).getTextField();
        colEditor.setEditable(false);

        saveFile = new JButton("SAVE");

    }

    private void addButtonsMainMenu2Arr() {
        mainPanelMenu.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanelMenu.add(titleGame);
        mainPanelMenu.add(Box.createVerticalGlue());
        mainPanelMenu.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanelMenu.add(imageMenu);
        addButtonToPanel(createGameButton);
        addButtonToPanel(playGameButton);
        addButtonToPanel(exitButton);
        mainPanelMenu.add(Box.createVerticalGlue());
    }

    private void addButtonToPanel(JButton button) {
        button.setAlignmentX(0.5f);
        button.addActionListener(this);
        mainPanelMenu.add(button);
    }

    private void addEventListeners2Components() {
        rowSelector.addChangeListener(e -> updateGameBoard());
        colSelector.addChangeListener(e -> updateGameBoard());
        saveFile.addActionListener(e -> {
            board = getBoard();

            if (isValidBoard(board)) {
                saveToFile(board);
            }

        });
    }

    private void saveToFile(char[][] currentBoard) {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        writer.print(board[i][j]);
                    }
                    writer.println();
                }
                JOptionPane.showMessageDialog(null, "Board saved to " + fileChooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving board to file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Save canceled by user.");
        }
    }

    private void printMatrix(char[][] matrix) {
        for (char[] row : matrix) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private char[][] getBoard() {
        char[][] board = new char[buttons.length][buttons[0].length];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                String text = buttons[i][j].getText();
                if (text.equals("R")) {
                    board[i][j] = 'R';
                } else if (text.equals("V")) {
                    board[i][j] = 'V';
                } else if (text.equals("A")) {
                    board[i][j] = 'A';
                } else {
                    board[i][j] = '-';
                }
            }
        }

        return board;
    }

    private boolean isValidBoard(char[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }

        return true;
    }

    private void addComponents2Panel() {
        mainPanelMenu.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addComponents2Frame() {
        mainWindowMenu.add(mainPanelMenu);
    }

    private void createGameBoard(int numRows, int numCols) {
        buttons = new JButton[numRows][numCols];
        board = new char[numRows][numCols];
        panelGameCreation.setLayout(new BorderLayout());

        // Agregar selectores de fila y columna
        JPanel selectorsPanel = new JPanel();
        selectorsPanel.add(new JLabel("Rows:"));
        selectorsPanel.add(rowSelector);
        selectorsPanel.add(new JLabel("Cols:"));
        selectorsPanel.add(colSelector);
        selectorsPanel.add(saveFile);
        panelGameCreation.add(selectorsPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(numRows, numCols));

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Lógica para permitir que el usuario elija la pieza (R, V, A)
                        String[] options = { "R", "V", "A" };
                        int choice = JOptionPane.showOptionDialog(
                                null,
                                "Choose a piece:",
                                "Piece Selection",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (choice != -1) {
                            button.setText(options[choice]);
                        }
                    }
                });
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        panelGameCreation.add(boardPanel, BorderLayout.CENTER);
        frameGameCreation.add(panelGameCreation);

    }

    private void updateGameBoard() {
        int numRows = (int) rowSelector.getValue();
        int numCols = (int) colSelector.getValue();

        // Limpiar el contenido actual del panel de juego

        panelGameCreation.removeAll();

        // Crear un nuevo tablero con las dimensiones actualizadas
        createGameBoard(numRows, numCols);

        // Agregar el nuevo panel de juego al marco
        frameGameCreation.add(panelGameCreation);

        // Revalidar y repintar el marco
        frameGameCreation.revalidate();
        frameGameCreation.repaint();
    }

    protected void show() {
        mainWindowMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindowMenu.setSize(500, 500);
        mainWindowMenu.setResizable(false);
        mainWindowMenu.setLocationRelativeTo(null);
        mainWindowMenu.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionActivated) {
        if (actionActivated.getSource() == exitButton) {
            int responseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?", "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (responseExit == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Thanks for playing!");
                System.exit(0);
            }
        } else if (actionActivated.getSource() == createGameButton) {
            frameGameCreation = new JFrame("Create Game");
            frameGameCreation.setSize(500, 500);
            frameGameCreation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameGameCreation.setLocationRelativeTo(null);
            frameGameCreation.setResizable(false);

            // Create game board panel
            createGameBoard(DEFAULT_ROWS, DEFAULT_COLS);

            frameGameCreation.setVisible(true);
        } else if (actionActivated.getSource() == playGameButton) {

        }
    }
}
