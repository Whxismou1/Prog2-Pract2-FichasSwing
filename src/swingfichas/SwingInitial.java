package swingfichas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import javax.swing.SwingWorker;

public class SwingInitial {

    private List<List<char[][]>> undoManagerC;
    private List<List<char[][]>> redoManagerC;

    private List<char[][]> undoManagerP;
    private List<char[][]> redoManagerP;

    private FileHandler fileHandler;
    private SolverHelper solverHelper;
    // private RedoAndUndo redoAndUndo;

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
    private List<List<Integer>> actualMoves;

    /* Elementos del menu Jugar */
    private JFrame frameGamePlay;
    private JPanel panelGamePlay;

    private JButton importGameP;
    private JButton createGameP;
    private JButton showAvMovesP;

    private JButton undoButtonP;
    private JButton redoButtonP;
    private JButton[][] boardPiecesButtonsP;

    private int totalPoints;
    private int totalPiecesDeleted;

    public SwingInitial() {
        this.numFilas = DEFAULT_ROWS;
        this.numColumnas = DEFAULT_COLS;
        totalPoints = 0;
        totalPiecesDeleted = 0;
        actualMoves = new ArrayList<>();

        undoManagerC = new ArrayList<>();
        redoManagerC = new ArrayList<>();

        undoManagerP = new ArrayList<>();
        redoManagerP = new ArrayList<>();

        inicializeClasses();
        inicializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2MainFrame();
        addActionsLIstenerComponents();

    }

    private void inicializeClasses() {
        fileHandler = new FileHandler();
        // redoAndUndo = new RedoAndUndo();
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
        showAvMovesP = new JButton("Hints");

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
            actualMoves.clear();
            deactivateButton(createGameButton);
            createGameFrameMode();
        });

        playGameButton.addActionListener(e -> {
            actualMoves.clear();
            deactivateButton(playGameButton);
            playGameFrameMode();
            disableBoardPlayMode();
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

                activateButton(undoButtonC);
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

                solverHelper = new SolverHelper(board);

                changePieces(boardPiecesButtonsP);
                undoManagerP.add(board);
            } else {
                JOptionPane.showMessageDialog(null, "The board is not valid");
            }

        });

        createGameP.addActionListener(e -> {
            actualMoves.clear();
            char[][] auxboard = getBoard(boardPiecesButtonsP);

            if (isValid(auxboard)) {
                setRowsAndCols(auxboard.length, auxboard[0].length);
                frameGamePlay.dispose();
                panelGamePlay.removeAll();
                activateButton(playGameButton);
                deactivateButton(createGameButton);
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

            actualMoves.clear();
            char[][] importedBoard = fileHandler.loadGame();

            if (isValid(importedBoard)) {
                int nf = importedBoard.length;
                int nc = importedBoard[0].length;

                setRowsAndCols(nf, nc);

                panelGamePlay.removeAll();

                createPlayGameBoard(nf, nc);
                board = copyMatrix(importedBoard);
                // undoManagerP.add(board);
                solverHelper = new SolverHelper(board);

                // solverHelper.setGameBoard(board);
                changePieces(boardPiecesButtonsP);
            }

        });

        undoButtonC.addActionListener(e -> {
            // char[][] auxBoard = getBoard(boardPiecesButtons);
            // undoManagerC.add(auxBoard);
            // board = copyMatrix(auxBoard);
            // performUndo(undoManagerC, redoManagerC, currentStateIndexC);
        });

        redoButtonC.addActionListener(e -> {
            // performRedo(undoManagerC, redoManagerC, currentStateIndexC);
        });

        undoButtonP.addActionListener(e -> {
            // performUndo(undoManagerP, redoManagerP, currentStateIndexP);
            if (!undoManagerP.isEmpty()) {

                char[][] undoneState = undoManagerP.remove(undoManagerP.size() - 1);
                redoManagerP.add(copyMatrix(getBoard(boardPiecesButtonsP)));
                // board = copyMatrix(undoneState);

                panelGamePlay.removeAll();

                createPlayGameBoard(this.numFilas, this.numColumnas);
                board = copyMatrix(undoneState);

                solverHelper = new SolverHelper(board);

                changePieces(boardPiecesButtonsP);
            }

        });

        redoButtonP.addActionListener(e -> {
            // performRedo(undoManagerP, redoManagerP, currentStateIndexP);
            if (!redoManagerP.isEmpty()) {
                char[][] redoneState = copyMatrix(redoManagerP.remove(redoManagerP.size() - 1));

                undoManagerP.add(copyMatrix(getBoard(boardPiecesButtonsP)));
                // board = copyMatrix(redoneState);

                panelGamePlay.removeAll();
                createPlayGameBoard(this.numFilas, this.numColumnas);

                board = copyMatrix(redoneState);

                solverHelper = new SolverHelper(board);

                changePieces(boardPiecesButtonsP);
                // solverHelper.setGameBoard(board)
            }

        });

        showAvMovesP.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        char[][] auxboard = getBoard(boardPiecesButtonsP);

                        board = copyMatrix(auxboard);

                        solverHelper = new SolverHelper(board);
                        // solverHelper.setGameBoard(board);
                        solverHelper.play();

                        // setRowsAndCols(numFilas, numColumnas);

                        // panelGamePlay.removeAll();

                        // createPlayGameBoard(numFilas, numFilas);
                        // board = solverHelper.getBoardResult();

                        // changePieces(boardPiecesButtonsP);

                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Obtén el resultado del SwingWorker

                            // Muestra los movimientos en un JOptionPane
                            List<String> wazza = solverHelper.getResultListSolver();
                            StringBuilder movesText = new StringBuilder("Movimientos disponibles:\n");
                            for (String move : wazza) {
                                movesText.append(move).append("\n");
                            }

                            JOptionPane.showMessageDialog(null, movesText.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                if (isValidResult(getBoard(boardPiecesButtonsP))) {
                    worker.execute();
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: Tablero vacio");
                }

            }
            //
        });

    }

    public void printMatrices(List<char[][]> matrixList) {
        System.out.println("Matrices en la lista:");

        for (int i = 0; i < matrixList.size(); i++) {
            System.out.println("Matriz #" + (i + 1) + ":");
            printMatrix(matrixList.get(i));
            System.out.println();
        }
    }

    // private void performUndo(List<List<char[][]>> undoManager,
    // List<List<char[][]>> redoManager,
    // int currentStateIndex) {
    // if (currentStateIndex > 0) {
    // redoManager.add(new ArrayList<>(undoManager.get(currentStateIndex)));
    // currentStateIndex--;
    // restoreGameState(undoManager.get(currentStateIndex));
    // }
    // }

    // private void performRedo(List<List<char[][]>> undoManager,
    // List<List<char[][]>> redoManager,
    // int currentStateIndex) {
    // if (currentStateIndex < undoManager.size() - 1) {
    // currentStateIndex++;
    // redoManager.add(new ArrayList<>(undoManager.get(currentStateIndex)));
    // restoreGameState(undoManager.get(currentStateIndex));
    // }
    // }

    // private void restoreGameState(List<char[][]> gameStateList) {
    // // Restaurar el estado actual del juego
    // if (!gameStateList.isEmpty()) {
    // char[][] aux = gameStateList.get(gameStateList.size() - 1);
    // int nf = aux.length;
    // int nc = aux[0].length;

    // setRowsAndCols(nf, nc);
    // panelGamePlay.removeAll();
    // createPlayGameBoard(nf, nc);
    // board = copyMatrix(aux);

    // solverHelper = new SolverHelper(board);

    // // solverHelper.setGameBoard(board);
    // changePieces(boardPiecesButtonsP);
    // }
    // }

    private char[][] copyMatrix(char[][] original) {
        int numRows = original.length;
        int numCols = original[0].length;
        char[][] copy = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            copy[i] = Arrays.copyOf(original[i], numCols);
        }

        return copy;
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
                } else {
                    wazaBoard[i][j].setIcon(null);
                    wazaBoard[i][j].setText("-");
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

    private boolean isValidResult(char[][] matrix) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '-') {
                    count++;
                }
            }
        }

        int nf = matrix.length;
        int nc = matrix[0].length;

        if (count < (nf * nc)) {
            return true;

        } else {
            return false;
        }

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
                actualMoves.clear();
                undoManagerP.clear();
                redoManagerP.clear();
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
        selectorsPanel.add(showAvMovesP);

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
                undoManagerC.clear();
                redoManagerC.clear();
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
        board = getBoard(boardPiecesButtonsP);
        List<int[]> posibleMovesBegin = new ArrayList<>();

        if (isValidResult(board)) {
            solverHelper = new SolverHelper(board);
            // solverHelper.setGameBoard(board);

            int[] cord = getButtonClickedCoordinates(boardPiece);
            System.out.println("Coordenadas boton pulsado: " + cord[0] + " " + cord[1]);
            // solverHelper = new SolverHelper(board);
            solverHelper.checkMoves(board, posibleMovesBegin);

            int[] correctCoord = checkCoordsOnPosibleMoves(cord, board, posibleMovesBegin);

            if (correctCoord == null) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar porque no es grupo");
                return;
            }

            if (correctCoord[0] == -1) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar porque es una ficha vacia");
                return;
            }

            int coordX = correctCoord[0];
            int coordY = correctCoord[1];

            if (board[coordX][coordY] == '-') {
                JOptionPane.showMessageDialog(null, "No se puede eliminar porque es una pieza vacia");
                return;
            }
            undoManagerP.add(copyMatrix(board));

            redoManagerP.clear();

            // solverHelper.printMatrxz();
            char color = board[coordX][coordY];
            int piezasdeleted = solverHelper.removeGroup(board, coordX, coordY);
            int puntos = solverHelper.getPointWithDeletedPieces(board, piezasdeleted);

            actualMoves.add(List.of(coordX, coordY, piezasdeleted, puntos, (int) color));
            // System.out.println(actualMoves);
            totalPiecesDeleted += piezasdeleted;
            totalPoints += puntos;

            // System.out.println("Puntos y piezas eliminadas: " + puntos + " " +
            // piezasdeleted);

            board = solverHelper.getBoardResult();

            // printMatrix(board);

            changePieces(boardPiecesButtonsP);

            if (solverHelper.getLeftPieces(board) == 0) {
                totalPoints += 1000;
                disableBoardPlayMode();

                int option = JOptionPane.showOptionDialog(
                        null,
                        "¡Enhorabuena, has completado el tablero sin piezas restantes!\nPiezas eliminadas: "
                                + totalPiecesDeleted + "\nPuntos: " + totalPoints,
                        "Victoria",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[] { "Jugar otra vez", "Guardar movimientos", "Salir" },
                        null);

                if (option == JOptionPane.YES_OPTION) {
                    enableBoardPlayMode();
                    setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                    this.totalPoints = 0;
                    this.totalPiecesDeleted = 0;
                    frameGamePlay.dispose();
                    panelGamePlay.removeAll();

                    playGameFrameMode();
                } else if (option == JOptionPane.NO_OPTION) {
                    fileHandler.saveMyGameSolution(actualMoves, totalPoints,
                            solverHelper.getLeftPieces(board));
                    JOptionPane.showMessageDialog(null, "Thanks for playing!");
                    System.exit(0);
                } else {

                    int responseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (responseExit == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Thanks for playing!");
                        System.exit(0);
                    } else {
                        enableBoardPlayMode();
                        setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                        this.totalPoints = 0;
                        this.totalPiecesDeleted = 0;
                        frameGamePlay.dispose();
                        panelGamePlay.removeAll();

                        playGameFrameMode();
                    }
                }

            } else if (solverHelper.getLeftPieces(board) == 1) {
                disableBoardPlayMode();

                int option = JOptionPane.showOptionDialog(
                        null,
                        "¡Has completado el tablero pero con piezas restantes!\nPiezas eliminadas: "
                                + totalPiecesDeleted + "\nPuntos: " + totalPoints,
                        "Completado",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[] { "Jugar otra vez", "Guardar Movimientos", "Salir" },
                        null);

                if (option == JOptionPane.YES_OPTION) {
                    enableBoardPlayMode();
                    setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                    this.totalPoints = 0;
                    this.totalPiecesDeleted = 0;
                    frameGamePlay.dispose();
                    panelGamePlay.removeAll();

                    playGameFrameMode();
                } else if (option == JOptionPane.NO_OPTION) {
                    fileHandler.saveMyGameSolution(actualMoves, totalPoints,
                            solverHelper.getLeftPieces(board));
                    JOptionPane.showMessageDialog(null, "Thanks for playing!");
                    System.exit(0);
                } else {
                    int responseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (responseExit == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Thanks for playing!");
                        System.exit(0);
                    } else {
                        enableBoardPlayMode();
                        setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                        this.totalPoints = 0;
                        this.totalPiecesDeleted = 0;
                        frameGamePlay.dispose();
                        panelGamePlay.removeAll();

                        playGameFrameMode();
                    }
                }
            } else {
                List<int[]> posibleMoves = new ArrayList<>();
                solverHelper.checkMoves(board, posibleMoves);
                // for (int i = 0; i < posibleMoves.size(); i++) {
                // int[] move = posibleMoves.get(i);
                // // System.out.println("Movimiento #" + (i + 1) + ": (" + move[0] + ", " +
                // // move[1] + ")");
                // }

                if (posibleMoves.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay movimientos posibles");
                    disableBoardPlayMode();

                    int option = JOptionPane.showOptionDialog(
                            null,
                            "¡No hay mas movimientos posibles!\nPiezas eliminadas: "
                                    + totalPiecesDeleted + "\nPuntos: " + totalPoints,
                            "Fin de juego",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[] { "Jugar otra vez", "Salir" },
                            null);

                    if (option == JOptionPane.YES_OPTION) {
                        enableBoardPlayMode();
                        setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                        this.totalPoints = 0;
                        this.totalPiecesDeleted = 0;
                        frameGamePlay.dispose();
                        panelGamePlay.removeAll();

                        playGameFrameMode();
                    } else {

                        JOptionPane.showMessageDialog(null, "Thanks for playing!");
                        System.exit(0);

                    }
                }

            }

        }

    }

    private int[] checkCoordsOnPosibleMoves(int[] cord, char[][] board2Comprobe, List<int[]> posibleMovesBegin) {
        char target = board2Comprobe[cord[0]][cord[1]];
        if (target == '-') {
            return new int[] { -1, -1 };
        }

        if (solverHelper.isGroupByPos(cord[0], cord[1], board2Comprobe, target)) {
            for (int i = 0; i < posibleMovesBegin.size(); i++) {
                int[] move = posibleMovesBegin.get(i);
                int moveX = move[0];
                int moveY = move[1];

                if (board[moveX][moveY] == target) {
                    return new int[] { moveX, moveY };
                }
            }
        }

        return null;

    }

    private void disableBoardPlayMode() {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                boardPiecesButtonsP[i][j].setEnabled(false);
            }
        }
    }

    private void enableBoardPlayMode() {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                boardPiecesButtonsP[i][j].setEnabled(true);
            }
        }
    }

    private int[] getButtonClickedCoordinates(JButton boardPiece) {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                if (boardPiecesButtonsP[i][j] == boardPiece) {
                    System.out.println("Coordenadas: " + i + " " + j);
                    return new int[] { i, j };
                }
            }
        }

        // Si no se encuentra el botón, devuelve un valor por defecto o lanza una
        // excepción según tus necesidades
        return new int[] { -1, -1 };

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