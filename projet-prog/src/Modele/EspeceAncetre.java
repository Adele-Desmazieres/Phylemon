package Modele;

import java.util.ArrayList;
import java.util.List;

public class EspeceAncetre extends Espece {
	// les espèces représentant les ancêtres communs des espèces vivantes
	// contient la liste de ses descendants vivants
	
	private List<EspeceVivante> descendants = new ArrayList<EspeceVivante>();
	
	// constructeur qui fusionne 2 espèces
	public EspeceAncetre(Espece e1, Espece e2) {
		Espece[] desc = {e1, e2};
		this.genome = (e1.genome + e2.genome)/2; // moyenne des 2 génomes
		for (Espece e:desc) {
			// si espece vivante, elle est ajoutée à la liste des descendants
			if (e instanceof EspeceVivante) {
				this.descendants.add((EspeceVivante) e);
			} else { // sinon, on ajoute les descendants de l'ancetre fusionné
				this.descendants.addAll(((EspeceAncetre) e).descendants);
			}
		}
	}
	
	// renvoie le nom composé de la 1ere lettre du nom de chaque descendant
	@Override
	public String getNom() {
		String s = "";
		for (EspeceVivante e:descendants) {
			s += Character.toString(e.getNom().charAt(0)).toUpperCase();
		}
		return s;
	}
	
	@Override
	public double getGenome() {
		return this.genome;
	}
	
	// renvoie une descritpion de la forme
	// "Ancêtre commun de espece1, espece2, espece3 et espece4."
	@Override
	public String getDescription() {
		String s = "Ancêtre commun de ";
		for (int i = 0; i < descendants.size()-1; i++) {
			s += descendants.get(i).getNom() + ", ";
		}
		s = s.substring(0, s.length()-2);
		s += " et " + descendants.get(descendants.size()-1).getNom() + ". ";
		return s;
	}
	
	public List<EspeceVivante> getDescendants() {
		return descendants;
	}
	
	
	
}
