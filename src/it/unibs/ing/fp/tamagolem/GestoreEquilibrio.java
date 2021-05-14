package it.unibs.ing.fp.tamagolem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * <h1>GestoreEquilibrio classe che gestisce l' equilibrio degli elementi</h1>
 * <p>Metodo Principale: equilibrio</p>
 * @author Thomas Causetti
 */
public class GestoreEquilibrio {

    private final int NUMERO_ELEMENTI; //Mai Minore di 5
    private static final int VALORE_MAX_RANDOM_NUM = 3; //incide solo sui numeri random, ma non su quelli calcolati
    /*
    Per il parametro V: il mio metodo controlla il VALORE_MAX_RANDOM_NUM solo dei random, ma comunque essendo
    che i valori calcolati dipendono dai numeri random (perche' e' necessario bilanciare la matrice, sono somme e sottrazioni),
    ho deciso di non implementare V come variabile, visto che matematicamente, i random possono avere un valore massimo
    di VALORE_MAX_RANDOM_NUM e quindi il valore finale non può essere mai maggiore di un determinato numero.

    I valori massimi che puo' raggiungere V (in base alla dimensione della matrice e a VALORE_MAX_RANDOM_NUM) sono:
    Matrice:3 = V:9, 4=24, 5=42, 6=67, 7=90, 8=126, 9=152, 10=185

    */

    /**
     * Costruttore
     * @author Thomas Causetti
     * @param numero_elementi numero elementi
     */
    public GestoreEquilibrio(int numero_elementi) {
        NUMERO_ELEMENTI = numero_elementi;
    }

    /**
     * Test main
     * @param args
     * @author Thomas Causetti
     */
    public static void main(String[] args) {

        GestoreEquilibrio g_equilibrio = new GestoreEquilibrio(5);

        int matrice[][];
        int cont=0;
        for (int i = 0; i < 1000; i++) {

            matrice=g_equilibrio.equilibrioMatrice();
            g_equilibrio.visualizzaMatrice(matrice);

            //Controllo disattivato per efficienza
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
     * <b> Metodo di generazione dell' equilibrio </b>
     * <p>Metodo che crea equilibrio, richiamando il metodo che genera la matrice di un grafo, assegna la matrice ad
     *  Arraylist di tipi e salva i dati all' interno dei TreeMap di Arco in ogni Tipo</p>
     *  @author Thomas Causetti
     */
    public ArrayList<Tipo> equilibrio(){

        ArrayList<Tipo> tipi = new ArrayList<>();

        int matrice[][];

        matrice = equilibrioMatrice();

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


        Collections.shuffle(tipi);

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

        return tipi;
    }


    /**
     * <h1>Controllo Numero Collegamenti</h1>
     * <p>Controlla che il numero di collegamenti sia corretto, altrimenti mette -1 a Matrice[0][0]</p>
     * <p>(Utilizzato solo in testing)</p>
     * @author Thomas Causetti
     * @param matrice_adia matrice di adiacenza
     */
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

    /**
     * Visualizza una matrice utile per debug
     * @author Thomas Causetti
     * @param matrice matrice generale
     */
    private void visualizzaMatrice(int[][] matrice) {
        //Visualizza
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                System.out.print(" " + matrice[i][j] + "\t");
            }
            System.out.print("\n");
        }
    }



    //Nuovo generatore Test

    /**
     * <h1> equilibrioMatrice </h1>
     * <p> Metodo che crea la matrice (di adiacenza per generare il grafo)</p>
     *  es:
     *  0	0	2	0	1
     *  2	0	0	0	4
     *  0	3	0	1	0
     *  1	3	0	0	0
     *  0	0	2	3	0
     *
     * <p>Nota: I for sono ottimizzati in modo che, singolarmente, non passino per tutta la matrice visto che non è necessario</p>
     * <p>Nota 2: terza versione, nella repository e' possibile trovare le vecchie versioni in Altro\EquilibrioOld.txt</p>
     * @author Thomas Causetti
     * @return matrice adiacenza
     */
    private int[][] equilibrioMatrice() {

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


            //somma valori precedenti
            for (int j = 0; j < (i + 1); j++) {
                temp_somma -= matrice[i][j];
            }
            for (int j = 0; j < (i + 1); j++) {
                temp_somma += matrice[j][i];
            }

            for (int k = 1 + i; k < NUMERO_ELEMENTI - 2; k++) {
                int temp;
                //Genero random diverso da 0
                do {
                    temp = rand.nextInt((2 * VALORE_MAX_RANDOM_NUM) + 1) - VALORE_MAX_RANDOM_NUM;
                    temp_somma += temp;
                } while (temp == 0);

                //li genero sia positivi che negativi per scegliere se inserirli nel triangolo superiore o inferiore
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

                    //Genero random diverso da 0
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
        /*
            Prima:
             0	 0	 0	 0	 8
             3	 0	 0	 1	 0
             2	 2	 0	 2	 0
             3	 0	 0	 0	|0|
             0	 2	 6	|0|	 0

            Dopo:
             0	 0	 0	 0	 9
             3	 0	 0	 1	 0
             2	 2	 0	 2	 0
             4	 0	 0	 0	|0|
             0	 2	 6	|1|  0

            i due |0|.

            Questo metodo e' in grado di sistemarlo senza rigenerare la matrice
        */

        if (matrice[NUMERO_ELEMENTI - 2][NUMERO_ELEMENTI - 1]==0 && matrice[NUMERO_ELEMENTI - 1][NUMERO_ELEMENTI - 2]==0) {

            int temp_somma;
            do {
                //Capisce se il valore da modificare si trova nella parte superiore o inferiore
                //il valore non centra con i 2 zeri, ma e' per bilanciare la matrice
                if (matrice[NUMERO_ELEMENTI - 2][0] == 0) {
                    matrice[0][NUMERO_ELEMENTI - 2]++;
                } else if (matrice[0][NUMERO_ELEMENTI - 2] == 0) {
                    matrice[NUMERO_ELEMENTI - 2][0]++;
                }

                temp_somma = 0;

                //Calcolo somma per bilanciare matrice
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

            //Calcolo somma per bilanciare matrice
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

        //Disabilitato il controllo che mette matrice[0][0]=-1 se non corretto
        //Per efficienza
        //controlloNumeroCollegamenti(matrice);
        return matrice;

    }

}
