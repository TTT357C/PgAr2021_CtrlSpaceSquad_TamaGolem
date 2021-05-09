package it.unibs.ing.fp.tamagolem;

public class Arco {
    Boolean senso;

    public Arco(Boolean senso) {
        this.senso = senso;
    }

    @Override
    public String toString() {
        return senso+"";
    }
}
