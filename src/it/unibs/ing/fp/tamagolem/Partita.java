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
        ArrayList<Tipo> tipi = equilibrio();

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
            equilibrio += tipi.get(i).name() + " predomina su: ";
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

    /**
     * <h1> equilibrio </h1>
     * @author Thomas Causetti
     * <b> Metodo di generazione dell' equilibrio </b>
     */
    public static ArrayList<Tipo> equilibrio(){

        ArrayList<Tipo> tipi = new ArrayList<>();

        int matrice[][];
        do {
            matrice= equilibrioMatrice();
        }while(matrice[0][0]==-1);



        //tutti
        ArrayList<Tipo> tipi_temp = new ArrayList<>();
        for (Tipo dir : Tipo.values()) {
            tipi_temp.add(dir);
        }
        //elimino in piu' aggiungendo a secondo array solo quelli che servono
        int arr_lenght = tipi_temp.size();
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            tipi.add(tipi_temp.get(i));
        }
        //setto a true quelli da utilizzare
        for (Tipo dir : tipi) {
            dir.setM(true);
        }



        //Parte da i + 1 per evitare doppioni
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                if(i!=j){
                    if (matrice[i][j]!=0){
                        tipi.get(i).getArchi().put(j,new Arco(true,matrice[i][j]));
                        tipi.get(j).getArchi().put(i,new Arco(false,matrice[i][j]));
                    }
                }
            }
        }

        for (Tipo dir : tipi) {
            //System.out.println(dir);
        }

        return tipi;
    }

    /**
     * <h1> equilibrioMatrice </h1>
     * <p>Metodo che crea la matrice necessaria per generare l'equilibrio</p>
     * <p>
     * Primo passo:
     *
     *  Crea una matrice di questo tipo:
     *  (Quadrata di ordine NUMERO_ELEMENTI, simmetrica)
     *
     *  0	-3	1	2	1	-3	2
     *  -3	0	-1	1	3	3	-3
     *  1	-1	0	-3	-1	3	1
     *  2	1	-3	0	-3	1	2
     *  1	3	-1	-3	0	-1	1
     *  -3	3	3	1	-1	0	-3
     *  2	-3  1	2	1	-3	0
     *
     *  Secondo passo:
     *  Converte la matrice in una matrice di adiacenza
     *
     *  0	3	0	0	0	3	0
     *  0	0	1	0	0	3	3
     *  1	0	0	3	1	0	0
     *  2	1	0	0	3	0	0
     *  1	3	0	0	0	1	0
     *  0	0	3	1	0	0	3
     *  2	0	1	2	1	0	0
     *
     *  Terzo passo:
     *  Si assicura che la matrice sia corretta altrimenti il primo elemento della matrice viene
     *  portato a -1
     *  </p>
     *  <b> Nota: Funziona solo se richiamato da equilibrio </b>
     *
     * @author Thomas Causetti
     * @return int[][] se matrice non corretta int[0][0]=-1
     */

    public static int[][] equilibrioMatrice(){

        //=================================================
        //Inizializzo generatore N random
        Random rand = new Random();

        //=================================================
        //Creo nuova matrice
        int [][] matrice = new int[NUMERO_ELEMENTI][NUMERO_ELEMENTI];

        //=================================================
        //Diagonale di zeri
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            matrice[i][i]=0;
        }
        //=================================================
        //Genero la matrice partendo dal bordo esterno fino a quelli piu' interni. (Nota: non funziona nel verso opposto)
        boolean bool=true;
        do {
            for (int i = 0; i < (NUMERO_ELEMENTI/2); i++) {
                bool=generatoreBordoMatrice(rand, matrice, i);
                if (bool == false) {
                    break;
                }
            }
        }while(!bool);
        //=================================================

        //=================================================
        //Visualizzo (Utile per Debug) :P
        visualizzaMatrice(matrice);
        //System.out.print("\n");
        //=================================================

        //Creo una nuova matrice (Adiacenza)
        int [][] matrice_adia = new int[NUMERO_ELEMENTI][NUMERO_ELEMENTI];

        //Converto la matrice dal mio generatore a matrice adiacenza
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                //Triangolo inferiore
                if (i>j){
                    if (matrice[i][j]>0) {
                        matrice_adia[i][j]=matrice[i][j];
                    }
                    else {
                        matrice_adia[i][j]=0;
                    }
                }
                //Triangolo superiore
                else if (i<j){
                    if (matrice[i][j]<0) {
                        matrice_adia[i][j]=Math.abs(matrice[i][j]);
                    }
                    else {
                        matrice_adia[i][j]=0;
                    }
                }
                //Diagonale
                else {
                    matrice_adia[i][j]=0;
                }
            }
        }


        //=======================================================================
        //Fix 2 column (Per fare in modo che tutte le somme siano giuste)
        //=======================================================================

        int somma=0;
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            somma-=matrice_adia[i][1];
        }
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            somma+=matrice_adia[1][i];
        }

        //System.out.println(somma);

        //Visualizza
        //visualizzaMatrice(matrice_adia);
        //System.out.print("\n");


        //matrice_adia[][];



        //visualizzaMatrice(matrice_adia);

        if(matrice_adia[1][NUMERO_ELEMENTI-2]==0){
            matrice_adia[NUMERO_ELEMENTI-2][1]+=somma;
        }
        else {
            matrice_adia[1][NUMERO_ELEMENTI-2]-=somma;
        }

        if(matrice_adia[1][NUMERO_ELEMENTI-2]<0){
            matrice_adia[NUMERO_ELEMENTI-2][1]=Math.abs(matrice_adia[1][NUMERO_ELEMENTI-2]);
            matrice_adia[1][NUMERO_ELEMENTI-2]=0;
        }

        if(matrice_adia[NUMERO_ELEMENTI-2][1]<0){
            matrice_adia[1][NUMERO_ELEMENTI-2]=Math.abs(matrice_adia[NUMERO_ELEMENTI-2][1]);
            matrice_adia[NUMERO_ELEMENTI-2][1]=0;
        }

        //Visualizza
        //visualizzaMatrice(matrice_adia);
        /*
        for (int i = 0; i < NUMERO_ELEMENTI/2; i++) {
            //System.out.println(matrice_adia[(NUMERO_ELEMENTI-1)-i][i]+"+"+matrice_adia[i][(NUMERO_ELEMENTI-1)-i]);
            if(matrice_adia[(NUMERO_ELEMENTI-1)-i][i]==matrice_adia[i][(NUMERO_ELEMENTI-1)-i]){
                if(matrice_adia[((NUMERO_ELEMENTI-1)-i)-1][i]==0){
                    matrice_adia[((NUMERO_ELEMENTI-1)-i)-1][i]=1;
                    matrice_adia[((NUMERO_ELEMENTI-1)-i)][i+1]=1;
                    matrice_adia[i][(NUMERO_ELEMENTI-1)-i]=1;
                }
                else {
                    matrice_adia[i][((NUMERO_ELEMENTI-1)-i)-1]=matrice_adia[((NUMERO_ELEMENTI-1)-i)-1][i];
                    matrice_adia[i+1][((NUMERO_ELEMENTI-1)-i)]=matrice_adia[((NUMERO_ELEMENTI-1)-i)][i+1];
                    matrice_adia[((NUMERO_ELEMENTI-1)-i)-1][i]=0;
                    matrice_adia[((NUMERO_ELEMENTI-1)-i)][i+1]=0;
                    int somma1=0;
                    for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                        somma1-=matrice_adia[j][(NUMERO_ELEMENTI-1)-i];
                    }
                    for (int j = 0; j < NUMERO_ELEMENTI;j++) {
                        somma1+=matrice_adia[(NUMERO_ELEMENTI-1)-i][j];
                    }
                    matrice_adia[(NUMERO_ELEMENTI-1)-i][i]+=somma1;
                    if(matrice_adia[(NUMERO_ELEMENTI-1)-i][i]<0){
                        matrice_adia[i][(NUMERO_ELEMENTI-1)-i]=matrice_adia[(NUMERO_ELEMENTI-1)-i][i];
                        matrice_adia[(NUMERO_ELEMENTI-1)-i][i]=0;
                    }
                }
            }
        }
        */

        //=======================================================================
        //Controllo numero collegamenti
        //=======================================================================

        somma=0;
        for (int i = 1; i < NUMERO_ELEMENTI; i++) {
            somma+=i;
        }
        int cont=0;
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                if(matrice_adia[i][j]!=0){
                    cont++;
                }
            }
        }
        //System.out.println(cont);
        if(cont!=somma){
            matrice_adia[0][0]=-1;
        }
        //=======================================================================

        //=======================================================================
        //Return matrice Adiacenza
        return matrice_adia;
        //=======================================================================
    }

    private static void visualizzaMatrice(int[][] matrice) {
        //Visualizza
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                //System.out.print(" " + matrice[i][j] + "\t");
            }
            //System.out.print("\n");
        }
    }

    /**
     * <h1> generatoreBordoMatrice </h1>
     * <p> Metodo che genera il bordo della matrice seguendo uno schema prefissato </p>
     * <b> Nota: Funziona solo dall' esterno verso l' interno </b>
     * @author Thomas Causetti
     * @param rand java.util.Random
     * @param matrice e' la matrice da modificare
     * @param dim_rig_col Indica la se ci sono state iterazioni precedenti di questo metodo e quindi il metodo deve fare un bordo piu'
     *                    interno.
     */
    private static boolean generatoreBordoMatrice(Random rand, int[][] matrice, int dim_rig_col) {

        //=================================================
        final int UNO_P=1+dim_rig_col;
        final int ZERO_P=0+dim_rig_col;
        final int NUMERO_ELEMENTI_P=NUMERO_ELEMENTI-dim_rig_col;
        //Generatore numeri casuali
        int somma=0; //somma numeri (serve per dopo)
        int temp_somma=0;
        for (int i = NUMERO_ELEMENTI_P-1; i > (1+dim_rig_col); i--) {
            //Da + VALORE_MAX_RANDOM_NUM a - VALORE_MAX_RANDOM_NUM
            int temp;

            //Controlla che il numero random sia diverso da 0
            do {
                temp=rand.nextInt((2 * VALORE_MAX_RANDOM_NUM) + 1) - VALORE_MAX_RANDOM_NUM;
                temp_somma+=temp;
            } while(temp==0);

            //se bordo esterno e somma == 0 e i == al ultimo valore
            //per evitare che la somma dei numeri random = 0
            if(temp_somma==0 && i==(2+dim_rig_col)){
                //Inverto segno
                temp=0-temp;
            }

            matrice[i][ZERO_P] = temp;

            if (matrice[i][ZERO_P] == 0) {
                matrice[i][ZERO_P]=1;
            }
            //Rendi simmetrica
            matrice[ZERO_P][i]= matrice[i][ZERO_P];
            //System.out.println((NUMERO_ELEMENTI_P-1)+" + "+((NUMERO_ELEMENTI_P-1)-i));
            matrice[NUMERO_ELEMENTI_P-1][(NUMERO_ELEMENTI_P-1+dim_rig_col)-i]= matrice[i][ZERO_P];
            //System.out.println(((NUMERO_ELEMENTI_P-1)-i)+" + "+(NUMERO_ELEMENTI_P-1));
            matrice[(NUMERO_ELEMENTI_P-1+dim_rig_col)-i][NUMERO_ELEMENTI_P-1]= matrice[i][ZERO_P];

            somma+= matrice[i][ZERO_P];
        }


        //=================================================


        if (dim_rig_col!=0) {
            somma=0;
            //ZERO_P-1 Colonna precedente
            int cont=0;
            do {
                somma+=matrice[UNO_P][cont];
                cont++;
            }while(cont < ZERO_P);
        }

        if (somma==0){
            //non corretto
            //System.out.println("E1");
            return false;
        }


        //=================================================
        //Calcolo primo valore colonna
        matrice[UNO_P][ZERO_P]=0;
        matrice[UNO_P][ZERO_P]-=somma;
        matrice[ZERO_P][UNO_P]= matrice[UNO_P][ZERO_P];
        matrice[(NUMERO_ELEMENTI_P-2)][NUMERO_ELEMENTI_P-1]= matrice[UNO_P][ZERO_P];
        matrice[(NUMERO_ELEMENTI_P-1)][NUMERO_ELEMENTI_P-2]= matrice[UNO_P][ZERO_P];
        //=================================================

        somma=0;

        //TODO
        if (dim_rig_col!=0) {
            matrice[NUMERO_ELEMENTI_P-1][ZERO_P]=0;
            for (int i = 0; i < NUMERO_ELEMENTI; i++) {
                somma+=matrice[i][ZERO_P];
            }
            matrice[NUMERO_ELEMENTI_P-1][ZERO_P]=-somma;
            matrice[ZERO_P][NUMERO_ELEMENTI_P-1]=matrice[NUMERO_ELEMENTI_P-1][ZERO_P];
            if (somma==0 && NUMERO_ELEMENTI>5 ){
                //non corretto
                //System.out.println("E2");
                return false;
            }
        }


        int temp=somma;
        //TODO controlla 3 - 4
        if (((dim_rig_col!=0 && somma==0) || (dim_rig_col==1)) && NUMERO_ELEMENTI>4 ){
            somma=(0-matrice[1][0]);
            for (int i = UNO_P; i < NUMERO_ELEMENTI; i++) {
                somma+=matrice[i][1];
            }
            if(somma==matrice[NUMERO_ELEMENTI-2][1] || temp==0){
                //non corretto
                int index1=NUMERO_ELEMENTI_P-2;
                int index2=NUMERO_ELEMENTI_P-1;
                matrice[index1][ZERO_P]=(0-matrice[index1][1]);
                matrice[index2][ZERO_P+1]=matrice[index1][1];
                matrice[ZERO_P][index1]=matrice[index1][1];
                matrice[ZERO_P+1][index2]=matrice[index1][1];
                //System.out.println("E3");
                //return false;
            }
        }



        return true;
    }

    public Squadra getSquadra_uno() {
        return squadra_uno;
    }

    public Squadra getSquadra_due() {
        return squadra_due;
    }
}
