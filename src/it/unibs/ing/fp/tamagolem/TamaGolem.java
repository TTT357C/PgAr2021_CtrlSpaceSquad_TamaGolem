package it.unibs.ing.fp.tamagolem;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classe TamaGolem
 *
 * @author Rossi Mirko
 */
public class TamaGolem {

    private final static int SALUTE = 100;

    private Deque<Pietra> pietre = new ArrayDeque<>();
    private int salute;

    public TamaGolem(){

        this.salute = SALUTE;

    }

    public Pietra getPietre() {
        return pietre.getFirst();
    }


    public int getSalute(){
        return salute;
    }

    public void setSaluteDanno(int danno){

        this.salute -= danno;

    }

    /**
     * Metodo per aggiungere un oggetto di classe Pietra alla queue delle pietre del TamaGolem
     *
     * @param pietra pietra da aggiungere alla queue del TamaGolem caratteristica di un tipo di attacco
     * @author Rossi Mirko
     */
    public void addTipoPietra(Pietra pietra){
        this.pietre.addLast(pietra);
    }

    /**
     * Metodo per il cambio del tipo di pietra che userà il TamaGolem
     *
     * @author Rossi Mirko
     */
    public void cambioPietra(){

        Pietra pietra_aus = pietre.getFirst();
        pietre.removeFirst();
        pietre.addLast(pietra_aus);

    }
}