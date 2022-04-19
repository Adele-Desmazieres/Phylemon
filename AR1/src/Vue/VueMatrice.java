package Vue;
import Modele.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import Controleur.Controleur;

import java.awt.*;
import java.awt.event.MouseEvent;

public class VueMatrice extends JPanel implements MouseInputListener {
	protected static Case premiereCase=null;
    protected Case caseM;
    protected VueInterface vue;
    protected static boolean explicationsON=false;
	protected static boolean isComputeAlignement=false;

    public VueMatrice(Case c, VueInterface v){
        this.caseM = c;
        this.vue=v;
        this.setEnabled(false);//on désactive la case
        this.addMouseListener(this);
    }

    public Case getCaseM(){ return caseM; }
    
    public boolean casesVoisines() {
    	if(premiereCase.getX()==caseM.getX() && premiereCase.getY()==caseM.getY()+1) return true;
    	else if(premiereCase.getX()==caseM.getX()+1 && (premiereCase.getY()==caseM.getY() || premiereCase.getY()==caseM.getY()+1)) return true;
    	return false;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    	if(this.isEnabled()) {//si on peut cliquer sur la case
    		if(premiereCase==caseM) {//cela veut dire qu'on a de nouveau cliqué sur le bouton pour revenir en arrière
    			//il faut récupérer la case précédente pour la mettre dans premiereCase
    			premiereCase=VueMatrice.this.vue.getGlobal().estLaPrecedente((CaseMatrice)caseM);
    			((CaseMatrice)caseM).setColore(false);//on met l'attribut colore à false
    			VueMatrice.this.vue.getGlobal().retireDerniereCase();//on retire la dernière case ajoutée
    			if(this.caseM.getY()>1 && this.caseM.getX()>1)this.setBackground(Color.white);//on met la couleur de fond à white
				else this.setBackground(MyColors.rose);
    			VueMatrice.this.vue.miseAJourCustom();
				this.revalidate();
				this.repaint();
    		}
    		else if(premiereCase==null || this.casesVoisines()) {
    			premiereCase=caseM;
				((CaseMatrice)caseM).setColore(true);//on met l'attribut colore à true
				VueMatrice.this.vue.getGlobal().ajoutCaseSurChemin((CaseMatrice)caseM);//on ajoute la case dans le chemin
    			this.setBackground(MyColors.bleu);
    			VueMatrice.this.vue.miseAJourCustom();
				this.revalidate();
				this.repaint();
    		}
    		
    	}
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        //affichage comment le chemin est calculé
    	if (this.caseM instanceof CaseMatrice && this.caseM.getX()>1 && this.caseM.getY()>1 && vue.getIsADN()) {
    		explicationsON=true;
    		//si la case est coloriée, alors on affiche son calcul
    		//on la colore momentanément
    		this.setBackground(MyColors.jaune);//en bleu pour l'instant
    		
    		Controleur controleur = new Controleur(vue.getGlobal(),vue);
   		
   			JPanel explications=new JPanel();
   			explications.setLayout(new GridLayout(2,2,50,10));
    			
   			CaseMatrice[] triangle= vue.getGlobal().renvoieTriangle(caseM);
    			/*
    			 * triangle[0] -> case diagonale
    			 * triangle[1] -> case supérieure
    			 * triangle[2] -> case latérale
    			 */
   			//Explications case diagonale
    		JLabel titre1=new JLabel("Score from Diagonal cell");
   			JPanel case1=new JPanel();
    		
    		JLabel j1=controleur.explicationsCalcul(triangle[0], (CaseMatrice)caseM);
			//String s1=controleur.explicationsCalculBis(triangle[0], (CaseMatrice)caseM);
    		
    		case1.setLayout(new GridLayout(2,1));
   			case1.add(titre1);
   			case1.add(j1);
   			
   			//Explications case latérale
   			JLabel titre2=new JLabel("Score from Side cell");
   			JPanel case2=new JPanel();
   			
   			JLabel j2=controleur.explicationsCalcul(triangle[2], (CaseMatrice)caseM);
			//String s2=controleur.explicationsCalculBis(triangle[2], (CaseMatrice)caseM);
    		
   			case2.setLayout(new GridLayout(2,1));
   			case2.add(titre2);
   			case2.add(j2);
   			
   			//Explications case supérieure
   			JLabel titre3=new JLabel("Score from Upper cell");
   			JPanel case3=new JPanel();
   			
   			JLabel j3=controleur.explicationsCalcul(triangle[1], (CaseMatrice)caseM);
			//String s3=controleur.explicationsCalculBis(triangle[1], (CaseMatrice)caseM);
    		
   			case3.setLayout(new GridLayout(2,1));
   			case3.add(titre3);
   			case3.add(j3);
   			
   			String s="Wining (max) score is "+String.valueOf(((CaseMatrice)caseM).getScore());
   			JLabel titre4=new JLabel(s,SwingConstants.RIGHT);
			titre4.setHorizontalAlignment(2);
   			JPanel case4=new JPanel();
   			case4.add(titre4);

			case1.setBorder(BorderFactory.createLineBorder(MyColors.jaune,2));
			case1.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case2.setBorder(BorderFactory.createLineBorder(MyColors.jaune,2));
			case2.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case3.setBorder(BorderFactory.createLineBorder(MyColors.jaune,2));
			case3.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case4.setBorder(BorderFactory.createLineBorder(MyColors.jaune,2));
			case4.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
   			explications.add(case1);
   			explications.add(case3);
   			explications.add(case2);
   			explications.add(case4);
   			
			//vue.getScore().getComponentCount()>1 || (!VueMatrice.isComputeAlignement && vue.getScore().getComponentCount()==1)
   			if(vue.getScore().getComponentCount()>1) {
				vue.getScore().remove(vue.getScore().getComponentCount() - 1);// on retire le dernier composant ajouté
				this.revalidate();
				this.repaint();
			}
   			vue.getScore().add(explications);
   			this.revalidate();
   			this.repaint();
			this.vue.setScore();
    		
    	}
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    	if (caseM instanceof CaseMatrice && this.getBackground()== MyColors.jaune && vue.getIsADN()) {
    		if (((CaseMatrice)caseM).getColor() && this.caseM.getX()>1 && this.caseM.getY()>1)  this.setBackground(MyColors.bleu);//on remet sa couleur
    		else if(this.caseM.getX()==1 || this.caseM.getY()==1){
				this.setBackground(MyColors.rose);
			}
			else this.setBackground(Color.white);
    		if(explicationsON) {
    			vue.getScore().remove(vue.getScore().getComponentCount()-1);// on retire le dernier composant ajouté
    		}
			explicationsON=false;

    		this.revalidate();
    		this.repaint();
    	}
		this.vue.setScore();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
