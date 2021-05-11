package it.unibs.ing.fp.tamagolem;

public class Main {
    public static void main(String[] args) {
        Partita partita = new Partita(new Squadra(new Combattente("Paolo")),new Squadra(new Combattente("Giovanni")));
        partita.startGame();
    }
}
