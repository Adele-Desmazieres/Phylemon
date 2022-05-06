package Modele;

import java.io.Serializable;

public abstract class Espece implements Serializable {
	
	protected double genome; // double représentant taille du génome
	
	public abstract String getNom();
	
	public abstract double getGenome();
	
	public abstract String getDescription();
	
	// renvoie la distance génétique entre 2 espèces
	// méthode static
	public static double distanceGenetique(Espece e1, Espece e2) {
		return Math.sqrt( (e1.genome - e2.genome)*(e1.genome - e2.genome) );
	}
	
	// renvoie la distance génétique entre 2 espèces
	// méthode dynamique
	public double distanceGenetique(Espece e2) {
		return EspeceVivante.distanceGenetique(this, e2);
	}
	
	
}
