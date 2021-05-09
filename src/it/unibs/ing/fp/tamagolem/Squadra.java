package it.unibs.ing.fp.tamagolem;

import java.util.Stack;

public class Squadra {

    private Stack<TamaGolem> tamagolem ;
    private Combattente combattente;

    /**
     * Metodo costruttore della squadra
     * @param combattente Classe combattente che lo identifica
     */
    public Squadra(Combattente combattente) {
        this.combattente = combattente;
    }

    public void creaTama(){
        tamagolem.addElement(new TamaGolem());
    }

    public Stack<TamaGolem> getTamagolem() {
        return tamagolem;
    }

    public Combattente getCombattente() {
        return combattente;
    }
}
