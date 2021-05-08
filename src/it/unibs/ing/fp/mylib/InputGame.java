package it.unibs.ing.fp.mylib;
import java.util.*;

public class InputGame {
    private static Scanner lettore = creaScanner();

    private final static String ERRORE_FORMATO = " Attenzione: il dato inserito non e' nel formato corretto";

    private static Scanner creaScanner ()
    {
        Scanner creato = new Scanner(System.in);
        return creato;
    }

    public static int scegliDifficolta(){
        boolean finito = false;
        int valoreLetto = 0;
        do
        {
            System.out.println("------------------------");
            System.out.println("0. Facile");
            System.out.println("1. Intermedio");
            System.out.println("2. Difficile");
            System.out.print("Scegli difficolta: ");
            try
            {
                valoreLetto = lettore.nextInt();
                finito = true;
                if(!(finito && valoreLetto>=0 && valoreLetto<3)){
                    finito=false;
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println(ERRORE_FORMATO);
                @SuppressWarnings("unused")
                String daButtare = lettore.next();
            }

        } while (!finito);
        return valoreLetto;
    }

}
