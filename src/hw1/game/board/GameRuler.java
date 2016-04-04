package hw1.game.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** <b>IMPLEMENTARE I METODI DI DEFAULT CON L'INDICAZIONE "DA IMPLEMENTARE" SECONDO
 * LE SPECIFICHE DATE NEI JAVADOC. Non modificare le intestazioni dei metodi e non
 * aggiungere metodi.</b>
 * <br>
 * Un oggetto GameRuler rappresenta un gioco, cioè una partita in un particolare
 * tipo di gioco, ad es. una partita a Scacchi. Un (oggetto) GameRuler "conosce" le
 * regole del gioco e fornisce in ogni momento la situazione attuale delle
 * disposizioni dei pezzi sulla board, a quale giocatore tocca muovere e gli
 * eventuali punteggi. Permette di effettuare le mosse dei giocatori controllandone
 * la validità fino al termine della partita. Inoltre un GameRuler fornisce anche
 * alcuni metodi di utilità come l'insieme di tutte le mosse valide in un qualsiasi
 * turno di gioco e la possibilità di fare l'undo delle mosse eseguite.
 * <br>
 * Inoltre un GameRuler è clonabile tramite il metodo {@link GameRuler#copy()} per
 * fornire copie del GameRuler ai giocatori (vedi {@link Player}).
 * @param <P>  tipo del modello dei pezzi */
public interface GameRuler<P> {
    /** Ritorna il nome del gioco. Ritorna sempre la stessa stringa.
     * @return il nome del gioco */
    String name();

    /** Ritorna il valore del parametro di nome name.
     * @param name  il nome di un parametro del gioco
     * @param c  la classe del valore del parametro
     * @param <T>  il tipo del valore del parametro
     * @return il valore del parametro di nome name
     * @throws NullPointerException se name o c è null
     * @throws IllegalArgumentException se non c'è un parametro con nome name
     * @throws ClassCastException se il tipo del valore del parametro è
     * incompatibile con la classe c */
    <T> T getParam(String name, Class<T> c);

    /** Ritorna la lista con i nomi dei giocatori in ordine di turnazione. La lista
     * ritornata è immodificabile ed è sempre la stessa. Se un giocatore è in
     * posizione i della lista, allora il suo indice di turnazione è i+1.
     * @return la lista con i nomi dei giocatori in ordine di turnazione */
    List<String> players();

    /** Il colore assegnato al giocatore name o null se il gioco non assegna
     * un colore ai giocatori (ad es. in una variante del gioco Hex).
     * @param name  il nome di un giocatore
     * @return il colore assegnato al giocatore name o null
     * @throws NullPointerException se name è null
     * @throws IllegalArgumentException se name non è il nome di un giocatore */
    String color(String name);

    /** Ritorna la board del gioco con la disposizione attuale dei pezzi. L'oggetto
     * Board ritornato è immodificabile ed è una view della Board del gioco, cioè i
     * cambiamenti operati dalle mosse dei giocatori sono riportate nell'oggetto
     * Board ritornato. Il metodo dovrebbe ritornare sempre lo stesso oggetto.
     * @return la board del gioco con la disposizione attuale dei pezzi */
    Board<P> getBoard();

    /** Ritorna l'indice di turnazione del giocatore che deve fare la prossima mossa
     * o zero se il gioco è terminato.
     * @return l'indice di turnazione del giocatore che deve fare la prossima mossa
     * o zero */
    int turn();

    /** Se m è una mossa valida per il giocatore di turno, esegue la mossa m
     * aggiornando lo stato del gioco, passando il turno al prossimo giocatore e
     * ritorna true. Se invece la mossa non è valida, ritorna false ma il
     * comportamento dipende dal gioco, in alcuni giochi la mossa è semplicemente
     * ignorata e il turno o passa al prossimo giocatore o rimane all'attuale in
     * attesa che faccia una mossa valida in altri giochi il giocatore è eliminato.
     * @param m  una mossa
     * @return true se la mossa è valida e false altrimenti
     * @throws NullPointerException se m è null
     * @throws IllegalStateException se il gioco è terminato */
    boolean move(Move<P> m);

    /** Fa l'undo dell'ultima mossa eseguita, cioè riporta il gioco alla situazione
     * precedente a quella dell'ultima mossa eseguita, e ritorna true. Se il gioco è
     * all'inizio, cioè non è stata eseguita alcuna mossa, ritorna false.
     * @return true se c'è una mossa di cui fare l'undo */
    boolean unMove();

    /** Ritorna true se il giocatore con indice di turnazione i è ancora in gioco.
     * Se il gioco è terminato, ritorna sempre false.
     * @param i  indice di turnazione di un giocatore
     * @return true se il giocatore con indice di turnazione i è ancora in gioco
     * @throws IllegalArgumentException se i non è l'indice di turnazione di alcun
     * giocatore */
    boolean isPlaying(int i);

    /** Ritorna l'esito del gioco, se non è ancora terminato ritorna -1, se è
     * terminato con una patta ritorna 0, altrimenti ritorna l'indice di turnazione
     * del giocatore che ha vinto.
     * @return l'esito del gioco */
    int result();

    /** Ritorna l'insieme delle mosse valide nell'attuale situazione di gioco. In
     * altre parole ritorna tutte le mosse che può fare il giocatore attualmente
     * di turno. L'insieme ritornato è immodificabile e quindi può essere lo stesso
     * ad ogni invocazione nella stessa situazione di gioco.
     * @return l'insieme delle mosse valide, mai null
     * @throws IllegalStateException se il gioco è terminato */
    Set<Move<P>> validMoves();

    /** Ritorna true se m è una mossa valida nell'attuale situazione di gioco.
     * L'implementazione di default usa {@link GameRuler#result()} e
     * {@link GameRuler#validMoves()}.
     * @param m  una mossa
     * @return true se m è una mossa valida
     * @throws NullPointerException se m è null
     * @throws IllegalStateException se il gioco è terminato */
    default boolean isValid(Move<P> m) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(m == null) { throw new NullPointerException("La mossa non può essere null"); }
        if(turn() == 0) { throw new IllegalStateException("Il gioco è terminato"); }
        for(Move i : validMoves()) { if(m.equals(i)) { return true; } }
        return false;
    }

    /** Ritorna l'insieme delle mosse valide relative alla posizione p. Se nella
     * posizione p c'è un (modello di) pezzo, ritorna tutte le mosse valide che
     * iniziano con un'azione di tipo {@link Action.Kind#MOVE} o
     * {@link Action.Kind#JUMP} che muove il pezzo che è in posizione p o che
     * iniziano con un'azione di tipo {@link Action.Kind#SWAP} che sostituisce il
     * pezzo in posizione p. Se invece nella posizione p non c'è un pezzo, ritorna
     * le mosse valide che iniziano con una azione di tipo {@link Action.Kind#ADD}
     * che aggiunge un pezzo nella posizione p. L'insieme ritornato è immodificabile.
     * L'implementazione di default usa {@link GameRuler#getBoard()} e
     * {@link GameRuler#validMoves()}.
     * @param p  una posizione
     * @return l'insieme delle mosse valide relative alla posizione p
     * @throws NullPointerException se p è null
     * @throws IllegalArgumentException se p non è una posizione della board
     * @throws IllegalStateException se il gioco è terminato */
    default Set<Move<P>> validMoves(Pos p) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(p == null) { throw new NullPointerException("La posizione non può essere null"); }
        if(!getBoard().isPos(p)) { throw new IllegalArgumentException("Il pezzo non si trova nella Board"); }
        if(turn() == 0) { throw new IllegalStateException("Il gioco è ormai terminato"); }
        Set<Move<P>> ris = new HashSet<>();

        if(getBoard().get(p) != null) { //Se la posizione della board contiene un pezzo
            for(Move i : validMoves()) {
                if(i.getKind().equals(Move.Kind.ACTION)) { //Solo se la mossa è di tipo Action
                    Action a = ((Action) i.getActions().get(0));
                    if(a.getKind().equals(Action.Kind.MOVE) || a.getKind().equals(Action.Kind.JUMP) ||
                            a.getKind().equals(Action.Kind.SWAP)) { ris.add(i); }
                }
            }
        }
        if(getBoard().get(p) == null) { //Se la posizione della board NON contiene un pezzo
            for(Move i : validMoves()) {
                if(i.getKind().equals(Move.Kind.ACTION)) { //Solo se la mossa è di tipo Action
                    Action a = ((Action) i.getActions().get(0));
                    if(a.getKind().equals(Action.Kind.ADD)) { ris.add(i); }
                }
            }
        }
        return Collections.unmodifiableSet(ris);
    }

    /** Ritorna il punteggio attuale del giocatore con indice di turnazione i.
     * Questo metodo è implementato solamente se il gioco prevede dei punteggi come
     * ad es. Go e Othello.
     * @param i  indice di turnazione di un giocatore
     * @return il punteggio attuale del giocatore con indice di turnazione i
     * @throws IllegalArgumentException se i non è l'indice di turnazione di alcun
     * giocatore
     * @throws UnsupportedOperationException se questo gioco non prevede punteggi */
    default double score(int i) { throw new UnsupportedOperationException("Questo gioco non ha punteggi"); }

    /** Ritorna una copia profonda (una deep copy) di questo GameRuler. Questo
     * significa che tutti i valori mutabili dei campi del GameRuler devono essere
     * clonati in modo profondo mentre i valori immutabili possono essere condivisi.
     * @return una copia profonda di questo GameRuler */
    GameRuler<P> copy();
}
