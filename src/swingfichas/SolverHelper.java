
/**
 * Clase encargada de la logica de jugar
 */

package swingfichas;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SolverHelper {

    // Juego actual
    int actualGame;
    private int bestScore;
    private List<int[]> actualSolutions;
    private List<int[]> bestSolutions;

    // Lista creada para obtener los movimientos posibles en funcion del tablero
    // pasado
    private List<String> resultListSolver;

    // Matriz donde se guarda el tablero de chars
    private char[][] gameBoard;

    private char[][] gameBoardResult;

    SolverHelper(char[][] gameBoardP) {
        actualGame = 1;
        this.gameBoard = gameBoardP;
        this.gameBoardResult = gameBoardP;

        this.actualSolutions = new ArrayList<>();
        this.bestSolutions = new ArrayList<>();

        this.resultListSolver = new ArrayList<>();

    }

    /***
     * Metodo principal del juego
     */
    protected void play() {

        // Metodo que calcula las soluciones
        List<int[]> posibleMoves = new ArrayList<>();
        checkMoves(gameBoard, posibleMoves);

        findSolutions4Board(this.gameBoard, posibleMoves);

        String resultSolver = printActualGameSolutionForSolver(actualGame, gameBoard);

        resultListSolver.add(resultSolver);

        gameBoardResult = matrixCopy(gameBoard);

        gameBoard = null;
        bestScore = 0;
        actualSolutions.clear();
        bestSolutions.clear();

    }

    /***
     * Metodo que nos muestra los mejores movimientos y nos lo imprime en
     * el formato espcificado
     * 
     * @param actualGame -> juego actual
     * @param boardGameP -> tablero de juego
     * @return -> String con los movimientos
     */
    private String printActualGameSolutionForSolver(int actualGame, char[][] boardGameP) {
        StringBuilder sb = new StringBuilder();
        // sb.append("Movimientos disponibles\n");

        for (int i = 0; i < this.bestSolutions.size(); i++) {
            int[] arrSolutions = this.bestSolutions.get(i);

            int movesScore = getPointWithDeletedPieces(boardGameP, arrSolutions[2]);

            int row = arrSolutions[0];
            int col = arrSolutions[1];

            removeGroup(boardGameP, row, col);
            if (movesScore == 1) {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions[0])
                        .append(", ")
                        .append(arrSolutions[1] + 1).append("): para eliminar ").append(arrSolutions[2])
                        .append(" fichas de color ")
                        .append((char) arrSolutions[3]).append("\n");
            } else {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions[0])
                        .append(", ")
                        .append(arrSolutions[1] + 1).append("): para eliminar ").append(arrSolutions[2])
                        .append(" fichas de color ")
                        .append((char) arrSolutions[3]).append("\n");
            }

        }
        return sb.toString();
    }

    /**
     * Metodo que comprueba los mejores movimientos posibles
     * 
     * @param boardGameP   -> tablero de juego
     * @param posibleMoves -> lista de movimientos posibles
     */
    protected void checkMoves(char[][] boardGameP, List<int[]> posibleMoves) {
        boolean visited[][] = new boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        for (int row = getTotalRows(boardGameP) - 1; row >= 0; row--) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (!visited[row][col] && isGroupByPos(row, col, boardGameP, boardGameP[row][col])
                        && boardGameP[row][col] != '-') {
                    posibleMoves.add(new int[] { row, col });
                    checkMovesWithRec(boardGameP, row, col, boardGameP[row][col], visited);
                }
            }
        }

    }

    /**
     * Metodo auxiliar para comrpobar los movimientos
     */
    private void checkMovesWithRec(char[][] boardGameP, int row, int col, char actualPiece,
            boolean[][] visited) {

        if (row < 0 || row >= getTotalRows(boardGameP) || col < 0 || col >= getTotalCols(boardGameP)
                || visited[row][col]
                || boardGameP[row][col] != actualPiece) {
            return;
        }

        visited[row][col] = true;

        checkMovesWithRec(boardGameP, row - 1, col, actualPiece, visited); // Check UP
        checkMovesWithRec(boardGameP, row + 1, col, actualPiece, visited); // check down
        checkMovesWithRec(boardGameP, row, col - 1, actualPiece, visited); // check left
        checkMovesWithRec(boardGameP, row, col + 1, actualPiece, visited); // check right

    }

    /**
     * Metodo que nos comprueba si la pieza que hay en las posiciones pasadas es
     * grupo
     * 
     * @param row         -> fila de la pieza
     * @param col         -> columna de la pieza
     * @param boardGameP  -> tablero de juego
     * @param actualColor -> color/caracter de la pieza
     * @return -> true si es grupo, false si no lo es
     */
    protected boolean isGroupByPos(int row, int col, char[][] boardGameP, char actualColor) {
        return isGroupByPosWithRec(boardGameP, row - 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row + 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col - 1, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col + 1, actualColor);
    }

    /**
     * Metodo auxiliar que comprueba si la pieza que hay en las posiciones pasadas
     * es grupo
     */
    private static boolean isGroupByPosWithRec(char[][] boardGameP, int row, int col, char actualColor) {
        return row >= 0 && row < boardGameP.length &&
                col >= 0 && col < boardGameP[0].length &&
                boardGameP[row][col] == actualColor;
    }

    /**
     * Metodo que calcula las soluciones para el tablero pasado
     * 
     * @param boardGameP   -> tablero de juego
     * @param posibleMoves -> lista de movimientos posibles
     */
    private void findSolutions4Board(char[][] boardGameP, List<int[]> posibleMoves) {

        char[][] copyBoardGame = matrixCopy(boardGameP);

        for (int i = 0; i < posibleMoves.size(); i++) {
            int[] actualMove = posibleMoves.get(i);

            int row = actualMove[0];
            int col = actualMove[1];

            char actualColor = copyBoardGame[row][col];

            int deletedPieces = removeGroup(copyBoardGame, row, col);

            this.actualSolutions.add(new int[] { row, col, deletedPieces, actualColor });

            List<int[]> nextMovesList = new ArrayList<>();
            checkMoves(copyBoardGame, nextMovesList);
            findSolutions4Board(copyBoardGame, nextMovesList);
            copyBoardGame = matrixCopy(boardGameP);
            this.actualSolutions.remove(this.actualSolutions.size() - 1);
        }

        if (posibleMoves.size() == 0) {
            if (bestSolutions.size() == 0) {
                bestSolutions.addAll(actualSolutions);
            }

            checkSolutions(copyBoardGame);
        }

    }

    /**
     * Metodo que comrpueba entre las soluciones obteniendo las mejores
     * 
     * @param boardGameP -> tablero de juego
     */
    private void checkSolutions(char[][] boardGameP) {
        int finalScore = getTotalMoveScore(boardGameP);

        if (this.bestScore < finalScore) {
            this.bestScore = finalScore;
            this.bestSolutions.clear();
            this.bestSolutions.addAll(this.actualSolutions);
        }

    }

    /**
     * Metodo que devuelve el total de puntos de un movimiento
     * 
     * @param boardGameP -> tablero de juego
     * @return -> total de puntos
     */
    private int getTotalMoveScore(char[][] boardGameP) {
        int finalMoveScore = 0;

        for (int i = 0; i < this.actualSolutions.size(); i++) {
            int[] matrixSol = this.actualSolutions.get(i);
            int numPointsMv = getPointWithDeletedPieces(boardGameP, matrixSol[2]);
            finalMoveScore += numPointsMv;
        }

        finalMoveScore += (getLeftPieces(boardGameP) == 0) ? 1000 : 0;

        return finalMoveScore;
    }

    /**
     * Metodo que nos devuelve los puntos en funcion de las piezas eliminadas
     * 
     * @param boardGameP    -> tablero de juego
     * @param deletedPieces -> numero de piezas eliminadas
     * @return -> puntos
     */
    protected int getPointWithDeletedPieces(char[][] boardGameP, int deletedPieces) {
        return (deletedPieces - 2) * (deletedPieces - 2);
    }

    /***
     * Metodo que devuelve el numero de piezas restante
     * 
     */
    protected int getLeftPieces(char[][] boardGameP) {
        int piecesLeft = 0;

        for (int row = 0; row < getTotalRows(boardGameP); row++) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (boardGameP[row][col] != '-') {
                    piecesLeft++;
                }
            }
        }

        return piecesLeft;
    }

    /***
     * Metodo encargado de eliminar el grupo
     * 
     * @param col
     * @param intento
     */
    protected int removeGroup(char[][] boardGameP, int row, int col) {
        char actualColor = boardGameP[row][col];

        boolean[][] visited = new boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        return removeGroupRec(boardGameP, row, col, actualColor, visited);

    }

    /**
     * Metodo auxiliar recursivo que elimina el grupo pasado
     * 
     * @param boardGameP -> tablero de juego
     * @param row        -> fila de la pieza
     * @param col        -> columna de la pieza
     * @param color      -> color/caracter de la pieza
     * @param visited    -> matriz de visitados
     * @return -> numero de piezas eliminadas
     */
    private int removeGroupRec(char[][] boardGameP, int row, int col, char color, boolean[][] visited) {
        int numPiecesRem = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { row, col });

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            row = current[0];
            col = current[1];

            if (row < 0 || row >= boardGameP.length || col < 0 || col >= boardGameP[0].length || visited[row][col]
                    || boardGameP[row][col] != color) {
                continue;
            }

            visited[row][col] = true;
            numPiecesRem++;

            stack.push(new int[] { row - 1, col }); // Comprobar arriba
            stack.push(new int[] { row + 1, col }); // Comprobar abajo
            stack.push(new int[] { row, col - 1 }); // Comprobar izquierda
            stack.push(new int[] { row, col + 1 }); // Comprobar derecha

            boardGameP[row][col] = '-';
        }

        return numPiecesRem;

    }

    /**
     * Metodo que nos devuelve el numero de columnas
     * 
     * @param matrix -> matriz
     * @return -> numero de columnas de la matriz
     */
    private int getTotalCols(char[][] matrix) {
        return matrix[0].length;
    }

    /**
     * Metodo que nos devuelve el numero de filas
     * 
     * @param matrix -> matriz
     * @return -> numero de filas de la matriz
     */
    private int getTotalRows(char[][] matrix) {
        return matrix.length;
    }

    /**
     * Metodo que nos devuelve una copia de la matriz pasada
     * 
     * @param originalMatrix -> matriz original
     * @return -> copia de la matriz
     */
    private char[][] matrixCopy(char[][] originalMatrix) {

        char[][] copiedMatrix = new char[getTotalRows(originalMatrix)][getTotalCols(originalMatrix)];

        for (int row = 0; row < getTotalRows(originalMatrix); row++) {
            for (int col = 0; col < getTotalCols(originalMatrix); col++) {
                copiedMatrix[row][col] = originalMatrix[row][col];
            }
        }

        return copiedMatrix;

    }

    /**
     * Metodo que nos devuelve la matriz resultante despues de hacer los movimientos
     * 
     * @return -> matriz resultante
     */
    protected char[][] getBoardResult() {
        char[][] boardResult = new char[gameBoardResult.length][gameBoardResult[0].length];

        for (int row = 0; row < gameBoardResult.length; row++) {
            for (int col = 0; col < gameBoardResult[0].length; col++) {
                boardResult[row][col] = gameBoardResult[row][col];
            }
        }

        return boardResult;
    }

    /**
     * Metodo que nos devuelve la lista de movimientos realizados
     * 
     * @return -> lista de movimientos
     */
    protected List<String> getResultListSolver() {
        return resultListSolver;
    }

}
