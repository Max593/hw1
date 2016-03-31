/** <b>HOMEWORK 1</b>
 * <br>
 * L'homework ha come obiettivo la realizzazione di un framework per costruire
 * giochi. Essendo questo un obiettivo troppo ambizioso per un homework, ci
 * limiteremo a giochi da tavolo, cioè board games come gli Scacchi, Go, Othello e
 * similari. La struttura del sistema è pensata per poter essere agevolmente estesa
 * anche a giochi di altro tipo. La struttura è stata progettata per separare
 * l'implementazione dei giochi, cioè il loro comportamento di interazione con i
 * giocatori, dalla loro resa tramite una UI che potrebbe essere testuale o grafica.
 * La parte della UI non è contemplata dall'homework.
 * <br>
 * I javadoc delle interfacce, classi e metodi spiegano l'intento di ogni
 * definizione e specificano il comportamento che l'implementazione deve realizzare.
 *
 * Si consiglia di seguire nell'implementazione il seguente ordine:
 * la classe {@link hw1.game.board.Pos}, la classe {@link hw1.game.board.PieceModel},
 * i metodi di default dell'interfaccia {@link hw1.game.board.Board}, la classe
 * {@link hw1.game.board.Action}, la classe {@link hw1.game.board.Move}, i metodi di
 * default dell'interfaccia {@link hw1.game.board.GameRuler}, la classe
 * {@link hw1.game.util.BoardOct}, i metodi statici di {@link hw1.game.util.Utils},
 * la classe {@link hw1.game.util.RandPlayer}, la classe {@link hw1.games.Othello} e
 * la classe {@link hw1.games.OthelloFactory}.
 * <br>
 * La classe {@link hw1.test.PartialGrade} è riservata per effettuare il grade
 * parziale dell'homework. */
package hw1;