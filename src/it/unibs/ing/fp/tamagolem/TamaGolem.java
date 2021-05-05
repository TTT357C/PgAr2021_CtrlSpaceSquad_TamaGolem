package it.unibs.ing.fp.tamagolem;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classe TamaGolem
 *
 * @author Rossi Mirko
 */
public class TamaGolem {

    Deque<Pietra> pietre = new ArrayDeque<>();
    private int salute = 100;

    public Deque<Pietra> getPietre() {
        return pietre;
    }

    public void setPietre(Deque<Pietra> pietre) {
        this.pietre = pietre;
    }

    public int getSalute(){
        return salute;
    }

    public void setSalute(int salute){
        this.salute = salute;
    }

    /**
     * Metodo per aggiungere un oggetto di classe Pietra alla queue delle pietre del TamaGolem
     *
     * @param pietra pietra da aggiungere alla queue del TamaGolem caratteristica di un tipo di attacco
     * @author Rossi Mirko
     */
    public void addTipoPietra(Pietra pietra){
        pietre.add(pietra);
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
