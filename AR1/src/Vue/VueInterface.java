package Vue;

import Controleur.Controleur;
import Modele.CaseSeq;
import Modele.CaseMatrice;
import Modele.Global;
import Modele.GlobalProt;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VueInterface extends JFrame {
    private JTextField s1;
    private JTextField s2;
    private Global global;
    private JPanel matrice;
    private JPanel conteneur;
    private JPanel score;
    private boolean isADN;
    private boolean matriceSub;

    public VueInterface(){
        //initialisation de global
        this.isADN = true;
        this.matriceSub = false;

        this.global=new Global();

        this.conteneur = new JPanel();
        this.score = new JPanel();
        this.score.setLayout(new GridLayout(2,1));

        // création de la fenêtre
        this.setTitle("Alignement de séquence");
        this.setSize(1000,800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.getContentPane().setLayout(new GridLayout(2,1));

        // JPanel du haut
        JPanel haut = new JPanel();
        this.add(haut);

        haut.setLayout(new GridLayout(1,2));

        // JPanel en haut à gauche qui contiendra les espaces pour écrire les séquences, faire les réglages de scores et les boutons
        JPanel echange = new JPanel();
        echange.setLayout(new GridLayout(5, 1));

        //Panel de la séquence 1
        JPanel seq1 = new JPanel();
        JLabel t1 = new JLabel("Séquence 1 :");
        seq1.add(t1);
        s1 = new JTextField(this.global.getSeq1());
        s1.setColumns(30);

        seq1.add(s1);
        echange.add(seq1);

        // Panel de la séquence 2
        JPanel seq2 = new JPanel();
        JLabel t2 = new JLabel("Séquence 2 :");
        seq2.add(t2);
        s2 = new JTextField( this.global.getSeq2(),30);
        seq2.add(s2);
        echange.add(seq2);


        // Panel de l'affichage du texte
        JPanel score = new JPanel();
        score.setLayout(new GridLayout(1,3));
        JLabel sc1 = new JLabel("Match Score");
        JLabel sc2 = new JLabel("Mismatch Score");
        JLabel sc3 = new JLabel("Gap Score");
        score.add(sc1);
        score.add(sc2);
        score.add(sc3);
        echange.add(score);

        // Panel des JSpinner
        JPanel spinner = new JPanel();
        spinner.setLayout(new GridLayout(1,3));

        // IL FAUDRA PEUT ETRE METTRE LES JSPINNER EN ATTRIBUT DE LA FENÊTRE POUR RECUPERER FACILEMENT LES VALEURS A VOIR
        SpinnerModel mS = new SpinnerNumberModel(1,-5,5,1);
        JSpinner matchS = new JSpinner(mS);
        matchS.setSize(300,300);
        spinner.add(matchS);

        SpinnerModel misS = new SpinnerNumberModel(-1,-5,5,1);
        JSpinner mismatchS = new JSpinner(misS);
        mismatchS.setSize(800,300);
        spinner.add(mismatchS);

        SpinnerModel gS = new SpinnerNumberModel(-2,-5,5,1);
        JSpinner gapS = new JSpinner(gS);
        gapS.setSize(300,300);
        spinner.add(gapS);

        echange.add(spinner);

        //controle des actions des spinners
        matchS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Controleur ctrl = new Controleur(VueInterface.this.global, VueInterface.this);
                ctrl.miseAJourScore(1,(Integer)matchS.getValue());
            }
        });

        mismatchS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Controleur ctrl = new Controleur(VueInterface.this.global, VueInterface.this);
                ctrl.miseAJourScore(2,(Integer)mismatchS.getValue());
            }
        });

        gapS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Controleur ctrl = new Controleur(VueInterface.this.global, VueInterface.this);
                ctrl.miseAJourScore(3,(Integer)gapS.getValue());
            }
        });

        // Panel des boutons
        JPanel boutons = new JPanel();
        MyButton COA = new MyButton();
        COA.setText("Compute Optimal Alignement");
        MyButton clear = new MyButton();
        clear.setText("Clear Path");
        MyButton custom = new MyButton();
        custom.setText("Custom Path");
        boutons.add(COA);
        boutons.add(clear);
        boutons.add(custom);
        echange.add(boutons);
        
     // JPanel en haut à droite où on verra l'alignement de séquence et le score
        //remplacé par le JPanel score qui est un attribut
        //JPanel sequences = new JPanel();
        //sequences.setBackground(Color.green);

        //calcule le chemin et l'affiche
        COA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controleur controleur = new Controleur(global, VueInterface.this);
                controleur.miseAJourSeq(true,s1.getText());
                controleur.miseAJourSeq(false,s2.getText());
                if (VueInterface.this.score.getComponentCount()!=0)controleur.effacerChemin(false);//retire l'ancien panneau du score s'il existe
                controleur.calculerChemin();
                controleur.affichageSeq_Score();
                VueMatrice.isComputeAlignement=true;
            }
        });

        //efface le chemin
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controleur controleur = new Controleur(global, VueInterface.this);
                controleur.miseAJourSeq(true,s1.getText());
                controleur.miseAJourSeq(false,s2.getText());
                controleur.effacerChemin(false);
                VueMatrice.isComputeAlignement=false;
            }
        });
        
        custom.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                Controleur controleur = new Controleur(global, VueInterface.this);
                controleur.miseAJourSeq(true,s1.getText());
                controleur.miseAJourSeq(false,s2.getText());
                VueMatrice.premiereCase=null;
                controleur.effacerChemin(true);
                VueInterface.this.score.add(new JLabel("En cours d'alignement"));
            }
        });

        
        haut.add(echange);
        haut.add(this.score);

        this.affichageMatrice();

        // La barre de menu pour changer les modes
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);

        JMenu mode = new JMenu("Changer de mode");
        menu.add(mode);

        JMenuItem ADNProt = new JMenuItem("Protéines");
        mode.add(ADNProt);

        ADNProt.addActionListener((event) -> {
            if(this.isADN) {
                ADNProt.setText("ADN");
                this.global = new GlobalProt();

                //mise à jour séquences
                seq1.remove(s1);
                seq2.remove(s2);
                this.s1 = new JTextField(((GlobalProt)this.global).getSeq1(),30);
                this.s2 = new JTextField(((GlobalProt)this.global).getSeq2(),30);
                seq1.add(s1);
                seq2.add(s2);

                sc1.setVisible(false);
                sc2.setVisible(false);
                matchS.setVisible(false);
                mismatchS.setVisible(false);

                this.conteneur.remove(this.matrice);
                affichageMatrice();

                this.revalidate();
                this.repaint();
            }
            else {
                ADNProt.setText("Protéines");
                this.global = new Global();
                //this.conteneur.remove(this.matrice);

                seq1.remove(s1);
                seq2.remove(s2);
                this.s1 = new JTextField(this.global.getSeq1(),30);
                this.s2 = new JTextField(this.global.getSeq2(),30);
                seq1.add(s1);
                seq2.add(s2);

                sc1.setVisible(true);
                sc2.setVisible(true);
                matchS.setVisible(true);
                mismatchS.setVisible(true);

                this.conteneur.remove(this.matrice);
                affichageMatrice();

                this.revalidate();
                this.repaint();
            }
            this.isADN = !this.isADN;
        });
    }

    public void affichageMatrice(){
        // JPanel du bas qui contiendra la matrice
        conteneur.setLayout(new GridLayout(1,3));
        JPanel matrice = new JPanel();
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

    // pour faire les tests de l'affichage
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VueInterface();
            }
        });
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
}
