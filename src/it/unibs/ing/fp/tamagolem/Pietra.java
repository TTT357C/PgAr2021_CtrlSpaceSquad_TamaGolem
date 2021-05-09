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

    public Pietra(Tipo tipo, int quantita){
            tipo_pietra = tipo;
            quantita_pietra = quantita;

    }

    public Pietra(Tipo tipo){
        tipo_pietra = tipo;
    }

    public Tipo getTipo_pietra() {
        return tipo_pietra;
    }

    public int getQuantita_pietra() {

        return quantita_pietra;

    }

    public void setQuantita_pietra(int quantita_pietra) {

        this.quantita_pietra = quantita_pietra;

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

