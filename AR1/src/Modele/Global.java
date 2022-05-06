package Modele;

import java.util.ArrayList;
import java.util.LinkedList;

public class Global {
    protected Case[][] cases;
    protected String seq1 = "GATTACA";
    protected String seq2 = "GTCGACGCA";
    protected LinkedList<CaseMatrice> chemin;
    protected int matchScore = 1;
    protected int misMatchScore = -1;
    protected int gapScore = -2;
    protected int score = 0;
    protected ArrayList<ArrayList<Integer>> matrice;

    //CONSTRUCTEURS
    //le constructeur de Global nécessite simplement de savoir si on parle d'ADN ou de protéine
    public Global() {
        if(this instanceof GlobalProt){
            seq1= "AEQAERYDEMGGEE";
            seq2= "AEAERFDEMGPEE";
        }
        this.cases = new Case[seq1.length()+2][seq2.length()+2]; //+2 car bordure + les cases des séquences de type CaseADN
        this.chemin = new LinkedList<>();

        //remplissage des cases
        this.matrice = new ArrayList();
        this.setSubstitution();
        this.remplissageSeq();
        this.remplissageMatrice();
    }

    //ACCESSEURS
    public Case[][] getCases() { return cases; }
    public String getSeq1() { return seq1; }
    public String getSeq2() { return seq2; }
    public LinkedList<CaseMatrice> getChemin() { return chemin; }
    public int getMatchScore() { return matchScore; }
    public int getMisMatchScore() { return misMatchScore; }
    public int getGapScore() { return gapScore; }
    public void setSeq1(String seq1) { this.seq1 = seq1; }
    public void setSeq2(String seq2) { this.seq2 = seq2; }
    public int getScore() { return this.score; }

    public void clearChemin() { this.chemin = new LinkedList<>();} //efface le chemin

    //renvoie sous la forme d'un tableau à deux entrées, les cases ADN parentales de la case c
    public CaseSeq[] chercheParent(Case c) {
        Case[][] cases = this.cases; //on prend le tableau de cases du Global
        CaseSeq[] res = new CaseSeq[2]; //on initialise le tableau de réponse
        res[0] = (CaseSeq)cases[c.getX()][0]; //première case = case parentale1 (sur la verticale)
        res[1] = (CaseSeq)cases[0][c.getY()]; //seconde case = case parentale2 (sur l'horizontal)
        return res; //renvoie le tableau des cases parents
    }

    //MUTATEURS
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public void setMisMatchScore(int misMatchScore) { this.misMatchScore = misMatchScore; }
    public void setGapScore(int gapScore) { this.gapScore = gapScore; }

    //VÉRIFICATIONS
    //vérifie que s est une séquence valide (20 nucléotides max)
    public static void verifADN(String s) throws IllegalArgumentException {
        if (s.length() == 0) throw new IllegalArgumentException("Veuillez rentrer une séquence d'au moins 1 nucléotide (20 maximum).");
        else if (s.length()<=20) { //max 20 nucléotides
            char[] bases = {'A','T','C','G'}; //possibilités
            for (int i = 0; i < s.length(); i++) { //on parcourt la chaîne de caractères
                char c = s.charAt(i); //le caractère de s à la position i
                boolean ok = false; //le caractère n'est pas un nucléotide
                for (int j = 0; j < bases.length; j++) { //on parcourt les possibilités
                    if (c==bases[j]) ok = true; //le caractère est un nucléotide
                }
                if (!ok) {
                    throw new IllegalArgumentException("Seuls les nucléotides A, T, C et G en majuscules sont recevables."); //si le caractère n'est pas un nucléotide, alors s est invalide
                }
            }
        }
        else throw new IllegalArgumentException("Veuillez rentrer une séquence d'au plus 20 nucléotides.");
    }

    //INITIALISATION DE LA MATRICE

    //cette fonction ajoutes les cases de type CaseADN dans cases (les 2 séquences d'ADN)
    public void remplissageSeq(){
        if(this.seq1!=null && this.seq2!=null) { //verifier que les séquences ont bien été saisies
            //sequence 1
            for (int i = 2; i < this.cases.length; i++) {
                String l = this.seq1.charAt(i-2)+"";
                cases[i][0] = new CaseSeq(0, i, l);
            }

            //sequence 2
            for (int j = 2; j < this.cases[0].length; j++) {
                String l = this.seq2.charAt(j-2)+"";
                cases[0][j] = new CaseSeq(j,0,l);
            }
        }
    }

    public void remplissageMatrice(){
        //initialisation de la premiere ligne et colonne de la matrice ( F[i][1] = d*(i-1) et F[1][j] = d*(j-1) ) => décalage dû aux séquences
        for(int i=1; i<this.cases.length;i++){
            cases[i][1] = new CaseMatrice(i, 1, this.gapScore * (i-1));
        }
        for(int j=1; j<this.cases[0].length;j++){
            cases[1][j] = new CaseMatrice(1, j, this.gapScore * (j-1));
        }

        //initialisation des autres cases de la matrice et calcul de leur score basé sur le principe d'optimalité (récursion)
        scoreOptimal();
        initialisationMonScore();
    }

    public void scoreOptimal(){
        for(int i=2; i<this.cases.length; i++){
            for (int j = 2; j <this.cases[i].length ; j++) {
                String s1 = ((CaseSeq) cases[i][0]).getNuc();
                String s2 = ((CaseSeq) cases[0][j]).getNuc();
                int s=0;
                if(s1.equals(s2)) s=this.matchScore; //si les deux nucleotides sont egaux s est le matchScore
                else s=this.misMatchScore; //sinon s est egale au misMatchScore
                //on garde le score maximal parmi les 3 possibilités
                int score = Math.max(Math.max(((CaseMatrice)cases[i-1][j-1]).getScore()+s, ((CaseMatrice)cases[i-1][j]).getScore()+this.gapScore), ((CaseMatrice)cases[i][j-1]).getScore()+this.gapScore);
                cases[i][j] = new CaseMatrice(i, j, score);
            }
        }
    }
    
    //initialise l'entier correspondant à l'état de la caseMatrice en fonction de ses casesADN parentes sans prendre en compte le score global
    public void initialisationMonScore() {
    	for (int i=2;i<cases.length;i++) {
    		for (int j=2;j<cases[0].length;j++) {
    			CaseSeq[] monomere = this.chercheParent(cases[i][j]);
       			if(monomere[0].getNuc().equals(monomere[1].getNuc()) && cases[i][j] instanceof CaseMatrice) {
                       ((CaseMatrice)cases[i][j]).setValeur(this.matchScore);
                }
       			else if(cases[i][j] instanceof CaseMatrice) {
                       ((CaseMatrice)cases[i][j]).setValeur(this.misMatchScore);
                }
    		}
    	}
    }
    
    //renvoie la dernière case
    public CaseMatrice renvoieDerniereCase() {
    	return (CaseMatrice)this.cases[this.cases.length-1][this.cases[0].length-1];
    }
    
    //met à jour le chemin optimal
    public void cheminOptimal(){
    	LinkedList<CaseMatrice> enCours=new LinkedList<CaseMatrice>();	
    	enCours.add(this.renvoieDerniereCase());//on ajoute la dernière case en bas à droite
    	this.cheminOptimalRec(enCours, this.renvoieDerniereCase());
    	for(CaseMatrice c : this.chemin) c.setColore(true);//on remplit à true le boolean
    	this.score=this.renvoieDerniereCase().getScore();
    }
    
  //renvoie le triangle des cases utilisées lors du calcul du score
    public CaseMatrice[] renvoieTriangle(Case c) {
    	if (c.getX()<=1 || c.getY()<=1) return null;
    	CaseMatrice[] triangle=new CaseMatrice[3];
    	triangle[0]=(CaseMatrice)this.cases[c.getX()-1][c.getY()-1];//en haut à gauche
    	triangle[1]=(CaseMatrice)this.cases[c.getX()-1][c.getY()];//en haut au-dessus
    	triangle[2]=(CaseMatrice)this.cases[c.getX()][c.getY()-1];//sur la même ligne à gauche
    	return triangle;
    }
    
    //méthode utilisée dans la recherche du chemin optimal
    public void cheminOptimalRec(LinkedList<CaseMatrice> enCours, CaseMatrice c){
    	if (this.estVoisinAvec0(c) || (c.getX()==2 && c.getY()==2)){
            this.chemin=this.copieList(enCours);//on copie la liste actuelle et on l'a met dans le chemin
            return;
        }
        CaseMatrice[] triangle=this.renvoieTriangle(c);//on récupère les 3 cases qui ont permis de calculer le score d'une case
    	if(triangle==null) return ;//alors la case est "en bordure"

    	int val0=c.getValeur(), val1=c.getValeur(), val2=c.getValeur();// on récupère la valeur issue de l'alignement des deux nucléotides parents pour la case en paramètre de la fonction
    	if(this.estUnGap(c, triangle[0])) val0=this.gapScore;//si c'est un gap, on met à jour la valeur
    	if(this.estUnGap(c, triangle[1])) val1=this.gapScore;
    	if(this.estUnGap(c, triangle[2])) val2=this.gapScore;
    	if((triangle[0].getScore()+val0)==c.getScore()) {//on regarde si le score dans la case en diagonale sommée avec la valeur de la case actuelle est égale au score de la case actuelle
    		enCours.add(triangle[0]);//on ajoute la case en question 
    		this.cheminOptimalRec(enCours, triangle[0]);// on fait un appel récursif en changeant de point de vue
    		enCours.remove(triangle[0]);// on enlève la case précédemment ajoutée et on teste de la même manière avec les deux autres
    	}
    	if((triangle[1].getScore()+val1)==c.getScore()) {
    		enCours.add(triangle[1]);
    		this.cheminOptimalRec(enCours, triangle[1]);
    		enCours.remove(triangle[1]);
    	}
    	if((triangle[2].getScore()+val2)==c.getScore()) {
    		enCours.add(triangle[2]);
    		this.cheminOptimalRec(enCours, triangle[2]);
    		enCours.remove(triangle[2]);
    	}
    }
    
  //renvoie false si les deux cases sont en diagonales
    public boolean estUnGap(Case c1, Case c2) {
    	if(Math.abs(c1.getX()-c2.getX())==1 && Math.abs(c1.getY()-c2.getY())==1) return false;//les deux cases sont en diagonale
    	return true;// alors les cases partagent une de leurs coordonnées (le x ou le y)
    }
    
    //renvoie true si deux cases peuvent faire un gap horizontal
    public boolean estUnGapHorizontal(CaseMatrice c1, CaseMatrice c2) {
    	if(Math.abs(c1.getX()-c2.getX())==0) return true;
    	return false;
    }
    
    //renvoie true si deux cases peuvent faire un gap vertical
    public boolean estUnGapVertical(CaseMatrice c1, CaseMatrice c2) {
    	if(Math.abs(c1.getY()-c2.getY())==0) return true;
    	return false;
    }
    
    //renvoie une copie d'une LinkedList de CaseMatrice sans partage de référence
    public LinkedList<CaseMatrice> copieList(LinkedList<CaseMatrice> list){
    	LinkedList<CaseMatrice> copie=new LinkedList<CaseMatrice>();
    	for(CaseMatrice caseM :list)copie.add(caseM);
    	return copie;
    }

    //ATENTION PEUT ETRE UTILISER NOUVELLE FONCTION POUR JUSTE CHANGER SCORE PAS NOUVELLES CASE
    public void miseAJourMatrice(){
        this.cases = new Case[seq1.length()+2][seq2.length()+2];
        this.remplissageSeq();
        this.remplissageMatrice();
    }
    
    //transmet la liste de CaseMatrice représentant le chemin en tableau de CaseMatrice
    public CaseMatrice[] cheminTableau(LinkedList<CaseMatrice> chemin) {
    	CaseMatrice[] tab=new CaseMatrice[chemin.size()];
    	int j=0;
    	for (int i=chemin.size()-1;i>=0;i--) {
    		tab[i]=chemin.get(j);
    		j++;
    	}
    	return tab;
    }
    
    //renvoie la caseMatrice précédent celle prise en paramètres
    public CaseMatrice estLaPrecedente(CaseMatrice c) {
    	CaseMatrice prec=null;
    	CaseMatrice tmp=this.chemin.getFirst();
    	int i=0;
    	while(tmp!=null) {
    		if(tmp==c) return prec;
    		prec=tmp;
    		tmp=chemin.get(i);
    		i++;
    	}
    	return prec;
    }
    
    //ajoute une CaseMatrice au chemin
    public void ajoutCaseSurChemin(CaseMatrice c) {
    	this.chemin.add(c);
    }
    
    //retire la dernière caseMatrice au chemin
    public void retireDerniereCase() {
    	this.chemin.removeLast();
    }

    //permet d'initialiser la matrice de substitution
    public void setSubstitution() {
        this.matrice = null;
    }

    public boolean estVoisinAvec0(CaseMatrice c){
        if(c.getX()==1 && c.getY()==2) return true;
        if(c.getX()==2 && c.getY()==1) return true;
        return false;
    }
}
