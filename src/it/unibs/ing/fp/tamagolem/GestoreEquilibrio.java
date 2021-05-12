package it.unibs.ing.fp.tamagolem;
import java.util.ArrayList;
import java.util.Random;



public class GestoreEquilibrio {

    private final int NUMERO_ELEMENTI; //Mai Minore di 5
    private static final int VALORE_MAX_RANDOM_NUM = 3; //incide solo sui numeri random, ma non su quelli calcolati

    public GestoreEquilibrio(int numero_elementi) {
        NUMERO_ELEMENTI = numero_elementi;
    }

    /**
     * Test main
     * @param args
     */
    public static void main(String[] args) {
       // ArrayList<Tipo> tipi = equilibrio();

        GestoreEquilibrio g_equilibrio = new GestoreEquilibrio(5);

        int matrice[][];
        int cont=0;
        for (int i = 0; i < 1000; i++) {
            //TODO controllo disattivato per efficienza
            matrice=g_equilibrio.equilibrioMatrice1();
            g_equilibrio.visualizzaMatrice(matrice);
            //Controllo
            /*int somma1;
            for (int k = 0; k < g_equilibrio.NUMERO_ELEMENTI; k++) {
                somma1=0;
                for (int j = 0; j < g_equilibrio.NUMERO_ELEMENTI; j++) {
                    somma1-=matrice[k][j];
                }
                for (int j = 0; j < g_equilibrio.NUMERO_ELEMENTI;j++) {
                    somma1+=matrice[j][k];
                }
                if(somma1!=0){
                    cont++;
                    System.out.println("errore");
                    break;
                }
            }
            System.out.println();


            if(matrice[0][0]==-1){
               cont++;
            }*/
        }
        System.out.println(cont);

        ArrayList<Tipo> tipi = g_equilibrio.equilibrio();
        System.out.println(tipi);
    }

    /**
     * <h1> equilibrio </h1>
     * @author Thomas Causetti
     * <b> Metodo di generazione dell' equilibrio </b>
     */
    public ArrayList<Tipo> equilibrio(){

        ArrayList<Tipo> tipi = new ArrayList<>();

        int matrice[][];

        matrice = equilibrioMatrice1();



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

    public int[][] equilibrioMatrice(){

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

        controlloNumeroCollegamenti(matrice_adia);
        //=======================================================================

        //=======================================================================
        //Return matrice Adiacenza
        return matrice_adia;
        //=======================================================================
    }

    private void controlloNumeroCollegamenti(int[][] matrice_adia) {
        int somma;
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
    }

    private void visualizzaMatrice(int[][] matrice) {
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
    private boolean generatoreBordoMatrice(Random rand, int[][] matrice, int dim_rig_col) {

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
            System.out.println("E1");
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
                System.out.println("E2");
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

    //Nuovo generatore Test
    public int[][] equilibrioMatrice1() {

        //=================================================
        //Inizializzo generatore N random
        Random rand = new Random();

        //=================================================
        //Creo nuova matrice
        int[][] matrice = new int[NUMERO_ELEMENTI][NUMERO_ELEMENTI];

        //=================================================
        //Diagonale di zeri e calcolo

        for (int i = 0; i < NUMERO_ELEMENTI-1; i++) {
            int temp_somma = 0;
            //Diagonale di zeri
            matrice[i][i] = 0;


            for (int j = 0; j < (i + 1); j++) {
                temp_somma -= matrice[i][j];
            }
            for (int j = 0; j < (i + 1); j++) {
                temp_somma += matrice[j][i];
            }

            for (int k = 1 + i; k < NUMERO_ELEMENTI - 2; k++) {
                int temp;
                do {
                    temp = rand.nextInt((2 * VALORE_MAX_RANDOM_NUM) + 1) - VALORE_MAX_RANDOM_NUM;
                    temp_somma += temp;
                } while (temp == 0);
                if (temp < 0) {
                    matrice[i][k] = Math.abs(temp);
                    matrice[k][i] = 0;
                } else {
                    matrice[k][i] = Math.abs(temp);
                    matrice[i][k] = 0;
                }
            }

            int temp;
            if (i!=NUMERO_ELEMENTI-2){

                //controllo no somma zero
                int temp_somma1;
                do {
                    temp_somma1 = temp_somma;

                    do {
                        temp = rand.nextInt((2 * VALORE_MAX_RANDOM_NUM) + 1) - VALORE_MAX_RANDOM_NUM;
                        temp_somma1 += temp;
                    } while (temp == 0);

                } while (temp_somma1 == 0);
                temp_somma += temp;

                if (temp < 0) {
                    matrice[i][NUMERO_ELEMENTI - 2] = Math.abs(temp);
                    matrice[NUMERO_ELEMENTI - 2][i] = 0;
                } else {
                    matrice[NUMERO_ELEMENTI - 2][i] = Math.abs(temp);
                    matrice[i][NUMERO_ELEMENTI - 2] = 0;
                }
            }


            //Ultimo valore
            if (temp_somma > 0) {
                matrice[i][NUMERO_ELEMENTI - 1] = Math.abs(temp_somma);
                matrice[NUMERO_ELEMENTI - 1][i] = 0;
            } else if (temp_somma < 0) {
                matrice[NUMERO_ELEMENTI - 1][i] = Math.abs(temp_somma);
                matrice[i][NUMERO_ELEMENTI - 1] = 0;
            }

        }

        //Caso particolare fixer
        if (matrice[NUMERO_ELEMENTI - 2][NUMERO_ELEMENTI - 1]==0 && matrice[NUMERO_ELEMENTI - 1][NUMERO_ELEMENTI - 2]==0) {

            int temp_somma;
            do {
                if (matrice[NUMERO_ELEMENTI - 2][0] == 0) {
                    matrice[0][NUMERO_ELEMENTI - 2]++;
                } else if (matrice[0][NUMERO_ELEMENTI - 2] == 0) {
                    matrice[NUMERO_ELEMENTI - 2][0]++;
                }

                temp_somma = 0;

                for (int j = 0; j < NUMERO_ELEMENTI - 1; j++) {
                    temp_somma -= matrice[0][j];
                }
                for (int j = 0; j < NUMERO_ELEMENTI - 1; j++) {
                    temp_somma += matrice[j][0];
                }
            }while(temp_somma==0);

            //Ultimo valore prima riga
            if (temp_somma > 0) {
                matrice[0][NUMERO_ELEMENTI - 1] = Math.abs(temp_somma);
                matrice[NUMERO_ELEMENTI - 1][0] = 0;
            } else if (temp_somma < 0) {
                matrice[NUMERO_ELEMENTI - 1][0] = Math.abs(temp_somma);
                matrice[0][NUMERO_ELEMENTI - 1] = 0;
            }

            temp_somma=0;

            for (int j = 0; j < NUMERO_ELEMENTI-1; j++) {
                temp_somma -= matrice[NUMERO_ELEMENTI-2][j];
            }
            for (int j = 0; j < NUMERO_ELEMENTI-1; j++) {
                temp_somma += matrice[j][NUMERO_ELEMENTI-2];
            }

            //Ultimo valore penultima riga
            if (temp_somma > 0) {
                matrice[NUMERO_ELEMENTI-2][NUMERO_ELEMENTI - 1] = Math.abs(temp_somma);
                matrice[NUMERO_ELEMENTI - 1][NUMERO_ELEMENTI-2] = 0;
            } else if (temp_somma < 0) {
                matrice[NUMERO_ELEMENTI - 1][NUMERO_ELEMENTI-2] = Math.abs(temp_somma);
                matrice[NUMERO_ELEMENTI-2][NUMERO_ELEMENTI - 1] = 0;
            }
        }

        //TODO controlla
        //controlloNumeroCollegamenti(matrice);
        return matrice;

    }

}
