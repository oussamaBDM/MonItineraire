package poo;

import graph.*;
import fr.ulille.but.sae_s2_2026.Connexion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main{

    public static void main(String[] args){
        System.out.println("Importation du jeu de données");
        Plateforme plateforme = new Plateforme("data/exemple-reseau.csv"); 
        List<Connexion> reseau = plateforme.getToutesConnexions();
        System.out.println("Jeu de données chargé : " + reseau.size() + " trajets disponibles dans le système.\n");
        System.out.println("--- Scénario d'un utilisateur ---");
        System.out.println("Profil de l'utilisateur : Étudiant avec un budget serré (moi malheuresement)");
        System.out.println("  --> Préférences : 80% PRIX, 20% TEMPS, 0% CO2.");
        PreferencesMultiCriteres prefsEtudiant = new PreferencesMultiCriteres();
        prefsEtudiant.setImportance(TypeCout.PRIX, 0.8);
        prefsEtudiant.setImportance(TypeCout.TEMPS, 0.2);
        String vD = "L";
        String vA = "F";
        System.out.println("Recherche en cours pour un trajet direct : " + vD + " -> " + vA + "\n");
        Route trajetChoisi = null;
        double meilleurScore = Double.MAX_VALUE;
        for (Connexion c : reseau) {
            Route r = (Route) c;
            if (r.getDepart().toString().equalsIgnoreCase(vD) && r.getArrivee().toString().equalsIgnoreCase(vA)){
                double score = r.calculerScore(prefsEtudiant);
                System.out.println("   - Option trouvée : " + r.getModalite() + " (" + r.getPrix() + "€, " + r.getDuree() + "min) => Score POO : " + score);
                if (score < meilleurScore) {
                    meilleurScore = score;
                    trajetChoisi = r;
                }
            }
        }
        if(trajetChoisi != null){
            System.out.println("\n Résultat : L'application recommande le " + trajetChoisi.getModalite() + " pour moi");
        }
        System.out.println("\n --- Validation de l'historique et des statistiques ---");
        String fichierBinaire = "historiqueBinaire.dat";
        try{
            List<Recherche> historique = new ArrayList<>();
            File testFichier = new File(fichierBinaire);
            if (testFichier.exists()) {
                historique = GestionnaireHistorique.charger(fichierBinaire);
                System.out.println("Ancien historique rechargé avec succès (" + historique.size() + " recherches)");
            }
            historique.add(new Recherche(vD, vA, prefsEtudiant));
            GestionnaireHistorique.sauvegarder(historique, fichierBinaire);
            System.out.println("Nouvelle recherche sauvegardée en binaire dans : " + fichierBinaire);
            AnalyseurHistorique analyseur = new AnalyseurHistorique(historique);
            System.out.println("\n Données extraites par l'AnalyseurHistorique :");
            System.out.println("   - Ville la plus demandée : " + analyseur.getVilleDepartFavorite());
            System.out.println("   - Trajet le plus fréquent : " + analyseur.getTrajetFavori());
            System.out.println("   - Sensibilité dominante : " + analyseur.getCritereFavori()); 
        }catch(Exception e){
            System.out.println("❌ Erreur lors de la gestion POO : " + e.getMessage());
        }
    }
}