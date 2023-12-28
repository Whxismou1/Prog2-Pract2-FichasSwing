package swingfichas;

import java.util.Stack;

import javax.swing.JButton;

public class RedoAndUndo {

    private Stack<char[][]> undoStack;
    private Stack<char[][]> redoStack;

    public RedoAndUndo() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void push(char[][] board) {
        undoStack.push(cloneBoard(board));
        redoStack.clear(); // Al realizar una nueva acción, se debe limpiar el stack de redo
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(cloneBoard(undoStack.pop()));
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(cloneBoard(redoStack.pop()));
        }
    }

    public char[][] getCurrentBoard() {
        if (!undoStack.isEmpty()) {
            return cloneBoard(undoStack.peek());
        }
        return null; // Puedes manejar esto según tus necesidades
    }

    private char[][] cloneBoard(char[][] original) {
        int rows = original.length;
        int cols = original[0].length;
        char[][] clone = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                clone[i][j] = original[i][j];
            }
        }
        return clone;
    }

    protected void resetUndoAndRedo() {
        undoStack.clear();
        redoStack.clear();
    }

}