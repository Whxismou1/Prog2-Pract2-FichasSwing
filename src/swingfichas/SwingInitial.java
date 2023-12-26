package swingfichas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SwingInitial implements ActionListener {
    private FileHandler fileHandler;

    /* Elementos del menu principal */
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
    /* Elementos del menu Creacion de juego */
    private JFrame frameGameCreation;
    private JPanel panelGameCreation;
    private JButton[][] boardPiecesButtons;
    private int DEFAULT_ROWS = 3;
    private int DEFAULT_COLS = 3;
    private JSpinner rowSelector;
    private JSpinner colSelector;
    private JButton saveFile;
    private JButton playButton;
    private JButton undoButtonC;
    private JButton redoButtonC;
    private JButton importGameC;
    private char[][] board;

    /* Elementos del menu Jugar */
    private JFrame frameGamePlay;
    private JPanel panelGamePlay;

    private JButton playButtonP;
    private JButton importGameP;
    private JButton createGameP;
    private JButton solveButton;
    private JButton saveSolutionButton;
    private JButton undoButtonP;
    private JButton redoButtonP;
    private JButton[][] boardPiecesButtonsP;

    SwingInitial() {
        initializeClasses();
        initializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2Panel();
        addEventListeners2Components();
        addComponents2Frame();
    }

    private void initializeClasses() {
        fileHandler = new FileHandler();
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
        playButton = new JButton("PLAY");
        undoButtonC = new JButton("<-");
        redoButtonC = new JButton("->");
        importGameC = new JButton("IMPORT GAME");

        panelGamePlay = new JPanel();

        playButtonP = new JButton("PLAY");
        importGameP = new JButton("IMPORT GAME");
        createGameP = new JButton("CREATE GAME");
        solveButton = new JButton("SOLVE");
        saveSolutionButton = new JButton("SAVE SOLUTION");
        undoButtonP = new JButton("<-");
        redoButtonP = new JButton("->");

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
                fileHandler.saveToFile(board);
            }

        });

        playButton.addActionListener(e -> {
            board = getBoard();

            if (isValidBoard(board)) {
                frameGameCreation.dispose();
                activateButton(createGameButton);
                createPlay();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid board!");
            }

        });

        importGameC.addActionListener(e -> {
            board = fileHandler.loadGame();
            if (isValidBoard(board)) {
                printMatrix(board);
                int numF = board.length;
                int numCol = board[0].length;

                rowSelector.setValue(numF);
                colSelector.setValue(numCol);
                updateGameBoard();
                for (int i = 0; i < numF; i++) {
                    for (int j = 0; j < numCol; j++) {
                        String piece = String.valueOf(board[i][j]).trim();
                        updatePieceImage(boardPiecesButtons[i][j], piece);
                        boardPiecesButtons[i][j].setText(piece);
                    }
                }
                JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid game file!");
            }

        });

        createGameP.addActionListener(e -> {
            frameGamePlay.dispose();
            activateButton(playGameButton);
            createGameButton.doClick();
        });

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
        char[][] board = new char[boardPiecesButtons.length][boardPiecesButtons[0].length];

        for (int i = 0; i < boardPiecesButtons.length; i++) {
            for (int j = 0; j < boardPiecesButtons[0].length; j++) {
                String text = boardPiecesButtons[i][j].getText();
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
        if (board == null) {
            return false;
        }

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
        boardPiecesButtons = new JButton[numRows][numCols];
        board = new char[numRows][numCols];
        panelGameCreation.setLayout(new BorderLayout());

        // Agregar selectores de fila y columna
        JPanel selectorsPanel = new JPanel();
        selectorsPanel.add(new JLabel("Rows:"));
        selectorsPanel.add(rowSelector);
        selectorsPanel.add(new JLabel("Cols:"));
        selectorsPanel.add(colSelector);
        selectorsPanel.add(saveFile);
        selectorsPanel.add(playButton);
        selectorsPanel.add(undoButtonC);
        selectorsPanel.add(redoButtonC);
        selectorsPanel.add(importGameC);
        panelGameCreation.add(selectorsPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(numRows, numCols));

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton boardPiece = createBoardPieceButton();
                boardPiecesButtons[i][j] = boardPiece;
                boardPanel.add(boardPiece);
            }
        }

        panelGameCreation.add(boardPanel, BorderLayout.CENTER);
        frameGameCreation.add(panelGameCreation);
    }

    private JButton createBoardPieceButton() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));
        boardPiece.addActionListener(e -> handleBoardPieceButtonClick(boardPiece));
        return boardPiece;
    }

    private JButton createBoardPieceButtonPlay() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));
        boardPiece.addActionListener(e -> handleBoardPieceButtonClickPlay(boardPiece));
        return boardPiece;
    }

    private void handleBoardPieceButtonClickPlay(JButton boardPiece) {

    }

    private void handleBoardPieceButtonClick(JButton boardPiece) {
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
            updatePieceImage(boardPiece, options[choice]);
            boardPiece.setText(options[choice]);
        }
    }

    private void updatePieceImage(JButton boardPiece, String piece) {
        try {
            ImageIcon icon;
            Image image;

            boardPiece.setText("");
            boardPiece.setIcon(null);

            switch (piece) {
                case "R":
                    icon = new ImageIcon("etc/images/fichaRoja.png");
                    image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    boardPiece.setIcon(new ImageIcon(image));
                    break;
                case "V":
                    icon = new ImageIcon("etc/images/fichaVerde.png");
                    image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    boardPiece.setIcon(new ImageIcon(image));
                    break;
                case "A":
                    icon = new ImageIcon("etc/images/fichaAzul.png");
                    image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    boardPiece.setIcon(new ImageIcon(image));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading images: " + e.getMessage());
        }
    }

    private void updateGameBoard() {
        int numRows = (int) rowSelector.getValue();
        int numCols = (int) colSelector.getValue();

        // Limpiar el contenido actual del panel de juego

        panelGameCreation.removeAll();

        createGameBoard(numRows, numCols);

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
            deactivateButton(createGameButton);
            frameGameCreation = new JFrame("Create Game");
            frameGameCreation.setSize(610, 610);
            frameGameCreation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameGameCreation.setLocationRelativeTo(null);
            frameGameCreation.setResizable(false);

            createGameBoard(DEFAULT_ROWS, DEFAULT_COLS);
            frameGameCreation.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Llamado cuando se cierra la ventana de juego
                    activateButton(createGameButton); // Vuelve a activar el botón al cerrar
                    rowSelector.setValue(DEFAULT_ROWS);
                    colSelector.setValue(DEFAULT_COLS);
                }
            });

            frameGameCreation.setVisible(true);

        } else if (actionActivated.getSource() == playGameButton) {
            createPlay();
            printMatrix(board);
        }
    }

    private void createPlay() {
        deactivateButton(playGameButton);
        frameGamePlay = new JFrame("Play Game");
        frameGamePlay.setSize(610, 610);
        frameGamePlay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameGamePlay.setLocationRelativeTo(null);
        frameGamePlay.setResizable(false);

        if (board == null) {
            boardPiecesButtonsP = new JButton[DEFAULT_ROWS][DEFAULT_COLS];
            createPlayBoard(DEFAULT_ROWS, DEFAULT_COLS);
        } else {
            boardPiecesButtonsP = new JButton[board.length][board[0].length];
            createPlayBoard(board.length, board[0].length);
        }

        frameGamePlay.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Llamado cuando se cierra la ventana de juego
                activateButton(playGameButton); // Vuelve a activar el botón al cerrar
            }
        });

        frameGamePlay.setVisible(true);

    }

    private void createPlayBoard(int numF, int numCol) {

        panelGamePlay.setLayout(new BorderLayout());
        JPanel toolPanel = new JPanel();
        toolPanel.add(playButtonP);
        toolPanel.add(undoButtonP);
        toolPanel.add(redoButtonP);
        toolPanel.add(importGameP);
        toolPanel.add(createGameP);
        toolPanel.add(solveButton);
        toolPanel.add(saveSolutionButton);
        panelGamePlay.add(toolPanel, BorderLayout.NORTH);
        frameGamePlay.add(panelGamePlay);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(numF, numCol));
        if (board != null) {
            for (int i = 0; i < numF; i++) {
                for (int j = 0; j < numCol; j++) {
                    JButton boardPiece = createBoardPieceButtonPlay();

                    boardPiece.setText(String.valueOf(board[i][j]));

                    updatePieceImage(boardPiece, String.valueOf(board[i][j]));

                    boardPiecesButtons[i][j] = boardPiece;
                    boardPanel.add(boardPiece);
                }
            }
        }

        panelGamePlay.add(boardPanel, BorderLayout.CENTER);
        frameGamePlay.add(panelGamePlay);

    }

    private void activateButton(JButton button2activate) {
        button2activate.setEnabled(true);
    }

    private void deactivateButton(JButton button2deactivate) {
        button2deactivate.setEnabled(false);
    }
}
