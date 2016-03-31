package hw1.game.board;

import java.util.Objects;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi nè i campi pubblici.</b>
 * <br>
 * Un oggetto Pos rappresenta una posizione in una {@link Board}. Gli oggetti
 * Pos sono immutabili. Per il significato delle coordinate vedere {@link Board} e
 * la documentazione relative agli elementi di {@link Board.System}. */
public class Pos {
    /** Le coordinate della posizione, b è la coordinata rispetto all'asse base
     * e t quella relativa all'asse trasversale. */
    public final int b, t;

    /** Crea una posizione con le coordinate date.
     * @param b  coordinata asse base (non negativa)
     * @param t  coordinata asse trasversale (non negativa)
     * @throws IllegalArgumentException se una delle coordinate è nagativa */
    public Pos(int b, int t) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        this.b = b;
        this.t = t;
        if(b<0 || t<0) { throw new IllegalArgumentException("Una delle coordinate è negativa"); }
    }

    /**
     * Get per recuperare b e T.
     * @return b e t su richiesta
     */
    public int getB(){ return b; }
    public int getT(){ return t; }

    /** Questa posizione è uguale a x se e solo se x è della classe {@link Pos}
     * ed ha le stesse coordinate.
     * @param x un oggetto (o null)
     * @return true se x è uguale a questa posizione */
    @Override
    public boolean equals(Object x) {
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        if(x instanceof Pos && ((Pos) x).getB() == b && ((Pos) x).getT() == t){ return true; }
        return false;
    }

    /** Ridefinito coerentemente con la ridefinizione di {@link Pos#equals(Object)}.
     * @return l'hash code di questa posizione */
    @Override
    public int hashCode() { //Tentativo di risoluzione!! compongo le coordinate e le uso come hascode
        /*throw new UnsupportedOperationException("DA IMPLEMENTARE");*/
        String c = String.valueOf(b)+String.valueOf(t);
        return Integer.valueOf(c);
    }
}
