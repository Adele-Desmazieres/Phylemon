package Modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GlobalProt extends Global{

    public GlobalProt(){
        super();
    }


    //@Override
    //permet d'initialiser la matrice de substitution
    public void setSubstitution() {
        String file = "src/Modele/matriceProt.csv";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while(line != null) {
                String[] values = line.split("\n");
                ArrayList<Integer> ligne = new ArrayList<>();
                for(String v : values) {
                    String[] values2 = v.split(",");
                    for(String elem : values2) {
                        ligne.add(Integer.parseInt(elem));
                    }
                }
                this.matrice.add(ligne);
                line = br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //vérifie que s est une séquence valide (20 acides aminés max)
    public static void verifProt(String s) {
        if (s.length() == 0) throw new IllegalArgumentException("Veuillez rentrer un peptide d'au moins 1 acide aminé (20 maximum).");
        else if (s.length()<=20) { //max 20 acides aminés
            char[] aas = {'E','D','A','R','N','C','Q','G','H','I','L','K','M','F','P','S','T','W','Y','V'}; //possibilités
            for (int i = 0; i < s.length(); i++) { //on parcourt la chaîne de caractères
                char c = s.charAt(i); //le caractère de s à la position i
                boolean ok = false; //le caractère n'est pas un acide aminé
                for (int j = 0; j < aas.length; j++) { //on parcourt les possibilités
                    if (c==aas[j]) ok = true; //le caractère est un acide aminé
                }
                if (!ok) {
                    throw new IllegalArgumentException("Seuls les 20 acides aminés (en code à une lettre capitale) du code génétique sont recevables."); //si le caractère n'est pas un nucléotide, alors s est invalide
                }
            }
        }
        else throw new IllegalArgumentException("Veuillez rentrer un peptide d'au plus 20 acides aminés.");
    }

    // Calcul du score optimal pour les protéines
    @Override
    public void scoreOptimal(){
        for(int i=2; i<this.cases.length; i++){
            for(int j=2; j<this.cases[i].length; j++){
                String s1 = ((CaseSeq) cases[i][0]).getNuc();
                String s2 = ((CaseSeq) cases[0][j]).getNuc();
                // récupérère la valeur de la matrice pour ces deux protéines
                int s = this.matrice.get(getPositionLettreProt(s1.charAt(0))).get(getPositionLettreProt(s2.charAt(0)));
                // on garde le score maximal parmi les 3 possibilités
                int score = Math.max(Math.max(((CaseMatrice)cases[i-1][j-1]).getScore()+s, ((CaseMatrice)cases[i-1][j]).getScore()+this.gapScore), ((CaseMatrice)cases[i][j-1]).getScore() + this.gapScore);
                cases[i][j] = new CaseMatrice(i,j,score);
            }
        }
    }

    public int getPositionLettreProt(char c){
        char[] tab = {'A','R','N', 'D','C','Q','E','G', 'H', 'I', 'L','K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V'};
        for(int i=0; i<tab.length; i++){
            if(tab[i]==c) return i;
        }
        return -1;
    }

    @Override
    public CaseSeq[] chercheParent(Case c) {
        return super.chercheParent(c);
    }


    @Override
    public void initialisationMonScore() {
        for(int i=2; i<cases.length; i++){
            for(int j=2; j<cases[0].length; j++){
                if(cases[i][j] instanceof CaseMatrice) {
                    String s1 = ((CaseSeq)cases[i][0]).getNuc();
                    String s2 = ((CaseSeq)cases[0][j]).getNuc();
                    ((CaseMatrice) cases[i][j]).setValeur(this.matrice.get(getPositionLettreProt(s1.charAt(0))).get(getPositionLettreProt(s2.charAt(0))));
                }
            }
        }
    }
}
