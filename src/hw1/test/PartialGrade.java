package hw1.test;

import hw1.game.GameFactory;
import hw1.game.Param;
import hw1.game.board.*;
import hw1.game.util.*;

import static hw1.game.board.Board.Dir;
import static hw1.game.board.PieceModel.Species;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/** Per calcolare il punteggio parziale dell'homework eseguire il main di questa
 * classe. Il punteggio è limitato ai primi 30 punti, i rimanenti 10 saranno
 * assegnati da un'altro grade eseguito dal docente dopo la consegna.
 * <br>
 * <b>ATTENZIONE: NON MODIFICARE IN ALCUN MODO QUESTA CLASSE.</b> */
public class PartialGrade {
    public static void main(String[] args) {
        test();
    }



    private static void test() {
        System.setOut(new PrintStream(OUTPUT));
        System.setIn(INPUT);
        OUTPUT.setThread(Thread.currentThread());
        OUTPUT.standardOutput(true);
        INPUT.setThread(Thread.currentThread());
        INPUT.standardInput(true);
        testing = true;
        testWord = null;
        totalScore = 0;
        boolean ok = test_Pos(1f, 500);
        if (ok) ok = test_PieceModel(1f, 500);
        if (ok) ok = test_BoardDef(4f, 500);
        if (ok) ok = test_Action(5f, 500);
        if (ok) ok = test_Move(3f, 500);
        if (ok) ok = test_GameRulerDef(4f, 500);
        if (ok) ok = test_BoardOct(5f, 1000);
        if (ok) ok = test_UnmodifiableBoard(3f, 500);
        if (ok) ok = test_PlayRandPlayer(4f, 1000);
        System.out.println("Punteggio parziale: "+totalScore);
        testing = false;
    }

    private static boolean test_Pos(float sc, int ms) {
        return runTest("Test Pos", sc, ms, () -> {
            try {
                Pos p1 = new Pos(12,13), p2 = new Pos(12,13), p3 = new Pos(12,11);
                if (p1.equals(null)) return new Result("ERRORE Pos.equals true invece di false");
                if (!p1.equals(p2)) return new Result("ERRORE Pos.equals false invece di true");
                if (p1.equals(p3)) return new Result("ERRORE Pos.equals true invece di false");
                if (p1.hashCode() != p2.hashCode()) new Result("ERRORE Pos.hashCode() diversi invece di uguali");
                boolean err = true;
                for (int i = 0 ; i < 13 ; i++)
                    if (p1.hashCode() != new Pos(i,i).hashCode()) err = false;
                if (err) new Result("ERRORE Pos.hashCode()");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_PieceModel(float sc, int ms) {
        return runTest("Test PieceModel", sc, ms, () -> {
            try {
                PieceModel<PieceModel.Species> pm1 = new PieceModel<>(PieceModel.Species.DAMA, "white"),
                        pm2 = new PieceModel<>(PieceModel.Species.DAMA, "white"),
                        pm3 = new PieceModel<>(PieceModel.Species.DAMA, "black");
                if (pm1.equals(null)) return new Result("ERRORE PieceModel.equals true invece di false");
                if (!pm1.equals(pm2)) return new Result("ERRORE PieceModel.equals false invece di true");
                if (pm1.equals(pm3)) return new Result("ERRORE PieceModel.equals true invece di false");
                if (pm1.hashCode() != pm2.hashCode()) new Result("ERRORE PieceModel.hashCode() diversi invece di uguali");
                boolean err = true;
                for (Species s : Species.values())
                    if (pm1.hashCode() != new PieceModel<>(s, "black").hashCode()) err = false;
                if (err) new Result("ERRORE PieceModel.hashCode()");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_BoardDef(float sc, int ms) {
        return runTest("Test Board default methods", sc, ms, () -> {
            try {
                DummyBoard<PieceModel<Species>> b = new DummyBoard<>();
                if (!b.isPos(new Pos(0,0))) return new Result("ERRORE Board.isPos false invece di true");
                if (b.isPos(new Pos(10,0))) return new Result("ERRORE Board.isPos true invece di false");
                try {
                    b.isPos(null);
                    return new Result("ERRORE Board.isPos(null): non lancia eccezione");
                } catch (Exception ex) {}
                Set<Pos> pSet = b.get();
                if (!pSet.isEmpty()) return new Result("ERRORE Board.get(): insieme non vuoto");
                try {
                    pSet.add(new Pos(0,0));
                    return new Result("ERRORE Board.get(): insieme modificabile");
                } catch (Exception ex) {}
                PieceModel<Species> pm = new PieceModel<>(Species.BISHOP, "white");
                Pos p = new Pos(2,3);
                b.put(pm, p);
                pSet = b.get();
                if (pSet.size() != 1 || !pSet.contains(p)) return new Result("ERRORE Board.get(): insieme errato");
                try {
                    PieceModel<Species> pm2 = null;
                    b.get(pm2);
                    return new Result("ERRORE Board.get(P pm) con pm == null non lancia eccezione");
                } catch (Exception ex) {}
                pSet = b.get(pm);
                try {
                    pSet.add(new Pos(0,0));
                    return new Result("ERRORE Board.get(P pm): insieme modificabile");
                } catch (Exception ex) {}
                if (pSet.size() != 1 || !pSet.contains(p)) return new Result("ERRORE Board.get(P pm): insieme errato");
                pSet = b.get(new PieceModel<>(Species.BISHOP, "black"));
                if (!pSet.isEmpty()) return new Result("ERRORE Board.get(P pm): insieme errato");
                try {
                    PieceModel<Species> pmNull = null;
                    notEx(() -> b.put(pmNull, new Pos(0,0), Dir.UP, 1), "con pm == null non lancia eccezione");
                    notEx(() -> b.put(pm, null, Dir.UP, 1), "con p == null non lancia eccezione");
                    notEx(() -> b.put(pm, new Pos(0,0), null, 1), "con d == null non lancia eccezione");
                    notEx(() -> b.put(pm, new Pos(0,0), Dir.UP, 0), "con n == 0 non lancia eccezione");
                    String err = "se una posizione della linea non è nella board non lancia eccezione";
                    notEx(() -> b.put(pm, new Pos(0,0), Dir.UP, 15), err);
                    notEx(() -> b.put(pm, new Pos(3,0), Dir.DOWN, 2), err);
                    notEx(() -> b.put(pm, new Pos(10,0), Dir.UP, 1), err);
                } catch (Exception ex) { return new Result("ERRORE Board.put(P pm,Pos p,Dir d,int n) "+ex.getMessage()); }
                b.put(new PieceModel<>(Species.KING, "white"), new Pos(1,2), Dir.UP_R, 5);
                pSet = b.get(new PieceModel<>(Species.KING, "white"));
                if (pSet.size() != 5) new Result("ERRORE Board.put(P pm,Pos p,Dir d,int n): insieme pos errato");
                for (Pos pp : new Pos[] {new Pos(1,2),new Pos(2,3),new Pos(3,4),new Pos(4,5),new Pos(5,6)})
                    if (!pSet.contains(pp)) new Result("ERRORE Board.put(P pm,Pos p,Dir d,int n): insieme pos errato");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_Action(float sc, int ms) {
        return runTest("Test Action", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                Pos p = new Pos(0,0), p2 = new Pos(1,1);
                PieceModel<Species> pmNull = null;
                PieceModel<Species> pm = new PieceModel<>(Species.KING, "black");
                Action<PieceModel<Species>> a = new Action<>(p, pm);
                if (a.kind != Action.Kind.ADD || !pm.equals(a.piece) || a.pos == null ||
                        a.pos.size() != 1 || !a.pos.contains(p))
                    return new Result("ERRORE Action(Pos p, P pm)");
                try {
                    notEx(() -> new Action<>(null, pm), "con p == null"+nE);
                    notEx(() -> new Action<>(new Pos(0,0), pmNull), "con pm == null"+nE);
                } catch (Exception ex) { return new Result("ERRORE Action(Pos p, P pm) "+ex.getMessage()); }
                Action<PieceModel<Species>> a1 = new Action<>(p, pm);
                Action<PieceModel<Species>> a2 = new Action<>(p, pm);
                if (!a1.equals(a2)) return new Result("ERRORE Action.equals false invece di true");
                PieceModel<Species> pm2 = new PieceModel<>(Species.PAWN, "black");
                Action<PieceModel<Species>> a3 = new Action<>(p, pm2);
                if (a2.equals(a3)) return new Result("ERRORE Action.equals true invece di false");
                boolean herr = true;
                for (Species s : Species.values())
                    if (a1.hashCode() != new Action<>(p, new PieceModel<>(s, "a")).hashCode()) herr = false;
                if (herr) new Result("ERRORE Action.hashCode()");

                a = new Action<>(p);
                if (a.kind != Action.Kind.REMOVE || a.piece != null || a.pos == null || a.pos.size() != 1 || !a.pos.contains(p))
                    return new Result("ERRORE Action(Pos...pp)");
                try {
                    Action<PieceModel<Species>> af = a;
                    notEx(() -> af.pos.add(p), "la lista pos è modificabile");
                    notEx(() -> new Action<>(), "con pp vuoto"+nE);
                    notEx(() -> new Action<>(p, p), "con pp che contiene duplicati"+nE);
                    notEx(() -> new Action<>(p, (Pos)null), "con pp che contiene elementi null"+nE);
                } catch (Exception ex) { return new Result("ERRORE Action(Pos...pp) "+ex.getMessage()); }
                a1 = new Action<>(p);
                a2 = new Action<>(p);
                if (!a1.equals(a2)) return new Result("ERRORE Action.equals false invece di true");
                a3 = new Action<>(p, new Pos(1,1));
                if (a2.equals(a3)) return new Result("ERRORE Action.equals true invece di false");

                a = new Action<>(Dir.RIGHT, 2, p);
                if (a.kind != Action.Kind.MOVE || a.piece != null || a.pos == null ||
                        a.pos.size() != 1 || !a.pos.contains(p) || a.dir != Dir.RIGHT || a.steps != 2)
                    return new Result("ERRORE Action(Dir d,int ns,Pos...pp)");
                try {
                    Action<PieceModel<Species>> af = a;
                    notEx(() -> af.pos.add(p), "la lista pos è modificabile");
                    notEx(() -> new Action<>(Dir.RIGHT, 2), "con pp vuoto"+nE);
                    notEx(() -> new Action<>(Dir.RIGHT, 2, p, p), "con pp che contiene duplicati"+nE);
                    notEx(() -> new Action<>(Dir.RIGHT, 2, p, null), "con pp che contiene elementi null"+nE);
                    notEx(() -> new Action<>(null, 2, p), "con d null"+nE);
                    notEx(() -> new Action<>(Dir.RIGHT, 0, p), "con ns < 1"+nE);
                } catch (Exception ex) { return new Result("ERRORE Action(Dir d,int ns,Pos...pp) "+ex.getMessage()); }
                a1 = new Action<>(Dir.UP, 3, p, p2);
                a2 = new Action<>(Dir.UP, 3, p, p2);
                if (!a1.equals(a2)) return new Result("ERRORE Action.equals false invece di true");
                a3 = new Action<>(Dir.UP, 3, p);
                if (a2.equals(a3)) return new Result("ERRORE Action.equals true invece di false");

                a = new Action<>(p, p2);
                if (a.kind != Action.Kind.JUMP || a.piece != null || a.pos == null ||
                        a.pos.size() != 2 || !p.equals(a.pos.get(0)) || !p2.equals(a.pos.get(1)))
                    return new Result("ERRORE Action(Pos p1,Pos p2)");
                try {
                    Action<PieceModel<Species>> af = a;
                    notEx(() -> af.pos.add(p), "la lista pos è modificabile");
                    notEx(() -> new Action<>((Pos)null, p2), "con p1 null"+nE);
                    notEx(() -> new Action<>(p, (Pos)null), "con p2 null"+nE);
                    notEx(() -> new Action<>(p, p), "con p1 == p2"+nE);
                } catch (Exception ex) { return new Result("ERRORE Action(Pos p1,Pos p2) "+ex.getMessage());}
                a1 = new Action<>(p, p2);
                a2 = new Action<>(p, p2);
                if (!a1.equals(a2)) return new Result("ERRORE Action.equals false invece di true");
                a3 = new Action<>(p, new Pos(4,1));
                if (a2.equals(a3)) return new Result("ERRORE Action.equals true invece di false");

                a = new Action<>(pm, p);
                if (a.kind != Action.Kind.SWAP || !pm.equals(a.piece) || a.pos == null ||
                        a.pos.size() != 1 || !p.equals(a.pos.get(0)))
                    return new Result("ERRORE Action(P pm,Pos...pp)");
                try {
                    Action<PieceModel<Species>> af = a;
                    notEx(() -> af.pos.add(p), "la lista pos è modificabile");
                    notEx(() -> new Action<>(pmNull, p), "con pm null"+nE);
                    notEx(() -> new Action<>(pm), "con pp vuotoe+nE");
                    notEx(() -> new Action<>(pm, p, null), "con pp che contiene elementi null"+nE);
                    notEx(() -> new Action<>(pm, p, p), "con pp che contiene duplicati"+nE);
                } catch (Exception ex) { return new Result("ERRORE Action(P pm,Pos...pp) "+ex.getMessage()); }
                a1 = new Action<>(pm, p, p2);
                a2 = new Action<>(pm, p, p2);
                if (!a1.equals(a2)) return new Result("ERRORE Action.equals false invece di true");
                a3 = new Action<>(pm, p, new Pos(4,1));
                if (a2.equals(a3)) return new Result("ERRORE Action.equals true invece di false");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_Move(float sc, int ms) {
        return runTest("Test Move", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                Pos p = new Pos(0,0), p2 = new Pos(2,2);
                Move.Kind kNull = null;
                PieceModel<Species> pm = new PieceModel<>(Species.KING, "black");
                Action<PieceModel<Species>> a = new Action<>(p, p2), a2 = new Action<>(new Pos(1,1));
                Move<PieceModel<Species>> m = new Move<>(Move.Kind.PASS);
                if (!Move.Kind.PASS.equals(m.kind) || m.actions == null || m.actions.size() != 0)
                    return new Result("ERRORE Move(Kind k)");
                try {
                    Move<PieceModel<Species>> mf = m;
                    notEx(() -> mf.actions.add(a), "la lista actions è modificabile");
                    notEx(() -> new Move<>(kNull), "con k null"+nE);
                    notEx(() -> new Move<>(Move.Kind.ACTION), "con k == ACTION"+nE);
                } catch (Exception ex) { return new Result("ERRORE Move(Kind k) "+ex.getMessage()); }
                Move<PieceModel<Species>> m1 = new Move<>(Move.Kind.RESIGN);
                Move<PieceModel<Species>> m2 = new Move<>(Move.Kind.RESIGN);
                if (!m1.equals(m2)) return new Result("ERRORE Move.equals false invece di true");
                Move<PieceModel<Species>> m3 = new Move<>(Move.Kind.PASS);
                if (m2.equals(m3)) return new Result("ERRORE Move.equals true invece di false");

                m = new Move<>(a, a2);
                if (!Move.Kind.ACTION.equals(m.kind) || m.actions == null ||
                        m.actions.size() != 2 || !a.equals(m.actions.get(0)) || !a2.equals(m.actions.get(1)))
                    return new Result("ERRORE Move(Action<P>...aa)");
                try {
                    Move<PieceModel<Species>> mf = m;
                    notEx(() -> mf.actions.add(a), "la lista actions è modificabile");
                    notEx(() -> new Move<>(a, null), "con aa che contiene un elemento null"+nE);
                    notEx(() -> new Move<>(), "con aa vuoto"+nE);
                } catch (Exception ex) { return new Result("ERRORE Move(Kind k) "+ex.getMessage()); }
                m1 = new Move<>(a, a2);
                m2 = new Move<>(a, a2);
                if (!m1.equals(m2)) return new Result("ERRORE Move.equals false invece di true");
                m3 = new Move<>(a);
                if (m2.equals(m3)) return new Result("ERRORE Move.equals true invece di false");

                List<Action<PieceModel<Species>>> aa = Arrays.asList(a, a2);
                m = new Move<>(aa);
                if (!Move.Kind.ACTION.equals(m.kind) || m.actions == null ||
                        m.actions.size() != 2 || !a.equals(m.actions.get(0)) || !a2.equals(m.actions.get(1)))
                    return new Result("ERRORE Move(List<Action<P>> aa)");
                aa.set(0,a2);
                if (!a.equals(m.actions.get(0)) || !a2.equals(m.actions.get(1)))
                    return new Result("ERRORE Move(List<Action<P>> aa): mantiene la lista aa");
                try {
                    Move<PieceModel<Species>> mf = m;
                    notEx(() -> mf.actions.add(a), "la lista actions è modificabile");
                    notEx(() -> new Move<>(a, null), "con aa che contiene un elemento null"+nE);
                    notEx(() -> new Move<>(), "con aa vuoto"+nE);
                } catch (Exception ex) { return new Result("ERRORE Move(Kind k) "+ex.getMessage()); }
                m1 = new Move<>(a, a2);
                m2 = new Move<>(a, a2);
                if (!m1.equals(m2)) return new Result("ERRORE Move.equals false invece di true");
                m3 = new Move<>(a);
                if (m2.equals(m3)) return new Result("ERRORE Move.equals true invece di false");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_GameRulerDef(float sc, int ms) {
        return runTest("Test GameRuler default methods", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                Pos p = new Pos(0,0), p2 = new Pos(2,2), pNull = null;
                DummyGU gR = new DummyGU("a", "b");
                Set<Move<PieceModel<Species>>> vm = gR.validMoves();
                Move<PieceModel<Species>> last = null;
                for (Move<PieceModel<Species>> m : vm) {
                    if (!gR.isValid(m))
                        return new Result("ERRORE isValid(m) ritorna false invece di true");
                    last = m;
                }
                vm = gR.validMoves(last.actions.get(0).pos.get(0));
                if (vm == null || vm.size() != 1 || !vm.contains(last))
                    return new Result("ERRORE validMoves(p)");
                try {
                    Set<Move<PieceModel<Species>>> vm2 = vm;
                    Move<PieceModel<Species>> m = last;
                    notEx(() -> vm2.add(m), "l'insieme ritornato da validMoves(p) è modificabile");
                    notEx(() -> gR.isValid(null), "isValid(null)"+nE);
                    notEx(() -> gR.validMoves(null), "validMoves(null)"+nE);
                } catch (Exception ex) { return new Result("ERRORE "+ex.getMessage()); }
                gR.move(last);
                if (gR.isValid(last))
                    return new Result("ERRORE isValid(m) ritorna true invece di false");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }


    private static boolean test_BoardOct(float sc, int ms) {
        return runTest("Test BoardOct", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                Pos p = new Pos(0,0), p2 = new Pos(2,2), pNull = null;
                PieceModel<Species> pm = new PieceModel<>(Species.KING, "black"), pmNull = null;
                BoardOct<PieceModel<Species>> b = new BoardOct<>(8, 8);
                if (b.width() != 8 || b.height() != 8 || b.system() != Board.System.OCTAGONAL)
                    return new Result("ERRORE BoardOct(8,8): width() o height() o system()");
                if (!b.isModifiable())
                    return new Result("ERRORE BoardOct(8,8): isModifiable() ritorna false");
                List<Pos> lp = b.positions();
                try {
                    notEx(() -> lp.add(p2), "la lista ritornata da positions() è modificabile");
                } catch (Exception ex) { return new Result("ERRORE BoardOct(8,8): "+ex.getMessage()); }
                Pos pp = b.adjacent(new Pos(0,0), Dir.UP);
                if (!new Pos(0,1).equals(pp)) return new Result("ERRORE BoardOct(8,8): adjacent((0,0),UP) errato");
                pp = b.adjacent(new Pos(0,0), Dir.RIGHT);
                if (!new Pos(1,0).equals(pp)) return new Result("ERRORE BoardOct(8,8): adjacent((0,0),RIGHT) errato");
                pp = b.adjacent(new Pos(0,0), Dir.LEFT);
                if (pp != null) return new Result("ERRORE BoardOct(8,8): adjacent((0,0),LEFT) errato");
                p = new Pos(2,2);
                b.put(pm, p);
                if (!pm.equals(b.get(p))) return new Result("ERRORE BoardOct(8,8): put(P pm,Pos p) o get(Pos p) errato");
                b.remove(p);
                if (b.get(p) != null) return new Result("ERRORE BoardOct(8,8): remove(Pos p) errato");
                try {
                    notEx(() -> b.adjacent(pNull, Dir.UP), "adjacent(null,d)"+nE);
                    notEx(() -> b.adjacent(new Pos(0,0), null), "adjacent(p,null)"+nE);
                    notEx(() -> b.put(pm, new Pos(10,10)), "put(pm,(10,10))"+nE);
                } catch (Exception ex) { return new Result("ERRORE BoardOct(8,8): "+ex.getMessage()); }

                BoardOct<PieceModel<Species>> b2 = new BoardOct<>(12, 15, Arrays.asList(new Pos(0,0),new Pos(5,5)));
                if (b2.width() != 12 || b2.height() != 15 || b2.system() != Board.System.OCTAGONAL)
                    return new Result("ERRORE BoardOct(12,15): width() o height() o system()");
                if (!b2.isModifiable())
                    return new Result("ERRORE BoardOct(12,15): isModifiable() ritorna false");
                List<Pos> lp2 = b.positions();
                try {
                    notEx(() -> lp2.add(p2), "la lista ritornata da positions() è modificabile");
                } catch (Exception ex) { return new Result("ERRORE BoardOct(12,15): "+ex.getMessage()); }
                if (b2.isPos(new Pos(5,5))) return new Result("ERRORE BoardOct(12,5,{(0,0),(5,5)}): isPos((5,5)) errato");
                if (b2.adjacent(new Pos(4,4), Dir.UP_R) != null)
                    return new Result("ERRORE BoardOct(12,15,{(0,0),(5,5)}): adjacent((4,4),UP_R) errato");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_UnmodifiableBoard(float sc, int ms) {
        return runTest("Test UnmodifiableBoard con BoardOct", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                Pos p = new Pos(0,0), p2 = new Pos(2,2), pNull = null;
                PieceModel<Species> pm = new PieceModel<>(Species.KING, "black"), pmNull = null;
                Board<PieceModel<Species>> ub = Utils.UnmodifiableBoard(new BoardOct<>(8, 8));
                if (ub.isModifiable()) return new Result("ERRORE isModifiable() ritorna true");
                try {
                    notEx(() -> ub.put(pm, p), "put(pm,p)"+nE);
                    notEx(() -> ub.put(pm, p, Dir.UP, 1), "put(pm,p,UP,1)"+nE);
                    notEx(() -> ub.remove(p), "remove(p)"+nE);
                } catch (Exception ex) { return new Result("ERRORE: "+ex.getMessage()); }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_PlayRandPlayer(float sc, int ms) {
        return runTest("Test Play e RandPlayer", sc, ms, () -> {
            try {
                String nE = " non lancia eccezione";
                DummyGUFactory gF = new DummyGUFactory();
                Player<PieceModel<Species>> py1 = new RandPlayer<>("A");
                Player<PieceModel<Species>> py2 = new RandPlayer<>("B");
                GameRuler<PieceModel<Species>> gR = Utils.play(gF, py1, py2);
                if (gR.result() != 0) return new Result("ERRORE");
                try {
                    notEx(() -> Utils.play(null, py1, py2), "play(null,p1,p2)"+nE);
                    notEx(() -> Utils.play(gF,py1), "play(gf,p1) quando il gioco richiede 2 giocatori"+nE);
                } catch (Exception ex) { return new Result("ERRORE: "+ex.getMessage()); }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }








    private static class DummyBoard<P> implements Board<P> {
        private DummyBoard() {
            List<Pos> pl = new ArrayList<>();
            for (int b = 0 ; b < 10 ; b++)
                for (int t = 0 ; t < 10 ; t++)
                    pl.add(new Pos(b,t));
            posList = Collections.unmodifiableList(pl);
            pmMap = new HashMap<>();
        }
        @Override
        public System system() { return System.OCTAGONAL; }
        @Override
        public int width() { return 10; }
        @Override
        public int height() { return 10; }
        @Override
        public Pos adjacent(Pos p, Dir d) {
            Objects.requireNonNull(p);
            Objects.requireNonNull(d);
            if (!(p.b >= 0 && p.b < 10 && p.t >= 0 && p.t < 10)) return null;
            Disp ds = toDisp.get(d);
            int b = p.b + ds.db, t = p.t + ds.dt;
            if (!(b >= 0 && b < 10 && t >= 0 && t < 10)) return null;
            return new Pos(b, t);
        }
        @Override
        public List<Pos> positions() { return posList; }
        @Override
        public P get(Pos p) {
            Objects.requireNonNull(p);
            return pmMap.get(p);
        }
        @Override
        public boolean isModifiable() { return true; }
        @Override
        public P put(P pm, Pos p) {
            Objects.requireNonNull(pm);
            Objects.requireNonNull(p);
            check(p);
            return pmMap.put(p, pm);
        }
        @Override
        public P remove(Pos p) {
            Objects.requireNonNull(p);
            check(p);
            return pmMap.remove(p);
        }
        private DummyBoard(DummyBoard<P> b) {
            List<Pos> lp = new ArrayList<>();
            lp.addAll(b.posList);
            posList = Collections.unmodifiableList(lp);
            pmMap = new HashMap<>();
            pmMap.putAll(b.pmMap);
        }
        static class Disp {
            final int db, dt;
            Disp(int db, int dt) { this.db = db; this.dt = dt; }
        }
        static EnumMap<Dir, Disp> toDisp = new EnumMap<>(Dir.class);
        static {
            toDisp.put(Dir.UP, new Disp(0,1));
            toDisp.put(Dir.UP_R, new Disp(1,1));
            toDisp.put(Dir.RIGHT, new Disp(1,0));
            toDisp.put(Dir.DOWN_R, new Disp(1,-1));
            toDisp.put(Dir.DOWN, new Disp(0,-1));
            toDisp.put(Dir.DOWN_L, new Disp(-1,-1));
            toDisp.put(Dir.LEFT, new Disp(-1,0));
            toDisp.put(Dir.UP_L, new Disp(-1,1));
        }
        private void check(Pos p) {
            if (!(p.b >= 0 && p.b < 10 && p.t >= 0 && p.t < 10)) throw new IllegalArgumentException();
        }
        private final List<Pos> posList;
        private final Map<Pos,P> pmMap;
    }

    private static class DummyGU implements GameRuler<PieceModel<Species>> {
        private DummyGU(String p1, String p2) {
            board = new DummyBoard<>();
            unModB = new Board<PieceModel<Species>>() {
                @Override
                public System system() { return board.system(); }
                @Override
                public int width() { return board.width(); }
                @Override
                public int height() { return board.height(); }
                @Override
                public Pos adjacent(Pos p, Dir d) { return board.adjacent(p, d); }
                @Override
                public List<Pos> positions() { return board.positions(); }
                @Override
                public PieceModel<Species> get(Pos p) { return board.get(p); }
            };
            pNames = Collections.unmodifiableList(Arrays.asList(p1, p2));
        }
        @Override
        public String name() { return "DummyGU"; }
        @Override
        public <T> T getParam(String name, Class<T> c) { return null; }
        @Override
        public List<String> players() { return pNames; }
        @Override
        public String color(String name) {
            Objects.requireNonNull(name);
            if (!pNames.contains(name)) throw new IllegalArgumentException();
            return "white";
        }
        @Override
        public Board<PieceModel<Species>> getBoard() { return unModB; }
        @Override
        public int turn() { return gRes == -1 ? cTurn : 0; }
        @Override
        public boolean move(Move<PieceModel<Species>> m) {
            Objects.requireNonNull(m);
            if (gRes != -1) throw new IllegalArgumentException();
            if (!validMoves().contains(m)) {
                gRes = 3 - cTurn;
                return false;
            }
            board.put(disc, m.actions.get(0).pos.get(0));
            if (validMoves().isEmpty()) gRes = 0;
            cTurn = 3 - cTurn;
            return true;
        }
        @Override
        public boolean unMove() { return false; }
        @Override
        public boolean isPlaying(int i) {
            if (i != 1 && i != 2) throw new IllegalArgumentException();
            if (gRes != -1) return false;
            return true;
        }
        @Override
        public int result() { return gRes; }
        @Override
        public Set<Move<PieceModel<Species>>> validMoves() {
            Set<Move<PieceModel<Species>>> vm = new HashSet<>();
            for (Pos p : board.positions()) {
                if (board.get(p) == null) {
                    Move<PieceModel<Species>> m = new Move<>(new Action<>(p, disc));
                    vm.add(m);
                }
            }
            return Collections.unmodifiableSet(vm);
        }
        @Override
        public GameRuler<PieceModel<Species>> copy() {
            return new DummyGU(this);
        }

        private DummyGU(DummyGU g) {
            board = new DummyBoard<>(g.board);
            unModB = new Board<PieceModel<Species>>() {
                @Override
                public System system() { return board.system(); }
                @Override
                public int width() { return board.width(); }
                @Override
                public int height() { return board.height(); }
                @Override
                public Pos adjacent(Pos p, Dir d) { return board.adjacent(p, d); }
                @Override
                public List<Pos> positions() { return board.positions(); }
                @Override
                public PieceModel<Species> get(Pos p) { return board.get(p); }
            };
            List<String> nn = new ArrayList<>();
            nn.addAll(g.pNames);
            pNames = Collections.unmodifiableList(nn);
            gRes = g.gRes;
            cTurn = g.cTurn;
        }

        private final DummyBoard<PieceModel<Species>> board;
        private final Board<PieceModel<Species>> unModB;
        private final List<String> pNames;
        private final PieceModel<Species> disc = new PieceModel<>(Species.DISC, "white");
        private int gRes = -1, cTurn = 1;
    }

    private static class DummyGUFactory implements GameFactory<DummyGU> {
        @Override
        public String name() { return "DummyGU"; }
        @Override
        public int minPlayers() { return 2; }
        @Override
        public int maxPlayers() { return 2; }
        @Override
        public List<Param<?>> params() { return null; }
        @Override
        public void setPlayerNames(String...names) {
            for (String n : names)
                Objects.requireNonNull(n);
            if (names.length != 2) throw new IllegalArgumentException();
            pNames = names;
        }
        @Override
        public DummyGU newGame() {
            if (pNames == null) throw new IllegalStateException();
            return new DummyGU(pNames[0], pNames[1]);
        }

        private String[] pNames;
    }

    private static void notEx(Runnable r, String err) {
        try {
            r.run();
            throw new Exception(err);
        } catch(Exception ex) {}
    }


    private static void print(Result r) {
        System.out.println(!r.fatal && r.err == null ? "OK" : r.err);
    }
    private static void print(String m) { System.out.print(m); }
    private static void println(String m) { print(m+"\n"); }

    private static Result handleThrowable(Throwable t) {
        String msg = "";
        boolean fatal = false;
        if (t instanceof Exception)
            msg += "Eccezione inattesa: ";
        else {
            msg += "ERRORE GRAVE, impossibile continuare il test: ";
            fatal = true;
        }
        return new Result(msg+t, fatal);
    }

    private static boolean runTest(String msg, float score, int ms, Callable<Result> test) {
        FutureTask<Result> future = new FutureTask<>(test);
        Thread t = new Thread(future);
        t.setDaemon(true);
        print(msg+" ");
        OUTPUT.standardOutput(false);
        INPUT.standardInput(false);
        OUTPUT.setThread(t);
        INPUT.setThread(t);
        t.start();
        Result res = null;
        try {
            res = future.get(ms, TimeUnit.MILLISECONDS);
        } catch (CancellationException | InterruptedException | TimeoutException | ExecutionException e) {}
        future.cancel(true);
        OUTPUT.setThread(Thread.currentThread());
        INPUT.setThread(Thread.currentThread());
        OUTPUT.standardOutput(true);
        INPUT.standardInput(true);
        if (res == null)
            println("ERRORE: limite di tempo superato ("+ms+" ms)");
        else if (res.fatal) {
            println(res.err);
            return false;
        } else if (res.err != null) {
            println(res.err);
        } else {
            println(" score "+score);
            totalScore += score;
        }
        return true;
    }

    private static class Result {
        public final String err;
        public final boolean fatal;

        public Result(String e, boolean f) {
            err = e;
            fatal = f;
        }

        public Result() { this(null, false); }
        public Result(String e) { this(e, false); }
    }

    private static class Locker {
        public Locker() {
            lock = new ReentrantLock(true);
        }

        public synchronized boolean acquireLock() {
            try {
                if (Thread.currentThread().getId() == MAIN_THREAD.getId()) {
                    while (!lock.tryLock()) ;
                } else
                    lock.lockInterruptibly();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return false; }
            return true;
        }

        public synchronized void releaseLock() { lock.unlock(); }


        private final ReentrantLock lock;
    }

    private static class Output extends OutputStream {
        public void standardOutput(boolean std) {
            if (!lock.acquireLock()) return;
            standardOut = std;
            lock.releaseLock();
        }
        @Override
        public void write(int b) throws IOException {
            if (!lock.acquireLock()) return;
            try {
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    if (standardOut) {
                        STD_OUT.write(b);
                    } else
                        buffer.offer(b);
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
        }

        public String getBuffer() {
            if (!lock.acquireLock()) return null;
            String s = "";
            try {
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    int size = buffer.size();
                    if (size > 0) {
                        Integer[] aux = buffer.toArray(new Integer[size]);
                        int[] cps = new int[size];
                        for (int i = 0; i < cps.length; i++) cps[i] = aux[i];
                        s = new String(cps, 0, cps.length);
                        buffer.clear();
                    }
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
            return s;
        }

        public void setThread(Thread t) {
            if (!lock.acquireLock()) return;
            currThread = t;
            lock.releaseLock();
        }

        private final Locker lock = new Locker();
        private final Queue<Integer> buffer = new ConcurrentLinkedDeque<>();
        private volatile Thread currThread = null;
        private volatile boolean standardOut = false;
    }

    private static class Input extends InputStream {
        public void standardInput(boolean std) {
            if (!lock.acquireLock()) return;
            standardIn = std;
            lock.releaseLock();
        }

        public void setContent(String s) {
            if (!lock.acquireLock()) return;
            try {
                content = new ByteArrayInputStream(s.getBytes());
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
        }

        @Override
        public int read() throws IOException {
            int b = -1;
            try {
                if (!lock.acquireLock()) return b;
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    if (standardIn) {
                        b = STD_IN.read();
                    } else
                        b = content.read();
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
            return b;
        }

        public void setThread(Thread t) {
            if (!lock.acquireLock()) return;
            currThread = t;
            lock.releaseLock();
        }

        private final Locker lock = new Locker();
        private volatile Thread currThread = null;
        private volatile ByteArrayInputStream content = null;
        private volatile boolean standardIn = false;
    }

    private static String sessionToString(String[][] session) {
        String s = "";
        for (String[] t : session)
            s += t[1]+"\n";
        return s;
    }


    private static final PrintStream STD_OUT = System.out;
    private static final InputStream STD_IN = System.in;
    private static final Output OUTPUT = new Output();
    private static final Input INPUT = new Input();
    private static final Thread MAIN_THREAD = Thread.currentThread();

    private static double totalScore;
    private static volatile boolean testing = false;
    private static volatile String testWord = null;
    private static final Random RND = new Random();
}
