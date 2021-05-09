package it.unibs.ing.fp.tamagolem;

import it.unibs.ing.fp.mylib.InputDati;
import it.unibs.ing.fp.mylib.InputGame;

import java.util.ArrayList;
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

        //inizializzazione tamagolem
        inizializzaTama(G);

        // CREARE EQUILIBRIO PASSANDO N
        //Todo

        //creazione Set di pietre comuni Todo
        ArrayList<Pietra> scorta_comune = new ArrayList<>();
        //TIPI INDICA L'ARRAYLIST DI THOMAS con i TIPI PRESENTI Todo
        /*for (Tipo dir : tipi) {
            if(dir.getM()){
                scorta_comune.add(new Pietra(dir,(S/N)));
            }
        }*/

        ArrayList <Tipo> t = new ArrayList<>();
        t.add(Tipo.LUCE);
        t.add(Tipo.ACQUA);
        t.add(Tipo.ARIA);
        t.add(Tipo.FUOCO);t.add(Tipo.PSICO);


        for (int i = 0; i < N; i++) {
            scorta_comune.add(new Pietra(t.get(i),S/N));
        }
        evoluzione(squadra_uno,scorta_comune,P);
        evoluzione(squadra_due,scorta_comune,P);

        //Inizio scontro Todo

        do{


        }while(isTerminata());


        //Dichiarazione vincitore
        Combattente vincitore;
        if(squadra_uno.getTamagolems().size() == 0){
            //VITTORIA SQUADRA DUE
            vincitore = squadra_due.getCombattente();
        }
        else if(squadra_due.getTamagolems().size() == 0){
            //VITTORIA SQUADRA UNO
            vincitore = squadra_uno.getCombattente();
        }
        else{
            //PAREGGIO
            vincitore = null;
        }
        InputGame.stampaVittoria(vincitore);

        //Visualizza equilibrio Todo

    }

    /**
     *
     * @param squadra Squadra che deve scegliere i tipi di pietra per il golem
     * @param scorta_comune ArrayList con la scorta di pietre che è possibile scegliere
     * @param p Numero di pietre per ogni golem
     */
    private void evoluzione(Squadra squadra, ArrayList<Pietra> scorta_comune, int p) {
        System.out.println("Evoluzione del golem da parte di " + squadra.getCombattente().getNome_combattente());
        for (int i = 0; i < p; i++) {
            stampaScorte(scorta_comune);
            int pietra_scelta;
            do {
                pietra_scelta = InputDati.leggiIntero("Scegli il numero della pietra da mettere nel golem: ", 0, scorta_comune.size() - 1);
            }while(scorta_comune.get(pietra_scelta).getQuantita_pietra()< 1);
            squadra.getTamagolem().addTipoPietra(new Pietra(scorta_comune.get(pietra_scelta).getTipo_pietra()));
            scorta_comune.get(pietra_scelta).decrementaQuantitaPietra();
            System.out.println();
        }
    }

    /**
     * Metodo che stampa la scorte comune di pietre disponibili
     * @param scorta_comune Arraylist di pietre
     */
    private void stampaScorte(ArrayList<Pietra> scorta_comune) {
        System.out.println("======================================================");
        System.out.println("\tNUMERO \t NOME \t QUANTITA'");
        for (int i = 0; i < scorta_comune.size(); i++) {

            System.out.println("\t"+i+" \t\t "+ scorta_comune.get(i).getTipo_pietra().name() + " \t\t " + scorta_comune.get(i).getQuantita_pietra());
        }
        System.out.println("======================================================");
    }

    /**
     * Metodo che calcola se la partita è terminata
     * @return Ritorna true se NON è termianta
     */
    private boolean isTerminata() {
        return squadra_uno.getTamagolems().size() > 0 || squadra_due.getTamagolems().size() > 0;
    }

    /**
     * Metodo che inizializza i golem delle squadre
     * @param nGolem Numero di golem per squadra
     */
    private void inizializzaTama(int nGolem){
        for (int i = 0; i < nGolem; i++) {
            squadra_uno.creaTama();
            squadra_due.creaTama();
        }
    }

    /**
     * Metodo che chiede la difficolta e in base ad essa restituisce il numero di elementi dell'equilibrio
     * @return intero con numero di elementi dell'equilibrio
     */
    private int setN(){
        Random rand = new Random();
        int difficolta = InputGame.scegliDifficolta();
        int n_elementi,max=0,min=0;
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
