package Modele;

public class CaseMatrice extends Case{
	protected int score;//le score calculé par le meilleur chemin
	protected boolean colore;//true si la case fait partie du chemin
	protected int valeur;//contient la valeur obtenue au croisement des deux nucléotides
	
	public CaseMatrice(int x, int y, int score) {
		super(x,y);
		this.score=score;
		this.colore=false;
		this.valeur=0;
	}

	//ACCESSEUR
	public int getScore() { return score; }
	public int getValeur() { return valeur;}
	public boolean getColor(){ return colore;}
	
	//SETTER
	public void setValeur(int v) { this.valeur=v;}
	public void setColore(boolean b) {this.colore=b;}
}
