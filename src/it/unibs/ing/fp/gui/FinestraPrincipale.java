package it.unibs.ing.fp.gui;

import it.unibs.ing.fp.tamagolem.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Thomas Causetti
 */
public class FinestraPrincipale extends JFrame {

    //=========================================================================================
    //Costanti di Testo
    //=========================================================================================

    public static final String INSERIRE_LE_PIETRE_NEL_TAMAGOLEM = "Selezionare le pietre (Necessario piu' di una)";
    public static final String AVETE_USATO_LO_STESSO_TIPO = "AVETE USATO LO STESSO TIPO E NON HA CREATO CONSEGUENZE";
    public static final String NUOVA_PARTITA = "Nuova Partita";
    public static final String NOMI_DELLA_PARTITA_PRECEDENTE = "Vuoi usare i nomi della partita precedente?";
    public static final String DIMMI_GIOCATORE_1 = "Dimmi il nome del giocatore1: ";
    public static final String DIMMI_GIOCATORE_2 = "Dimmi il nome del giocatore2: ";

    //=========================================================================================
    //Componenti interfaccia
    //=========================================================================================
    private JPanel mainPanel;
    private JPanel p1;
    private JPanel p2;
    private JLabel player1_img;
    private JLabel player2_img;
    private JProgressBar progressBar_2;
    private JProgressBar progressBar_1;
    private JButton attack1;
    private JButton freccia_destra;
    private JButton freccia_sinistra;
    private JButton freccia_destra2;
    private JButton freccia_sinistra2;
    private JLabel nomeGiocatore2;
    private JLabel nomeGiocatore1;
    private JPanel p1_ui;
    private JPanel p2_ui;
    private JToolBar menu_principale;
    private JLabel pietra1;
    private JLabel pietra2;
    private JLabel testo;
    private JPanel testo_panel;
    private JButton menu;
    private JButton menu2;
    private JButton menu3;
    private JButton conferma1;
    private JButton conferma2;
    private JProgressBar numeroTama;
    private JProgressBar numeroTama2;
    //=========================================================================================

    //=========================================================================================
    //Numero elementi
    private int numero_elementi = 0;
    //=========================================================================================

    //=========================================================================================
    //Variabili per pietre
    private int pietra_attuale;
    private int pietra_attuale2;
    private int cont_pietre;
    //=========================================================================================

    //=========================================================================================
    //Variabili per partita
    private Partita partita;
    private ArrayList<Tipo> tipi;
    private ArrayList<Pietra> scorta_comune;
    //=========================================================================================

    private boolean fase_inizial;

    //utile per riprendere i due valori alla creazione di una nuova partita
    private static String nomegiocatore1;
    private static String nomegiocatore2;

    /**
     * <h1> Finestra Principale </h1>
     * <p> Metodo che crea la finestra principale della GUI </p>
     * <p><b> Nota: parte del codice della finestra e' stato generato da Intellij IDEA</b></p>
     *
     * @author Thomas Causetti
     */
    public FinestraPrincipale() {

        fase_inizial = true;

        inizializzaTema("Metal");

        //Inizializza finestra
        inizializzaFinestra();

        //disabilito tutti i bottoni non necessari all' avvio
        disableAllButtons();

        //set default immagine
        pietra_attuale = -1;
        pietra_attuale2 = -1;
        setPietraP1Img(pietra_attuale + "");
        setPietraP2Img(pietra_attuale + "");

        //Colori
        setColori();

        //bordi
        setBordi();

        //set Immagini Tamagolem
        setImgTama();

        //set progressbar
        progressBar_1.setValue(0);
        progressBar_2.setValue(0);

        numeroTama.setValue(0);
        numeroTama2.setValue(0);


        //aggiungo tutti i componenti a finestra
        this.add(mainPanel);

        //rendo visibile
        this.setVisible(true);

        inizializzaTema("Nimbus");

        //====================================================

        //Finestra di benvenuto/spiegazione
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<p><font size=\"5\"> Per iniziare una nuova partita puoi selezionare <b>nuova partita</b> dal menu qui sopra.</font></p>");
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, textPane, "Benvenuto!", JOptionPane.INFORMATION_MESSAGE);


        //=======================================================================
        //Inizio ActionListener
        //=======================================================================

        //=======================================================================
        //Nuova Partita
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setEnabled(false);

                int scelta = 1;
                if (nomegiocatore1 != null) {
                    scelta = JOptionPane.showConfirmDialog(null, NOMI_DELLA_PARTITA_PRECEDENTE, NUOVA_PARTITA, JOptionPane.YES_NO_OPTION);
                }
                String nome1;
                String nome2;

                if (scelta == 1) {
                    nome1 = JOptionPane.showInputDialog(null, DIMMI_GIOCATORE_1, NUOVA_PARTITA, JOptionPane.INFORMATION_MESSAGE);
                    nome2 = JOptionPane.showInputDialog(null, DIMMI_GIOCATORE_2, NUOVA_PARTITA, JOptionPane.INFORMATION_MESSAGE);
                    nomegiocatore1 = nome1;
                    nomegiocatore2 = nome2;
                } else {
                    nome1 = nomegiocatore1;
                    nome2 = nomegiocatore2;
                }


                //Set GUI nomi giocatori
                nomeGiocatore1.setText(nome1);
                nomeGiocatore2.setText(nome2);

                //Creo Squadre
                Squadra q1 = new Squadra(new Combattente(nome1));
                Squadra q2 = new Squadra(new Combattente(nome2));

                //Creo Partita
                partita = new Partita(q1, q2);

                //====================================================
                //JoptionPane con 4 bottoni per scelta difficolta'
                //====================================================

                String[] buttons = {"Facile", "Medio", "Difficile", "Caotico"};
                int returnValue = JOptionPane.showOptionDialog(null, "Seleziona difficolta':", NUOVA_PARTITA, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
                System.out.println(returnValue);

                int numero_ele;
                switch (returnValue) {
                    case 0:
                        numero_ele = 5;
                        break;
                    case 1:
                        numero_ele = 7;
                        break;
                    case 2:
                        numero_ele = 9;
                        break;
                    case 3:
                        numero_ele = 10;
                        break;
                    default:
                        numero_ele = 6;
                        break;
                }

                //====================================================
                //Inizializzo costanti gioco
                partita.inizializzazioneGUI(numero_ele);

                //====================================================
                //Creo Equilibrio
                GestoreEquilibrio g_equilibrio = new GestoreEquilibrio(numero_ele);
                tipi = g_equilibrio.equilibrio();

                //====================================================

                //====================================================
                //Creo la scorta comune
                scorta_comune = partita.generaScortaComune(tipi);
                //====================================================

                //porto le pietre a zero prima -1
                pietra_attuale++;
                pietra_attuale2++;

                //mi salvo il numero degli elementi
                numero_elementi = numero_ele;

                numeroTama.setMaximum(partita.getSquadra_uno().getTamagolems().size());
                numeroTama2.setMaximum(partita.getSquadra_due().getTamagolems().size());
                numeroTama.setValue(partita.getSquadra_uno().getTamagolems().size());
                numeroTama2.setValue(partita.getSquadra_due().getTamagolems().size());
                numeroTama.setString(partita.getSquadra_uno().getTamagolems().size() + "");
                numeroTama2.setString(partita.getSquadra_due().getTamagolems().size() + "");

                //attivo bottoni necessari
                abilitaBottoniP1();
                setPietraP1(tipi.get(0).name());
                tutorialSceltaPietre();
            }
        });
        //=======================================================================

        //=======================================================================
        //GESTISCI visualizzatore pietre
        //=======================================================================
        freccia_sinistra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                pietra_attuale--;
                if (pietra_attuale < 0) {
                    pietra_attuale = numero_elementi - 1;
                }
                setPietraP1(tipi.get(pietra_attuale).name());

            }
        });
        freccia_destra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale++;
                if (pietra_attuale >= numero_elementi) {
                    pietra_attuale = 0;
                }
                setPietraP1(tipi.get(pietra_attuale).name());
            }
        });
        freccia_sinistra2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale2--;
                if (pietra_attuale2 < 0) {
                    pietra_attuale2 = numero_elementi - 1;
                }
                setPietraP2(tipi.get(pietra_attuale2).name());
            }
        });
        freccia_destra2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale2++;
                //cerca il prossimo elemento non a zero
                if (pietra_attuale2 >= numero_elementi) {
                    pietra_attuale2 = 0;
                }
                setPietraP2(tipi.get(pietra_attuale2).name());
            }
        });
        //=======================================================================

        //=======================================================================
        //Visualizza la storia
        menu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizzaStoria();
            }
        });
        //=======================================================================

        //=======================================================================
        //Crediti
        menu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon = new ImageIcon("Altro/Logo CTRL_SPACE_SQUAD.jpeg"); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back
                JOptionPane.showMessageDialog(null, " Creato da Causetti Thomas, Rossi Mirko e Visini Mattia", "Credits", JOptionPane.INFORMATION_MESSAGE, imageIcon);
            }
        });
        //=======================================================================

        //=======================================================================
        //Pietra Scelta 1
        conferma1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scorta_comune.get(pietra_attuale).getQuantita_pietra() > 0) {

                    evoluzioneGUI(partita.getSquadra_uno(), pietra_attuale);
                    setPietraP1(tipi.get(pietra_attuale).name());
                    cont_pietre++;
                    //Se tutte pietre sono state inserite
                    if (cont_pietre == Partita.PIETRE_PER_GOLEM) {
                        freccia_sinistra.setEnabled(false);
                        freccia_destra.setEnabled(false);
                        conferma1.setEnabled(false);
                        attack1.setEnabled(true);
                        cont_pietre = 0;
                        if (fase_inizial) {
                            //Disabilito visualizzazione pietra
                            setPietraP1Img(-1 + "");
                            pietra1.setText("Pietre");
                            abilitaBottoniP2();
                            attack1.setEnabled(false);
                            setPietraP2(tipi.get(0).name());
                            tutorialSceltaPietre();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Non sono piu presenti pietre di quel tipo, scegli un altra pietra", "Pietre finite", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        //=======================================================================

        //=======================================================================
        //Pietra Scelta 2
        conferma2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scorta_comune.get(pietra_attuale2).getQuantita_pietra() > 0) {
                    evoluzioneGUI(partita.getSquadra_due(), pietra_attuale2);
                    setPietraP2(tipi.get(pietra_attuale2).name());
                    cont_pietre++;
                    //Se tutte pietre sono state inserite
                    if (cont_pietre == Partita.PIETRE_PER_GOLEM) {
                        freccia_sinistra2.setEnabled(false);
                        freccia_destra2.setEnabled(false);
                        conferma2.setEnabled(false);
                        attack1.setEnabled(true);
                        cont_pietre = 0;
                        if (fase_inizial) {
                            //Disabilito visualizzazione pietra
                            setPietraP1(tipi.get(pietra_attuale).name());
                            progressBar_1.setValue(100);
                            progressBar_2.setValue(100);
                            attack1.setEnabled(true);
                            fase_inizial = false;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Non sono piu presenti pietre di quel tipo, scegli un altra pietra", "Pietre finite", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        //=======================================================================

        attack1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int cont = 0;
                for (int i = 0; i < Partita.PIETRE_PER_GOLEM; i++) {
                    int posp_test = partita.posPietra(tipi, partita.getSquadra_uno());
                    int posq_test = partita.posPietra(tipi, partita.getSquadra_due());

                    if (tipi.get(posp_test).name().equals(tipi.get(posq_test).name())) {
                        cont++;
                    }

                    partita.cambioPietre();
                }
                if (cont == Partita.PIETRE_PER_GOLEM) {
                    JOptionPane.showMessageDialog(null, " I tamagolem hanno le stesse pietre " + partita.getSquadra_uno().getCombattente().getNome_combattente() + " reinserisci le tue", "Errore stesse pietre", JOptionPane.WARNING_MESSAGE);

                    for (int i = 0; i < Partita.ELEMENTI_IN_SCORTA / Partita.NUMERO_ELEMENTI; i++) {
                        int j = 0;
                        boolean trova = false;
                        do {
                            //todo controlla java.util.NoSuchElementException
                            try {
                                if (scorta_comune.get(j).getTipo_pietra().name().equals(partita.getSquadra_uno().getTamagolem().getPietre().getTipo_pietra().name())) {
                                    scorta_comune.get(j).aumentaQuantitaPietra();
                                    partita.getSquadra_uno().getTamagolem().rimuoviPrimaPietra();
                                    trova = true;
                                }
                            } catch (Exception ex) {
                                trova = true;
                            }
                            j++;
                        } while (!trova);
                    }
                    aggiornaPietre();
                    tutorialSceltaPietre();
                    disableAllButtons();
                    abilitaBottoniP1();
                } else {
                    boolean check_finish;

                    int posp = partita.posPietra(tipi, partita.getSquadra_uno());
                    int posq = partita.posPietra(tipi, partita.getSquadra_due());
                    if (tipi.get(posp).name().equals(tipi.get(posq).name())) {
                        testo.setText(AVETE_USATO_LO_STESSO_TIPO);
                    } else {
                        if (tipi.get(posp).getArchi().get(posq).getSenso()) {
                            //uno predomina
                            //System.out.println("TOLGO A DUE : "+  tipi.get(posp).getArchi().get(posq).getValore());
                            ImageIcon img1 = new ImageIcon("Immagini/1_.gif");
                            player1_img.setIcon(img1);
                            ImageIcon img2 = new ImageIcon("Immagini/2__.gif");
                            player2_img.setIcon(img2);
                            partita.getSquadra_due().getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                        }
                        if (!(tipi.get(posp).getArchi().get(posq).getSenso())) {
                            //due predomina
                            //System.out.println("TOLGO A UNO : "+tipi.get(posp).getArchi().get(posq).getValore());
                            ImageIcon img1 = new ImageIcon("Immagini/1__.gif");
                            player1_img.setIcon(img1);
                            ImageIcon img2 = new ImageIcon("Immagini/2_.gif");
                            player2_img.setIcon(img2);
                            partita.getSquadra_uno().getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                        }
                    }
                    testo.setText(partita.getSquadra_uno().getCombattente().getNome_combattente() + " ha usato " + partita.getSquadra_uno().getTamagolem().getPietre().getTipo_pietra().name() + "  -  "
                            + partita.getSquadra_due().getCombattente().getNome_combattente() + " ha usato " + partita.getSquadra_due().getTamagolem().getPietre().getTipo_pietra().name() + "");
                    aggiornaPietre();


                    progressBar_1.setValue((partita.getSquadra_uno().getTamagolem().getSalute() * 100) / TamaGolem.SALUTE);
                    progressBar_2.setValue((partita.getSquadra_due().getTamagolem().getSalute() * 100) / TamaGolem.SALUTE);

                    //effettuo cambio pietre
                    partita.cambioPietre();


                    //controllo vita dei due tamagolem
                    controllaVita2TamaGUI(partita);

                    check_finish = partita.isTerminata();

                    if (check_finish) {
                        int a_zero = 0;
                        for (int i = 0; i < scorta_comune.size(); i++) {
                            if (scorta_comune.get(i).getQuantita_pietra() == 0) {
                                a_zero++;
                            }
                        }
                        if (a_zero == scorta_comune.size()) {
                            check_finish = false;
                        }
                    }

                    if (check_finish) {
                        numeroTama.setString(partita.getSquadra_uno().getTamagolems().size() + "");
                        numeroTama2.setString(partita.getSquadra_due().getTamagolems().size() + "");
                        numeroTama.setValue(partita.getSquadra_uno().getTamagolems().size());
                        numeroTama2.setValue(partita.getSquadra_due().getTamagolems().size());
                        //Dichiarazione vincitore
                        visualizzaVincitore();
                        //Visualizza equilibrio
                        JTextPane textPane = new JTextPane();
                        textPane.setContentType("text/html");
                        textPane.setText(partita.stringaEquilibrioHtml(tipi));
                        JScrollPane scrollPane = new JScrollPane(textPane);

                        scrollPane.setPreferredSize(new Dimension(400, 300));
                        JOptionPane.showMessageDialog(null, scrollPane, "Equilibrio", JOptionPane.INFORMATION_MESSAGE);

                        //Reset
                        creaNuova();
                    }
                }
            }
        });
    }

    private void aggiornaPietre() {
        setPietraP1Img(partita.getSquadra_uno().getTamagolem().getPietre().getTipo_pietra().name());
        pietra1.setText(" " + partita.getSquadra_uno().getTamagolem().getPietre().getTipo_pietra().name());
        setPietraP2Img(partita.getSquadra_due().getTamagolem().getPietre().getTipo_pietra().name());
        pietra2.setText(" " + partita.getSquadra_due().getTamagolem().getPietre().getTipo_pietra().name());
    }

    private void visualizzaVincitore() {
        ImageIcon imageIcon = new ImageIcon("Immagini/Trofeo.gif");
        JOptionPane.showMessageDialog(null, " Il vincitore e' " + partita.getCombattenteVincente().getNome_combattente(), "Tamagolem", JOptionPane.INFORMATION_MESSAGE, imageIcon);
    }

    private void tutorialSceltaPietre() {
        ImageIcon imageIcon = new ImageIcon("Immagini/Tutorial.gif");
        JOptionPane.showMessageDialog(null, "", INSERIRE_LE_PIETRE_NEL_TAMAGOLEM, JOptionPane.INFORMATION_MESSAGE, imageIcon);
    }

    private void creaNuova() {
        inizializzaTema("Metal");
        FinestraPrincipale finestraPrincipale = new FinestraPrincipale();
        this.dispose();
    }

    private void abilitaBottoniP1() {
        freccia_sinistra.setEnabled(true);
        freccia_destra.setEnabled(true);
        conferma1.setEnabled(true);
    }

    private void abilitaBottoniP2() {
        freccia_sinistra2.setEnabled(true);
        freccia_destra2.setEnabled(true);
        conferma2.setEnabled(true);
    }

    private void setBordi() {
        Border line = BorderFactory.createLineBorder(Color.gray);
        pietra1.setBorder(line);
        pietra2.setBorder(line);
        testo_panel.setBorder(line);
    }

    private void setColori() {
        mainPanel.setBackground(Color.GRAY);
        progressBar_1.setForeground(Color.RED);
        progressBar_2.setForeground(Color.RED);
        p1.setBackground(Color.LIGHT_GRAY);
        p2.setBackground(Color.LIGHT_GRAY);
        attack1.setBackground(Color.RED);
        attack1.setForeground(Color.WHITE);
        menu_principale.setBackground(Color.LIGHT_GRAY);
        testo_panel.setBackground(Color.DARK_GRAY);
        testo.setForeground(Color.WHITE);
        conferma1.setBackground(Color.DARK_GRAY);
        conferma1.setForeground(Color.WHITE);
        conferma2.setBackground(Color.DARK_GRAY);
        conferma2.setForeground(Color.WHITE);
    }

    private void inizializzaFinestra() {
        this.setMinimumSize(new Dimension(1200, 800));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("TamaGolem");
    }

    public void inizializzaTema(String stringa) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if (stringa.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }


    private void evoluzioneGUI(Squadra squadra, int pietra_scelta) {
        testo.setText(" Evoluzione del golem da parte di " + squadra.getCombattente().getNome_combattente());
        squadra.getTamagolem().addTipoPietra(new Pietra(scorta_comune.get(pietra_scelta).getTipo_pietra()));
        scorta_comune.get(pietra_scelta).decrementaQuantitaPietra();
        testo.setText(" Pietra aggiunta (Tot:" + (cont_pietre + 1) + ")");
    }

    public void controllaVita2TamaGUI(Partita partita) {
        controlloVitaGUI(partita.getSquadra_uno(), 1);
        controlloVitaGUI(partita.getSquadra_due(), 2);
    }

    private void controlloVitaGUI(Squadra squadra, int numero) {
        if (squadra.getTamagolem().getSalute() <= 0) {
            squadra.removeTama();
            //disabilito tasto attacco
            attack1.setEnabled(false);
            if (squadra.getTamagolems().size() > 0) {
                if (numero == 1) {
                    //squadra i tama eliminato
                    JOptionPane.showMessageDialog(this, " " + squadra.getCombattente().getNome_combattente() + " il tuo tamagolem e' stato sconfitto, presto caricane un altro con nuove pietre", "Tama Sconfitto", JOptionPane.WARNING_MESSAGE);
                    numeroTama.setValue(partita.getSquadra_uno().getTamagolems().size());
                    numeroTama.setString(partita.getSquadra_uno().getTamagolems().size() + "");
                    aggiornaPietre();
                    tutorialSceltaPietre();
                    abilitaBottoniP1();
                } else {
                    JOptionPane.showMessageDialog(this, " " + squadra.getCombattente().getNome_combattente() + " il tuo tamagolem e' stato sconfitto, presto caricane un altro con nuove pietre", "Tama Sconfitto", JOptionPane.WARNING_MESSAGE);
                    numeroTama2.setValue(partita.getSquadra_due().getTamagolems().size());
                    numeroTama2.setString(partita.getSquadra_due().getTamagolems().size() + "");
                    aggiornaPietre();
                    tutorialSceltaPietre();
                    abilitaBottoniP2();
                }
            }
        }
    }

    private void visualizzaStoria() {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<h1>Storia</h1>" +
                "<font size=\"5\">" +
                "<p>" +
                "Il delicato <b>Equilibrio del Mondo</b> si basa da sempre sull’interazione fra le diverse forze naturali, dalle più miti " +
                "alle più distruttive. Ogni elemento in natura ha i suoi punti forti e le sue debolezze, caratteristiche che " +
                "mantengono il nostro Universo stabile e sicuro." +
                "</p>" +
                "</font>" +
                "<font size=\"5\"><p> Esistono in tutto 10 elementi:" +
                "<ul>" +
                "<li><font color=\"red\"><b>FUOCO</b></font></li>" +
                "<li><font color=\"blue\"><b>ACQUA</b></font></li>" +
                "<li><font color=\"#99ccff\"><b>ARIA</b></font></li>" +
                "<li><font color=\"#663300\"><b>TERRA</b></font></li>" +
                "<li><font <span style=\"background-color: #ffff00\"><b>ELETTRO</b></span></li>" +
                "<li><font <span style=\"background-color: #FFFF80\"><b>LUCE</b></span></li>" +
                "<li><font color=\"black\"><b>BUIO</b></font></li>" +
                "<li><font color=\"green\"><b>ERBA</b></font></li>" +
                "<li><font color=\"gray\"><b>MAGNETICO</b></font></li>" +
                "<li><font color=\"#ff3399\"><b>PSICO</b></font></li>" +
                "</ul>" +
                "</p></font><hr>" +
                "<font size=\"5\"><p>" +
                "Da migliaia di anni, L’Accademia studia le tecniche per governare tali elementi: utilizzando alcune pietre " +
                "particolari e dandole in pasto a strane creature denominate TamaGolem, infatti, è possibile conservare il " +
                "potere degli elementi per liberarlo al bisogno." +
                "</p></font>" +
                "<font size=\"5\"><p>" +
                "Gli allievi dell’Accademia, per questo motivo, sono soliti sfidarsi in combattimenti clandestini fra " +
                "<b>TamaGolem</b>. L’abilità dei combattenti, in questo caso, sta nella scelta delle giuste Pietre degli Elementi in " +
                "modo che lo scontro abbia il risultato sperato. Tale scelta non è scontata, poiché gli Equilibri del Mondo " +
                "sono mutevoli, e possono modificarsi radicalmente da una battaglia all’altra." +
                "</p></font>" +
                "<font size=\"5\">" +
                "<p>Solamente il TamaGolem che resiste fino alla fine decreta la vittoria del proprio combattente.</p>" +
                "<br></font>");

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Storia!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setImgTama() {
        ImageIcon img1 = new ImageIcon("Immagini/1__.gif");
        player1_img.setIcon(img1);

        ImageIcon img2 = new ImageIcon("Immagini/2__.gif");
        player2_img.setIcon(img2);
    }

    private void setPietraP1Img(String nome) {
        String nomefile = "Immagini/pietre/" + nome + ".gif";
        ImageIcon img = new ImageIcon(nomefile);
        pietra1.setIcon(img);
    }

    private void setPietraP1(String nome) {
        setPietraP1Img(nome);
        pietra1.setText(" " + scorta_comune.get(pietra_attuale).getTipo_pietra().name() + " " + scorta_comune.get(pietra_attuale).getQuantita_pietra() + "");
    }

    private void setPietraP2Img(String nome) {
        String nomefile = "Immagini/pietre/" + nome + ".gif";
        ImageIcon img = new ImageIcon(nomefile);
        pietra2.setIcon(img);
    }

    private void setPietraP2(String nome) {
        setPietraP2Img(nome);
        pietra2.setText(" " + scorta_comune.get(pietra_attuale2).getTipo_pietra().name() + " " + scorta_comune.get(pietra_attuale2).getQuantita_pietra() + "");
    }

    private void disableAllButtons() {
        freccia_sinistra.setEnabled(false);
        freccia_destra.setEnabled(false);
        conferma1.setEnabled(false);

        freccia_sinistra2.setEnabled(false);
        freccia_destra2.setEnabled(false);
        conferma2.setEnabled(false);

        attack1.setEnabled(false);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setForeground(new Color(-4473925));
        p1 = new JPanel();
        p1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(p1, gbc);
        player1_img = new JLabel();
        player1_img.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        p1.add(player1_img, gbc);
        p1_ui = new JPanel();
        p1_ui.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        p1.add(p1_ui, gbc);
        progressBar_1 = new JProgressBar();
        Font progressBar_1Font = this.$$$getFont$$$("Dungeon", -1, -1, progressBar_1.getFont());
        if (progressBar_1Font != null) progressBar_1.setFont(progressBar_1Font);
        progressBar_1.setForeground(new Color(-3137536));
        progressBar_1.setStringPainted(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p1_ui.add(progressBar_1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p1_ui.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p1_ui.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p1_ui.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p1_ui.add(spacer4, gbc);
        nomeGiocatore1 = new JLabel();
        Font nomeGiocatore1Font = this.$$$getFont$$$("Eras Demi ITC", Font.BOLD, 18, nomeGiocatore1.getFont());
        if (nomeGiocatore1Font != null) nomeGiocatore1.setFont(nomeGiocatore1Font);
        nomeGiocatore1.setText("Player1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        p1_ui.add(nomeGiocatore1, gbc);
        numeroTama = new JProgressBar();
        Font numeroTamaFont = this.$$$getFont$$$("Dungeon", -1, -1, numeroTama.getFont());
        if (numeroTamaFont != null) numeroTama.setFont(numeroTamaFont);
        numeroTama.setIndeterminate(false);
        numeroTama.setString("");
        numeroTama.setStringPainted(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p1_ui.add(numeroTama, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        p1.add(panel1, gbc);
        freccia_sinistra = new JButton();
        freccia_sinistra.setText("<");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(freccia_sinistra, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        freccia_destra = new JButton();
        freccia_destra.setText(">");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(freccia_destra, gbc);
        pietra1 = new JLabel();
        Font pietra1Font = this.$$$getFont$$$("Eras Demi ITC", -1, 19, pietra1.getFont());
        if (pietra1Font != null) pietra1.setFont(pietra1Font);
        pietra1.setText("Pietra");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 30;
        gbc.ipady = 10;
        panel1.add(pietra1, gbc);
        conferma1 = new JButton();
        conferma1.setText("Conferma");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(conferma1, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 250;
        panel1.add(spacer7, gbc);
        p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(p2, gbc);
        player2_img = new JLabel();
        player2_img.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        p2.add(player2_img, gbc);
        p2_ui = new JPanel();
        p2_ui.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        p2.add(p2_ui, gbc);
        progressBar_2 = new JProgressBar();
        Font progressBar_2Font = this.$$$getFont$$$("Dungeon", -1, -1, progressBar_2.getFont());
        if (progressBar_2Font != null) progressBar_2.setFont(progressBar_2Font);
        progressBar_2.setForeground(new Color(-3137536));
        progressBar_2.setStringPainted(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p2_ui.add(progressBar_2, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 40.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p2_ui.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p2_ui.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p2_ui.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p2_ui.add(spacer11, gbc);
        nomeGiocatore2 = new JLabel();
        Font nomeGiocatore2Font = this.$$$getFont$$$("Eras Demi ITC", Font.BOLD, 18, nomeGiocatore2.getFont());
        if (nomeGiocatore2Font != null) nomeGiocatore2.setFont(nomeGiocatore2Font);
        nomeGiocatore2.setHorizontalAlignment(11);
        nomeGiocatore2.setText("Player2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        p2_ui.add(nomeGiocatore2, gbc);
        numeroTama2 = new JProgressBar();
        Font numeroTama2Font = this.$$$getFont$$$("Dungeon", -1, -1, numeroTama2.getFont());
        if (numeroTama2Font != null) numeroTama2.setFont(numeroTama2Font);
        numeroTama2.setString("");
        numeroTama2.setStringPainted(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p2_ui.add(numeroTama2, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        p2.add(panel2, gbc);
        freccia_sinistra2 = new JButton();
        freccia_sinistra2.setText("<");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(freccia_sinistra2, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer12, gbc);
        freccia_destra2 = new JButton();
        freccia_destra2.setText(">");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(freccia_destra2, gbc);
        pietra2 = new JLabel();
        Font pietra2Font = this.$$$getFont$$$("Eras Demi ITC", -1, 19, pietra2.getFont());
        if (pietra2Font != null) pietra2.setFont(pietra2Font);
        pietra2.setText("Pietra");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 30;
        gbc.ipady = 10;
        panel2.add(pietra2, gbc);
        conferma2 = new JButton();
        conferma2.setText("Conferma");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(conferma2, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        panel2.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 250;
        panel2.add(spacer14, gbc);
        menu_principale = new JToolBar();
        menu_principale.setBorderPainted(false);
        menu_principale.setFloatable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 5;
        mainPanel.add(menu_principale, gbc);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        menu_principale.add(toolBar$Separator1);
        menu = new JButton();
        menu.setText("Nuova Partita");
        menu_principale.add(menu);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        menu_principale.add(toolBar$Separator2);
        menu2 = new JButton();
        menu2.setText("Storia");
        menu_principale.add(menu2);
        final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
        menu_principale.add(toolBar$Separator3);
        menu3 = new JButton();
        menu3.setText("Credits");
        menu_principale.add(menu3);
        testo_panel = new JPanel();
        testo_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 50;
        mainPanel.add(testo_panel, gbc);
        testo = new JLabel();
        Font testoFont = this.$$$getFont$$$("Eras Demi ITC", -1, 18, testo.getFont());
        if (testoFont != null) testo.setFont(testoFont);
        testo.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        testo_panel.add(testo, gbc);
        attack1 = new JButton();
        Font attack1Font = this.$$$getFont$$$("Dungeon", -1, 18, attack1.getFont());
        if (attack1Font != null) attack1.setFont(attack1Font);
        attack1.setText("ATTACCO");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        gbc.ipady = 20;
        mainPanel.add(attack1, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
