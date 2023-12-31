
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

    private List<String> resultList;
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
        this.resultList = new ArrayList<>();
        this.resultListSolver = new ArrayList<>();

    }

    // SolverHelper() {
    // this.actualSolutions = new ArrayList<>();
    // this.bestSolutions = new ArrayList<>();
    // actualGame = 1;
    // }

    // public void setGameBoard(char[][] actualGameBoard) {
    // this.gameBoard = new char[actualGameBoard.length][actualGameBoard[0].length];
    // for (int i = 0; i < actualGameBoard.length; i++) {
    // for (int j = 0; j < actualGameBoard[0].length; j++) {
    // this.gameBoard[i][j] = actualGameBoard[i][j];
    // }
    // }

    // }

    /***
     * Metodo principal del juego
     */
    protected void play() {

        // while (this.actualGame <= this.totalGames) {
        // Se obtiene el tablero de chars
        // System.out.println("Lista antes de sacaer:" + boardLists);

        // Metodo que calcula las soluciones
        List<int[]> posibleMoves = new ArrayList<>();
        checkMoves(gameBoard, posibleMoves);

        findSolutions4Board(this.gameBoard, posibleMoves);

        String result = printActualGameSolution(actualGame, gameBoard);
        String resultSolver = printActualGameSolutionForSolver(actualGame, gameBoard);

        resultList.add(result);
        resultListSolver.add(resultSolver);
        // resultListPosiblesMoves.add(coords);
        gameBoardResult = matrixCopy(gameBoard);

        gameBoard = null;
        bestScore = 0;
        actualSolutions.clear();
        bestSolutions.clear();

    }

    protected void printMatrxz() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }
    }

    protected String printActualGameSolution(int actualGame, char[][] boardGameP) {
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
        // System.out.println(sb);
        return sb.toString();
    }

    protected String printActualGameSolutionForSolver(int actualGame, char[][] boardGameP) {
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

        // if (!isLastGame) {
        // sb.append("\n");
        // }
        // System.out.println(sb);
        return sb.toString();
    }

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

    protected boolean isGroupByPos(int row, int col, char[][] boardGameP, char actualColor) {
        return isGroupByPosWithRec(boardGameP, row - 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row + 1, col, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col - 1, actualColor) ||
                isGroupByPosWithRec(boardGameP, row, col + 1, actualColor);
    }

    protected static boolean isGroupByPosWithRec(char[][] boardGameP, int row, int col, char actualColor) {
        return row >= 0 && row < boardGameP.length &&
                col >= 0 && col < boardGameP[0].length &&
                boardGameP[row][col] == actualColor;
    }

    protected void checkMovesWithRec(char[][] boardGameP, int row, int col, char actualPiece,
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

    protected void findSolutions4Board(char[][] boardGameP, List<int[]> posibleMoves) {

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

    protected void checkSolutions(char[][] boardGameP) {
        int finalScore = getTotalMoveScore(boardGameP);

        if (this.bestScore < finalScore) {
            this.bestScore = finalScore;
            this.bestSolutions.clear();
            this.bestSolutions.addAll(this.actualSolutions);
        }

    }

    protected int getTotalMoveScore(char[][] boardGameP) {
        int finalMoveScore = 0;

        for (int i = 0; i < this.actualSolutions.size(); i++) {
            int[] matrixSol = this.actualSolutions.get(i);
            int numPointsMv = getPointWithDeletedPieces(boardGameP, matrixSol[2]);
            finalMoveScore += numPointsMv;
        }

        finalMoveScore += (getLeftPieces(boardGameP) == 0) ? 1000 : 0;

        return finalMoveScore;
    }

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

    protected int removeGroupRec(char[][] boardGameP, int row, int col, char color, boolean[][] visited) {
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

            stack.push(new int[] { row - 1, col }); // Check UP
            stack.push(new int[] { row + 1, col }); // Check Down
            stack.push(new int[] { row, col - 1 }); // Check Left
            stack.push(new int[] { row, col + 1 }); // Check Right

            boardGameP[row][col] = '-';
        }

        return numPiecesRem;

        // // System.out.println("Matrzi movida");
        // // for (int i = 0; i < boardGameP.length; i++) {
        // // for (int j = 0; j < boardGameP[0].length; j++) {
        // // System.out.println(boardGameP[i][j]);
        // // }
        // // System.out.println();
        // // }
        // boardGameP[row][col] = '-';
        // getPiecesDown(boardGameP);
        // movePiecesCol(boardGameP);

        // // gameBoardResult = matrixCopy(boardGameP);
        // return cont + 1;
    }

    protected char[][] getPiecesDown(char[][] boardGameP) {
        for (int row = 0; row < getTotalRows(boardGameP) - 1; row++) {
            for (int col = 0; col < getTotalCols(boardGameP); col++) {
                if (boardGameP[row + 1][col] == '-' && boardGameP[row][col] != '-') {
                    boardGameP[row + 1][col] = boardGameP[row][col];
                    boardGameP[row][col] = '-';
                    getPiecesDown(boardGameP);
                }

            }
        }

        return boardGameP;

    }

    protected char[][] movePiecesCol(char[][] boardGameP) {
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

        return boardGameP;
    }

    protected void changeCols(char[][] boardGameP, int numEmptyPieces) {
        for (int colLast = numEmptyPieces; colLast < getTotalCols(boardGameP) - 1; colLast++) {
            for (int colFirst = 0; colFirst < getTotalRows(boardGameP) - 1; colFirst++) {
                boardGameP[colFirst][colLast] = boardGameP[colFirst][colLast + 1];
                boardGameP[colFirst][colLast + 1] = '-';
            }
        }
    }

    protected int getTotalCols(char[][] matrix) {
        return matrix[0].length;
    }

    protected int getTotalRows(char[][] matrix) {
        return matrix.length;
    }

    protected char[][] matrixCopy(char[][] originalMatrix) {

        char[][] copiedMatrix = new char[getTotalRows(originalMatrix)][getTotalCols(originalMatrix)];

        for (int row = 0; row < getTotalRows(originalMatrix); row++) {
            for (int col = 0; col < getTotalCols(originalMatrix); col++) {
                copiedMatrix[row][col] = originalMatrix[row][col];
            }
        }

        return copiedMatrix;

    }

    protected char[][] getBoardResult() {
        char[][] boardResult = new char[gameBoardResult.length][gameBoardResult[0].length];

        for (int row = 0; row < gameBoardResult.length; row++) {
            for (int col = 0; col < gameBoardResult[0].length; col++) {
                boardResult[row][col] = gameBoardResult[row][col];
            }
        }

        return boardResult;
    }

    protected List<String> getResultList() {
        return resultList;
    }

    protected List<String> getResultListSolver() {
        return resultListSolver;
    }

}
