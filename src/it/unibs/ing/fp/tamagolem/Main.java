package it.unibs.ing.fp.tamagolem;
import java.util.ArrayList;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        final int NUMERO_ELEMENTI = 6;
        final int VALORE_MAX = 8;
        Random rand = new Random();
        ArrayList<Tipo> tipi = new ArrayList<>();
        boolean[] array = new boolean[100];

        //tutti
        for (Tipo dir : Tipo.values()) {
            tipi.add(dir);
        }
        //elimino in piu'
        int arr_lenght = tipi.size();
        for (int i = 0; i < (arr_lenght - NUMERO_ELEMENTI); i++) {
            tipi.remove(0);
        }
        //setto a true quelli da utilizzare
        for (Tipo dir : tipi) {
            dir.setM(true);
        }

        System.out.println(tipi);

        int cont = 0;
        int cont_true=0;
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {

            //parte da i + 1 per evitare doppioni
            for (int j = i + 1; j < NUMERO_ELEMENTI; j++) {
                //Creo i sensi delle frecce del grafo

                //Random
                boolean temp = rand.nextBoolean();

                //Per evitare tutti true
                //=================================================
                if (temp == true) {
                    cont_true++;
                }
                if (cont_true >= NUMERO_ELEMENTI-(i+1)){
                    temp=false;
                }
                //=================================================

                //Aggiungo i sensi al TreeMap di Tipo e del Tipo unito dall' arco
                tipi.get(i).getArchi().put(tipi.get(j).ordinal(), (new Arco(temp)));
                //L' arco avra' il senso al contrario perche' cambia il punto di vista
                tipi.get(j).getArchi().put(tipi.get(i).ordinal(), new Arco(!temp));

                cont++;
            }
            //reset contatore true
            cont_true=0;
        }

        for (Tipo dir : tipi) {
            System.out.println(dir);
        }


    }
}
