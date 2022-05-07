package AR2.Vue;

import AR2.Controleur.Controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Vue extends JFrame{
    private Color col;
    private final Controleur controleur;
    private JMenuBar menuBar;
    private Accueil accueil;
    protected ChoixEspeces choixEspece;
    protected CreaArbre creaArbre;
    private CreaEspeces creaEspece;
    private Bestiaire bestiaire;
    protected Vcharg vcharge;


    public Vue(Controleur c) {
        controleur = c;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800,700);
        this.setMinimumSize(new Dimension(600,500));
        this.setTitle("Phylemon");
        creaMenubar();
        creaPanels();
        this.setContentPane(accueil);
        this.setLocationRelativeTo(null);
    }

    public void creaMenubar() {
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        this.menuBar = new JMenuBar();

        JMenuItem accueilItem = new JMenuItem("Accueil");
        accueilItem.setMaximumSize(new Dimension(60,25));
        accueilItem.addActionListener(event -> this.changePanel(accueil));

        //JMenu fichier = new JMenu("Fichier"); techniquement non necesaire
        //fichier.setMnemonic('f');

        JMenu especeMenu = new JMenu("Espèces");
        especeMenu.setMnemonic('e');

        JMenuItem choixespeceItem = new JMenuItem("Sélection", 's');
        choixespeceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        choixespeceItem.addActionListener(l);
        especeMenu.add(choixespeceItem);
        //choixespeceItem.addActionListener(event -> changePanel(new ChoixEspeces(this.controleur, this)));
        choixespeceItem.addActionListener(event -> changePanel(choixEspece));

        JMenuItem creaEspeceItem = new JMenuItem("Création", 'c');
        creaEspeceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        creaEspeceItem.addActionListener(l);
        especeMenu.add(creaEspeceItem);
        creaEspeceItem.addActionListener(event -> changePanel(creaEspece));

        JMenuItem bestiaireItem = new JMenuItem("Bestiaire", 'b');
        bestiaireItem.addActionListener(event -> changePanel(new Bestiaire(this.controleur)));
        bestiaireItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
        especeMenu.add(bestiaireItem);

        JMenu choixAlgo = new JMenu("Lancement");
        choixAlgo.setMnemonic('l');

        JMenuItem creaArbre = new JMenuItem("Arbre et matrice", 'm');
        creaArbre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        creaArbre.addActionListener(l);
        choixAlgo.add(creaArbre);
        creaArbre.addActionListener(event ->
            {
                this.changePanel(this.creaArbre);
                if (!this.controleur.getEspecesChoisis().isEmpty()) {
                    this.creaArbre.demarageCreaArbre(event);
                }
            });

        menuBar.add(accueilItem);
        //menuBar.add(fichier);
        menuBar.add(especeMenu);
        menuBar.add(choixAlgo);
        this.setJMenuBar(menuBar);
    }
    

    public void creaPanels() {
        this.accueil = new Accueil();
        this.creaArbre = new CreaArbre(this.controleur);
        this.choixEspece = new ChoixEspeces(this.controleur, this);
        this.creaEspece = new CreaEspeces(this.controleur, choixEspece, this);
        this.bestiaire = new Bestiaire(this.controleur);
        this.vcharge = new Vcharg(this.controleur);
    }

    // appelée par Vue et ChoixEspece
    public void changePanel(JPanel p) {
        this.setContentPane(p);
        revalidate();
        p.repaint();
    }

}
