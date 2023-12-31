/***
 * Clase encargada de exportar e importar los tableros
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
     * Metodo encargado de exportar el tablero en un archivo
     * 
     * @param currentBoard -> tablero actual
     */
    protected void saveToFile(char[][] currentBoard) {
        int userOption = fileChooser.showSaveDialog(null);

        if (userOption == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter wrt = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (int i = 0; i < currentBoard.length; i++) {
                    for (int j = 0; j < currentBoard[0].length; j++) {
                        wrt.print(currentBoard[i][j]);
                    }
                    wrt.println();
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

        int userOption = fileChooser.showOpenDialog(null);

        if (userOption == JFileChooser.APPROVE_OPTION) {
            File fileSelected = fileChooser.getSelectedFile();
            return getBoardFromFile(fileSelected);
        } else {
            return null;
        }

    }

    /**
     * Metodo auxiliar encargado de leer el archivo y cargar el tablero
     * 
     * @param fileSelected -> archivo seleccionado
     * @return -> tablero cargado o null si no es valido
     */
    private char[][] getBoardFromFile(File fileSelected) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(fileSelected));

            // Contar las filas y columnas del tablero
            int numRows = 0;
            int numCols = 0;

            String line;
            while ((line = rd.readLine()) != null) {
                numRows++;
                // Suponemos que todas las filas tienen la misma longitud
                if (numCols == 0) {
                    numCols = line.trim().length();
                }
            }

            // Volver a leer el archivo para cargar el tablero
            char[][] importedBoard = new char[numRows][numCols];
            rd.close();
            rd = new BufferedReader(new FileReader(fileSelected));

            int row = 0;
            while ((line = rd.readLine()) != null) {
                char[] rowLine = line.trim().toCharArray();
                for (int i = 0; i < rowLine.length; i++) {
                    importedBoard[row][i] = rowLine[i];
                }
                row++;
            }

            if (isValid(importedBoard)) {
                return importedBoard;
            } else {

                JOptionPane.showMessageDialog(null, "ERROR: Invalid board in import");
                return null;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Invalid board in import");

            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: Invalid board in import");
            return null;
        }
    }

    /**
     * Metodo auxiliar encargado de validar el tablero importado
     * 
     * @param importedBoard -> tablero importado a comprobar
     * @return -> true si es valido, false en caso contrario
     */
    private boolean isValid(char[][] importedBoard) {
        if (importedBoard == null) {
            return false;
        }

        int numRows = importedBoard.length;
        int numCols = (numRows > 0) ? importedBoard[0].length : 0;

        // Comprobar que el número de filas esté en el rango [1, 20]
        if (numRows < 1 || numRows > 20) {
            return false;
        }

        if (numCols < 1 || numCols > 20) {
            return false;
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char actualChar = importedBoard[i][j];
                if (actualChar != 'R' && actualChar != 'V' && actualChar != 'A') {
                    return false;
                }
            }
        }

        return true;
    }

    protected void saveGameSolution(List<String> movesMaked) {
        int userOption = fileChooser.showSaveDialog(null);

        if (userOption == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter wrt = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (String move : movesMaked) {
                    wrt.println(move);
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
        int userOption = fileChooser.showSaveDialog(null);

        if (userOption == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter wrt = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {

                wrt.println("Juego 1:");

                for (int i = 0; i < actualMoves.size(); i++) {
                    List<Integer> move = actualMoves.get(i);
                    int row = move.get(0);
                    int col = move.get(1);
                    int piecesDeleted = move.get(2);
                    int points = move.get(3);
                    char color = (char) (int) move.get(4);

                    wrt.println("Movimiento " + (i + 1) + " en (" + row + ", " + col + "): eliminó " +
                            piecesDeleted + " fichas de color " + color + " y obtuvo " +
                            points + " punto" + (points > 1 ? "s" : "") + ".");
                }

                wrt.println("Puntuación final: " + totalPuntos + ", quedando " +
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
