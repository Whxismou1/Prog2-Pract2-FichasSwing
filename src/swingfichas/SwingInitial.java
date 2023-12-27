package swingfichas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.event.ChangeListener;

public class SwingInitial {

    private FileHandler fileHandler;
    private SolverHelper solverHelper;

    private final int DEFAULT_ROWS = 3;
    private final int DEFAULT_COLS = 3;

    private int numFilas;
    private int numColumnas;

    // Main menu
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JButton createGameButton;
    private JButton playGameButton;
    private JButton exitButton;
    private JLabel titleGame;
    private ImageIcon menuIcon;
    private Image imageMenuScaled;
    private ImageIcon iconMenuScaled;
    private JLabel imageMenu;

    // Menu create game
    private JFrame createGameFrame;
    private JPanel createGamePanel;
    private JButton[][] boardPiecesButtons;

    private JSpinner rowSelector;
    private JSpinner colSelector;
    private JButton saveFileC;
    private JButton playButtonC;
    private JButton undoButtonC;
    private JButton redoButtonC;
    private JButton importGameC;
    private char[][] board;

    /* Elementos del menu Jugar */
    private JFrame frameGamePlay;
    private JPanel panelGamePlay;

    private JButton importGameP;
    private JButton createGameP;
    private JButton solveButton;
    private JButton saveSolutionButton;
    private JButton undoButtonP;
    private JButton redoButtonP;
    private JButton[][] boardPiecesButtonsP;

    public SwingInitial() {
        this.numFilas = DEFAULT_ROWS;
        this.numColumnas = DEFAULT_COLS;
        inicializeClasses();
        inicializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2MainFrame();
        addActionsLIstenerComponents();

    }

    private void inicializeClasses() {
        fileHandler = new FileHandler();
        solverHelper = new SolverHelper();
    }

    private void inicializeComponents() {

        mainMenuInicializeComponents();

        createMenuInicializeComponents();

        playMenuInicializeComponents();

    }

    private void mainMenuInicializeComponents() {
        mainFrame = new JFrame("SwingFichas");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(0.5f);

        createGameButton = new JButton("Create Game");
        playGameButton = new JButton("Play Game");
        exitButton = new JButton("Exit");

        titleGame = new JLabel("SwingFichas");
        titleGame.setAlignmentX(0.5f);

        menuIcon = new ImageIcon("etc/images/fichasroja,verde,azul.png");

        imageMenuScaled = menuIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        iconMenuScaled = new ImageIcon(imageMenuScaled);
        imageMenu = new JLabel(iconMenuScaled);
        imageMenu.setAlignmentX(0.5f);
    }

    private void createMenuInicializeComponents() {
        createGamePanel = new JPanel();
        boardPiecesButtons = new JButton[this.numFilas][this.numColumnas];

        rowSelector = new JSpinner(new SpinnerNumberModel(this.numFilas, 1, 20, 1));
        JFormattedTextField rowEditor = ((JSpinner.DefaultEditor) rowSelector.getEditor()).getTextField();
        rowEditor.setEditable(false);

        colSelector = new JSpinner(new SpinnerNumberModel(this.numColumnas, 1, 20, 1));
        JFormattedTextField colEditor = ((JSpinner.DefaultEditor) colSelector.getEditor()).getTextField();
        colEditor.setEditable(false);

        saveFileC = new JButton("Save Board");
        playButtonC = new JButton("Play Game");
        undoButtonC = new JButton("<-");
        redoButtonC = new JButton("->");
        importGameC = new JButton("Import Board");
        board = new char[this.numFilas][this.numColumnas];
    }

    private void playMenuInicializeComponents() {
        panelGamePlay = new JPanel();

        importGameP = new JButton("Import Board");
        createGameP = new JButton("Create Board");
        solveButton = new JButton("Solve");
        saveSolutionButton = new JButton("Save Solution");
        undoButtonP = new JButton("<-");
        redoButtonP = new JButton("->");
        board = new char[this.numFilas][this.numColumnas];
    }

    private void addButtonsMainMenu2Arr() {
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(titleGame);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(imageMenu);
        addButtonToPanel(createGameButton);
        addButtonToPanel(playGameButton);
        addButtonToPanel(exitButton);
        mainPanel.add(Box.createVerticalGlue());
    }

    private void addButtonToPanel(JButton button) {
        button.setAlignmentX(0.5f);
        mainPanel.add(button);
    }

    private void addComponents2MainFrame() {
        mainFrame.add(mainPanel);
    }

    private void addActionsLIstenerComponents() {
        createGameButton.addActionListener(e -> {
            deactivateButton(createGameButton);
            createGameFrameMode();
        });

        playGameButton.addActionListener(e -> {
            deactivateButton(playGameButton);
            playGameFrameMode();
        });

        exitButton.addActionListener(e -> {
            int responseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?", "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (responseExit == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Thanks for playing!");
                System.exit(0);
            }
        });

        rowSelector.addChangeListener(e -> {
            updateGameBoard();
        });

        colSelector.addChangeListener(e -> {
            updateGameBoard();
        });

        saveFileC.addActionListener(e -> {
            board = getBoard(boardPiecesButtons);

            if (isValid(board)) {
                fileHandler.saveToFile(board);

            } else {
                JOptionPane.showMessageDialog(null, "The board is not valid");
            }

        });

        importGameC.addActionListener(e -> {
            char[][] importedBoard = fileHandler.loadGame();

            if (isValid(importedBoard)) {
                int nf = importedBoard.length;
                int nc = importedBoard[0].length;

                setRowsAndCols(nf, nc);

                rowSelector.setValue(nf);
                colSelector.setValue(nc);

                board = copyMatrix(importedBoard);

                changePieces(boardPiecesButtons);
            }

        });

        playButtonC.addActionListener(e -> {
            char[][] auxboard = getBoard(boardPiecesButtons);

            if (isValid(auxboard)) {
                setRowsAndCols(auxboard.length, auxboard[0].length);
                createGameFrame.dispose();
                createGamePanel.removeAll();
                activateButton(createGameButton);
                playGameFrameMode();
                board = copyMatrix(auxboard);

                System.out.println(board.length + " " + board[0].length);
                System.out.println(boardPiecesButtonsP.length + " " +
                        boardPiecesButtonsP[0].length);

                changePieces(boardPiecesButtonsP);
            } else {
                JOptionPane.showMessageDialog(null, "The board is not valid");
            }

        });

        createGameP.addActionListener(e -> {
            System.out.println();
            char[][] auxboard = getBoard(boardPiecesButtonsP);

            System.out.println(auxboard.length + " " + auxboard[0].length);
            System.out.println(boardPiecesButtons.length + " " +
                    boardPiecesButtons[0].length);

            if (isValid(auxboard)) {
                setRowsAndCols(auxboard.length, auxboard[0].length);
                frameGamePlay.dispose();
                panelGamePlay.removeAll();
                activateButton(playGameButton);
                createGameFrameMode();

                board = copyMatrix(auxboard);

                changePieces(boardPiecesButtons);
            } else {
                resetRowsAndCols2Default();
                frameGamePlay.dispose();
                panelGamePlay.removeAll();
                activateButton(playGameButton);
                createGameFrameMode();
            }

        });

        importGameP.addActionListener(e -> {
            char[][] importedBoard = fileHandler.loadGame();

            if (isValid(importedBoard)) {
                int nf = importedBoard.length;
                int nc = importedBoard[0].length;

                setRowsAndCols(nf, nc);

                panelGamePlay.removeAll();

                createPlayGameBoard(nf, nc);
                board = copyMatrix(importedBoard);

                changePieces(boardPiecesButtonsP);
            }

        });

        solveButton.addActionListener(e -> {
            char[][] auxboard = getBoard(boardPiecesButtonsP);

            if (isValid(auxboard)) {

                solverHelper.setGameBoard(auxboard);
                solverHelper.play();

                // changePieces(boardPiecesButtonsP);
            } else {
                JOptionPane.showMessageDialog(null, "The board is not valid");
            }

        });

    }

    private char[][] copyMatrix(char[][] importedBoard) {
        char[][] copiedMatrix = new char[importedBoard.length][importedBoard[0].length];

        for (int row = 0; row < importedBoard.length; row++) {
            for (int col = 0; col < importedBoard[0].length; col++) {
                copiedMatrix[row][col] = importedBoard[row][col];
            }
        }

        return copiedMatrix;
    }

    private void changePieces(JButton[][] wazaBoard) {

        for (int i = 0; i < wazaBoard.length; i++) {
            for (int j = 0; j < wazaBoard[0].length; j++) {
                char pieceInBoard = board[i][j];

                if (pieceInBoard == 'R') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaRoja.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    wazaBoard[i][j].setIcon(new ImageIcon(image));
                    wazaBoard[i][j].setText("R");
                } else if (pieceInBoard == 'V') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaVerde.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    wazaBoard[i][j].setIcon(new ImageIcon(image));
                    wazaBoard[i][j].setText("V");

                } else if (pieceInBoard == 'A') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaAzul.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    wazaBoard[i][j].setIcon(new ImageIcon(image));
                    wazaBoard[i][j].setText("A");
                }

            }
        }

    }

    private void printMatrix(char[][] matrix) {
        for (char[] row : matrix) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private char[][] getBoard(JButton[][] wazaBoard) {
        char[][] auxBoard = new char[wazaBoard.length][wazaBoard[0].length];

        for (int i = 0; i < auxBoard.length; i++) {
            for (int j = 0; j < auxBoard[0].length; j++) {
                String text = wazaBoard[i][j].getText();
                if (text.equals("R")) {
                    auxBoard[i][j] = 'R';
                } else if (text.equals("V")) {
                    auxBoard[i][j] = 'V';
                } else if (text.equals("A")) {
                    auxBoard[i][j] = 'A';
                } else {
                    auxBoard[i][j] = '-';
                }
            }
        }
        return auxBoard;

    }

    private boolean isValid(char[][] auxboard) {
        if (auxboard == null) {
            return false;
        }

        for (int i = 0; i < auxboard.length; i++) {
            for (int j = 0; j < auxboard[0].length; j++) {
                if (auxboard[i][j] == '-') {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Metodo encargado de actualizar el tablero de juego
     */
    private void updateGameBoard() {
        int numRows = (int) rowSelector.getValue();
        int numCols = (int) colSelector.getValue();

        // Limpiar el contenido actual del panel de juego

        createGamePanel.removeAll();

        createGameBoard(numRows, numCols);

        createGameFrame.revalidate();
        createGameFrame.repaint();

    }

    private void playGameFrameMode() {
        frameGamePlay = new JFrame("Play Game");
        frameGamePlay.setSize(680, 680);
        frameGamePlay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameGamePlay.setLocationRelativeTo(null);
        frameGamePlay.setResizable(true);

        frameGamePlay.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Llamado cuando se cierra la ventana de juego
                panelGamePlay.removeAll();
                resetRowsAndCols2Default();
                activateButton(playGameButton); // Vuelve a activar el botón al cerrar
            }
        });

        if (board == null) {
            setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
        }

        createPlayGameBoard(this.numFilas, this.numColumnas);

        frameGamePlay.setVisible(true);
    }

    private void createPlayGameBoard(int nR, int nC) {
        boardPiecesButtonsP = new JButton[nR][nC];

        board = new char[nR][nC];

        panelGamePlay.setLayout(new BorderLayout());

        // Agregar selectores de fila y columna
        JPanel selectorsPanel = new JPanel();

        selectorsPanel.add(importGameP);
        selectorsPanel.add(createGameP);
        selectorsPanel.add(solveButton);
        selectorsPanel.add(saveSolutionButton);
        selectorsPanel.add(undoButtonP);
        selectorsPanel.add(redoButtonP);
        panelGamePlay.add(selectorsPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(nR, nC));

        for (int i = 0; i < nR; i++) {
            for (int j = 0; j < nC; j++) {
                JButton boardPiece = createBoardPieceButtonOnPlayMode();
                boardPiecesButtonsP[i][j] = boardPiece;
                boardPanel.add(boardPiece);
            }
        }

        panelGamePlay.add(boardPanel, BorderLayout.CENTER);
        frameGamePlay.add(panelGamePlay);
    }

    private void createGameFrameMode() {
        createGameFrame = new JFrame("Create Game");
        createGameFrame.setSize(680, 680);
        createGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createGameFrame.setLocationRelativeTo(null);
        createGameFrame.setResizable(true);

        createGameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Llamado cuando se cierra la ventana de juego
                activateButton(createGameButton); // Vuelve a activar el botón al cerrar
                createGamePanel.removeAll();
                resetRowsAndCols2Default();

            }
        });
        createGameBoard(this.numFilas, this.numColumnas);

        createGameFrame.setVisible(true);
    }

    private void createGameBoard(int numRows, int numCols) {
        boardPiecesButtons = new JButton[numRows][numCols];

        board = new char[numRows][numCols];

        createGamePanel.setLayout(new BorderLayout());

        // Agregar selectores de fila y columna
        JPanel selectorsPanel = new JPanel();
        selectorsPanel.add(new JLabel("Rows:"));
        selectorsPanel.add(rowSelector);
        selectorsPanel.add(new JLabel("Cols:"));
        selectorsPanel.add(colSelector);
        selectorsPanel.add(saveFileC);
        selectorsPanel.add(playButtonC);
        selectorsPanel.add(undoButtonC);
        selectorsPanel.add(redoButtonC);
        selectorsPanel.add(importGameC);
        createGamePanel.add(selectorsPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(numRows, numCols));

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                JButton boardPiece = createBoardPieceButton();
                boardPiecesButtons[i][j] = boardPiece;
                boardPanel.add(boardPiece);
            }
        }

        createGamePanel.add(boardPanel, BorderLayout.CENTER);
        createGameFrame.add(createGamePanel);
    }

    /**
     * Metodo encargado de crear los botones de las fichas en el modo creacion
     * 
     * @return boton de ficha
     */
    private JButton createBoardPieceButton() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));
        boardPiece.addActionListener(e -> handleBoardPieceButtonClick(boardPiece));
        return boardPiece;
    }

    private JButton createBoardPieceButtonOnPlayMode() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));
        boardPiece.addActionListener(e -> handleBoardPieceButtonClickonPlayMode(boardPiece));
        return boardPiece;
    }

    /**
     * Metodo encargado de manejar el evento de click en un boton de ficha en el
     * modo creacion
     * 
     * @param boardPiece -> boton de ficha
     */
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

    private void handleBoardPieceButtonClickonPlayMode(JButton boardPiece) {

        printMatrix(board);
    }

    /**
     * Metodo encargado de actualizar la imagen de la ficha
     * 
     * @param boardPiece -> boton de ficha
     * @param piece      -> ficha
     */
    private void updatePieceImage(JButton boardPiece, String piece) {
        try {
            ImageIcon icon;
            Image image;

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

    /**
     * Metodo encargado de activar un boton
     * 
     * @param button2activate -> boton a activar
     */
    private void activateButton(JButton button2activate) {
        button2activate.setEnabled(true);
    }

    /**
     * Metodo encargado de desactivar un boton
     * 
     * @param button2deactivate -> boton a desactivar
     */
    private void deactivateButton(JButton button2deactivate) {
        button2deactivate.setEnabled(false);
    }

    protected void show() {
        mainFrame.setSize(400, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void setRowsAndCols(int filas, int columnas) {
        this.numFilas = filas;
        this.numColumnas = columnas;
    }

    private void resetRowsAndCols2Default() {
        this.numFilas = DEFAULT_ROWS;
        this.numColumnas = DEFAULT_COLS;
    }

}