package it.unibs.ing.fp.tamagolem;

import java.util.*;

/**
 * Enum dei tipi di forze naturali dello scontro tra TamaGolem
 *
 * @author Rossi Mirko
 */
public enum Tipo implements Comparable <Tipo>{

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


    //Boolean = senso, Integer = valore
    private Map <Integer, Arco> archi = new TreeMap<>();
    private boolean ePresente;

    Tipo( boolean e_presente) {
        this.ePresente = e_presente;
    }

    public void setM(boolean modifica){
       this.ePresente = modifica;
    }

    public boolean getM() {
        return ePresente;
    }

    public Map<Integer, Arco> getArchi() {
        return archi;
    }

    @Override
    public String toString() {
        return this.name()+":\n " +
                "\n archi=" + archi +
                "\n ePresente=" + ePresente +
                '}';
    }
}
