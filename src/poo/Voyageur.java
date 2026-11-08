package poo;

public class Voyageur {
    private final String nom;
    private final TypeCout critereOptimiser;

    public Voyageur(String nom, TypeCout critereAOptimiser){
        this.nom = nom;
        this.critereOptimiser = critereAOptimiser;
    }

    public String getNom(){
        return this.nom;
    }

    public TypeCout getCritereAOptimiser(){
        return this.critereOptimiser;
    }
}