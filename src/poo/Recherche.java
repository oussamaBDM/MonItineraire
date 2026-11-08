package poo;

import java.io.Serializable;

public class Recherche implements Serializable{
    private String villeDepart;
    private String villeArrivee;
    private PreferencesMultiCriteres preferences;

    public Recherche(String vD, String vA,PreferencesMultiCriteres p){
        this.villeDepart = vD;
        this.villeArrivee = vA;
        this.preferences = p;
    }

    public String getVilleDepart(){
        return this.villeDepart;
    }

    public String getVilleArrivee(){
        return this.villeArrivee;
    }

    public PreferencesMultiCriteres getPreferences(){
        return this.preferences;
    }

    public String toString(){
        return this.villeDepart + " vers " + this.villeArrivee + " " + this.preferences.toString();
    }
}