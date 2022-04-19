package Modele;

public class CaseSeq extends Case{
	protected String nuc;//le nucléotide

	//constructeur
	public CaseSeq(int x, int y, String s) {
		super(x,y);
		this.nuc=s;
	}

	//ACCESSEUR
	public String getNuc() { return nuc; }
}
