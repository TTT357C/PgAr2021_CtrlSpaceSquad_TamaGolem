package it.unibs.ing.fp.tamagolem;

/**
 * Enum dei tipi di forze naturali dello scontro tra TamaGolem
 *
 * @author Rossi Mirko
 */
public enum Tipo {


    FUOCO(false),
    ACQUA(false),
    ARIA(false),
    TERRA(false),
    ELETTRO(false),
    LUCE(false),
    BUIO(false),
    GHIACCIO(false),
    MAGNETICO(false),
    PSICO(false);

    private  boolean ePresente;
    Tipo( boolean stato) {
        this.ePresente = stato;
    }

    public void setM(boolean modifica){
       this.ePresente = modifica;
    }

    public boolean getM() {
        return ePresente;
    }
}
