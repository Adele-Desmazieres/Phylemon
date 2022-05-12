package AR2.Vue;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.EAST;


public class Accueil extends JPanel{

    protected Accueil() {
        creaAccueil();
    }

    
    protected void creaAccueil() {

        this.setLayout(new BorderLayout());

        // Création du titre suivi de l'ajout
        JLabel title = new JLabel("Arbre Phylogenetique");
        title.setSize(100,100);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setSize(this.getWidth()/10,this.getHeight()/10);
        this.add(title, BorderLayout.NORTH);

        // Création de l'image suivi de l'ajout au CENTER
        ImagePanel imageJP = new ImagePanel("../res/img/logo_v2.jpg",0,0);
        imageJP.setAlignmentX(JPanel.CENTER_ALIGNMENT); imageJP.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        this.add(imageJP,BorderLayout.CENTER);

        // Création des auteurs
        JPanel createur0 = new JPanel();
        //JPanel crea1 = new JPanel();
        JLabel createurs1 = new JLabel("Auteurs : Dounia B., Alexandre B., Linéa D., Benoit D., Adèle D., Joachim E., Ewen G., Louise L., Elvire T.");
        //crea1.add(createurs1);
        //JPanel crea2 = new JPanel ();
        //JLabel createurs2 = new JLabel("Joachim EKOKA, Ewen GLASZIOU, Louise LAM, Elvire TRAMA");
        //crea2.add(createurs2);
        createur0.add(createurs1);
        //createur0.add(createurs2);
        this.add(createur0, BorderLayout.SOUTH);
        //this.add(createurs2, BorderLayout.SOUTH);

        this.add(new JPanel(),BorderLayout.WEST);
        this.add(new JPanel(), EAST);

    }

}
