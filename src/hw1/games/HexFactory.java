package hw1.games;

import hw1.game.GameFactory;
import hw1.game.Param;
import hw1.game.board.GameRuler;
import hw1.game.board.PieceModel;

import java.util.List;

/** <b>L'IMPLEMENTAZIONE DI QUESTA CLASSE È LASCIATA COME FALCOLTATIVA.</b>
 * <br>
 * Una HexFactory è una fabbrica di {@link GameRuler} per giocare a Hex. Per le
 * regole di Hex si può consultare
 * <a href="https://it.wikipedia.org/wiki/Hex_(gioco)">Hex</a>. */
public class HexFactory implements GameFactory<GameRuler<PieceModel<PieceModel.Species>>> {
    @Override
    public String name() { return "Hex"; }

    @Override
    public int minPlayers() { return 2; }

    @Override
    public int maxPlayers() { return 2; }

    /** Si dovrebbe gestire un parametro di tipo Integer per determinare la
     * dimensione della board da 11x11 a 14x14, cioè i valori 11, 12, 13 e 14. */
    @Override
    public List<Param<?>> params() {
        throw new UnsupportedOperationException("OPZIONALE");
    }

    @Override
    public void setPlayerNames(String... names) {
        throw new UnsupportedOperationException("OPZIONALE");
    }

    @Override
    public GameRuler<PieceModel<PieceModel.Species>> newGame() {
        throw new UnsupportedOperationException("OPZIONALE");
    }
}
