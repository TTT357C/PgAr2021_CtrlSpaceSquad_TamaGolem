package it.unibs.ing.fp.tamagolem;

import it.unibs.ing.fp.mylib.InputGame;

import java.util.Random;

public class Partita {

    private Squadra squadra_uno;
    private Squadra squadra_due;
    private boolean termina;

    /**
     *
     * @param squadra_uno Squadra A
     * @param squadra_due Squadra B
     */
    public Partita(Squadra squadra_uno, Squadra squadra_due) {
        this.squadra_uno = squadra_uno;
        this.squadra_due = squadra_due;
    }

    public Partita() {

    }

    public void startGame(){
        //N = Numero di elementi nell'equilibrio
        int N = setN();
        //P Numero di pietre per ogni golem
        int P = ((N+1)/3)+1;
        // G Numero di golem per partita
        int G =  (N - 1)*(N - 2) / (2 * P);
        //S Quantita di pietre nella scorta comune
        int S = ((2 * G * P) / N) * N +N;

        /*
        System.out.println(N);
        System.out.println(G);
        System.out.println(P);
        System.out.println(S);*/

        // CREARE EQUILIBRIO PASSANDO N
        //Todo

        //creazione Set di pietre comuni
        boolean[][] set_pietre_comuni = new boolean[N][S/N];
        creazioneSet(set_pietre_comuni);
        //creare un altro array con i nomi parallelo a setpietrecomuni Todo

        //Inizio scontro Todo


        //Dichiara vincitore Todo

        //Visualizza equilibrio Todo

    }

    /**
     * Metodo utilizzato per l'inizializzazione del set di pietre comuni
     * Metto true = non utilizzata
     * Poi quando verrà presa mettero false
     * @param set_pietre_comuni Matrice booleana
     */
    private void creazioneSet(boolean[][] set_pietre_comuni) {
        for (int i = 0; i < set_pietre_comuni.length; i++) {
            for (int j = 0; j < set_pietre_comuni[0].length; j++) {
                set_pietre_comuni[i][j]=true;
            }

        }
    }

    /**
     * Metodo che chiede la difficolta e in base ad essa restituisce il numero di elementi dell'equilibrio
     * @return intero con numero di elementi dell'equilibrio
     */
    private int setN(){
        Random rand = new Random();
        int difficolta = InputGame.scegliDifficolta();
        int n_elementi=0,max=0,min=0;
        switch (difficolta){
            //Facile
            case 0:
                max=5;
                min=4;
                break;
            //Intermedio
            case 1:
                max=8;
                min=6;
                break;
            //Difficile
            case 2:
                max=10;
                min=9;
                break;
        }
        n_elementi = rand.nextInt((max - min) + 1) + min;
        return n_elementi;
    }
}
