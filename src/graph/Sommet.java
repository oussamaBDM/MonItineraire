package graph;

import fr.ulille.but.sae_s2_2026.Lieu;
import java.util.Objects;

public class Sommet implements Lieu{

    private final String nom;

    public Sommet(String nom){
        this.nom = nom;
    }

    public String getNom(){
        return this.nom;
    }

    public String toString(){
        return this.nom;
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Sommet)) return false;
        Sommet v = (Sommet) o;
        return this.nom.equals(v.nom);
    }

    public int hashCode() {
        return Objects.hash(nom);
    }
}