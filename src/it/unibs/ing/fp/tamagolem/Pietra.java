package it.unibs.ing.fp.tamagolem;

/**
 * Classe per indicare il tipo di una pietra
 *
 * @author Rossi Mirko
 */
public class Pietra {
    public static final String NESSUNA_PIETRA = "Nessuna Pietra disponibile per questo tipo";
    private final Tipo tipo_pietra;
    private int quantita_pietra;

    /**
     * Metodo costruttore di pietra usato per l'array list della scorta comune
     * @param tipo Tipo di pietra
     * @param quantita Intero che indica la quantita di pietre
     */
    public Pietra(Tipo tipo, int quantita){
            tipo_pietra = tipo;
            quantita_pietra = quantita;

    }

    /**
     * Metodo costuttore
     * @param tipo Tipo del tipo di pietro
     */
    public Pietra(Tipo tipo){
        tipo_pietra = tipo;
    }

    /**
     * @return Ritorna un oggetto di tipo Tipo della pietra
     */
    public Tipo getTipo_pietra() {
        return tipo_pietra;
    }

    /**
     *
     * @return Ritorna un intero che indica la quantita di pietre presenti nella scorta di questo tipo
     */
    public int getQuantita_pietra() {
        return quantita_pietra;
    }

    /**
     * AUMENTA quantita di un unita
     */
    public void aumentaQuantitaPietra() {
        this.quantita_pietra++;
    }

    /**
     * Decrementa la quantita di un unita
     */
    public void decrementaQuantitaPietra() {

        this.quantita_pietra--;

    }

    public String removeQuantita(){
        if(quantita_pietra>0) {
            this.quantita_pietra--;
            return null;

        }
        else{
            return NESSUNA_PIETRA;
        }
    }
}

