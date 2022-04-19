package Modele;

public abstract class Case {
	protected final int x;
	protected final int y;
	//les coordonnées x et y de la case
	
	public Case(int x, int y) {
		this.x=x;
		this.y=y;
	}

	//ACCESSEURS
	public int getY() { return y; }

	public int getX() { return x; }
}
