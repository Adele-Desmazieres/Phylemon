package AR2.Vue;


import AR2.Controleur.Controleur;
import AR2.Modele.Sauvegarde;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Vcharg extends JPanel {
    private Controleur cont ;

    public Vcharg(Controleur controleur){
        cont = controleur;
    }

    //le but est de permettre à l'utilisateur de chercher directement le fichier qu'il souhaite dans son arborescence
    //le booleen save permet de modifier les actions ; true permet de sauvegarder , false de charger
    //______________________________ordre des conditions peut être encore modifié___________________________
    public void choose(boolean save){
        JFileChooser jf = new JFileChooser(".src/../res/save/");//permet de definir le dossier save comment etant le premier dossier ouvert

        FileNameExtensionFilter fileter = new FileNameExtensionFilter("Fichier texte .txt","txt");
        jf.setFileFilter(fileter); //il s'agit d'un filtre ne permettant de sélectionner que les fichiers txt

        int returnVal = jf.showOpenDialog(jf);
        this.add(jf);//jf est est ajouter au panel courant

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            String path = jf.getSelectedFile().getPath(); //recupere le chemin sélectionner

            File fo = new File(path);


            if(fo.exists()) { //si le fichier existe ont peut alors charger ou sauvegarder
                if (!save) { //permet le chargement du fichier sélectionné s'il existe
                    boolean[] val = cont.chargementDiverse(fo.getPath());
                    if (val[0] && val[1]) JOptionPane.showMessageDialog(this, "Espèces chargées!");
                    else if (!val[0]) JOptionPane.showMessageDialog(this, "Erreur de chargement : fichiers manquants");
                    else JOptionPane.showMessageDialog(this, "aucune especes stockée dans le fichié ! ou Espèces déjà présentes dans la base de donnée");
                } else { //permet la sauvegarde des espèces sélectionnées precedement , plus vérification
                    int ok = JOptionPane.showConfirmDialog(this, "Fichier déjà existent voulez vous vraiment l'écraser ?(possibilité de suppression)", null, JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.NO_OPTION) return;
                    if (cont.sauvegardeDiverse(fo.getPath())) JOptionPane.showMessageDialog(this, "Sauvegarde terminé !");
                    else JOptionPane.showMessageDialog(this, "erreur de sauvegarde !");
                }
            }else if(save){ //permet de créer un nouveau fichier de sauvegarde
                String nom = jf.getSelectedFile().getName();
                nom = Sauvegarde.traitement(nom);
                if (cont.sauvegardeDiverse("./res/save/"+nom+".txt")) JOptionPane.showMessageDialog(this, "Sauvegarde terminé !");
                else JOptionPane.showMessageDialog(this, "erreur de sauvegarde !");
            }else{
                JOptionPane.showMessageDialog(this, "fichier inexistant !!");
            }

        }
    }

}
