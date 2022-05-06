package Vue;

import Controleur.Controleur;
import Modele.Arbre;
import Modele.Espece;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static java.awt.BorderLayout.EAST;
import static java.lang.Math.log;

public class CreaArbre extends JPanel {

    private final Controleur controleur;
    private JTree arbre = new JTree(new DefaultMutableTreeNode("Racine"));
    private JPanel matrice_pan = new JPanel();
    private JButton suivante = new JButton("Etape suivante");
    private JPanel indiqFusion = new JPanel();
    private JPanel echelle = new JPanel();

    protected CreaArbre(Controleur controleur) {
        this.controleur = controleur;
        this.creaCreaArbre();
    }

    protected void creaCreaArbre() {
        this.setLayout(new BorderLayout());
        arbre.setBackground(new Color(200,200,200));
        arbre.setRootVisible(false);
        JScrollPane sarbre = new JScrollPane(arbre);
        sarbre.setPreferredSize(new Dimension(200, 200));

        matrice_pan.setBackground(new Color(230, 220, 236));
        JScrollPane smatrice = new JScrollPane(matrice_pan);
        suivante.addActionListener(this::suiteCreaArbre);
        suivante.setEnabled(false);

        indiqFusion.setBackground(new Color(160, 145 ,145));
        echelle.setBackground(new Color(220, 220, 220));
        echelle.setLayout(new BoxLayout(echelle,BoxLayout.Y_AXIS));

        this.add(echelle, BorderLayout.WEST);
        this.add(smatrice, BorderLayout.CENTER);
        this.add(sarbre, BorderLayout.EAST);
        this.add(indiqFusion, BorderLayout.NORTH);
        JButton start = new JButton("Redémarrer");
        start.addActionListener(this::demarageCreaArbre);
        JButton defiler = new JButton("Obtenir l'arbre");
        defiler.addActionListener(this::defiler);
        JPanel boutons = new JPanel();
        boutons.add(start, EAST);
        boutons.add(suivante, BorderLayout.CENTER);
        boutons.add(defiler, BorderLayout.WEST);
        this.add(boutons, BorderLayout.SOUTH);
    }
    // Cette méthode fait passer la matrice et l'arbre phylogénétique à la dernière étape
    private void defiler(ActionEvent e) {
        while (controleur.getEspecesTableau().size() > 1) {
            suiteCreaArbre(e);
        }
    }

    // Cette méthode fait passer la matrice et l'arbre phylogénétique à l'étape suivante
    private void suiteCreaArbre(ActionEvent event) {
        try {
            Espece[] especesFusion = controleur.etapeSuivante();
            if (controleur.getEspecesTableau().size() <= 1) {
                suivante.setEnabled(false);
            }
            JLabel fusion = new JLabel("Les espèces : " + especesFusion[0].getNom() + " et " + especesFusion[1].getNom() + " ont été fusionnées.");
            this.indiqFusion.removeAll();
            this.indiqFusion.add(fusion);
            this.affArbre();
            this.affMatrice();
            revalidate();
            repaint();
        } catch (IndexOutOfBoundsException e) {
            suivante.setEnabled(false);
            revalidate();
            repaint();
        }
    }

    protected void demarageCreaArbre(ActionEvent event) {
        this.controleur.initialiseMatrice();
        this.affArbre();
        this.affMatrice();
        suivante.setEnabled(true);
        this.indiqFusion.removeAll();
        this.affEchelle();

        revalidate();
        repaint();
    }
    // Cette méthode rajoute (l'affichage graphique de) l'échelle de couleur dans le JPanel Principale (this)
    private void affEchelle() {
        this.echelle.removeAll();
        for (int i = 0; i < 1; i++) {
            JPanel Sep = new JPanel();
            Sep.setBackground(new Color(220, 220, 220));
            echelle.add(Sep);
        }
        JLabel proche = new JLabel("Proche");
        proche.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        echelle.add(proche);
        JPanel place = new JPanel();
        place.setBackground(new Color(220, 220, 220));
        place.setLayout(new GridLayout(0,1));
        place.setPreferredSize(new Dimension(60,50));
        place.setBorder(BorderFactory.createEmptyBorder(0,30,0,20));
        for (double i = 1; i < controleur.getDistMax(); i+=controleur.getDistMax()/100) {
            JPanel ligne = new JPanel();
            ligne.setBackground(gradient(i, controleur.getDistMax(), controleur.getDistMin()));
            place.add(ligne);
        }
        echelle.add(place);
        JLabel loin = new JLabel("Loin");
        loin.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        echelle.add(loin);
        for (int i = 0; i < 1; i++) {
            JPanel Sep = new JPanel();
            Sep.setBackground(new Color(220, 220, 220));
            echelle.add(Sep);
        }
    }

    private void affArbre() {
        List<Arbre> arbres = controleur.getArbres();
        DefaultTreeModel model = (DefaultTreeModel)arbre.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        root.removeAllChildren();
        if (arbres != null) {
            for (Arbre a : arbres) {
                if (!a.estVide()) {
                    DefaultMutableTreeNode branche = new DefaultMutableTreeNode(a.getRacine().getEspece().getNom());
                    Arbre.Noeud droit = a.getRacine().getDroit();
                    if (droit != null) {
                        this.affNoeud(droit, branche);
                    }
                    Arbre.Noeud gauche = a.getRacine().getGauche();
                    if (gauche != null) {
                        this.affNoeud(gauche, branche);
                    }
                    root.add(branche);
                }
            }
        }
        model.reload(root);
    }

    private void affNoeud(Arbre.Noeud n, DefaultMutableTreeNode branche) {
        DefaultMutableTreeNode branche_suiv = new DefaultMutableTreeNode(n.getEspece().getNom());
        Arbre.Noeud droit = n.getDroit();
        if (droit != null) {
            this.affNoeud(droit, branche_suiv);
        }
        Arbre.Noeud gauche = n.getGauche();
        if (gauche != null) {
            this.affNoeud(gauche, branche_suiv);
        }
        branche.add(branche_suiv);
    }
    // Cette méthode rajoute (l'affichage graphique de) la matrice dans le JPanel Principale (this)
    private void affMatrice () {
        //System.out.println("CreaArbre : max = " + controleur.getDistMax());
        
        this.matrice_pan.removeAll();
        GridLayout gridy = new GridLayout(controleur.getTableau().length+2,controleur.getTableau().length+2,10,10);
        this.matrice_pan.setLayout(gridy);

        // Initialisation de la premiere ligne contenant les noms des especes
        this.matrice_pan.add(new JLabel("", SwingConstants.CENTER));
        for (int i = 0; i < controleur.getEspecesTableau().size(); i++) {
            this.matrice_pan.add(new JLabel(controleur.getEspecesTableau().get(i).getNom(), SwingConstants.CENTER));
        }
        this.matrice_pan.add(new JLabel("", SwingConstants.CENTER));

        // Initialisation des autres lignes du JPanel
        for (int i = 0; i < controleur.getEspecesTableau().size(); i++) {
            this.matrice_pan.add(new JLabel(controleur.getEspecesTableau().get(i).getNom(), SwingConstants.RIGHT));
            for (int j=0; j < controleur.getTableau().length; j++) {
                JLabel caseVal = new JLabel(controleur.getTableau()[i][j]+"", SwingConstants.CENTER);
                caseVal.setOpaque(true);
                // Initialisation de la Couleur de fond en fonction de sa valeur ; gradient(double d, double max)
                caseVal.setBackground(gradient(controleur.getTableau()[i][j], controleur.getDistMax(), controleur.getDistMin()));
                this.matrice_pan.add(caseVal);
            }
            this.matrice_pan.add(new JLabel("", SwingConstants.CENTER));
        }
        for (int i = 0; i < controleur.getEspecesTableau().size(); i++) {
            this.matrice_pan.add(new JLabel("", SwingConstants.CENTER));
        }
    }
    // Cette méthode calcule le gradiant de couleur de la valeur d en fonction de la valeur max
    private Color gradient(double d, double max, double min) {
        if (d == 0) return new Color(200, 200, 200);
        //float h = (float) -( log(d) / (log(max) * 2)); // version naïve, qui ne prend pas en compte le minimum
        
        // on prend le logarithme de la différence d entre génome, pour mieux visualiser les petites différences
        // noralisation de log(d) entre log(min) à 0 et log(max) à 1
        float h = (float) ( (log(d) - log(min)) / (log(max)));
        
        // on divise h par deux pour prendre la moitié du spectre des couleurs
        // et en négatif pour que les petites distances soient rouges et les grandes soient cyan
        return Color.getHSBColor(1 - h/2, 0.6F, 1F);
    }
}