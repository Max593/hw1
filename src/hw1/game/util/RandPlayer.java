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
    public String name;
    public GameRuler<P> gameRul;
    public Move<P> mov; //Mossa che deve fare il player

    /** Crea un giocatore random, capace di giocare a un qualsiasi gioco, che ad
     * ogni suo turno fa una mossa scelta in modo random tra quelle valide.
     * @param name  il nome del giocatore random
     * @throws NullPointerException se name è null */
    public RandPlayer(String name) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(name == null) { throw new NullPointerException("Il nome del player non può essere null"); }
        this.name = name;
        this.gameRul = null;
        this.mov = null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setGame(GameRuler<P> g) { gameRul = g; } //Oppure g.copy()?

    @Override
    public void moved(int i, Move<P> m) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(gameRul == null || gameRul.result() >= 0) { throw new IllegalStateException("Il gioco non è impostato o è terminato"); }
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(i < 1 || i > gameRul.players().size()
                || !gameRul.isValid(m)) { throw new IllegalArgumentException("Indice di turnazione non consentito o mossa non valida"); }
        gameRul.move(m);
        if(i == gameRul.turn()) { mov = m; } //Se è il turno del giocatore salva la mossa
    }

    @Override
    public Move<P> getMove() {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(gameRul == null || gameRul.result() >= 0 || gameRul.players().indexOf(name)+1 != gameRul.turn()) {
            throw new IllegalStateException("Il non può essere null, terminato o non è il turno di questo giocatore"); }
        return mov;
    }
}
