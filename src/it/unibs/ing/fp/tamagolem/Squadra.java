package it.unibs.ing.fp.tamagolem;

import java.util.Stack;

public class Squadra {

    private Stack<TamaGolem> tamagolem ;
    private Combattente combattente;

    /**
     * Metodo costruttore della squadra
     * @param tamagolem Stack di tamagolem
     * @param combattente Classe combattente che lo identifica
     */
    public Squadra(Stack<TamaGolem> tamagolem, Combattente combattente) {
        this.tamagolem = tamagolem;
        this.combattente = combattente;
    }
}
