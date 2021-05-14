package it.unibs.ing.fp.tamagolem;
import java.util.ArrayList;
import java.util.Collections;
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
            matrice=g_equilibrio.equilibrioMatrice();
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



        /*for (Tipo dir : tipi) {
            System.out.println(dir);
        }*/

        return tipi;
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



    //Nuovo generatore Test
    public int[][] equilibrioMatrice() {

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
