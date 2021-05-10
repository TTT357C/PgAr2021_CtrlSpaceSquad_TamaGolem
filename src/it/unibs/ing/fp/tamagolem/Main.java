package it.unibs.ing.fp.tamagolem;
import java.util.ArrayList;
import java.util.Random;



public class Main {

    private static final int NUMERO_ELEMENTI = 7; //Mai Minore di 3
    private static final int VALORE_MAX_RANDOM_NUM = 3; //incide solo sui numeri random, ma non su quelli calcolati

    public static void main(String[] args) {
        ArrayList<Tipo> tipi = equilibrio();
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
            System.out.println(dir);
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
        for (int i = 0; i < (NUMERO_ELEMENTI/2); i++) {
            generatoreBordoMatrice(rand, matrice, i);
        }
        //=================================================

        //=================================================
        //Visualizzo (Utile per Debug) :P
        //visualizzaMatrice(matrice);
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

        //=======================================================================
        //Controllo numero collegamenti
        //=======================================================================

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
        visualizzaMatrice(matrice_adia);


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
                System.out.print(" " + matrice[i][j] + "\t");
            }
            System.out.print("\n");
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
    private static void generatoreBordoMatrice(Random rand, int[][] matrice, int dim_rig_col) {

        //=================================================
        final int UNO_P=1+dim_rig_col;
        final int ZERO_P=0+dim_rig_col;
        final int NUMERO_ELEMENTI_P=NUMERO_ELEMENTI-dim_rig_col;
        //Generatore numeri casuali
        int somma=0; //somma numeri (serve per dopo)
        for (int i = NUMERO_ELEMENTI_P-1; i > (1+dim_rig_col); i--) {
            //Da + VALORE_MAX_RANDOM_NUM a - VALORE_MAX_RANDOM_NUM
            matrice[i][ZERO_P]= rand.nextInt((2*VALORE_MAX_RANDOM_NUM) + 1)- VALORE_MAX_RANDOM_NUM;
            if (matrice[i][ZERO_P] == 0) {
                matrice[i][ZERO_P]=1;
            }
            //Rendi simmetrica
            matrice[ZERO_P][i]= matrice[i][ZERO_P];
            System.out.println((NUMERO_ELEMENTI_P-1)+" + "+((NUMERO_ELEMENTI_P-1)-i));
            matrice[NUMERO_ELEMENTI_P-1][(NUMERO_ELEMENTI_P-1+dim_rig_col)-i]= matrice[i][ZERO_P];
            System.out.println(((NUMERO_ELEMENTI_P-1)-i)+" + "+(NUMERO_ELEMENTI_P-1));
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
        //=================================================
        //Calcolo primo valore colonna
        matrice[UNO_P][ZERO_P]=0;
        matrice[UNO_P][ZERO_P]-=somma;
        matrice[ZERO_P][UNO_P]= matrice[UNO_P][ZERO_P];
        matrice[(NUMERO_ELEMENTI_P-2)][NUMERO_ELEMENTI_P-1]= matrice[UNO_P][ZERO_P];
        matrice[(NUMERO_ELEMENTI_P-1)][NUMERO_ELEMENTI_P-2]= matrice[UNO_P][ZERO_P];
        //=================================================

        somma=0;

        if (dim_rig_col!=0) {
            matrice[NUMERO_ELEMENTI_P-1][ZERO_P]=0;
            for (int i = 0; i < NUMERO_ELEMENTI; i++) {
                somma+=matrice[i][ZERO_P];
            }
            matrice[NUMERO_ELEMENTI_P-1][ZERO_P]=-somma;
            matrice[ZERO_P][NUMERO_ELEMENTI_P-1]=matrice[NUMERO_ELEMENTI_P-1][ZERO_P];
        }
    }

}
