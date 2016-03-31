package hw1.games;

import hw1.game.GameFactory;
import hw1.game.Param;
import hw1.game.board.GameRuler;
import hw1.game.board.PieceModel;

import java.util.Collections;
import java.util.List;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una OthelloFactory è una fattoria di {@link GameRuler} per giocare a Othello.
 * I {@link GameRuler} fabbricati dovrebbero essere oggetti {@link Othello}. */
public class OthelloFactory implements GameFactory<GameRuler<PieceModel<PieceModel.Species>>> {
    /** Crea una fattoria di {@link GameRuler} per giocare a Othello */
    public OthelloFactory() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public String name() { return "Othello"; }

    @Override
    public int minPlayers() { return 2; }

    @Override
    public int maxPlayers() { return 2; }

    @Override
    @SuppressWarnings("unchecked")
    public List<Param<?>> params() { return Collections.EMPTY_LIST; }

    @Override
    public void setPlayerNames(String... names) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public GameRuler<PieceModel<PieceModel.Species>> newGame() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }
}
