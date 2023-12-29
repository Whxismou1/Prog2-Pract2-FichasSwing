package swingfichas;
// ...

public class RedoAndUndo {
    private char[][] boardState;
    private int totalPoints;
    private int totalPiecesDeleted;

    public RedoAndUndo(char[][] boardState, int totalPoints, int totalPiecesDeleted) {
        this.boardState = boardState;
        this.totalPoints = totalPoints;
        this.totalPiecesDeleted = totalPiecesDeleted;
    }

    public char[][] getBoardState() {
        return boardState;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalPiecesDeleted() {
        return totalPiecesDeleted;
    }
}
