package it.unibs.ing.fp.tamagolem;
import it.unibs.ing.fp.mylib.InputDati;
import it.unibs.ing.fp.mylib.MyMenu;


public class Main {

    public static final String MSG_PRIMO_ALLENATORE = "Inserisci il nome del primo allenatore: ";
    public static final String MSG_SECONDO_ALLENATORE = "Inserisci il nome del secondo allenatore: ";
    public static final String MSG_EXIT = "Grazie per aver giocato \n Arrivederci!!";

    public static void main(String[] args) {

        String[] menu1 = new String[]{"Inizia una nuova partita"};
        String[] menu2 = new String[]{"Rivincita con gli stessi allenatori","Nuova partita con nuovi personaggi"};

        MyMenu m1 = new MyMenu("Menu iniziale",menu1);
        MyMenu m2 = new MyMenu("Menu",menu2);
        String p1,p2;
        int scelta = m1.scegli();
        if(scelta == 1){
            p1 = InputDati.leggiStringaNonVuota(MSG_PRIMO_ALLENATORE);
            p2 = InputDati.leggiStringaNonVuota(MSG_SECONDO_ALLENATORE);
            Squadra q1 = new Squadra(new Combattente(p1));
            Squadra q2 = new Squadra(new Combattente(p2));
            Partita a = new Partita(q1,q2);
            a.startGame();
            do{
                scelta = m2.scegli();
                switch (scelta){
                    case 1:
                        //Rivincita
                        a.startGame();
                        break;
                    case 2:
                        //Nuova partita
                        p1 = InputDati.leggiStringaNonVuota(MSG_PRIMO_ALLENATORE);
                        p2 = InputDati.leggiStringaNonVuota(MSG_SECONDO_ALLENATORE);
                        a.startGame();
                        break;
                }
            }while (scelta!=0);
        }
        System.out.println(MSG_EXIT);
    }

}
