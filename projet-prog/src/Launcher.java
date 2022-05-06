
import Controleur.Controleur;
import Modele.*;
import Vue.Vue;

public class Launcher {

	public static void main(String[] args) {
		Controleur controleur = new Controleur();
		Vue test = new Vue(controleur);
		test.setVisible(true);
	}
}
