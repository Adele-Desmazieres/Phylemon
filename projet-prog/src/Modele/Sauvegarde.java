package Modele;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class Sauvegarde {


    public static boolean sauvegarder(LinkedList<EspeceVivante> tel , String chemin){
        try{
            //verification que le fichier existe + destruction de ce dernier ...
            //Il s'agit d'un moyen pour vider entierement le fichier, peut être modifier s
            //Files.deleteIfExists(Path.of(chemin));
            //recree un fichier Sauvegarde au même endroit
            OutputStream out = Files.newOutputStream(Path.of(chemin), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            out.close();

            File f = new File(chemin);
            return ecriture(tel ,f);
        }catch (IOException e){
            return false;
        }
    }

    private static boolean ecriture(LinkedList<EspeceVivante> tel,File f){
        try {
            FileOutputStream fos = new FileOutputStream(f); //Cree un OutputStream permettant d'entrer dans le fichier f
            ObjectOutputStream oos = new ObjectOutputStream(fos); //a partir de l'OutputStream precedent, permet d'entrer specifiquement des objets
            //oos.writeUTF("UTF-8");
            for(EspeceVivante ev : tel)oos.writeObject(ev); //entre un par un les Especes vivantes dans le fichier
            fos.close();
        } catch (IOException fnfe){
            return false;
        }
        return true;
    }

    public static LinkedList<EspeceVivante> chargement(String chemin){
        LinkedList<EspeceVivante> tel = new LinkedList<>();
        try{
            File f = new File(chemin);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //ois.readUTF();
            EspeceVivante v = (EspeceVivante) ois.readObject();

            do {
                tel.add(v);
                v = (EspeceVivante) ois.readObject();
            }while( v != null);

            fis.close();
        }catch (FileNotFoundException f){
            // erreur : fichier non trouvé"
            return null;
        }catch (IOException ioe){
            return tel;
        }catch (ClassNotFoundException cnf) {
            // "erreur : Classe non trouvée" ;
            return null;
        }
        return tel;
    }

    //modifie le nom pour le rendre utilisable
    //necessite probablement encore d'autres choses à vérifier
    // <, >, :, “, /, \, |, ?, *
    public static String traitement(String amod){
        amod = amod.replace(".","");
        amod = amod.replace("/","");
        amod = amod.replace("\"","");
        amod = amod.replace("?","");
        amod = amod.replace("|","");
        amod = amod.replace(":","");
        amod = amod.replace(">","");
        amod = amod.replace("<","");
        amod = amod.replace("'","");
        amod = amod.replace("\\","");
        amod = amod.replace("*","");
        amod = amod.replace("\n","");
        return amod;
    }

}
