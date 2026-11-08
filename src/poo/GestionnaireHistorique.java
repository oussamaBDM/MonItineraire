package poo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireHistorique{

    public static void sauvegarder(List<Recherche> historique, String nomFichier) throws Exception{
        FileOutputStream fichierOut = new FileOutputStream(nomFichier);
        ObjectOutputStream objetOut = new ObjectOutputStream(fichierOut);
        objetOut.writeObject(historique);
        objetOut.close();
        fichierOut.close();
    }

    public static List<Recherche> charger(String nomFichier) throws Exception{
        File fichier = new File(nomFichier);
        if(!fichier.exists()){
            return new ArrayList<>();
        }
        FileInputStream fichierIn = new FileInputStream(nomFichier);
        ObjectInputStream objetIn = new ObjectInputStream(fichierIn);
        List<Recherche> historique = (List<Recherche>) objetIn.readObject();
        objetIn.close();
        fichierIn.close();
        return historique;
    }
}