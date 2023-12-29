/***
 * Clase encargada de guardar y importar los tableros
 */

package swingfichas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileHandler {

    private JFileChooser fileChooser;

    FileHandler() {
        fileChooser = new JFileChooser();

    }

    /**
     * Metodo encargado de guardar el tablero en un archivo
     * 
     * @param currentBoard -> tablero actual
     */
    protected void saveToFile(char[][] currentBoard) {
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (int i = 0; i < currentBoard.length; i++) {
                    for (int j = 0; j < currentBoard[0].length; j++) {
                        writer.print(currentBoard[i][j]);
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

    /**
     * Metodo encargado de importar el tablero desde un archivo
     */
    protected char[][] loadGame() {
        fileChooser.setDialogTitle("Load game");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userChoice = fileChooser.showOpenDialog(null);

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileSelected = fileChooser.getSelectedFile();
            return getBoardFromFile(fileSelected);
        } else {
            return null;
        }

    }

    /**
     * Metodo encargado de leer el archivo y cargar el tablero
     * 
     * @param fileSelected -> archivo seleccionado
     * @return -> tablero cargado
     */
    private char[][] getBoardFromFile(File fileSelected) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileSelected));

            // Contar las filas y columnas del tablero
            int numRows = 0;
            int numCols = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                numRows++;
                // Suponemos que todas las filas tienen la misma longitud
                if (numCols == 0) {
                    numCols = line.trim().length();
                }
            }

            // Volver a leer el archivo para cargar el tablero
            char[][] loadedBoard = new char[numRows][numCols];
            reader.close();
            reader = new BufferedReader(new FileReader(fileSelected));

            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                char[] rowChars = line.trim().toCharArray();
                for (int i = 0; i < rowChars.length; i++) {
                    loadedBoard[currentRow][i] = rowChars[i];
                }
                currentRow++;
            }

            if (isValid(loadedBoard)) {
                return loadedBoard;
            } else {
                // Manejar el error según tus necesidades (por ejemplo, mostrar un mensaje de
                // error)
                JOptionPane.showMessageDialog(null, "ERROR: Tablero invalido en importar");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private boolean isValid(char[][] loadedBoard) {
        if (loadedBoard == null) {
            return false;
        }

        int numRows = loadedBoard.length;
        int numCols = (numRows > 0) ? loadedBoard[0].length : 0;

        // Comprobar que el número de filas esté en el rango [1, 20]
        if (numRows < 1 || numRows > 20) {
            return false;
        }

        if (numCols < 1 || numCols > 20) {
            return false;
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char actualChar = loadedBoard[i][j];
                if (actualChar != 'R' && actualChar != 'V' && actualChar != 'A') {
                    return false;
                }
            }
        }

        return true;
    }

    protected void saveGameSolution(List<String> wazza) {
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (String move : wazza) {
                    writer.println(move);
                }
                JOptionPane.showMessageDialog(null, "Game solution saved to " + fileChooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving game solution to file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Save canceled by user.");
        }
    }

    protected void saveMyGameSolution(List<List<Integer>> actualMoves, int totalPuntos, int leftPieces) {
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {

                writer.println("Juego 1:");

                for (int i = 0; i < actualMoves.size(); i++) {
                    List<Integer> move = actualMoves.get(i);
                    int row = move.get(0);
                    int col = move.get(1);
                    int piecesDeleted = move.get(2);
                    int points = move.get(3);
                    char color = (char) (int) move.get(4);

                    writer.println("Movimiento " + (i + 1) + " en (" + row + ", " + col + "): eliminó " +
                            piecesDeleted + " fichas de color " + color + " y obtuvo " +
                            points + " punto" + (points > 1 ? "s" : "") + ".");
                }

                writer.println("Puntuación final: " + totalPuntos + ", quedando " +
                        leftPieces + " fichas.");

                JOptionPane.showMessageDialog(null, "Game solution saved to " + fileChooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving game solution to file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Save canceled by user.");
        }
    }

}
