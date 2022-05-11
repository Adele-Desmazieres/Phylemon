package AR1.Vue;

import AR1.Controleur.Controleur;
import AR1.Modele.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class VueMatrice extends JPanel implements MouseInputListener {
	protected static Case premiereCase=null;
    protected Case caseM;
    protected VueInterface vue;
    protected static boolean explicationsON=false;
	protected static boolean isComputeAlignement=false;
	protected static boolean isCustomPath=false;

    public VueMatrice(Case c, VueInterface v){
		this.setBackground(MyColors.fond);
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
    		this.setBackground(MyColors.jaune);
    		
    		Controleur controleur = new Controleur(vue.getGlobal(),vue);
   		
   			JPanel explications=new JPanel();
   			explications.setLayout(new GridLayout(2,2,50,10));
			explications.setBackground(MyColors.fond);
    			
   			CaseMatrice[] triangle= vue.getGlobal().renvoieTriangle(caseM);
    			/*
    			 * triangle[0] -> case diagonale
    			 * triangle[1] -> case supérieure
    			 * triangle[2] -> case latérale
    			 */
   			//Explications case diagonale
    		JLabel titre1=new JLabel("Score from Diagonal cell", SwingConstants.CENTER);
   			JPanel case1=new JPanel();

			case1.setBackground(MyColors.jaune);

    		JLabel j1=controleur.explicationsCalcul(triangle[0], (CaseMatrice)caseM);
    		j1.setHorizontalAlignment(SwingConstants.CENTER);
    		case1.setLayout(new GridLayout(2,1));
   			case1.add(titre1);
   			case1.add(j1);
   			
   			//Explications case latérale
   			JLabel titre2=new JLabel("Score from Side cell", SwingConstants.CENTER);
   			JPanel case2=new JPanel();

			case2.setBackground(MyColors.jaune);
   			
   			JLabel j2=controleur.explicationsCalcul(triangle[2], (CaseMatrice)caseM);
    		j2.setHorizontalAlignment(SwingConstants.CENTER);
   			case2.setLayout(new GridLayout(2,1));
   			case2.add(titre2);
   			case2.add(j2);
   			
   			//Explications case supérieure
   			JLabel titre3=new JLabel("Score from Upper cell", SwingConstants.CENTER);
   			JPanel case3=new JPanel();

			 case3.setBackground(MyColors.jaune);

   			JLabel j3=controleur.explicationsCalcul(triangle[1], (CaseMatrice)caseM);
			j3.setHorizontalAlignment(SwingConstants.CENTER);
   			case3.setLayout(new GridLayout(2,1));
   			case3.add(titre3);
   			case3.add(j3);
   			
   			String s="Wining (max) score is "+String.valueOf(((CaseMatrice)caseM).getScore());
   			JLabel titre4=new JLabel(s,SwingConstants.RIGHT);
			titre4.setHorizontalAlignment(2);
   			JPanel case4=new JPanel();
   			case4.add(titre4);

			case4.setBackground(MyColors.jaune);

			case1.setBorder(BorderFactory.createLineBorder(MyColors.jauneBordure,2));
			case1.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case2.setBorder(BorderFactory.createLineBorder(MyColors.jauneBordure,2));
			case2.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case3.setBorder(BorderFactory.createLineBorder(MyColors.jauneBordure,2));
			case3.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
			case4.setBorder(BorderFactory.createLineBorder(MyColors.jauneBordure,2));
			case4.setSize(vue.getScore().getWidth()/2,vue.getScore().getHeight()/2);
   			explications.add(case1);
   			explications.add(case3);
   			explications.add(case2);
   			explications.add(case4);


   			if(vue.getScore().getComponentCount()>1) {
				   System.out.println("ici");
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
