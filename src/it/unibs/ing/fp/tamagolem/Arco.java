package it.unibs.ing.fp.tamagolem;

import java.util.Objects;

public class Arco implements Comparable <Arco>{
    private Boolean senso; // true e' dominante
    private int valore; // il valore dell' arco

    public Arco(Boolean senso) {
        this.senso = senso;
        this.valore = -1;
    }

    public Arco(Boolean senso, int valore) {
        this.senso = senso;
        this.valore = valore;
    }

    public Boolean getSenso() {
        return senso;
    }

    public int getValore() {
        return valore;
    }

    public void setSenso(Boolean senso) {
        this.senso = senso;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arco arco = (Arco) o;
        return valore == arco.valore && senso.equals(arco.senso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senso, valore);
    }

    @Override
    public String toString() {
        return "\n  Arco: " + "\n\tsenso=" + senso + " - valore=" + valore+";\n";
    }

    @Override
    public int compareTo(Arco o) {
        if (this.hashCode()==o.hashCode()){
            return 0;
        }
        else {
            return -1;
        }
    }
}
