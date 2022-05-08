package AR2.Vue;

import AR2.Controleur.Controleur;
import AR1.Modele.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class CreaEspeces extends JPanel {
    private Color fond = new Color(114, 164, 236);
    private String chemin = "./res/img/default.jpg";
    public CreaEspeces(Controleur controleur, ChoixEspeces choixEspeces, Vue vue){
        creaCreaEspece(controleur, choixEspeces, vue);
    }
    public void creaCreaEspece(Controleur controleur, ChoixEspeces choixEspeces, Vue vue) {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // Création des dimensions utilisés pour les JLabel, JTextField, et le JTextArea
        Dimension dim1 = new Dimension(400,30), dim2 = new Dimension(400,100);
        Dimension dimL = new Dimension(150,35);

        // Creation du nom (JLabel + JTextField)
        JLabel nomJL = new JLabel("Nom : ");
        JTextField nomJT = new JTextField("");
        nomJL.setPreferredSize(dimL);
        nomJT.setPreferredSize(dim1);

        // Creation du genome
        JLabel genomeJL = new JLabel("Genome : ");
        JTextField genomeJT = new JTextField("");
        genomeJL.setPreferredSize(dimL);
        genomeJT.setPreferredSize(dim1);

        // Creation du chemin vers l'image
        JLabel imageJL = new JLabel("Image (chemin) : ");
        JTextField imageJT = new JTextField("");
        JFileChooser imageJFC = new JFileChooser();
        imageJL.setPreferredSize(dimL);
        imageJT.setPreferredSize(dim1);

        // Creation de la description
        JLabel descriptionJL = new JLabel("Description : ");
        JTextArea descriptionJT = new JTextArea();
        descriptionJL.setPreferredSize(dimL);
        descriptionJT.setPreferredSize(dim2);

        // Création du JFileChooser et initialisation de ses filtres (de type) de fichier
        JFileChooser chooser = new JFileChooser(); chooser.setDialogTitle("Choisissez une image");
        chooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "JPEG Image";
            }
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg");
                }
            }
        });
        chooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "PNG Image";
            }
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".png");
                }
            }
        });

        // Creation des JButtons et de leur ActionListener
        JButton effacer = new JButton("Effacer");
        JButton valider = new JButton("Valider");
        JButton choisir = new JButton("Choisir une image");
        // -- actionListener de effacer
        effacer.addActionListener(event -> {
            nomJT.setText(""); descriptionJT.setText("");
            imageJT.setText(""); genomeJT.setText("");
            JOptionPane.showMessageDialog(this,"Informations effacées");
            // **** Si pb changer res par une JFrame
        });
        // -- actionListener de valider
        valider.addActionListener(event -> {
            String genome = genomeJT.getText();
            try {
                profilComplet(nomJT.getText(), genome);
                boolean existeDeja = controleur.ajouterNouvEspece(nomJT.getText(), genome, descriptionJT.getText(), imageJT.getText());
                if (!existeDeja)
                    JOptionPane.showMessageDialog(this, "Cette espèce existe déjà !"); //Si l'espèce n'as pas été ajouté, c'est qu'elle existe déja.
                else {
                    JOptionPane.showMessageDialog(this, "Espece sauvegardée");
                    nomJT.setText("");
                    descriptionJT.setText("");
                    imageJT.setText("");
                    genomeJT.setText("");
                    //Pas clean on peut improve
                    controleur.getEspecesChoisis().removeAll(controleur.getEspecesChoisis());//On désélectionne toutes les espèces.
                    vue.choixEspece = new ChoixEspeces(controleur, vue);
                }
            }
            catch (IllegalArgumentException e){
                String erreur = e.getMessage();
                JOptionPane.showMessageDialog(this, erreur);
            }
        });
        // -- actionListener de choisir
        choisir.addActionListener((event) -> {
            int userSelection = chooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fichierChoisi = chooser.getSelectedFile();
                chemin = fichierChoisi.getAbsolutePath();
            }
            imageJT.setText(chemin);
            this.repaint();
        });

        // Ajout des JLabel, des JTextField, et des JButtons dans des JPanels intermédiaires
        /* Explications :
         * Des JPanel intermédiaire ont été créé pour rassembler chaque couple JLabel-JTextField voulu dans le même cadre.
         * Cela permet d'avoir un résultat propre dont le positionnement se fait automatiquement. */

        // On est obligé de recréer un nouveau BorderLayout à chaque fois sinon le JPanel inialiser avant est effacé.
        JPanel interNom = placementInter(new BorderLayout(),nomJL,nomJT);
        JPanel interGenome = placementInter(new BorderLayout(),genomeJL,genomeJT);

        JPanel interImage = placementInter(new BorderLayout(),imageJL,imageJT,choisir);
        // interImage.add(imageJL); interImage.add(imageJT); interImage.add(choisir);

        JPanel interDescription = placementInter(new BorderLayout(), descriptionJL, descriptionJT);


        JPanel interBoutons = new JPanel();
        interBoutons.add(valider); interBoutons.add(effacer);

        JPanel[] all = {interNom, interGenome, interImage, interDescription, interBoutons};

        placementJPanel(all);
        backGround(all);

    }
    // Cette méthode vérifie que les informations essentielles ont bien été écrite et qu'elles sont corrects.
    private void profilComplet(String nom, String genome) throws IllegalArgumentException {
        if (nom.equals("") || genome.equals("")) throw new IllegalArgumentException("Veuillez remplir le champ.");
        Global.verifADN(genome);
    }
    // Cette méthode prend un paramètre un String, elle renvoie true ssi c'est un nombre, false sinon.
    private boolean estNombre(String genome) {
        for (int i=0; i<genome.length(); i++) if ( Character.isLetter( genome.charAt(i) )) return false;
        return true;
    }
    // Cette méthode initialise le Background de tous les JPanel du tableau à l'attribut fond.
    private void backGround(JPanel[] all) { for (int i=0; i<all.length; i++) all[i].setBackground(fond);}
    // Cette méthode dispose dans le JPanel Principal (this) les différents JPanel intermédiaires.
    private void placementJPanel(JPanel[] all) {
        this.add(all[4],BorderLayout.SOUTH);

        JPanel inter = new JPanel();
        GridLayout box = new GridLayout(3,1);
        inter.setLayout(box);

        JPanel interGrid1 = new JPanel(new GridLayout(1,2));
        interGrid1.add(all[0]); // Nom
        interGrid1.add(all[1]); // Genome

        JPanel interGrid2 = new JPanel(new GridLayout(1,2));
        interGrid2.add(all[2]); // Image
        interGrid2.add(new ImagePanel(chemin,0,0)); this.repaint();

        inter.add(interGrid1);
        inter.add(interGrid2);
        inter.add(all[3]); // Description

        this.add(inter,BorderLayout.CENTER);
    }
    // Cette méthode renvoie le JPanel intermédiaire pour la création du Nom ou du Genome.
    public JPanel placementInter(BorderLayout border, JLabel enTete, JTextField text) {
        JPanel res = new JPanel(border);
        JPanel inter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inter.add(text); inter.setBackground(fond);
        res.add(enTete,BorderLayout.NORTH); res.add(inter, BorderLayout.CENTER);
        return res;
    }
    // Cette méthode renvoie un JPanel intermédiaire pour le choix de l'Image.
    public JPanel placementInter(BorderLayout border, JLabel enTete, JTextField text, JButton button) {
        JPanel res = new JPanel(border);
        JPanel inter1 = new JPanel(new FlowLayout(FlowLayout.LEFT)), inter2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inter1.add(text); inter1.setBackground(fond);
        inter2.add(button); inter2.setBackground(fond);
        res.add(enTete,BorderLayout.NORTH); res.add(inter1, BorderLayout.CENTER); res.add(inter2,BorderLayout.SOUTH);
        return res;
    }
    // Cette méthode renvoie le JPanel intermédiaire pour la création de la description.
    public JPanel placementInter(BorderLayout border, JLabel enTete, JTextArea text) {
        JPanel res = new JPanel(border);
        JPanel inter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inter.add(text); inter.setBackground(fond);
        res.add(enTete,BorderLayout.NORTH); res.add(inter, BorderLayout.CENTER);
        return res;
    }

}
