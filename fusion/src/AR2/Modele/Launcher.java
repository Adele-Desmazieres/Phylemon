package AR2.Modele;

import AR2.Controleur.Controleur;
import AR2.Vue.Vue;

public class Launcher {

	public static void main(String[] args) {
		Controleur controleur = new Controleur();
		Vue test = new Vue(controleur);
		test.setVisible(true);
	}
}
