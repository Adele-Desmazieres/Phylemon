package Vue;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import Controleur.Controleur;

import static java.awt.BorderLayout.*;

public class ChoixEspeces extends JPanel {

    //Constructeur
    public ChoixEspeces(Controleur controleur, Vue vue) {
        controleur.clear(); creaChoixEspece(controleur, vue);
    }
    //Une fonction pour créer toute la page choix espèces

    public void creaChoixEspece(Controleur controleur, Vue vue) {
        Color colorSelect = new Color(90, 215, 140);
        Color colorNotSelect = new Color(107, 76, 76);

        //Le scrollPane contenant la liste de checkbox
        JScrollPane listeEsp = new JScrollPane();
        JViewport view = new JViewport();
        view.setBackground(new Color(107, 76, 76));

        //view.setSize(new Dimension(250,200));
        //listeEsp.getHAdjustable().setBlockIncrement(100);
        //listeEsp.getVAdjustable().setValue(25);
        //view.setBackground(new Color(107, 76, 76));

        this.setLayout(new BorderLayout());
        //JPanel especeChoix = new JPanel();
        Box especeChoix = new Box(BoxLayout.Y_AXIS);
        //Creation des Jpanel
        //Description
        JPanel description = new JPanel();
        description.setBackground(new Color(186, 177, 182, 255));
        description.setLayout(new BoxLayout(description, BoxLayout.Y_AXIS));
        //Boutons
        JPanel boutons = new JPanel();


        view.add(especeChoix);

        //listeEsp.add(especeChoix);
        listeEsp.setViewport(view);

        //Construction du JPanel choixEspèce
        this.add(listeEsp, WEST);
        this.add(description, CENTER);
        this.add(boutons, BorderLayout.SOUTH);
        this.setBackground(colorNotSelect);

        //Création de la liste de choix des espèces.
        LinkedList<JCheckBox> listBoxEspeces = new LinkedList<>();
        LinkedList<String> especes = controleur.recupTousLesNom(); //On récupère le nom de toutes les espèces de notre fichier

        for (String nom : especes) { //On crée une checkbox pour chaque espèce dans notre fichier
            JCheckBox jcheck = new JCheckBox(nom); //On crée une checkbox pour chaque espèce dans notre fichier
            jcheck.setSize(new Dimension(250,10));
            jcheck.setBackground(colorNotSelect); //pour plus de facilité visuelle on change la couleur de la case de la checkbox a Rouge lorsque qu'elle n'est pas choisie
            listBoxEspeces.add(jcheck);
            jcheck.addActionListener(event -> { // On initialise les caractéristiques de la checkbox
                if (jcheck.isSelected()) {
                    controleur.listeAjouter(jcheck.getText()); //lorsque qu'une espèce est choisie on l'ajoute a la liste des espèces choisie
                    jcheck.setBackground(colorSelect); //pour plus de facilité visuelle on change la couleur de la case de la checkbox a Vert lorsque choisie

                    //Permet d'ouvrir une fenêtre avec la description
                    String nomJO = jcheck.getText();
                    String descriptionJO = controleur.getDescription(jcheck.getText());
                    String cheminImage = controleur.getChemin(jcheck.getText());

                    description.removeAll();
                    JLabel nomEsp  = new JLabel(nomJO);
                    nomEsp.setFont(new Font("Titre", Font.BOLD, 20));
                    description.add(nomEsp, Component.CENTER_ALIGNMENT);
                    description.add(new JLabel(descriptionJO));
                    description.add(new ImagePanel(cheminImage,600,500));
                    description.setVisible(true);
                    description.revalidate();
                    description.repaint();
                } else {
                    controleur.listeEnlever(jcheck.getText()); //lorsque qu'une espèce est décochée, on la retire de la liste des espèces choisie
                    jcheck.setBackground(colorNotSelect); //pour plus de facilité visuelle on change la couleur de la case de la checkbox a Rouge
                }
            });
            //jcheck.setPreferredSize(new Dimension(200,30)); ///ne marche pas
            //view.add(jcheck);
            especeChoix.add(jcheck); //on ajoute la checkbox aux scrollPane
        }
        // bouton identique à celui du menu qui lance la matrice
        JButton valide = new JButton("Valider");
        valide.addActionListener(event ->
        {
            vue.changePanel(vue.creaArbre);
            if (!controleur.getEspecesChoisis().isEmpty()) {
                vue.creaArbre.demarageCreaArbre(event);
            }
        });
        boutons.add(valide, EAST);

        //bouton pour cocher toutes les espèces.
        JButton selectAll = new JButton("Tout sélectionner");
        selectAll.addActionListener(event -> {
            for (JCheckBox jb : listBoxEspeces) {
                if (!jb.isSelected()) { //On coche toutes les cases non cochées
                    jb.setSelected(true); //On set la valeur de la JCheckbox a selected
                    jb.setBackground(colorSelect); //on modifie son background pour la cohérence
                    controleur.listeAjouter(jb.getText()); //on ajoute l'espèce sélectionnée à la liste d'espèces choisie
                }
            }
        });

        boutons.add(selectAll, BorderLayout.CENTER);
        //bouton pour décocher toutes les espèces.
        JButton unselectAll = new JButton("Tout désélectionner");
        unselectAll.addActionListener(event -> {
            for (JCheckBox jb : listBoxEspeces) {
                if (jb.isSelected()) { //On décoche toutes les cases non cochées
                    jb.setSelected(false); //On set la valeur de la JCheckbox a unselected
                    jb.setBackground(colorNotSelect); //on modifie son background pour la cohérence
                    controleur.listeEnlever(jb.getText()); //on enlève l'espèce de la liste des espèces choisie
                }
            }
        });
        boutons.add(unselectAll, BorderLayout.WEST);

        JButton save = new JButton("Sauvegarder");
        save.addActionListener(event ->{
            vue.vcharge.choose(true);
        });
        boutons.add(save);

        JButton charge = new JButton("Chargement");
        charge.addActionListener(event -> {
            vue.vcharge.choose(false);
            vue.changePanel( vue.choixEspece =  new ChoixEspeces(controleur, vue) );
            controleur.clear();
        });
        boutons.add(charge);
    }
}