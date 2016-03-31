package hw1.game.util;

import hw1.game.board.GameRuler;
import hw1.game.board.Move;
import hw1.game.board.Player;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto RandPlayer è un oggetto che può giocare un qualsiasi gioco regolato
 * da un {@link GameRuler} perché, ad ogni suo turno, sceglie in modo random una
 * mossa tra quelle valide.
 = @param <P>  tipo del modello dei pezzi */
public class RandPlayer<P> implements Player<P> {
    /** Crea un giocatore random, capace di giocare a un qualsiasi gioco, che ad
     * ogni suo turno fa una mossa scelta in modo random tra quelle valide.
     * @param name  il nome del giocatore random
     * @throws NullPointerException se name è null */
    public RandPlayer(String name) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public void setGame(GameRuler<P> g) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public void moved(int i, Move<P> m) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public Move<P> getMove() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }
}
