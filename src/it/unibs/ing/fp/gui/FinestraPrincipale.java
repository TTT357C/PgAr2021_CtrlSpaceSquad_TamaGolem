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
import java.util.Scanner;

/**
 * @author Thomas Causetti
 */
public class FinestraPrincipale extends JFrame {
    private JLabel player1_img;
    private JLabel player2_img;
    private JPanel mainPanel;
    private JPanel p1;
    private JPanel p2;
    private JProgressBar progressBar_2;
    private JProgressBar progressBar_1;
    private JButton freccia_destra;
    private JButton attack1;
    private JButton freccia_sinistra;
    private JLabel nomeGiocatore2;
    private JLabel nomeGiocatore1;
    private JButton menu;
    private JPanel p1_ui;
    private JPanel p2_ui;
    private JToolBar menu_principale;
    private JLabel pietra1;
    private JLabel testo;
    private JPanel testo_panel;
    private JButton freccia_sinistra2;
    private JButton freccia_destra2;
    private JLabel pietra2;
    private JButton menu2;
    private JButton menu3;
    private JButton conferma1;
    private JButton conferma2;

    private int numero_elementi = 0;

    private int pietra_attuale;
    private int pietra_attuale2;
    private int cont_pietre;

    private Partita partita;
    private ArrayList<Tipo> tipi;

    private ArrayList<Pietra> scorta_comune;

    public FinestraPrincipale() {

        //Inizializzatore tema
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }


        this.setMinimumSize(new Dimension(1200, 800));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("TamaGolem");

        //set default
        disableAll();
        pietra_attuale = -1;
        pietra_attuale2 = -1;
        setPietraP1Img(pietra_attuale);
        setPietraP2Img(pietra_attuale);

        freccia_sinistra.setEnabled(false);
        freccia_destra.setEnabled(false);
        conferma1.setEnabled(false);

        freccia_sinistra2.setEnabled(false);
        freccia_destra2.setEnabled(false);
        conferma2.setEnabled(false);


        //Colori
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

        //bordi
        Border line = BorderFactory.createLineBorder(Color.gray);
        pietra1.setBorder(line);
        pietra2.setBorder(line);
        testo_panel.setBorder(line);


        setImgTama();

        progressBar_1.setValue(0);
        progressBar_2.setValue(0);
        this.add(mainPanel);
        this.setVisible(true);

        //Finestra di benvenuto/spiegazione
        JOptionPane.showMessageDialog(this, " Per iniziare una nuova partita puoi selezionare - nuova partita - dal menu qui sopra", "Benvenuto!", JOptionPane.INFORMATION_MESSAGE);

        //=======================================================================
        //Nuova Partita
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setEnabled(false);
                String nome1 = JOptionPane.showInputDialog(null, "Dimmi il nome del giocatore1: ", "Nuova Partita", JOptionPane.INFORMATION_MESSAGE);
                String nome2 = JOptionPane.showInputDialog(null, "Dimmi il nome del giocatore2: ", "Nuova Partita", JOptionPane.INFORMATION_MESSAGE);

                //Set GUI nomi giocatori
                nomeGiocatore1.setText(nome1);
                nomeGiocatore2.setText(nome2);

                Squadra q1 = new Squadra(new Combattente(nome1));
                Squadra q2 = new Squadra(new Combattente(nome2));
                partita = new Partita(q1, q2);

                String[] buttons = {"Facile", "Medio", "Difficile", "Caotico"};
                int returnValue = JOptionPane.showOptionDialog(null, "Seleziona difficolta':", "Nuova Partita", JOptionPane.INFORMATION_MESSAGE, 0, null, buttons, buttons[0]);
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
                        numero_ele = 7;
                        break;
                }

                partita.inizializzazioneGUI(numero_ele);

                tipi = partita.equilibrio();

                scorta_comune = partita.generaScortaComune(tipi);

                //porto le pietre a zero prima -1
                pietra_attuale++;
                pietra_attuale2++;

                numero_elementi = partita.NUMERO_ELEMENTI;

                freccia_sinistra.setEnabled(true);
                freccia_destra.setEnabled(true);
                conferma1.setEnabled(true);
            }
        });
        //=======================================================================

        attack1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon img1 = new ImageIcon("Immagini/1_.gif");
                player1_img.setIcon(img1);
                ImageIcon img2 = new ImageIcon("Immagini/2__.gif");
                player2_img.setIcon(img2);
                attack1.setEnabled(false);
            }
        });

        //GESTISCI visualizzatore pietre
        //=======================================================================
        freccia_sinistra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                pietra_attuale--;
                if (pietra_attuale < 0) {
                    pietra_attuale = numero_elementi - 1;
                }
                setPietraP1(pietra_attuale);

            }
        });
        freccia_destra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale++;
                if (pietra_attuale >= numero_elementi) {
                    pietra_attuale = 0;
                }
                setPietraP1(pietra_attuale);
            }
        });
        freccia_sinistra2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale2--;
                if (pietra_attuale2 < 0) {
                    pietra_attuale2 = numero_elementi - 1;
                }
                setPietraP2(pietra_attuale2);
            }
        });
        freccia_destra2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pietra_attuale2++;
                if (pietra_attuale2 >= numero_elementi) {
                    pietra_attuale2 = 0;
                }
                setPietraP2(pietra_attuale2);
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

        //Pietra Scelta 1
        conferma1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evoluzioneGUI(partita.getSquadra_uno(), pietra_attuale);
                setPietraP1(pietra_attuale);
                cont_pietre++;
                //Se tutte pietre sono state inserite
                if (cont_pietre == partita.PIETRE_PER_GOLEM) {
                    freccia_sinistra.setEnabled(false);
                    freccia_destra.setEnabled(false);
                    conferma1.setEnabled(false);
                    //Disabilito visualizzazione pietra
                    setPietraP1Img(-1);
                    pietra1.setText("Pietre");
                    freccia_sinistra2.setEnabled(true);
                    freccia_destra2.setEnabled(true);
                    conferma2.setEnabled(true);
                    cont_pietre = 0;
                }
            }
        });

        //Pietra Scelta 2
        conferma2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evoluzioneGUI(partita.getSquadra_uno(), pietra_attuale2);
                setPietraP2(pietra_attuale2);
                cont_pietre++;
                //Se tutte pietre sono state inserite
                if (cont_pietre == partita.PIETRE_PER_GOLEM) {
                    freccia_sinistra2.setEnabled(false);
                    freccia_destra2.setEnabled(false);
                    conferma2.setEnabled(false);
                    //Disabilito visualizzazione pietra
                    setPietraP1(pietra_attuale);
                    cont_pietre = 0;
                }
            }
        });

        attack1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean check_finisch;

                int posp = partita.posPietra(tipi, partita.getSquadra_uno());
                int posq = partita.posPietra(tipi, partita.getSquadra_due());
                if(tipi.get(posp).name().equals(tipi.get(posq).name())){
                    testo.setText("AVETE USATO LO STESSO TIPO E NON HA CREATO CONSEGUENZE");
                }
                else{
                    if(tipi.get(posp).getArchi().get(posq).getSenso()){
                        //uno predomina
                        //System.out.println("TOLGO A DUE : "+  tipi.get(posp).getArchi().get(posq).getValore());
                        partita.getSquadra_due().getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                    }
                    if(!(tipi.get(posp).getArchi().get(posq).getSenso())){
                        //due predomina
                        //System.out.println("TOLGO A UNO : "+tipi.get(posp).getArchi().get(posq).getValore());
                        partita.getSquadra_uno().getTamagolem().setSaluteDanno(tipi.get(posp).getArchi().get(posq).getValore());
                    }
                }
                testo.setText(partita.getSquadra_uno().getCombattente().getNome_combattente()+ " ha usato " + partita.getSquadra_uno().getTamagolem().getPietre().getTipo_pietra().name() + " Vita del Tamagolem: "+ partita.getSquadra_uno().getTamagolem().getSalute()+"\n"+
                partita.getSquadra_due().getCombattente().getNome_combattente()+ " ha usato " + partita.getSquadra_due().getTamagolem().getPietre().getTipo_pietra().name() + " Vita del Tamagolem: "+ partita.getSquadra_due().getTamagolem().getSalute() );

                //effettuo cambio pietre
                partita.cambioPietre();
                //controllo vita dei due tamagolem
                partita.controllaVita2Tama(scorta_comune);

                check_finisch = partita.isTerminata();

                if(check_finisch){
                    int a_zero=0;
                    for (int i = 0; i < scorta_comune.size(); i++) {
                        if(scorta_comune.get(i).getQuantita_pietra()==0){
                            a_zero++;
                        }
                    }
                    if(a_zero==scorta_comune.size()){
                        //Dichiarazione vincitore
                        JOptionPane.showMessageDialog(null, " Il vincitore e' "+partita.getCombattenteVincente().getNome_combattente(), "Tamagolem", JOptionPane.INFORMATION_MESSAGE);
                        //Visualizza equilibrio
                        JOptionPane.showMessageDialog(null, partita.stringaEquilibrio(tipi), "Equilibrio", JOptionPane.INFORMATION_MESSAGE);

                        //Reset
                        freccia_sinistra.setEnabled(false);
                        freccia_destra.setEnabled(false);
                        conferma1.setEnabled(false);
                        freccia_sinistra2.setEnabled(false);
                        freccia_destra2.setEnabled(false);
                        conferma2.setEnabled(false);
                        setPietraP1Img(-1);
                        pietra1.setText("Pietra");
                        setPietraP2Img(-1);
                        pietra2.setText("Pietra");
                    }
                }
            }
        });
    }


    private void evoluzioneGUI(Squadra squadra, int pietra_scelta) {
        testo.setText(" Evoluzione del golem da parte di " + squadra.getCombattente().getNome_combattente());
        squadra.getTamagolem().addTipoPietra(new Pietra(scorta_comune.get(pietra_scelta).getTipo_pietra()));
        scorta_comune.get(pietra_scelta).decrementaQuantitaPietra();
        testo.setText(" Pietra aggiunta (Tot:" + cont_pietre + ")");
    }

    private void visualizzaStoria() {
        JTextArea textArea = new JTextArea("Il delicato Equilibrio del Mondo si basa da sempre sull’interazione fra le diverse forze naturali, dalle più miti " +
                "alle più distruttive. Ogni elemento in natura ha i suoi punti forti e le sue debolezze, caratteristiche che " +
                "mantengono il nostro Universo stabile e sicuro.\n\n" +
                "Esistono in tutto 10 elementi:\n - FUOCO\n" +
                " - ACQUA,\n" +
                " - ARIA,\n" +
                " - TERRA,\n" +
                " - ELETTRO,\n" +
                " - LUCE,\n" +
                " - BUIO,\n" +
                " - ERBA,\n" +
                " - MAGNETICO,\n" +
                " - PSICO.\n\n" +
                "Da migliaia di anni, L’Accademia studia le tecniche per governare tali elementi: utilizzando alcune pietre " +
                "particolari e dandole in pasto a strane creature denominate TamaGolem, infatti, è possibile conservare il " +
                "potere degli elementi per liberarlo al bisogno.\n\n" +
                "Gli allievi dell’Accademia, per questo motivo, sono soliti sfidarsi in combattimenti clandestini fra " +
                "TamaGolem. L’abilità dei combattenti, in questo caso, sta nella scelta delle giuste Pietre degli Elementi in " +
                "modo che lo scontro abbia il risultato sperato. Tale scelta non è scontata, poiché gli Equilibri del Mondo " +
                "sono mutevoli, e possono modificarsi radicalmente da una battaglia all’altra.\n\n" +
                "Solamente il TamaGolem che resiste fino alla fine decreta la vittoria del proprio combattente.\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Storia!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setImgTama() {
        ImageIcon img1 = new ImageIcon("Immagini/1__.gif");
        player1_img.setIcon(img1);

        ImageIcon img2 = new ImageIcon("Immagini/2__.gif");
        player2_img.setIcon(img2);
    }

    private void setPietraP1Img(int numero) {
        String nomefile = "Immagini/pietre/" + numero + ".gif";
        ImageIcon img = new ImageIcon(nomefile);
        pietra1.setIcon(img);
    }

    private void setPietraP1(int numero) {
        setPietraP1Img(numero);
        pietra1.setText(" " + scorta_comune.get(pietra_attuale).getTipo_pietra().name() + " " + scorta_comune.get(pietra_attuale).getQuantita_pietra() + "");
    }

    private void setPietraP2Img(int numero) {
        String nomefile = "Immagini/pietre/" + numero + ".gif";
        ImageIcon img = new ImageIcon(nomefile);
        pietra2.setIcon(img);
    }

    private void setPietraP2(int numero) {
        setPietraP2Img(numero);
        pietra2.setText(" " + scorta_comune.get(pietra_attuale2).getTipo_pietra().name() + " " + scorta_comune.get(pietra_attuale2).getQuantita_pietra() + "");
    }

    private void disableAll() {
        p1.setEnabled(false);
        p2.setEnabled(false);
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
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p1_ui.add(spacer4, gbc);
        nomeGiocatore1 = new JLabel();
        Font nomeGiocatore1Font = this.$$$getFont$$$("Eras Demi ITC", Font.BOLD, 18, nomeGiocatore1.getFont());
        if (nomeGiocatore1Font != null) nomeGiocatore1.setFont(nomeGiocatore1Font);
        nomeGiocatore1.setText("HP");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        p1_ui.add(nomeGiocatore1, gbc);
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
        pietra1.setText("Pietra");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
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
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 40.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p2_ui.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p2_ui.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        p2_ui.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        p2_ui.add(spacer10, gbc);
        nomeGiocatore2 = new JLabel();
        Font nomeGiocatore2Font = this.$$$getFont$$$("Eras Demi ITC", Font.BOLD, 18, nomeGiocatore2.getFont());
        if (nomeGiocatore2Font != null) nomeGiocatore2.setFont(nomeGiocatore2Font);
        nomeGiocatore2.setHorizontalAlignment(11);
        nomeGiocatore2.setText("HP");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        p2_ui.add(nomeGiocatore2, gbc);
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
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer11, gbc);
        freccia_destra2 = new JButton();
        freccia_destra2.setText(">");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(freccia_destra2, gbc);
        pietra2 = new JLabel();
        pietra2.setText("Pietra");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
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
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        panel2.add(spacer12, gbc);
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
