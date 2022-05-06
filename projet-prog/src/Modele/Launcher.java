package Modele;

import Controleur.Controleur;
import Vue.Vue;

public class Launcher {

	public static void main(String[] args) {
		Controleur controleur = new Controleur();
		Vue test = new Vue(controleur);
		test.setVisible(true);
	}
}
