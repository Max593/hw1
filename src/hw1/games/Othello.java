package hw1.games;

import hw1.game.board.*;
import hw1.game.util.BoardOct;
import hw1.game.util.RandPlayer;
import hw1.game.util.Utils;

import java.util.*;

import static hw1.game.board.PieceModel.Species;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto Othello rappresenta un GameRuler per fare una partita a Othello. Il
 * gioco Othello si gioca su una board di tipo {@link Board.System#OCTAGONAL} 8x8.
 * Si gioca con pezzi o pedine di specie {@link Species#DISC} di due
 * colori "nero" e "bianco". Prima di inziare a giocare si posizionano due pedine
 * bianche e due nere nelle quattro posizioni centrali della board in modo da creare
 * una configurazione a X. Quindi questa è la disposzione iniziale (. rappresenta
 * una posizione vuota, B una pedina bianca e N una nera):
 * <pre>
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . B N . . .
 *     . . . N B . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 * </pre>
 * Si muove alternativamente (inizia il nero) appoggiando una nuova pedina in una
 * posizione vuota in modo da imprigionare, tra la pedina che si sta giocando e
 * quelle del proprio colore già presenti sulla board, una o più pedine avversarie.
 * A questo punto le pedine imprigionate devono essere rovesciate (da bianche a nere
 * o viceversa, azione di tipo {@link hw1.game.board.Action.Kind#SWAP}) e diventano
 * di proprietà di chi ha eseguito la mossa. È possibile incastrare le pedine in
 * orizzontale, in verticale e in diagonale e, a ogni mossa, si possono girare
 * pedine in una o più direzioni. Sono ammesse solo le mosse con le quali si gira
 * almeno una pedina, se non è possibile farlo si salta il turno. Non è possibile
 * passare il turno se esiste almeno una mossa valida. Quando nessuno dei giocatori
 * ha la possibilità di muovere o quando la board è piena, si contano le pedine e si
 * assegna la vittoria a chi ne ha il maggior numero. Per ulteriori informazioni si
 * può consultare
 * <a href="https://it.wikipedia.org/wiki/Othello_(gioco)">Othello</a> */
public class Othello implements GameRuler<PieceModel<Species>> {
    public final Player player1;
    public final Player player2;
    public Board<PieceModel<Species>> board;
    public int cT;

    /** Crea un GameRuler per fare una partita a Othello.
     * @param p1  il nome del primo giocatore
     * @param p2  il nome del secondo giocatore
     * @throws NullPointerException se p1 o p2 è null */
    public Othello(String p1, String p2) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        this.board = new BoardOct(8, 8);
        board.put(new PieceModel(Species.DISC, "bianco"), new Pos(4, 4));
        board.put(new PieceModel(Species.DISC, "nero"), new Pos(5, 4));
        board.put(new PieceModel(Species.DISC, "nero"), new Pos(4, 5));
        board.put(new PieceModel(Species.DISC, "bianco"), new Pos(5, 5));
        this.player1 = new RandPlayer<>(p1); //
        this.player2 = new RandPlayer<>(p2);
        for(Player i : Arrays.asList(player1, player2)) { i.setGame(copy()); } //Copia il gameruler ai giocatori
        this.cT = 1; //Inizia il player1
    }

    @Override
    public String name() { return "Othello"; }

    @Override
    public <T> T getParam(String name, Class<T> c) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(c);
        throw new IllegalArgumentException("Non ci sono parametri");
    }

    @Override
    public List<String> players() {
        return Arrays.asList(player1.name(), player2.name());
    }

    /** Assegna il colore "nero" al primo giocatore e "bianco" al secondo. */
    @Override
    public String color(String name) {
        if(name == null) { throw new NullPointerException("Il nome del player non può essere null"); }
        if(!players().contains(name)) { throw new IllegalArgumentException("Il player non è presente nel gioco"); }
        if(players().indexOf(name) == 0) { return "nero"; }
        return "bianco";
    }

    @Override
    public Board getBoard() { return Utils.UnmodifiableBoard(board); }

    /** Se il giocatore di turno non ha nessuna mossa valida il turno è
     * automaticamente passato all'altro giocatore. Ma se anche l'altro giocatore
     * non ha mosse valide, la partita termina. */
    @Override
    public int turn() {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(cT == 1 && validMoves().size() == 0) { cT = 2; }
        if(cT == 2 && validMoves().size() == 0) { cT = 1; }
        if(player1.getMove() == null && player2.getMove() == null) { cT = 0; }
        return cT;
    }

    @Override
    public boolean move(Move<PieceModel<Species>> m) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(result() > -1) { throw new IllegalStateException("Il gioco è già terminato"); }
        if(m.getKind() == Move.Kind.ACTION) {
            String c = "bianco";
            if(cT == 1) { c = "nero"; }
            for(Object i : m.getActions()) { //Le varie azioni
                if(((Action) i).getKind() == Action.Kind.ADD) {
                    board.put(new PieceModel<Species>(Species.DISC,c), (Pos) ((Action) i).getPos().get(0));
                }
                if(((Action) i).getKind() == Action.Kind.SWAP) {
                    for(Object p : ((Action) i).getPos()) {
                        board.put(new PieceModel<Species>(Species.DISC,c), (Pos) ((Action) i).getPos().get(0));
                    } } }
            if(cT == 1) { cT = 2; }
            cT = 1; return true; }
        return false; }

    @Override
    public boolean unMove() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public boolean isPlaying(int i) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public int result() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public Set<Move<PieceModel<Species>>> validMoves() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public double score(int i) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }

    @Override
    public GameRuler<PieceModel<Species>> copy() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE");
    }
}
