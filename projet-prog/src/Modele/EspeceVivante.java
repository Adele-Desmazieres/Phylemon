package Modele;

import java.io.Serializable;

public class EspeceVivante extends Espece implements Serializable {
	// les espèces vivantes retrouvées par les scientifiques
	// elles sont dans les feuilles de l'arbre phylogénétique
	
	private String nom; // nom de l'espèce
	private String description; // description de l'espèce
	private String img; // TO DO : implémenter image de l'espèce
	
	// constructeur de l'espèce selon ses caracteristiques
	public EspeceVivante(String nom, double genome, String description) { //Penser a rajouter l'argument image
		this(nom, genome, description, "../res/img/default.jpg");
	}
	
	public EspeceVivante(String nom, double genome, String description, String cheminIMG) {
		this.nom = nom;
		this.genome = genome;
		this.description = description;
		this.img = cheminIMG;
	}
	
	
	@Override
	public String getNom() { return this.nom; }
	
	@Override
	public double getGenome() { return this.genome; }
	
	@Override
	public String getDescription() { return this.description; }
	
	public String getImg() { return this.img; }

	public void setImg(String img) {
		this.img = img;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
