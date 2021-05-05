package it.unibs.ing.fp.tamagolem;

public class Combattente {

    private String nome_combattente;

    /**
     * Costruttore che setta il nome del player
     * @param nome_combattente Nome del player
     */
    public Combattente(String nome_combattente){
        this.nome_combattente = nome_combattente;
    }

    /**
     * Metodo che restituisce come stringa il nome del combattente
     * @return Get nome del combattente
     */
    public String getNome_combattente() {
        return nome_combattente;
    }

    /**
     * Metodo che permette di settare il nome del giocatore
     * @param nome_combattente Stringa con il nome del player
     */
    public void setNome_combattente(String nome_combattente) {
        this.nome_combattente = nome_combattente;
    }
}
