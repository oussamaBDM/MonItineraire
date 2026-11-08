package poo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PreferencesMultiCriteres implements Serializable {
    private Map<TypeCout, Double> poidsCriteres;

    public PreferencesMultiCriteres() {
        this.poidsCriteres = new HashMap<>();
        for (TypeCout type : TypeCout.values()) {
            this.poidsCriteres.put(type, 0.0);
        }
    }

    public void setImportance(TypeCout critere, double pourcentage){
        if(pourcentage < 0.0 || pourcentage > 1.0){
            throw new IllegalArgumentException("L'importance doit être comprise entre 0.0 et 1.0");
        }
        this.poidsCriteres.put(critere, pourcentage);
    }

    public double getImportance(TypeCout critere){
        return this.poidsCriteres.get(critere);
    }

    public boolean estValide(){
        double somme = 0;
        for (double poids : this.poidsCriteres.values()){
            somme += poids;
        }
        return Math.abs(somme - 1.0) < 0.001;
    }

    public String toString(){
            String texte = "[";
            for (TypeCout critere : this.poidsCriteres.keySet()){
                double valeur = this.poidsCriteres.get(critere);
                if (valeur > 0){
                    texte += critere.name() + ":" +(valeur * 100) + "% ";
                }
            }
            texte += "]";
            return texte;
        }
}