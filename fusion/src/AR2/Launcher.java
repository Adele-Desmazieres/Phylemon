package AR2;

import AR2.Controleur.*;
import AR2.Vue.Vue;

public class Launcher {

	public static void main(String[] args) {
		Controleur controleur = new Controleur();
		Vue test = new Vue(controleur);
		test.setVisible(true);
	}
}
