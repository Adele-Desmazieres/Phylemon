package AR2.Controleur;

import AR2.Modele.*;
import AR2.Vue.*;
import AR1.Modele.*;
import AR1.Vue.*;

import java.util.LinkedList;
import java.util.List;

public class Controleur {
    private LinkedList<EspeceVivante> banqueDonnees = new LinkedList<>();
    private LinkedList<EspeceVivante> especesChoisies = new LinkedList<>();
    public Matrice m;
    public Vue v;

    public void clear(){
        especesChoisies.clear();
    }

    public LinkedList<String> recupTousLesNom(){
        LinkedList<String> especes = new LinkedList<>();
        for (EspeceVivante ev : getBanqueDonnees()) {
            especes.add(ev.getNom());
        }
        return especes;
    }

    public void listeAjouter(String nom){
        for (EspeceVivante e : banqueDonnees) { //on est sûr que l'espèce existe dans la base de données
            if(nom.equals(e.getNom())) especesChoisies.addLast(e); //On pourrait faire une copie
        }
    }
    public void listeEnlever(String nom){
        for (EspeceVivante e : banqueDonnees) { //on est sûr que l'espèce existe dans la base de données
            if(nom.equals(e.getNom())) especesChoisies.remove(e);
        }
    }

    public boolean ajouterNouvEspece(String nom, String genome, String description, String chemin){
        for (EspeceVivante ev: this.banqueDonnees) {
            if (ev.getNom().equalsIgnoreCase(nom)) return false;
        }
        if (chemin == null || chemin.equals("")) chemin = "./res/img/default.jpg";
        banqueDonnees.add(new EspeceVivante(nom, genome, description, chemin));
        return true;
    }

    public String getChemin(String nom) {
        for (EspeceVivante ev :especesChoisies) {
            if (ev.getNom().equalsIgnoreCase(nom)) return ev.getImg();
        }
        return ""; //théoriquement inateniable
    }
    public String getDescription(String nom){
        for (EspeceVivante ev: this.banqueDonnees) {
            if (ev.getNom().equalsIgnoreCase(nom)) return ev.getDescription();
        }
        return ""; //théoriquement inateniable
    }

    public void initialiseMatrice() {
        this.m = new Matrice(especesChoisies);
    }

    public Espece[] etapeSuivante() {
        return this.m.etapeSuivante();
    }
    
    //getters et setters
    public List<Arbre> getArbres() { return m.getArbres(); }
    public double[][] getTableau() { return m.getTableau();}
    public Global[][] getGlobal() {return  m.getGlobals();}
    public List<Espece> getEspecesTableau() { return m.getEspecesTableau(); }
    public double getDistMax() { return m.getDistMax(); }
    public double getDistMin() { return m.getDistMin(); }
    public LinkedList<EspeceVivante> getBanqueDonnees() {
        return banqueDonnees;
    }
    public LinkedList<EspeceVivante> getEspecesChoisis() {
        return especesChoisies;
    }



    public boolean sauvegardeDiverse(String nom){
        //return Sauvegarde.sauvegardeDiverse(especesChoisies,nom);
        return Sauvegarde.sauvegarder(especesChoisies,nom);
    }

    public boolean[] chargementDiverse(String nom){
        LinkedList<EspeceVivante> tmp =  Sauvegarde.chargement(nom);
        boolean[] res = {false , false}; // ce tableau renvoie deux booléens pour afficher différentes choses
                                        //si res[0] = false => il y a eu une erreur de chargement du fichier
                                        //si res[1] = false => le fichier etait vide ou n'as pas pu être lu correctement
        if(tmp == null)return res;
        res[0] = true;

        //peut être mis dans une fonction auxiliaire
        for( int i = 0 ; i<tmp.size() ; i++){ //le but est d'enlever les espèces déjà présente dans la base de donnée
                                                //on ne peut pas compter sur le addAll car même si les espèces contiennent les même infos ce ne sont pas les mêmes objets
            for (EspeceVivante banqueDonnee : banqueDonnees) {
                String rv = tmp.get(i).getNom().toLowerCase(), rbd = banqueDonnee.getNom().toLowerCase();
                if (rv.equals(rbd)) {
                    tmp.remove(i);
                    i--; //les indices de listes sont gérés plus dynamiquement que les tableaux il faut donc modifier les indices pour ne pas oublier certaines espèces de tmp
                    break;
                }
            }
        }

        banqueDonnees.addAll(tmp);

        res[1] = tmp.size()>0;
        return res;
    }

}
