package it.unibs.ing.fp.tamagolem;

import java.util.*;

/**
 * Classe Squadra
 *
 * @author Visini Mattia
 */
public class Squadra {

    private Deque<TamaGolem> tamagolem = new ArrayDeque<>();
    private Combattente combattente;

    /**
     * Metodo costruttore della squadra
     * @param combattente Classe combattente che lo identifica
     */
    public Squadra(Combattente combattente) {
        this.combattente = combattente;
    }

    /**
     * Metodo che se invocato aggiunge in testa alla deque un golem
     */
    public void creaTama(){
        tamagolem.addFirst(new TamaGolem());
    }

    /**
     * Metodo che ritorna deque con i tamagolem della squadra
     * @return Ritorna Deque di tipo Tamagolem
     */
    public Deque<TamaGolem> getTamagolems() {
        return tamagolem;
    }

    /**
     * Metodo che ritorna ll'ultimo tamagolem della squadra
     * @return ritorna l'ultimo tamagolem della deque
     */
    public TamaGolem getTamagolem() {
        return tamagolem.getLast();
    }

    /**
     * Metodo che se invocato rimuove l'ultimo tamagolem quello in battaglia
     */
    public void removeTama(){
        tamagolem.removeLast();
    }

    /**
     *
     * @return Ritorna comne oggetto di tipo combattente l'allenatore della squadra
     */
    public Combattente getCombattente() {
        return combattente;
    }
}
