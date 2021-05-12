package it.unibs.ing.fp.tamagolem;

import it.unibs.ing.fp.mylib.InputDati;
import it.unibs.ing.fp.mylib.InputGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe Partita
 *
 * @author Visini Mattia
 */
public class Partita {

    public static int NUMERO_ELEMENTI = 5; //Mai Minore di 5
    public static int PIETRE_PER_GOLEM = 0;
    public static int GOLEM_PER_PLAYER = 0;
    public static int ELEMENTI_IN_SCORTA = 0;
    public static final int VALORE_MAX_RANDOM_NUM = 3; //incide solo sui numeri random, ma non su quelli calcolati

    private Squadra squadra_uno;
    private Squadra squadra_due;

    /**
     *
     * @param squadra_uno Squadra A
     * @param squadra_due Squadra B
     */
    public Partita(Squadra squadra_uno, Squadra squadra_due) {
        this.squadra_uno = squadra_uno;
        this.squadra_due = squadra_due;
    }

    public void inizializzazione(){
        //Metodo che calcola costati di gioco
        calcoloCostatiDiGioco();
        //inizializzazione tamagolem
        inizializzaTama(GOLEM_PER_PLAYER);
    }

    public void inizializzazioneGUI(int numero_elementi){
        //Metodo che calcola costati di gioco
        calcoloCostatiDiGiocoGUI(numero_elementi);
        //inizializzazione tamagolem
        inizializzaTama(GOLEM_PER_PLAYER);
    }

    public ArrayList<Pietra> generaScortaComune(ArrayList<Tipo> tipi){
        //creazione Set di pietre comuni
        ArrayList<Pietra> scorta_comune = new ArrayList<>();
        //TIPI INDICA L'ARRAYLIST con i TIPI PRESENTI
        for (Tipo appoggio : tipi) {
            if(appoggio.getM()){
                scorta_comune.add(new Pietra(appoggio,(ELEMENTI_IN_SCORTA /NUMERO_ELEMENTI)));
            }
        }


        return scorta_comune;
    }

    /**
     * Work in CUI
     */
    public void startGame(){

        inizializzazione();

        // CREARE EQUILIBRIO PASSANDO NUMERO_ELEMENTI Todo
        GestoreEquilibrio generatore = new GestoreEquilibrio(NUMERO_ELEMENTI);
        ArrayList<Tipo> tipi = generatore.equilibrio();

        ArrayList<Pietra> scorta_comune = generaScortaComune(tipi);

        //Evoluzione del primo tamagolem delle due squadre
        evoluzione(squadra_uno,scorta_comune, PIETRE_PER_GOLEM);
        evoluzione(squadra_due,scorta_comune, PIETRE_PER_GOLEM);

        //Inizio scontro Todo
        boolean check_finisch;
        do{
            boolean check = getElementiUguali()==squadra_due.getTamagolem().getPietreArray().size();
            while(check){
                System.out.println(squadra_due.getCombattente().getNome_combattente() + " HAI SCELTO GLI STESSI TIPI DI " + squadra_uno.getCombattente().getNome_combattente() + " PER FAVORE CAMBIA");

                for (int i = 0; i < ELEMENTI_IN_SCORTA /NUMERO_ELEMENTI; i++) {
                    int j=0;
                    boolean trova=false;
                    do{
                        //todo controlla java.util.NoSuchElementException
                        if(scorta_comune.get(j).getTipo_pietra().name().equals(squadra_due.getTamagolem().getPietre().getTipo_pietra().name())){
                            scorta_comune.get(j).aumentaQuantitaPietra();
                            squadra_due.getTamagolem().rimuoviPrimaPietra();
                            trova=true;
                        }
                        j++;
                    }while(!trova);
                }
                evoluzione(squadra_due,scorta_comune, PIETRE_PER_GOLEM);

                check = getElementiUguali()==squadra_due.getTamagolem().getPietreArray().size();

                int a_zero=0;
                for (int i = 0; i < scorta_comune.size(); i++) {
                    if(scorta_comune.get(i).getQuantita_pietra()==0){
                        a_zero++;
                    }
                }
                if(a_zero==scorta_comune.size()){
                    check=false;
                }

            }

            int posp = posPietra(tipi,squadra_uno);
            int posq = posPietra(tipi, squadra_due);
            if(tipi.get(posp).name().equals(tipi.get(posq).name())){
                System.out.println("AVETE USATO LO STESSO TIPO E NON HA CREATO CONSEGUENZE");
            }
            else{
                if(tipi.get(posp).getArchi().get(posq).getSenso()){
                    //uno predomina
                    //System.out.println("TOLGO A DUE : "+  tipi.get(posp).getArchi().get(posq).getValore());
                    squadra_due.getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                }
                if(!(tipi.get(posp).getArchi().get(posq).getSenso())){
                    //due predomina
                    //System.out.println("TOLGO A UNO : "+tipi.get(posp).getArchi().get(posq).getValore());
                    squadra_uno.getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                }
            }
            System.out.println("============");
            System.out.println(squadra_uno.getCombattente().getNome_combattente()+ " ha usato " + squadra_uno.getTamagolem().getPietre().getTipo_pietra().name() + " Vita del Tamagolem: "+ squadra_uno.getTamagolem().getSalute() );
            System.out.println(squadra_due.getCombattente().getNome_combattente()+ " ha usato " + squadra_due.getTamagolem().getPietre().getTipo_pietra().name() + " Vita del Tamagolem: "+ squadra_due.getTamagolem().getSalute() );
            System.out.println("Press \"ENTER\" to continue...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            //effettuo cambio pietre
            cambioPietre();
            //controllo vita dei due tamagolem
            controllaVita2Tama(scorta_comune);
            check_finisch = isTerminata();

            if(check_finisch){
                int a_zero=0;
                for (int i = 0; i < scorta_comune.size(); i++) {
                    if(scorta_comune.get(i).getQuantita_pietra()==0){
                        a_zero++;
                    }
                }
                if(a_zero==scorta_comune.size()){
                    check_finisch=false;
                }
            }
            //TODO
        }while(!check_finisch);
        //Dichiarazione vincitore
        stampaVincitore();
        //Visualizza equilibrio
        System.out.println("\n" + stringaEquilibrio(tipi));
    }

    public void controllaVita2Tama(ArrayList<Pietra> scorta_comune) {
        controlloVita(scorta_comune, PIETRE_PER_GOLEM, squadra_uno);
        controlloVita(scorta_comune, PIETRE_PER_GOLEM, squadra_due);
    }

    /**
     * Metodo che crea una stringa che rappresenta l'equilibrio
     * Ritorna una stringa che verra poi visualizzata
     * @param tipi Arralist con l'equilibrio
     * @return Ritorna una stringa per la visualizzazione dell'equilibrio
     */
    public String stringaEquilibrio(ArrayList<Tipo> tipi) {
        String equilibrio = "";
        for (int i = 0; i < tipi.size(); i++) {
            equilibrio += tipi.get(i).name() + " predomina su:\n";
            for (int j = 0; j < tipi.size(); j++) {
                if (i != j) {
                    if (tipi.get(i).getArchi().get(j).getSenso()) {
                        equilibrio += "\t" + tipi.get(j).name() + " valore danno: " + tipi.get(i).getArchi().get(j).getValore() + "\n";
                    }
                }
            }
            equilibrio += "\n";
        }
        return equilibrio;
    }

    /**
     * Metodo che calcola le costati di gioco necessarie per avviare la partita
     */
    private void calcoloCostatiDiGioco() {
        //NUMERO_ELEMENTI = Numero di elementi nell'equilibrio
        NUMERO_ELEMENTI = setN();
        //P Numero di pietre per ogni golem
        PIETRE_PER_GOLEM = ((NUMERO_ELEMENTI+1)/3)+1;
        // G Numero di golem per partita
        GOLEM_PER_PLAYER =  (NUMERO_ELEMENTI - 1)*(NUMERO_ELEMENTI - 2) / (2 * PIETRE_PER_GOLEM);
        //S Quantita di pietre nella scorta comune
        ELEMENTI_IN_SCORTA = ((2 * GOLEM_PER_PLAYER * PIETRE_PER_GOLEM) / NUMERO_ELEMENTI) * NUMERO_ELEMENTI +NUMERO_ELEMENTI;
    }

    /**
     * Metodo che calcola le costati di gioco necessarie per avviare la partita (GUI)
     */
    private void calcoloCostatiDiGiocoGUI(int numero_elementi) {
        //NUMERO_ELEMENTI = Numero di elementi nell'equilibrio
        NUMERO_ELEMENTI = numero_elementi;
        //P Numero di pietre per ogni golem
        PIETRE_PER_GOLEM = ((NUMERO_ELEMENTI+1)/3)+1;
        // G Numero di golem per partita
        GOLEM_PER_PLAYER =  (NUMERO_ELEMENTI - 1)*(NUMERO_ELEMENTI - 2) / (2 * PIETRE_PER_GOLEM);
        //S Quantita di pietre nella scorta comune
        ELEMENTI_IN_SCORTA = ((2 * GOLEM_PER_PLAYER * PIETRE_PER_GOLEM) / NUMERO_ELEMENTI) * NUMERO_ELEMENTI +NUMERO_ELEMENTI;
    }

    /**
     *
     * @param tipi Array dei tipi presenti nell'equilibrio
     * @param squadra Riferimento della squadra
     * @return Ritorna la posizione nell'array tipi della pietra attiva
     */
    public int posPietra(ArrayList<Tipo> tipi, Squadra squadra) {
        int pos=0;
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            if (squadra.getTamagolem().getPietre().getTipo_pietra().name().equals(tipi.get(i).name())) {
                pos=i;
            }
        }
        return pos;
    }

    /**
     * Metodo che controlla la vita del tamagolem in azione,
     * Se = 0 rimuove il tamagolem dalla squadra e richiama il metodo evoluzione
     * @param scorta_comune Scorta di pietre comune
     * @param p Numero intero che rappresenta il numero di pietre da scegliere durante l'evoluzione
     * @param squadra Riferimento della squadra in cui si effettua il controllo
     */
    private void controlloVita(ArrayList<Pietra> scorta_comune, int p, Squadra squadra) {
        if (squadra.getTamagolem().getSalute() <= 0) {
            squadra.removeTama();
            if (squadra.getTamagolems().size() > 0) {
                evoluzione(squadra, scorta_comune, p);
            }
        }
    }

    /**
     * Metodo che effettua la rotazione delle pietre
     */
    public void cambioPietre() {
        squadra_uno.getTamagolem().cambioPietra();
        squadra_due.getTamagolem().cambioPietra();
    }

    /**
     * Calcolo pietre uguali nelle stesse posizioni
     * @return Numero intero di elementi uguali tra le due scorte di pietre dei due tamagolem
     */
    private int getElementiUguali() {
        int yy=0;
        for (int i = 0; i < squadra_uno.getTamagolem().getPietreArray().size(); i++) {
            if(squadra_uno.getTamagolem().getPietre().getTipo_pietra().name().equals(squadra_due.getTamagolem().getPietre().getTipo_pietra().name())){
                yy++;
            }
            cambioPietre();
        }
        return yy;
    }

    /**
     * Metodo cerca il vincitore tra le due squadre
     * Viene poi invocato un metodo che stampa a video il nome del combattente vincitore
     */
    private void stampaVincitore() {
        Combattente vincitore = getCombattenteVincente();
        InputGame.stampaVittoria(vincitore);
    }

    public Combattente getCombattenteVincente() {
        Combattente vincitore;
        if(squadra_uno.getTamagolems().size()== 0){
            //VITTORIA SQUADRA DUE
            vincitore = squadra_due.getCombattente();
        }
        else if(squadra_due.getTamagolems().size()== 0){
            //VITTORIA SQUADRA UNO
            vincitore = squadra_uno.getCombattente();
        }
        else{
            //PAREGGIO
            vincitore = null;
        }
        return vincitore;
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
        System.out.println("\tNUMERO \t NOME \t\t QUANTITA'");
        for (int i = 0; i < scorta_comune.size(); i++) {

            System.out.println("\t"+i+" \t\t "+ scorta_comune.get(i).getTipo_pietra().name() + " \t\t " + scorta_comune.get(i).getQuantita_pietra());
        }
        System.out.println("======================================================");
    }

    /**
     * Metodo che calcola se la partita è terminata
     * @return Ritorna true se NON è termianta
     */
    public boolean isTerminata() {
        return (squadra_uno.getTamagolems().size() == 0 || squadra_due.getTamagolems().size() == 0);
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
                min=5;
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

    public Squadra getSquadra_uno() {
        return squadra_uno;
    }

    public Squadra getSquadra_due() {
        return squadra_due;
    }
}
