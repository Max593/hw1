package hw1.game;

import java.util.List;

/** Un Param gestisce il valore di un parametro di un gioco. Ad esempio le
 * dimensioni della board, la variante del gioco, l'handicap.
 * @param <T>  il tipo dei valori del parametro */
public interface Param<T> {
    /** Ritorna il nome del parametro che lo identifica.
     * @return il nome del parametro */
    String name();

    /** Ritorna la stringa di prompt del parametro, cioè una stringa che spiega il
     * significato del parametro e può essere usata in una UI.
     * @return la stringa di prompt del parametro */
    String prompt();

    /** Ritorna la lista dei possibili valori del parametro. La lista ritornata
     * deve sempre essere la stessa.
     * @return la lista dei possibili valori del parametro */
    List<T> values();

    /** Imposta il valore del parametro tramite l'indice nella lista dei valori
     * ritornata dal metodo {@link hw1.game.Param#values()}.
     * @param i  indice del valore nella lista dei valori
     * @throws IllegalStateException se l'indice è fuori range */
    void set(int i);

    /** @return il valore del parametro */
    T get();
}
