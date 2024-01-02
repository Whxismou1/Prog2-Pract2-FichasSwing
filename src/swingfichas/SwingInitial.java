/**
 * Clase encargada de la interfaz grafica del juego
 */

package swingfichas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

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
    private JButton importGameC;
    private char[][] board;
    private List<List<Integer>> actualMoves;

    // private JButton undoButtonC;
    // private JButton redoButtonC;
    private JMenu menuURC;
    private JMenuItem undoItemC;
    private JMenuItem redoItemC;
    private List<char[][]> undoManagerC;
    private List<char[][]> redoManagerC;

    /* Elementos del menu Jugar */
    private JFrame frameGamePlay;
    private JPanel panelGamePlay;
    private JButton importGameP;
    private JButton createGameP;
    private JButton showAvMovesP;

    private JMenu menuURP;
    private JMenuItem undoItemP;
    private JMenuItem redoItemP;

    // private JButton undoButtonP;
    // private JButton redoButtonP;

    private JButton[][] boardPiecesButtonsP;
    private int totalPoints;
    private int totalPiecesDeleted;
    private List<Map<String, Object>> undoManagerP;
    private List<Map<String, Object>> redoManagerP;

    public SwingInitial() {

        inicializeUtilities();
        inicializeComponents();
        addButtonsMainMenu2Arr();
        addComponents2MainFrame();
        addActionsLIstenerComponents();

    }

    /**
     * Metodo encargado de inicializar las variables de utilidad
     */
    private void inicializeUtilities() {
        this.numFilas = DEFAULT_ROWS;
        this.numColumnas = DEFAULT_COLS;
        totalPoints = 0;
        totalPiecesDeleted = 0;
        actualMoves = new ArrayList<>();

        undoManagerC = new ArrayList<>();
        redoManagerC = new ArrayList<>();

        undoManagerP = new ArrayList<>();
        redoManagerP = new ArrayList<>();
        fileHandler = new FileHandler();
    }

    /**
     * Metodo encargado de nicializar los componentes de la app
     */
    private void inicializeComponents() {

        mainMenuInicializeComponents();

        createMenuInicializeComponents();

        playMenuInicializeComponents();

    }

    /**
     * Metodo encargado de inicializar los compoentes del menu Principal
     */
    private void mainMenuInicializeComponents() {
        mainFrame = new JFrame("SwingFichas");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(0.5f);

        createGameButton = new JButton("Create Game");
        playGameButton = new JButton("Play Game");
        exitButton = new JButton("Exit");

        titleGame = new JLabel("Game Swing Same Pieces");
        titleGame.setAlignmentX(0.5f);

        menuIcon = new ImageIcon("etc/images/fichasroja,verde,azul.png");

        imageMenuScaled = menuIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        iconMenuScaled = new ImageIcon(imageMenuScaled);
        imageMenu = new JLabel(iconMenuScaled);
        imageMenu.setAlignmentX(0.5f);
    }

    /**
     * Metodo encargado de inicializar los compoentes del menu crear Juego
     */
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

        // undoButtonC = new JButton("<-");
        // redoButtonC = new JButton("->");
        undoItemC = new JMenuItem("Undo <-");
        redoItemC = new JMenuItem("Redo ->");
        menuURC = new JMenu("Editar");
        menuURC.add(undoItemC);
        menuURC.add(redoItemC);

        importGameC = new JButton("Import Board");
        board = new char[this.numFilas][this.numColumnas];
    }

    /**
     * Metodo encargado de inicializar los compoentes del menu jugar Juego
     */
    private void playMenuInicializeComponents() {
        panelGamePlay = new JPanel();
        importGameP = new JButton("Import Board");
        createGameP = new JButton("Create Board");
        showAvMovesP = new JButton("Hints");

        menuURP = new JMenu("Editar");
        undoItemP = new JMenuItem("Undo <-");
        redoItemP = new JMenuItem("Redo ->");

        menuURP.add(undoItemP);
        menuURP.add(redoItemP);
        // undoButtonP = new JButton("<-");
        // redoButtonP = new JButton("->");

        board = new char[this.numFilas][this.numColumnas];
    }

    /**
     * Metodo encargado de meter los botones del menu principal
     */
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

    /**
     * Metodo encargado de meter el panel al frame principal
     */
    private void addComponents2MainFrame() {
        mainFrame.getContentPane().add(mainPanel);
    }

    /**
     * Metodo encargado de añadir e implementar los action listeners
     */
    private void addActionsLIstenerComponents() {
        // Accion para el boton Crear Juego del menu principal
        createGameButton.addActionListener(e -> {
            actualMoves.clear();
            deactivateButton(createGameButton);
            createGameFrameMode();

        });
        // Accion para el boton Jugar Juego del menu principal
        playGameButton.addActionListener(e -> {
            actualMoves.clear();
            deactivateButton(playGameButton);
            playGameFrameMode();
            disableBoardPlayMode();
        });

        // Accion para el boton salir del menu principal
        exitButton.addActionListener(e -> {
            exitApp();
        });

        // Accion para el boton selector de filas del menu crear juego
        rowSelector.addChangeListener(e -> {
            updateGameBoard();
        });
        // Accion para el boton selector de columnas del menu crear juego
        colSelector.addChangeListener(e -> {
            updateGameBoard();
        });

        // Accion para el boton guardar del menu crear juego
        saveFileC.addActionListener(e -> {
            saveGame();

        });

        // Accion para el boton importar del menu crear juego
        importGameC.addActionListener(e -> {
            importGame();

        });

        // Accion para el boton jugar del menu crear juego
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

                List<int[]> posiblesMoves = new ArrayList<>();
                solverHelper.checkMoves(auxboard, posiblesMoves);

                if (posiblesMoves.isEmpty()) {
                    disableBoardPlayMode();

                    int option = JOptionPane.showOptionDialog(
                            null,
                            "¡El tablero no tiene solucion!",
                            "Game Over",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new Object[] { "Jugar otra vez", "Crear nuevo Tablero", "Salir" },
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
                        setRowsAndCols(DEFAULT_ROWS, DEFAULT_COLS);
                        this.totalPoints = 0;
                        this.totalPiecesDeleted = 0;
                        frameGamePlay.dispose();
                        panelGamePlay.removeAll();

                        createGameButton.doClick();

                    } else {

                        JOptionPane.showMessageDialog(null, "Thanks for playing!");
                        System.exit(0);

                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "The board is not valid");
            }

        });

        // Accion para el boton crear juego del menu jugar juego
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
                rowSelector.setValue(auxboard.length);
                colSelector.setValue(auxboard[0].length);
                board = copyMatrix(auxboard);

                changePieces(boardPiecesButtons);

            } else {
                resetRowsAndCols2Default();
                frameGamePlay.dispose();
                panelGamePlay.removeAll();
                activateButton(playGameButton);
                rowSelector.setValue(DEFAULT_ROWS);
                colSelector.setValue(DEFAULT_COLS);
                createGameFrameMode();
            }
            undoManagerP.clear();
            redoManagerP.clear();

        });

        // Accion para el boton importar juego del menu Jugar juego
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
                solverHelper = new SolverHelper(board);
                this.totalPoints = 0;
                this.totalPiecesDeleted = 0;

                changePieces(boardPiecesButtonsP);
            }

        });

        // Accion para el boton undo del menu jugar juego
        undoItemP.addActionListener(e -> {

            if (!undoManagerP.isEmpty()) {
                int cuurentPoints = this.totalPoints;
                int deletedPieces = this.totalPiecesDeleted;

                Map<String, Object> lastStatus = undoManagerP.remove(undoManagerP.size() - 1);
                this.totalPoints = (int) lastStatus.get("totalPoints");
                this.totalPiecesDeleted = (int) lastStatus.get("totalPiecesDeleted");
                char[][] boardState = (char[][]) lastStatus.get("boardState");

                Map<String, Object> currentState = new HashMap<>();
                currentState.put("boardState", getBoard(boardPiecesButtonsP));
                currentState.put("totalPoints", cuurentPoints);
                currentState.put("totalPiecesDeleted", deletedPieces);

                redoManagerP.add(currentState);

                panelGamePlay.removeAll();

                createPlayGameBoard(this.numFilas, this.numColumnas);
                board = copyMatrix(boardState);

                changePieces(boardPiecesButtonsP);
            }

        });

        undoItemC.addActionListener(e -> {
            if (!undoManagerC.isEmpty()) {
                char[][] lastStatus = undoManagerC.remove(undoManagerC.size() - 1);

                redoManagerC.add(getBoard(boardPiecesButtons));

                createGamePanel.removeAll();
                createGameBoard(this.numFilas, this.numColumnas);

                board = copyMatrix(lastStatus);

                changePieces(boardPiecesButtons);
            }
        });

        redoItemC.addActionListener(e -> {
            // printMatrix(getBoard(boardPiecesButtons));
            if (!redoManagerC.isEmpty()) {

                char[][] redoneState = redoManagerC.remove(redoManagerC.size() - 1);

                undoManagerC.add(getBoard(boardPiecesButtons));
                // board = copyMatrix(redoneState);

                createGamePanel.removeAll();
                createGameBoard(this.numFilas, this.numColumnas);

                board = copyMatrix(redoneState);

                changePieces(boardPiecesButtons);

            }
        });

        // Accion para el boton redo del menu jugar juego
        redoItemP.addActionListener(e -> {
            if (!redoManagerP.isEmpty()) {
                int currentPoints = this.totalPoints;
                int currentPiecesDeleted = this.totalPiecesDeleted;

                Map<String, Object> redoneState = redoManagerP.remove(redoManagerP.size() - 1);
                this.totalPoints = (int) redoneState.get("totalPoints");
                this.totalPiecesDeleted = (int) redoneState.get("totalPiecesDeleted");

                char[][] boardState = (char[][]) redoneState.get("boardState");

                Map<String, Object> currentState = new HashMap<>();
                currentState.put("boardState", getBoard(boardPiecesButtonsP));
                currentState.put("totalPoints", currentPoints);
                currentState.put("totalPiecesDeleted", currentPiecesDeleted);

                undoManagerP.add(currentState);

                panelGamePlay.removeAll();
                createPlayGameBoard(this.numFilas, this.numColumnas);

                board = copyMatrix(boardState);

                changePieces(boardPiecesButtonsP);
            }

        });

        // Accion para el boton Hints del menu jugar juego
        showAvMovesP.addActionListener(new ActionListener() {

            // MyProgressDialog progressDialog = new MyProgressDialog(null, "Progreso");
            public void actionPerformed(ActionEvent e) {
                deactivateButton(showAvMovesP);
                SwingWorker<Void, Integer> swgWorker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        char[][] auxboard = getBoard(boardPiecesButtonsP);

                        board = copyMatrix(auxboard);

                        solverHelper = new SolverHelper(board);

                        solverHelper.play();

                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            List<String> bestMoves = solverHelper.getResultListSolver();
                            StringBuilder movesText = new StringBuilder("Movimientos disponibles:\n");
                            for (String move : bestMoves) {
                                movesText.append(move).append("\n");
                            }

                            JOptionPane.showMessageDialog(null, movesText.toString());
                            activateButton(showAvMovesP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                if (isValidResult(getBoard(boardPiecesButtonsP))) {
                    swgWorker.execute();
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: Board is empty");
                    activateButton(showAvMovesP);
                }

            }
        });

    }

    /**
     * Metodo encargado de de copiar una matriz
     * 
     * @param original -> matriz original
     * @return copia de la matriz
     */
    private char[][] copyMatrix(char[][] originalMatrix) {
        int nR = originalMatrix.length;
        int nC = originalMatrix[0].length;
        char[][] matrixCopied = new char[nR][nC];

        for (int i = 0; i < nR; i++) {
            matrixCopied[i] = Arrays.copyOf(originalMatrix[i], nC);
        }

        return matrixCopied;
    }

    /**
     * Metodo encargado de cambiar las piezas del tablero
     * 
     * @param modeBoard -> Matriz de botones pasada (indica el
     *                  modo[boardPiecesButtons modo crear | boardPiecesButtonsP
     *                  modo jugar])
     */
    private void changePieces(JButton[][] modeBoard) {

        for (int i = 0; i < modeBoard.length; i++) {
            for (int j = 0; j < modeBoard[0].length; j++) {
                char pieceOnBoard = board[i][j];

                if (pieceOnBoard == 'R') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaRoja.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    modeBoard[i][j].setIcon(new ImageIcon(image));
                    modeBoard[i][j].setText("R");
                    modeBoard[i][j].setBackground(new Color(255, 0, 0));
                } else if (pieceOnBoard == 'V') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaVerde.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    modeBoard[i][j].setIcon(new ImageIcon(image));
                    modeBoard[i][j].setText("V");
                    modeBoard[i][j].setBackground(new Color(0, 255, 0));

                } else if (pieceOnBoard == 'A') {
                    ImageIcon icon = new ImageIcon("etc/images/fichaAzul.png");
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    modeBoard[i][j].setIcon(new ImageIcon(image));
                    modeBoard[i][j].setText("A");
                    modeBoard[i][j].setBackground(new Color(0, 0, 255));
                } else {
                    modeBoard[i][j].setIcon(null);
                    modeBoard[i][j].setText("-");
                    modeBoard[i][j].setBackground(new Color(255, 255, 255));
                }

            }
        }

    }

    /**
     * Metodo encargado de obtener la matriz actual
     * 
     * @param modeBoard -> Matriz de botones pasada (indica el
     *                  modo[boardPiecesButtons modo crear | boardPiecesButtonsP
     *                  modo jugar])
     * @return matriz actual
     */
    private char[][] getBoard(JButton[][] modeBoard) {
        char[][] auxBoard = new char[modeBoard.length][modeBoard[0].length];

        for (int i = 0; i < auxBoard.length; i++) {
            for (int j = 0; j < auxBoard[0].length; j++) {
                String text = modeBoard[i][j].getText();
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

    /**
     * Metodo encargado de comprobar si un tablero es correcto
     * 
     * @param auxboard -> matriz a comprobar
     * @return true si es correcto, false si no
     */
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
     * Metodo encargado de comprobar si un tablero es correcto(en este caso si es
     * vacio)
     * 
     * @param auxboard -> matriz a comprobar
     * @return true si es correcto, false si no
     */
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

        createGamePanel.removeAll();

        createGameBoard(numRows, numCols);

        createGameFrame.revalidate();
        createGameFrame.repaint();

    }

    /**
     * Metodo encargado de crear el modo de Jugar Juego
     */
    private void playGameFrameMode() {
        frameGamePlay = new JFrame("Play Game");
        frameGamePlay.setSize(680, 680);
        frameGamePlay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameGamePlay.setLocationRelativeTo(null);
        frameGamePlay.setResizable(true);

        frameGamePlay.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                panelGamePlay.removeAll();
                resetRowsAndCols2Default();
                activateButton(playGameButton);
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

    /**
     * Metodo encargado de crear el tablero del modo de Jugar Juego
     * 
     * @param nR -> numero filas del tablero
     * @param nC -> numero columnas del tablero
     */
    private void createPlayGameBoard(int nR, int nC) {
        boardPiecesButtonsP = new JButton[nR][nC];

        board = new char[nR][nC];

        panelGamePlay.setLayout(new BorderLayout());

        // Agregar selectores de fila y columna
        JPanel selectorsPanel = new JPanel();

        selectorsPanel.add(importGameP);
        selectorsPanel.add(createGameP);
        selectorsPanel.add(showAvMovesP);
        // selectorsPanel.add(undoButtonP);
        // selectorsPanel.add(redoButtonP);
        // selectorsPanel.add(progBar);
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

        JMenuBar menuBarP = new JMenuBar();
        menuBarP.add(menuURP);
        panelGamePlay.add(boardPanel, BorderLayout.CENTER);
        frameGamePlay.setJMenuBar(menuBarP);
        frameGamePlay.getContentPane().add(panelGamePlay);
    }

    /**
     * Metodo encargado de crear el modo de Crear Juego
     */

    private void createGameFrameMode() {
        createGameFrame = new JFrame("Create Game");
        createGameFrame.setSize(680, 680);
        createGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createGameFrame.setLocationRelativeTo(null);
        createGameFrame.setResizable(true);
        createGameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                activateButton(createGameButton);
                createGamePanel.removeAll();
                resetRowsAndCols2Default();
                undoManagerC.clear();
                redoManagerC.clear();
            }
        });
        createGameBoard(this.numFilas, this.numColumnas);

        createGameFrame.setVisible(true);
    }

    /**
     * Metodo encargado de crear el tablero del modo de Crear Juego
     * 
     * @param numRows -> numero filas del tablero
     * @param numCols -> numero columnas del tablero
     */
    private void createGameBoard(int numRows, int numCols) {
        boardPiecesButtons = new JButton[numRows][numCols];

        board = new char[numRows][numCols];

        createGamePanel.setLayout(new BorderLayout());

        JPanel selectorsPanel = new JPanel();
        selectorsPanel.add(new JLabel("Rows:"));
        selectorsPanel.add(rowSelector);
        selectorsPanel.add(new JLabel("Cols:"));
        selectorsPanel.add(colSelector);
        selectorsPanel.add(saveFileC);
        selectorsPanel.add(playButtonC);
        // selectorsPanel.add(undoButtonC);
        // selectorsPanel.add(redoButtonC);
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

        JMenuBar menuBarC = new JMenuBar();
        menuBarC.add(menuURC);

        createGamePanel.add(boardPanel, BorderLayout.CENTER);
        createGameFrame.setJMenuBar(menuBarC);
        createGameFrame.getContentPane().add(createGamePanel);

    }

    /**
     * Metodo encargado de crear los botones de las fichas en el modo Crear Juego
     * 
     * @return boton de ficha relleno
     */
    private JButton createBoardPieceButton() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));
        boardPiece.addActionListener(e -> handleBoardPieceButtonClick(boardPiece));
        return boardPiece;
    }

    /**
     * Metodo encargado de crear los botones de las fichas en el modo Jugar Juego
     * 
     * @return boton de ficha relleno
     */
    private JButton createBoardPieceButtonOnPlayMode() {
        JButton boardPiece = new JButton();
        boardPiece.setPreferredSize(new Dimension(50, 50));

        boardPiece.addActionListener(e -> handleBoardPieceButtonClickonPlayMode(boardPiece));
        return boardPiece;
    }

    /**
     * Metodo encargado de manejar el evento de click en un boton de ficha en el
     * modo Crear Juego
     * 
     * @param boardPiece -> boton de ficha pulsado
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
            undoManagerC.add(getBoard(boardPiecesButtons));
            redoManagerC.clear();
            updatePieceImage(boardPiece, options[choice]);
            boardPiece.setText(options[choice]);
        }

    }

    /**
     * Metodo encargado de manejar el evento de click en un boton de ficha en el
     * modo Jugar Juego
     * 
     * @param boardPiece -> boton de ficha pulsado
     */
    private void handleBoardPieceButtonClickonPlayMode(JButton boardPiece) {
        board = getBoard(boardPiecesButtonsP);
        List<int[]> posibleMovesBegin = new ArrayList<>();

        if (isValidResult(board)) {
            solverHelper = new SolverHelper(board);

            int[] cord = getButtonClickedCoordinates(boardPiece);

            solverHelper.checkMoves(board, posibleMovesBegin);
            int coordX = cord[0];
            int coordY = cord[1];

            if (board[coordX][coordY] == '-') {
                JOptionPane.showMessageDialog(null, "No se puede eliminar porque es una pieza vacia");
                return;
            }

            if (!solverHelper.isGroupByPos(coordX, coordY, board, board[coordX][coordY])) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar porque no es un grupo");
                return;
            }

            Map<String, Object> currentState = new HashMap<>();
            currentState.put("boardState", copyMatrix(board));
            currentState.put("totalPoints", this.totalPoints);
            currentState.put("totalPiecesDeleted", this.totalPiecesDeleted);

            undoManagerP.add(currentState);

            redoManagerP.clear();

            char color = board[coordX][coordY];
            int piezasdeleted = solverHelper.removeGroup(board, coordX, coordY);
            int puntos = solverHelper.getPointWithDeletedPieces(board, piezasdeleted);

            actualMoves.add(List.of(coordX, coordY, piezasdeleted, puntos, (int) color));
            totalPiecesDeleted += piezasdeleted;
            totalPoints += puntos;

            board = solverHelper.getBoardResult();

            getPiecesDown(board);
            movePiecesCol(board);

            changePieces(boardPiecesButtonsP);

            if (solverHelper.getLeftPieces(board) == 0) {
                totalPoints += 1000;
                disableBoardPlayMode();

                int option = JOptionPane.showOptionDialog(
                        null,
                        "¡Enhorabuena, has completado el tablero sin piezas restantes!\nPiezas eliminadas: "
                                + totalPiecesDeleted + "\nPuntos: " + totalPoints,
                        "WIN",
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
                        "Finished",
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

                if (posibleMoves.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay movimientos posibles");
                    disableBoardPlayMode();

                    int option = JOptionPane.showOptionDialog(
                            null,
                            "¡No hay mas movimientos posibles!\nPiezas eliminadas: "
                                    + totalPiecesDeleted + "\nPuntos: " + totalPoints,
                            "Game Over",
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

    /**
     * Metodo encargado de bajar las piezas
     * 
     * @param boardGameP -> tablero a comprobar
     */
    private void getPiecesDown(char[][] boardGameP) {
        for (int row = 0; row < boardGameP.length - 1; row++) {
            for (int col = 0; col < boardGameP[0].length; col++) {
                if (boardGameP[row + 1][col] == '-' && boardGameP[row][col] != '-') {
                    boardGameP[row + 1][col] = boardGameP[row][col];
                    boardGameP[row][col] = '-';
                    getPiecesDown(boardGameP);
                }

            }
        }

    }

    /**
     * Metodo encargado de mover las piezas en colummnas
     * 
     * @param boardGameP -> tablero a comprobar
     */
    private void movePiecesCol(char[][] boardGameP) {
        for (int row = 0; row < boardGameP[0].length; row++) {
            int numEmptyPieces = 0;
            for (int col = 0; col < boardGameP.length; col++) {
                if (boardGameP[col][row] == '-') {
                    numEmptyPieces++;
                }
            }
            if (numEmptyPieces == boardGameP.length) {
                changeCols(boardGameP, row);
            }
        }

    }

    /**
     * Metodo auxiliar para cambiar columnas
     * 
     * @param boardGameP -> tablero a cambiar
     * @param col        -> columna
     */
    private void changeCols(char[][] boardGameP, int col) {
        for (int destCol = col + 1; destCol < boardGameP[0].length; destCol++) {
            int numEmptySpaces = countEmptySpacesInColumn(boardGameP, destCol);
            if (numEmptySpaces != boardGameP.length) {
                shiftColumnContents(boardGameP, col, destCol);
                break;
            }
        }
    }

    private int countEmptySpacesInColumn(char[][] boardGameP, int col) {
        int emptySpaces = 0;
        for (int row = 0; row < boardGameP.length; row++) {
            if (boardGameP[row][col] == '-') {
                emptySpaces++;
            }
        }
        return emptySpaces;
    }

    private void shiftColumnContents(char[][] boardGameP, int srcCol, int destCol) {
        for (int row = 0; row < boardGameP.length; row++) {
            boardGameP[row][srcCol] = boardGameP[row][destCol];
            boardGameP[row][destCol] = '-';
        }
    }

    /**
     * Metodo encargado de desactivar el tablero en el modo Jugar
     */
    private void disableBoardPlayMode() {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                boardPiecesButtonsP[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Metodo encargado de activar el tablero en el modo Jugar
     */
    private void enableBoardPlayMode() {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                boardPiecesButtonsP[i][j].setEnabled(true);
            }
        }
    }

    /**
     * Metodo encargado de obtener las coordenadas del boton pulsado en el modo
     * Jugar Juego
     * 
     * @param boardPiece -> boton pulsado
     * @return -> posicion del boton (x,y)
     * 
     */
    private int[] getButtonClickedCoordinates(JButton boardPiece) {
        for (int i = 0; i < boardPiecesButtonsP.length; i++) {
            for (int j = 0; j < boardPiecesButtonsP[0].length; j++) {
                if (boardPiecesButtonsP[i][j] == boardPiece) {
                    // System.out.println("Coordenadas: " + i + " " + j);
                    return new int[] { i, j };
                }
            }
        }
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
                    boardPiece.setBackground(new Color(255, 0, 0));
                    break;
                case "V":
                    icon = new ImageIcon("etc/images/fichaVerde.png");
                    image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    boardPiece.setIcon(new ImageIcon(image));
                    boardPiece.setBackground(new Color(0, 255, 0));
                    break;
                case "A":
                    icon = new ImageIcon("etc/images/fichaAzul.png");
                    image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    boardPiece.setIcon(new ImageIcon(image));
                    boardPiece.setBackground(new Color(0, 0, 255));
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

    /**
     * Metodo encargado de hacer visible la app
     */
    protected void show() {
        mainFrame.setSize(400, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Metodo encargado de setear las filas y columnas
     * 
     * @param filas
     * @param columnas
     */
    private void setRowsAndCols(int filas, int columnas) {
        this.numFilas = filas;
        this.numColumnas = columnas;
    }

    /**
     * Metodo encargado de resetear las filas y columnas
     */
    private void resetRowsAndCols2Default() {
        this.numFilas = DEFAULT_ROWS;
        this.numColumnas = DEFAULT_COLS;
    }

    /**
     * Metodo encargado de salir de la aplicacion
     */
    private void exitApp() {
        int responseExit = JOptionPane.showConfirmDialog(null, "Do you want to exit the game?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (responseExit == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Thanks for playing!");
            System.exit(0);
        }
    }

    /**
     * Metodo encargado de exportar el juego a un fichero
     */
    private void saveGame() {
        board = getBoard(boardPiecesButtons);

        if (isValid(board)) {
            fileHandler.saveToFile(board);

        } else {
            JOptionPane.showMessageDialog(null, "The board is not valid");
        }
    }

    /**
     * Metodo encargado de importar un juego desde un fichero
     */
    private void importGame() {
        char[][] importedBoard = fileHandler.loadGame();

        if (isValid(importedBoard)) {
            redoManagerC.clear();
            undoManagerC.clear();
            int nf = importedBoard.length;
            int nc = importedBoard[0].length;

            setRowsAndCols(nf, nc);

            rowSelector.setValue(nf);
            colSelector.setValue(nc);

            board = copyMatrix(importedBoard);

            changePieces(boardPiecesButtons);
        }

    }

}