package poo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyseurHistorique{
    private List<Recherche> historique;
    
    public AnalyseurHistorique(List<Recherche> h){
        this.historique = h;
    }

    public List<Recherche> getDernieresRecherches(int nombre){
        if(this.historique.isEmpty()){
            return new ArrayList<>();
        }
        int taille = this.historique.size();
        int pointDeDepart = taille - nombre;
        if (pointDeDepart < 0){
            pointDeDepart = 0;
        }
        return this.historique.subList(pointDeDepart, taille);
    }

    public String getVilleDepartFavorite(){
        if(this.historique.isEmpty()) {
            return "Rien";
        }
        Map<String, Integer> compteurs = new HashMap<>();
        for (Recherche r : this.historique) {
            String ville = r.getVilleDepart();
            if(compteurs.containsKey(ville)){
                compteurs.put(ville, compteurs.get(ville) + 1);
            }else{
                compteurs.put(ville, 1);
            }
        }
        String villeFavorite = "";
        int record = 0;
        for (String ville: compteurs.keySet()){
            if (compteurs.get(ville) > record){
                record = compteurs.get(ville);
                villeFavorite = ville;
            }
        }
        return villeFavorite;
    }

    public TypeCout getCritereFavori(){
        if (historique.isEmpty()){
            return null;
        }
        Map<TypeCout, Integer> compteurs = new HashMap<>();
        for (Recherche r : historique){
            PreferencesMultiCriteres prefs = r.getPreferences();
            TypeCout criterePrincipal = null;
            double maxPourcentage = -1.0;
            for (TypeCout type : TypeCout.values()){
                if (prefs.getImportance(type) > maxPourcentage){
                    maxPourcentage = prefs.getImportance(type);
                    criterePrincipal = type;
                }
            }
            if(compteurs.containsKey(criterePrincipal)){
                compteurs.put(criterePrincipal, compteurs.get(criterePrincipal) + 1);
            }else{
                compteurs.put(criterePrincipal, 1);
            }
        }
        TypeCout critereFavori = null;
        int record = 0;    
        for (TypeCout critere : compteurs.keySet()){
            if(compteurs.get(critere) > record){
                record = compteurs.get(critere);
                critereFavori = critere;
            }
        }
        return critereFavori;
    }

    public String getTrajetFavori(){
        if(this.historique.isEmpty()){
            return "Aucun trajet";
        }
        Map<String, Integer> compteurs = new HashMap<>();
        for (Recherche r : this.historique) {
            String trajet = r.getVilleDepart() + " ---> " + r.getVilleArrivee();
            if(compteurs.containsKey(trajet)){
                compteurs.put(trajet, compteurs.get(trajet) + 1);
            }else{
                compteurs.put(trajet, 1);
            }
        }
        String trajetFavori = "";
        int record = 0;
        for(String trajet: compteurs.keySet()){
            if(compteurs.get(trajet) > record){
                record = compteurs.get(trajet);
                trajetFavori = trajet;
            }
        }
        return trajetFavori;
    }
}