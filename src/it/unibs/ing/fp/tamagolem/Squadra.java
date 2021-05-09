package it.unibs.ing.fp.tamagolem;

import java.util.*;


public class Squadra {

    //private Stack<TamaGolem> tamagolem = new Stack<>();
    private Deque<TamaGolem> tamagolem = new ArrayDeque<>();
    private Combattente combattente;

    /**
     * Metodo costruttore della squadra
     * @param combattente Classe combattente che lo identifica
     */
    public Squadra(Combattente combattente) {
        this.combattente = combattente;
    }

    public void creaTama(){
        tamagolem.addFirst(new TamaGolem());
    }

    public Deque<TamaGolem> getTamagolems() {
        return tamagolem;
    }
    public TamaGolem getTamagolem() {
        return tamagolem.getFirst();
    }

    public Combattente getCombattente() {
        return combattente;
    }
}
