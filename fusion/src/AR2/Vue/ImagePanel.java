package AR2.Vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private boolean etirer = false;
    private String chemin;

    public ImagePanel(String chemin,int width, int height) {
        this.chemin = chemin;
        if (chemin.equals("") || chemin==null) this.chemin = "./res/img/default.jpg";
        try {
            File img = new File(this.chemin);
            image = ImageIO.read(img);
            //Si nécessaire, on peut redimensionner un image
            if(width != 0 && height != 0) {
                //On crée une image auxiliaire
                BufferedImage resizedImage = new BufferedImage(width, height, image.getType());

                //On repeint dans l'image auxiliaire l'image d'origine en changeant sa taille
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, width, height, null);
                g.dispose();

                //Puis on modifie l'image d'origine v
                image = resizedImage;
            }


        } catch (IOException ex) {
            erreur(chemin);
        } catch (NullPointerException nullP) {
            erreur(chemin);
            throw nullP;

            /*
            this.chemin = "./res/img/defaulft.jpg";
            try {
                image = ImageIO.read(new File(chemin));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,"Vous avez modifier la base de données !", "Erreur",ERROR);
            }
            */

        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0, y = 0;
        int width = 0;
        int height = 0;
        if (image != null) {
            if (etirer) {
                width = this.getWidth();
                height = this.getHeight();
            } else {
                width = this.image.getWidth(this);
                height = this.image.getHeight(this);
                x = ((this.getWidth() - width) / 2);
                y = ((this.getHeight() - height) / 2);
            }
            g.drawImage(this.image, x, y, width, height, this);
        }
    }
    // Cette méthode affiche dans le JPanel Principale censé contenir l'image un message d'erreur
    private void erreur(String chemin) {
        JLabel erreur = new JLabel("Chemin Incorrect ! \""+chemin+"\"");
        JLabel possibilite0 = new JLabel("Erreurs possibles :");
        JLabel possibilite1 = new JLabel(" - chemin vers l'image incorrect");
        JLabel possibilite2 = new JLabel(" - nom de l'image incorrect");
        JLabel possibilite3 = new JLabel(" - extension de l'image incorrect");
        BoxLayout box = new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(box);

        this.add(new JLabel(" "));
        this.add(erreur);
        this.add(new JLabel(" "));
        this.add(possibilite0);
        this.add(possibilite1);
        this.add(possibilite2);
        this.add(possibilite3);
    }
}
