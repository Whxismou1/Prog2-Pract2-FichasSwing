package swingfichas;

import java.util.ArrayList;
import java.util.List;

public class SolverHelper {
    int totalGames;
    // Juego actual
    int actualGame;
    private int bestScore;
    private List<int[]> actualSolutions;
    private List<int[]> bestSolutions;

    // Matriz donde se guarda el tablero de chars
    private char[][] gameBoard;

    SolverHelper() {

    }

    /***
     * Metodo principal del juego
     */
    private void play() {

        // while (this.actualGame <= this.totalGames) {
        // Se obtiene el tablero de chars
        // System.out.println("Lista antes de sacaer:" + boardLists);

        // Metodo que calcula las soluciones
        List<int[]> posibleMoves = new ArrayList<>();
        checkMoves(gameBoard, posibleMoves);

        findSolutions4Board(this.gameBoard, posibleMoves);

        printActualGameSolution(actualGame, gameBoard);
        gameBoard = null;
        bestScore = 0;
        actualSolutions.clear();

        bestSolutions.clear();

        // se incrementa el juego
        // this.actualGame++;
        // }

    }

    private void printActualGameSolution(int actualGame, char[][] boardGameP) {
        StringBuilder sb = new StringBuilder();
        sb.append("Juego " + actualGame + ":" + "\n");

        int finalScore = 0;
        for (int i = 0; i < this.bestSolutions.size(); i++) {
            int[] arrSolutions = this.bestSolutions.get(i);

            int movesScore = getPointWithDeletedPieces(boardGameP, arrSolutions[2]);

            finalScore += movesScore;

            int row = arrSolutions[0];
            int col = arrSolutions[1];

            removeGroup(boardGameP, row, col);
            if (movesScore == 1) {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions[0])
                        .append(", ")
                        .append(arrSolutions[1] + 1).append("): eliminó ").append(arrSolutions[2])
                        .append(" fichas de color ")
                        .append((char) arrSolutions[3]).append(" y obtuvo ").append(movesScore)
                        .append(" punto.\n");
            } else {
                sb.append("Movimiento ").append(i + 1).append(" en (")
                        .append(getTotalRows(boardGameP) - arrSolutions[0])
                        .append(", ")
                        .append(arrSolutions[1] + 1).append("): eliminó ").append(arrSolutions[2])
                        .append(" fichas de color ")
                        .append((char) arrSolutions[3]).append(" y obtuvo ").append(movesScore)
                        .append(" puntos.\n");
            }

        }

        finalScore += (getLeftPieces(boardGameP) == 0) ? 1000 : 0;

        if (getLeftPieces(boardGameP) == 1) {
            sb.append("Puntuación final: ").append(finalScore).append(", quedando ")
                    .append(getLeftPieces(boardGameP))
                    .append(" ficha.");
        } else {
            sb.append("Puntuación final: ").append(finalScore).append(", quedando ")
                    .append(getLeftPieces(boardGameP))
                    .append(" fichas.");
        }
        // if (!isLastGame) {
        // sb.append("\n");
        // }
        System.out.println(sb);
    }

    private void checkMoves(char[][] boardGameP, List<int[]> posibleMoves) {
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

    private boolean isGroupByPos(int row, int col, char[][] boardGameP, char actualColor) {
        return isGroupByPosWithRec(boardGameP, row - 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row + 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col - 1, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col + 1, actualColor);
    }

    private static boolean isGroupByPosWithRec(char[][] boardGameP, int row, int col, char actualColor) {
        return row >= 0 && row < boardGameP.length &&
                col >= 0 && col < boardGameP[0].length &&
                boardGameP[row][col] == actualColor;
    }

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

    private void checkSolutions(char[][] boardGameP) {
        int finalScore = getTotalMoveScore(boardGameP);

        if (this.bestScore < finalScore) {
            this.bestScore = finalScore;
            this.bestSolutions.clear();
            this.bestSolutions.addAll(this.actualSolutions);
        }

    }

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

    private int getPointWithDeletedPieces(char[][] boardGameP, int deletedPieces) {
        return (deletedPieces - 2) * (deletedPieces - 2);
    }

    /***
     * Metodo que devuelve el numero de piezas restante
     * 
     */
    private int getLeftPieces(char[][] boardGameP) {
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
    private int removeGroup(char[][] boardGameP, int row, int col) {
        char actualColor = boardGameP[row][col];

        boolean[][] visited = new boolean[getTotalRows(boardGameP)][getTotalCols(boardGameP)];

        return removeGroupRec(boardGameP, row, col, actualColor, visited);

    }

    private int removeGroupRec(char[][] boardGameP, int row, int col, char color, boolean[][] visited) {
        if (row < 0 || row >= boardGameP.length || col < 0 || col >= boardGameP[0].length || visited[row][col]
                || boardGameP[row][col] != color) {
            return 0;
        }

        visited[row][col] = true;

        int numPiecesRem = removeGroupRec(boardGameP, row - 1, col, color, visited); // Check Up
        numPiecesRem += removeGroupRec(boardGameP, row + 1, col, color, visited); // Check DOnw
        numPiecesRem += removeGroupRec(boardGameP, row, col - 1, color, visited); // Check Left
        numPiecesRem += removeGroupRec(boardGameP, row, col + 1, color, visited); // CheckRigth

        boardGameP[row][col] = '-';
        getPiecesDown(boardGameP);
        movePiecesCol(boardGameP);

        return numPiecesRem + 1;

    }

    private void getPiecesDown(char[][] boardGameP) {
        for (int row = 0; row < getTotalRows(boardGameP) - 1; row++) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (boardGameP[row + 1][col] == '-' && boardGameP[row][col] != '-') {
                    boardGameP[row + 1][col] = boardGameP[row][col];
                    boardGameP[row][col] = '-';
                    getPiecesDown(boardGameP);
                }

            }
        }

    }

    private void movePiecesCol(char[][] boardGameP) {
        for (int row = 0; row < getTotalCols(boardGameP); row++) {
            int numEmptyPieces = 0;
            for (int col = 0; col < getTotalRows(boardGameP); col++) {
                if (boardGameP[col][row] == '-') {
                    numEmptyPieces++;
                }
            }
            if (numEmptyPieces == getTotalCols(boardGameP)) {
                changeCols(boardGameP, numEmptyPieces);
            }
        }

    }

    private void changeCols(char[][] boardGameP, int numEmptyPieces) {
        for (int colLast = numEmptyPieces; colLast < getTotalCols(boardGameP) - 1; colLast++) {
            for (int colFirst = 0; colFirst < getTotalRows(boardGameP) - 1; colFirst++) {
                boardGameP[colFirst][colLast] = boardGameP[colFirst][colLast + 1];
                boardGameP[colFirst][colLast + 1] = '-';
            }
        }
    }

    private int getTotalCols(char[][] matrix) {
        return matrix[0].length;
    }

    private int getTotalRows(char[][] matrix) {
        return matrix.length;
    }

    private char[][] matrixCopy(char[][] originalMatrix) {

        char[][] copiedMatrix = new char[getTotalRows(originalMatrix)][getTotalCols(originalMatrix)];

        for (int row = 0; row < getTotalRows(originalMatrix); row++) {
            for (int col = 0; col < getTotalCols(originalMatrix); col++) {
                copiedMatrix[row][col] = originalMatrix[row][col];
            }
        }

        return copiedMatrix;

    }
}
