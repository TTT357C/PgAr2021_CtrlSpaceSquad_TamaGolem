package it.unibs.ing.fp.tamagolem;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classe TamaGolem
 *
 * @author Rossi Mirko
 */
public class TamaGolem {

    public final static int SALUTE = 15;
    private Deque<Pietra> pietre = new ArrayDeque<>();
    private int salute;


    public void rimuoviPrimaPietra() {
        pietre.removeFirst();
    }
    /**
     * Metodo costruttore
     */
    public TamaGolem(){
        this.salute = SALUTE;
    }

    public Deque<Pietra> getPietreArray(){
        return pietre;
    }

    /**
     * @return Ritorna la prima pietra di tipo Pietra della Deque con tutte le pietre scelte in fase di evoluzione
     */
    public Pietra getPietre() {
        return pietre.getFirst();
    }

    /**
     *
     * @return Ritorna intero con la vita del tamagolem
     */
    public int getSalute(){
        return salute;
    }

    /**
     * Metodo che rimuove di un numero danno la vita
     * @param danno intero che rappresenta il danno subito
     */
    public void setSaluteDanno(int danno){
        this.salute = Math.max(0,this.salute-danno);
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