package AR2.Vue;

import AR2.Controleur.Controleur;
import AR2.Modele.EspeceVivante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;

import static java.awt.BorderLayout.*;

public class Bestiaire extends JPanel{

    public Bestiaire(Controleur controleur){
        creaBestiaire(controleur);
    }
    public void creaBestiaire(Controleur controleur) {
        this.setLayout(new BorderLayout());
        ScrollPane listeEsp = new ScrollPane();
        listeEsp.setPreferredSize(new Dimension(250,200));
        listeEsp.getHAdjustable().setBlockIncrement(10);
        this.add(listeEsp, BorderLayout.WEST);
        Box liste = new Box(BoxLayout.Y_AXIS);
        listeEsp.add(liste);
        // Création du JPanel contenant les informations à propos de l'EspeceVivante que sera sélectionné
        JPanel description = new JPanel();
        description.setBackground(new Color(186, 177, 182, 255));
        description.setLayout(new BoxLayout(description, BoxLayout.Y_AXIS));
        this.add(description,CENTER);

        LinkedList<EspeceVivante> especes =  controleur.getBanqueDonnees(); //Pas ouf ça

        for (EspeceVivante esp : especes) {

            //Creation de l'icône de chaque espèce
            ImageIcon imageIcon = new ImageIcon(esp.getImg()); //"  // mettre image de l'espèce et l'ajouter // "
            Image im = imageIcon.getImage();
            int	hauteur	= 80;
            int	largeur	= 80;
            im	= im.getScaledInstance(largeur,hauteur,Image.SCALE_DEFAULT);
            imageIcon = new ImageIcon(im);

            //creation du JPanel avec la liste des noms des espèces
            JPanel listeNom = new JPanel();

            //création d'un JLabel
            JLabel l = new JLabel(esp.getNom());
            l.setIcon(imageIcon); //ça ne semble pas marcher
            l.setIcon(imageIcon);
            l.setFont(new Font("Times", Font.PLAIN, 15)); //Pour avoir des JLabel plus gros
            listeNom.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //trouver dans quoi il faudrait mettre les éléments de description
                    description.removeAll();
                    JLabel nom  = new JLabel(esp.getNom());
                    nom.setFont(new Font("Titre", Font.BOLD, 20));
                    description.add(nom,NORTH);
                    //
                    JButton modifier = modifBouton(esp);
                    //
                    description.add(new JLabel(esp.getDescription()),CENTER); /**/
                    description.add(modifier,SOUTH);
                    description.add(new ImagePanel(esp.getImg(),600,500));
                    description.revalidate();
                    description.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {  // identique pour le comfort d'utilisation si on click en se déplacant ça compte pour du pressed
                    description.removeAll();
                    JLabel nom  = new JLabel(esp.getNom());
                    nom.setFont(new Font("Titre", Font.BOLD, 20));
                    description.add(nom,NORTH);
                    //
                    JButton modifier = modifBouton(esp);
                    //
                    description.add(new JLabel(esp.getDescription()),CENTER);
                    description.add(modifier,SOUTH);
                    description.add(new ImagePanel(esp.getImg(),600,500));
                    description.revalidate();
                    description.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //Rien
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    listeNom.setBackground(new Color(200,200,200));
                    l.setFont(new Font("Italic", Font.ITALIC, 15));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    listeNom.setBackground(null);
                    l.setFont(new Font("Times", Font.PLAIN, 15));
                }
            });
            listeNom.add(l, CENTER);
            liste.add( listeNom, CENTER);
        }

    }
    // Cette méthode modifie les informations prises en arguments de l'EspeceVivante ev
    private void modifier(EspeceVivante ev, String newNom, String newImg, String newDesc) {
        // Si les informations en paramètres ne sont pas correct, celle de base sont conservé.
        if (!newNom.equals("")) ev.setNom(newNom);
        if ( !newImg.equals("") && urlEstValide(newImg) ) ev.setImg(newImg);
        if (!newDesc.equals("")) ev.setDescription(newDesc);
    }
    // Cette méthode renvoie un JButton demandant les nouvelles informations de l'EspeceVivante ev
    private JButton modifBouton(EspeceVivante ev) {
        JButton res = new JButton("Modifier");
        res.addActionListener( (event) ->
        {
            String nom = JOptionPane.showInputDialog(this,
                    "Quel est le nouveau nom de l'espece?", ev.getNom());
            String img = JOptionPane.showInputDialog(this,
                    "Quel est le lien vers nouvelle image de l'espece?", ev.getImg());
            String des = JOptionPane.showInputDialog(this,
                    "Quelle est la nouvelle description de l'espece?", ev.getDescription());
            modifier(ev,nom,img,des);

        });
        return res;
    }
    // Cette méthode renvoie true si le chemin vers le fichier est correct, false sinon
    private boolean urlEstValide(String chemin) {
        File img = new File(chemin);
        return img.exists();
    }
}
