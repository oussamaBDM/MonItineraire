package graph;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import poo.PreferencesMultiCriteres;
import poo.TypeCout;

public class Route implements Connexion{
    private final Lieu depart;
    private final Lieu arrivee;
    private final ModaliteTransport modalite;
    private final double duree;
    private final double prix;
    private final double co2;

    public Route(Lieu depart, Lieu arrivee, ModaliteTransport modalite, double duree, double prix, double co2){
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
        this.duree = duree;
        this.prix = prix;
        this.co2 = co2;
    }

    public Lieu getDepart(){
        return this.depart;
    }

    public Lieu getArrivee(){
        return this.arrivee;
    }

    public ModaliteTransport getModalite(){
        return this.modalite;
    }
    
    public double getDuree() {
        return this.duree;
    }

    public double getPrix() {
        return this.prix;
    }

    public double getCo2(){
        return this.co2;
    }

    public String toString(){
        return "[" + this.modalite + "] " + this.depart + " -> " + this.arrivee+ 
               " (" + this.duree + " min | " + this.prix + " € | " + this.co2 + " kg CO2)";
    }

    public double calculerScore(PreferencesMultiCriteres prefs) {
        return (this.prix * prefs.getImportance(TypeCout.PRIX)) +
               (this.duree * prefs.getImportance(TypeCout.TEMPS)) +
               (this.co2 * prefs.getImportance(TypeCout.CO2));
    }
}