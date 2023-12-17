package swingfichas;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class RedoAndUndo {
    private ConcurrentHashMap<Integer, GmeState> previusState;
    private ConcurrentHashMap<Integer, GmeState> nextState;

    protected RedoAndUndo() {
        previusState = new ConcurrentHashMap<>();
        nextState = new ConcurrentHashMap<>();
    }

    protected boolean addPrevState(GmeState gameState) {
        GmeState put = previusState.put(null, gameState);
        return (put != null);
    }

    protected boolean addNextState(GmeState gameState) {
        GmeState put = previusState.put(null, gameState);
        return (put != null);
    }

    protected void undoState() {

    }

    protected Collection<GmeState> getPreviusState() {
        return previusState.values();
    }

    protected Collection<GmeState> getNextState() {
        return nextState.values();
    }

    protected boolean canUndo() {
        return !previusState.isEmpty();
    }

    protected boolean canRedo() {
        return !nextState.isEmpty();
    }

    public class GmeState {

    }

}
