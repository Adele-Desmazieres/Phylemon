package Modele;

public class Arbre {
    // *** Les attributs ***
    private Noeud racine;

    // *** Les constructeurs ***
    public Arbre(Noeud r) { racine=r; }
    public Arbre(Espece e) { this.racine = new Noeud(e); }
    
    // *** Le getter ***
    public Noeud getRacine() { return racine; }

    // *** Les methodes/fonctions ***
    public boolean estVide() { return racine == null; }

    public static class Noeud {
        // *** Les attributs ***
        private Noeud droit, gauche;
        private Espece espece;

        // *** Les constructeurs ***
        public Noeud(Espece e, Noeud d, Noeud g) { espece = e; droit = d; gauche = g; }
        public Noeud(Espece e) { this(e,null,null); }
        // *** Les getters ***
        public Espece getEspece() { return espece; }
        public Noeud getDroit() {return droit;}
        public Noeud getGauche() {return gauche;}
        

    }
    // Renvoie l'arbre ayant pour racine l'ancêtre commun des racines des deux Arbres
    public static Arbre fusionne(Arbre a1, Arbre a2,EspeceAncetre e0) {
        Noeud n0 = new Noeud(e0, a1.racine,a2.racine);
        return new Arbre(n0);
    }
}

