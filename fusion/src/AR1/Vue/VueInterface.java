package AR1.Vue;

import AR1.Controleur.Controleur;
import AR1.Modele.CaseMatrice;
import AR1.Modele.CaseSeq;
import AR1.Modele.Global;
import AR1.Modele.GlobalProt;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VueInterface extends JFrame {
    public static String csv_path;
    private JTextField s1;
    private JTextField s2;
    private Global global;
    private JPanel matrice;
    private JPanel conteneur;
    private JPanel score;
    private boolean isADN;
    private boolean matriceSub;

    public VueInterface(){
        this.isADN = true;
        this.matriceSub = false;

        this.global=new Global();

        this.conteneur = new JPanel();
        this.score = new JPanel();
        this.score.setLayout(new GridLayout(2,1));

        // création de la fenêtre
        this.setTitle("Alignement de séquence");
        this.setSize(800,700);
        this.setVisible(true);

        this.getContentPane().setLayout(new GridLayout(2,1));

        // JPanel du haut
        JPanel haut = new JPanel();
        this.add(haut);

        haut.setLayout(new GridLayout(1,2));

        // JPanel en haut à gauche qui contiendra les espaces pour écrire les séquences, faire les réglages de scores et les boutons
        JPanel echange = new JPanel();
        echange.setBackground(MyColors.fond);
        echange.setLayout(new GridLayout(5, 1, 100, 10));

        //Panel de la séquence 1
        JPanel seq1 = new JPanel();
        JLabel t1 = new JLabel("Séquence 1 :");
        seq1.add(t1);
        s1 = new JTextField(this.global.getSeq1());
        s1.setColumns(30);
        seq1.setBackground(MyColors.fond);

        seq1.add(s1);
        echange.add(seq1);

        // Panel de la séquence 2
        JPanel seq2 = new JPanel();
        JLabel t2 = new JLabel("Séquence 2 :");
        seq2.add(t2);
        s2 = new JTextField( this.global.getSeq2(),30);
        seq2.add(s2);
        echange.add(seq2);
        seq2.setBackground(MyColors.fond);

        JPanel score = new JPanel();
        score.setLayout(new GridLayout(1,3));
        score.setBackground(MyColors.fond);

        this.score.setBackground(MyColors.fond);
        haut.add(echange);
        haut.add(this.score);

        this.affichageMatrice();
    }

    public void affichageMatrice(){
        // JPanel du bas qui contiendra la matrice
        conteneur.setLayout(new GridLayout(1,3));
        JPanel matrice = new JPanel();
        matrice.setBackground(MyColors.fond);
        matrice.setSize(500,500);
        matrice.setLayout(new GridLayout(global.getCases().length, global.getCases()[0].length));
        for (int i = 0; i < global.getCases().length; i++) {
            for (int j = 0; j < global.getCases()[i].length; j++) {
                if(global.getCases()[i][j]!=null){
                    JPanel c = new VueMatrice(global.getCases()[i][j],this);
                    if(global.getCases()[i][j] instanceof CaseSeq){
                        JLabel texte = new JLabel(((CaseSeq) global.getCases()[i][j]).getNuc());
                        c.add(texte);
                        c.setBackground(MyColors.violet);
                    }
                    else{
                        int x = ((CaseMatrice) global.getCases()[i][j]).getScore();
                        JLabel texte = new JLabel(String.valueOf(x));
                        c.add(texte);
                        if(i==1 || j==1) c.setBackground(MyColors.rose);
                        else c.setBackground(Color.white);
                    }
                    Border lineborder = BorderFactory.createLineBorder(Color.black, 1);
                    c.setBorder(lineborder);
                    this.matrice=matrice;
                    matrice.add(c);
                }
                else matrice.add(new VueMatrice(null,this));
            }
        }
        conteneur.removeAll();
        conteneur.add(this.matrice);
        this.add(conteneur);
        this.revalidate();
        this.repaint();
    }

    //renvoie le panneau affichant la matrice
    public JPanel getMatrice() {
        return matrice;
    }
    
    //renvoie le panneau affichant le score
    public JPanel getScore() {
    	return score;
    }
    
    //réactualise le panneau de score
    public void setScore() {
    	this.revalidate();
        this.repaint();
    	this.score.setVisible(true);
    }
    
    //renvoie l'attribut global
    public Global getGlobal() { return this.global;}
    
    public boolean getIsADN() {return this.isADN;}

    // pout lancer l'affichage
    public static void main(String[] args) {
        if(args.length>1) {
            if (args[0].length()>=1) csv_path = args[0];
        }
        else csv_path = "../Modele/matriceProt.csv";
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VueInterface();
            }
        });*/
    }

    //affiche les messages d'erreur dans des JDialogue
    public void erreur(String erreur) {
        JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    public void miseAJourCustom() {
    	Controleur controleur = new Controleur(global, VueInterface.this);
    	this.score.removeAll();
    	controleur.affichageSeq_Score_custom();
    }

    //met à jour le Global d'une VueInterface
    public void setGlobal(Global g){
        this.global=g;
        this.s1.setText(g.getSeq1());
        this.s2.setText(g.getSeq2());
        this.s1.revalidate();
        this.s1.repaint();
        this.s2.revalidate();
        this.s2.repaint();
        this.setScore();
        this.affichageMatrice();
        Controleur controleur = new Controleur(global, VueInterface.this);
        controleur.miseAJourSeq(true,s1.getText());
        controleur.miseAJourSeq(false,s2.getText());
        if (VueInterface.this.score.getComponentCount()!=0)controleur.effacerChemin(false);//retire l'ancien panneau du score s'il existe
        controleur.calculerChemin();
        controleur.affichageSeq_Score();
        VueMatrice.isComputeAlignement=true;
        VueMatrice.isCustomPath=false;
    }
}
