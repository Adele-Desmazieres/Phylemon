package AR2.Modele;

import AR1.Modele.Global;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Matrice {

	private List<EspeceVivante> especesChoisies = new ArrayList<>(); // les espèces initiales
	private List<Espece> especesTableau = new ArrayList<>(); // les espèces en cours d'utilisation
	private double[][] tableau; // le tableau des distances génétiques en cours

	protected Global[][] globals;
	protected Global globalMin;
	private List<Arbre> arbres = new ArrayList<>(); // la liste de tous les arbres en cours
	private double distMax = 0;
	private double distMin = Double.MAX_VALUE;
	private Espece[] deuxAFusionner = new Espece[2];
	
	// constructeur
	public Matrice(List<EspeceVivante> e) {
		this.especesChoisies.addAll(e);
		this.especesTableau.addAll(e);
		this.globals = new Global[e.size()][e.size()];
		this.tableau = new double[e.size()][e.size()];
		for (Espece espece:e) {
			arbres.add(new Arbre(espece)); // ajoute un arbre pour chaque espèce
		}
		this.remplirGlobals();
	}
	
	public List<Arbre> getArbres() { return arbres; }
	public double[][] getTableau() { return tableau; }
	public List<Espece> getEspecesTableau() { return especesTableau; }
	public double getDistMax() { return this.distMax; }
	public double getDistMin() { return this.distMin; }
	public Global[][] getGlobals() { return this.globals;}

	public void remplirGlobals() {
		int minScore = Integer.MAX_VALUE, maxScore = Integer.MIN_VALUE;
		//REMPLIR GLOBALS
		for (int i = 0; i < tableau.length; i++) {
			for (int j = 0; j <= i; j++) { // parcourt triangulaire de la matrice :
				String seq1 = especesTableau.get(i).sequence;
				String seq2 = especesTableau.get(j).sequence;
				Global croisement = new Global(seq1, seq2);
				croisement.cheminOptimal();
				globals[i][j] = croisement; // enregistre croisement
				globals[j][i] = croisement; // remplit la case symétrique selon la diagonale

				if (croisement.getScore() > maxScore) {
					maxScore = croisement.getScore();
				}
				// enregistre la distance min et les espèces correspondantes
				if (croisement.getScore() < minScore && i != j) {
					minScore = croisement.getScore();
					globalMin = globals[i][j];
				}
			}
		}
		//REMPLIR TAB
		for (int i = 0; i < tableau.length; i++) {
			for (int j = 0; j <= i; j++) { // parcourt triangulaire de la matrice :
				// dans le sens de lecture toutes les cases sous ou dans la diagonale
				// car la matrice est symétrique selon sa diagonale
				double distance = 0;
				if(i!=j) {
					double dif = (double) (globals[i][j].getScore() - minScore) / (double) (maxScore - minScore);
					distance = (1 - dif) * 100;
				}
				tableau[i][j] = distance; // enregistre distance génétique dans le tableau
				tableau[j][i] = distance; // remplit la case symétrique selon la diagonale

				if (distance > distMax) {
					distMax = distance;
				}
				// enregistre la distance min et les espèces correspondantes
				if (distance < distMin && i != j) {
					distMin = distance;
					this.deuxAFusionner[0] = especesTableau.get(i);
					this.deuxAFusionner[1] = especesTableau.get(j);
				}
			}
		}

	}
	
	// renvoie les 2 Modele.Espece à fusionner (sous forme de tableau de 2 cases), qui ont la plus petite distance génétique
	private Espece[] especeAFusionner() {
		Espece[] t = {this.deuxAFusionner[0], this.deuxAFusionner[1]};
		return t;
	}
	
	// enlève les 2 espèces à fusionner de la matrice et ajoute le new ancêtre
	// maintient la liste d'especes et le tableau des distances à jour
	// renvoie l'ancetre commun créé
	private EspeceAncetre resize(Espece e1, Espece e2) {
		especesTableau.remove(e1); // retire e1 et e2 de la liste
		especesTableau.remove(e2);
		EspeceAncetre anc = new EspeceAncetre(globalMin, e1, e2);
		especesTableau.add(anc); // crée et ajoute le new ancetre commun dans la liste
		// change taille du tableau suivant taille liste
		this.tableau = new double[especesTableau.size()][especesTableau.size()];
		return anc; // renvoie le nouvel ancetre commun
	}
	
	// avance d'une étape dans la modélisation de la matrice et de l'arbre
	public Espece[] etapeSuivante() throws IndexOutOfBoundsException {
		Espece[] aFusionner = this.especeAFusionner();
		EspeceAncetre anc = this.resize(aFusionner[0], aFusionner[1]);
		this.remplirGlobals();
		System.out.println(aFusionner[0].getNom());
		Arbre a1 = this.arbreDeRacine(aFusionner[0]);
		Arbre a2 = this.arbreDeRacine(aFusionner[1]);
		arbres.remove(a1);
		arbres.remove(a2);
		arbres.add(Arbre.fusionne(a1, a2, anc));
		return aFusionner;
	}
	
	// trouve l'arbre ayant pour racine e1
	private Arbre arbreDeRacine(Espece e1) {
		Arbre[] tabArbres = {null, null};
		for (Arbre a:arbres) {
			if (a.getRacine().getEspece() == e1) {
				return a;
			}
		}
		return null;
	}
	
	// fonction d'affichage textuel de la matrice (pour debuggage)
	public String toString() {
		int LARGEUR_MIN_COL = 5; // valeur modifiable >= 4
		
		int n = especesTableau.size();
		int[] largeurColonnes = new int[n+1];
		int largeurMax = 0;
		for (int i = 0; i < n; i++) {
			String nom = especesTableau.get(i).getNom();
			largeurColonnes[i+1] = max(LARGEUR_MIN_COL, nom.length());
			largeurMax = max(largeurMax, nom.length());
		}
		largeurColonnes[0] = largeurMax;
		String s = "";
		// première case
		s = ajoutEspace(s, largeurColonnes[0]);
		s += "|";
		// première ligne
		for (int i = 0; i < n; i++) {
			String nom = especesTableau.get(i).getNom();
			s += nom;
			s = ajoutEspace(s, largeurColonnes[i+1]-nom.length());
			s += "|";
		}
		s += "\n";
		for (int i = 0; i < n; i++) {
			// premiere colonne
			String nom = especesTableau.get(i).getNom();
			s += nom;
			s = ajoutEspace(s, largeurColonnes[0]-nom.length());
			s += "|";
			// toutes les autres cases
			for (int j = 0; j < n; j++) {
				String num = String.valueOf(this.tableau[i][j]);
				s += num;
				s = ajoutEspace(s, largeurColonnes[j+1] - num.length());
				s += "|";
			}
			s += "\n";
		}
		return s;
	}
	
	private String ajoutEspace(String s, int n) {
		for (int i = 0; i < n; i++) {
			s += " ";
		}
		return s;
	}
	
	/*
	// tests d'efficacité de la matrice
	// crée l'entierté de l'arbre en moins d'une minute pour 300 especes
	public static void main(String[] args) {
		
		List<EspeceVivante> especes = new LinkedList<EspeceVivante>();
		for (int i = 0; i < 20; i++) {
			EspeceVivante e = new EspeceVivante("a" + String.valueOf(i), Math.random() * 10000, "description");
			especes.add(e);
		}
		Matrice m = new Matrice(especes);
		System.out.println(m);
		while (m.getEspecesTableau().size() > 1) {
			m.etapeSuivante();
		}
		System.out.println(m);
	}
	*/
	
}
