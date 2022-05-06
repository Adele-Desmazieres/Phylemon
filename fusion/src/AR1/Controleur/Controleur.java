package AR1.Controleur;

import AR1.Modele.*;
import AR1.Vue.MyColors;
import AR1.Vue.VueInterface;
import AR1.Vue.VueMatrice;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Controleur {

    private Global global;
    private VueInterface vue;

    public Controleur(Global global, VueInterface vue){
        this.global = global;
        this.vue = vue;
    }

    public void calculerChemin(){
    	this.effacerChemin(false);
        this.global.cheminOptimal();
        Component[] components = this.vue.getMatrice().getComponents();
        for (int i = 0; i < components.length; i++) {
            Case c = ((VueMatrice)components[i]).getCaseM();
            if(c!=null && c instanceof CaseMatrice && ((CaseMatrice)c).getColor()){
                components[i].setBackground(MyColors.bleu);
            }
        }
        this.vue.revalidate();
        this.vue.repaint();
    }

    //si b est true, alors on active les boutons correspondant aux cases -> custom path
    public void effacerChemin(boolean b){
        this.global.clearChemin();
        Component[] components = this.vue.getMatrice().getComponents();
        for (int i = 0; i < components.length; i++) {
            Case c = ((VueMatrice)components[i]).getCaseM();
            if(c!=null && c instanceof CaseMatrice){
            	if(((CaseMatrice)c).getColor()) {
            		((CaseMatrice)c).setColore(false);
                    components[i].setBackground(Color.white);
            	}
                if(b) components[i].setEnabled(true);
            }
        }
        this.vue.getScore().removeAll();
        this.vue.getScore().revalidate();
        this.vue.getScore().repaint();
    }
    

    public void miseAJourSeq(boolean estSeqUn, String newSeq){
        try {
			if(this.global instanceof GlobalProt){
				GlobalProt.verifProt(newSeq);
			}
            else Global.verifADN(newSeq);
            if(estSeqUn) this.global.setSeq1(newSeq); //mise à jour seq1 dans global
            else this.global.setSeq2(newSeq); //mise à jour seq2 dans global
            this.effacerChemin(false); //effacer chemin
            this.global.miseAJourMatrice(); //mettre à jour nouvelle matrice
            this.vue.affichageMatrice();
        }
        catch (IllegalArgumentException e) {
            String erreur = e.getMessage();
            this.vue.erreur(erreur);
        }
    }

    public void miseAJourScore(int n, int val){
        switch(n){
            case 1 : this.global.setMatchScore(val);break;
            case 2 : this.global.setMisMatchScore(val);break;
            case 3 : this.global.setGapScore(val);break;
        }
        this.global.miseAJourMatrice(); //mettre à jour nouvelle matrice
		this.global.cheminOptimal();
        this.vue.affichageMatrice();
    }
    
    //"construit" les alignements jugés optimaux
    public void affichageSeq_Score() {
    	JPanel affichage=this.vue.getScore();
		JPanel alignement=new JPanel();
		affichage.setBackground(MyColors.fond);
		alignement.setBackground(MyColors.fond);
		//alignement.setLayout(new GridLayout(3,1));
    	CaseMatrice[] chemin=this.global.cheminTableau(this.global.getChemin());
    	String s1=(this.global.getSeq1()).charAt(0)+"\t";//la séquence verticale
    	String s2=(this.global.getSeq2()).charAt(0)+"\t";//la séquence horizontale
    	for(int i=1;i<chemin.length;i++) {
			CaseSeq[] adn = this.global.chercheParent(chemin[i]);
			if (this.global.estUnGapHorizontal(chemin[i], chemin[i - 1])) {//s'il y a un gap horizontal
				s1 += "-\t";//on met un - pour le signifier
				s2 += adn[1].getNuc() + "\t";
			} else if (this.global.estUnGapVertical(chemin[i], chemin[i - 1])) {// s'il y a un gap vertical
				s1 += adn[0].getNuc() + "\t";
				s2 += "-\t";//on met un - pour le signifier
			} else {
				s1 += adn[0].getNuc() + "\t";
				s2 += adn[1].getNuc() + "\t";
			}
		}
    	//on crée des JLabel qui vont contenir les séquences et le score
    	JLabel seq=new JLabel();
    	seq.setText(s1);
		seq.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	
    	JLabel seq2=new JLabel();
    	seq2.setText(s2);
		seq2.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	
    	JLabel seq3=new JLabel();
    	seq3.setText("Score= "+this.global.getScore());
		seq3.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	//on les ajoute au JPanel score de la VueInitiale
    	
    	JPanel petit_panneau=new JPanel();
    	GridLayout grille=new GridLayout(4,1);
    	petit_panneau.setLayout(grille);
    	petit_panneau.add(seq2);
    	petit_panneau.add(seq);
    	petit_panneau.add(seq3);
		petit_panneau.setBackground(MyColors.fond);
    	
    	alignement.add(petit_panneau);
    	affichage.add(alignement);

    	this.vue.setScore();
    }
    
    //crée le chemin à partir des CaseMatrice coloriées
    public CaseMatrice[] cheminCustom() {
    	LinkedList<CaseMatrice> chemin=new LinkedList<CaseMatrice>();
    	Component[] components = this.vue.getMatrice().getComponents();
        for (int i = components.length-1; i>=0 ; i--) {
            Case c = ((VueMatrice)components[i]).getCaseM();
            if(c!=null && c instanceof CaseMatrice){
            	if(((CaseMatrice)c).getColor()) {
            		chemin.add((CaseMatrice)c);
            	}
            }
        }
    	return this.global.cheminTableau(chemin);
    }
    
    //va gérer la création de l'alignement choisi par le joueur
    // ATTENTION DÉPEND DES VALEURS DE MATCH, MISMATCH ET GAP CHOISIES
    public void affichageSeq_Score_custom() {
    	int score=0;
    	JPanel affichage=this.vue.getScore();
		JPanel alignement=new JPanel();
		affichage.setBackground(MyColors.fond);
		alignement.setBackground(MyColors.fond);
    	alignement.setLayout(new GridLayout(3,1));
    	CaseMatrice[] chemin=this.cheminCustom();
    	String s1="";//la séquence verticale
    	String s2="";//la séquence horizontale
    	for(int i=0;i<chemin.length;i++) {
    		CaseSeq[] adn=this.global.chercheParent(chemin[i]);
    		if(chemin[i].getX()==1 && chemin[i].getY()==1) {
    			//break; supprime tout l'affichage
    		}
    		else if(i!=0 && this.global.estUnGapHorizontal(chemin[i],chemin[i-1])){//s'il y a un gap horizontal
    			s1+="-\t";//on met un - pour le signifier
    			if(adn[1]!=null)s2+=adn[1].getNuc()+"\t";
    			else s2+="-\t";
    			score+=this.vue.getGlobal().getGapScore();
    		}else if(i!=0 && this.global.estUnGapVertical(chemin[i],chemin[i-1])) {// s'il y a un gap vertical
    			if(adn[0]!=null)s1+=adn[0].getNuc()+"\t";
    			else s1+="-\t";
    			s2+="-\t";//on met un - pour le signifier
    			score+=this.vue.getGlobal().getGapScore();
    		}
    		else {
    			if(adn[0]!=null)s1+=adn[0].getNuc()+"\t";
    			else s1+="-\t";
    			if(adn[1]!=null)s2+=adn[1].getNuc()+"\t";
    			else s2+="-\t";
    			score+=chemin[i].getValeur();//s'il s'agit d'un match ou d'un mismatch
    		}
    	}
    	//on crée des JLabel qui vont contenir les séquences et le score
    	JLabel seq=new JLabel();
    	seq.setText(s1);
		seq.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	
    	JLabel seq2=new JLabel();
    	seq2.setText(s2);
		seq2.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	
    	JLabel seq3=new JLabel();
    	seq3.setText("Score= "+score);
		seq3.setFont(new Font(Font.MONOSPACED,Font.PLAIN,20));
    	//on les ajoute au JPanel score de la VueInitiale
    	JPanel petit_panneau=new JPanel();
		petit_panneau.setBackground(MyColors.fond);
    	GridLayout grille=new GridLayout(3,1);
    	petit_panneau.setLayout(grille);
    	petit_panneau.add(seq2);
    	petit_panneau.add(seq);
    	petit_panneau.add(seq3);
    	
    	alignement.add(petit_panneau);
    	affichage.add(alignement);

    	this.vue.setScore();
    }
    
    public JLabel explicationsCalcul(CaseMatrice triangle, CaseMatrice caseM) {
		JLabel j1;
		String s1="";
		int i1=0;

		if(vue.getGlobal().estUnGap(caseM, triangle)) {
			i1=triangle.getScore()+vue.getGlobal().getGapScore();
			s1=triangle.getScore()+" + "+vue.getGlobal().getGapScore()+" (The Gap score) = "+i1;
		}
		else if (((CaseMatrice)caseM).getValeur()==vue.getGlobal().getMisMatchScore()) {
			i1=triangle.getScore()+vue.getGlobal().getMisMatchScore();
			s1=triangle.getScore()+" + "+vue.getGlobal().getMisMatchScore()+" (Due to a mismatch) = "+i1;
		}
		else {
			i1=triangle.getScore()+vue.getGlobal().getMatchScore();
			s1=triangle.getScore()+" + "+vue.getGlobal().getMatchScore()+" (Due to a match) = "+i1;
		}
		j1=new JLabel(s1, SwingConstants.RIGHT);
		j1.setHorizontalAlignment(2);


		return j1;
    }
    
}
