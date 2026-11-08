package poo;

import fr.ulille.but.sae_s2_2026.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;
import graph.Route;
import graph.Sommet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Plateforme{
    private Set<Lieu> lieux = new HashSet<>();
    private List<Connexion> connexions = new ArrayList<>();

    public Plateforme(String c){
        chargerDonnees(c);
    }

    private Lieu chercherOuCreerLieu(String nom){
        for(Lieu l :this.lieux){
            if(l.toString().equals(nom)){
                return l;
            }
        }
        Lieu nouveau = new Sommet(nom);
        this.lieux.add(nouveau);
        return nouveau;
    }

    public List<Connexion> getConnexionsFiltrees(ModaliteTransport modeChoisi){
        List<Connexion> filtre = new ArrayList<>();
        for (Connexion c : this.connexions){
            if(c.getModalite() == modeChoisi){
                filtre.add(c);
            }
        }
        return filtre;
    }

    public Set<Lieu> getLieux(){
        return this.lieux;
    }
    public List<Connexion> getToutesConnexions(){ 
        return this.connexions; 
    }
    
    private MultiGrapheOrienteValue construireGraphe(){
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        for(Lieu l : this.lieux){
            graphe.ajouterSommet(l);
        }
        for(Connexion c : this.connexions){
            Route route = (Route) c;
            graphe.ajouterArete(route, route.getDuree());
        }
        return graphe;
    }

    public List<Chemin> chercherItineraire(Lieu depart, Lieu arrivee, int k) throws VoyageImpossibleException{
        MultiGrapheOrienteValue graphe = construireGraphe();
        try{
            List<Chemin> resultats = AlgorithmeKPCC.kpcc(graphe,depart,arrivee,k);
            if(resultats.isEmpty()){
                throw new VoyageImpossibleException("Aucun voyage n'est possible entre " + depart + " et " + arrivee);
            }
            return resultats;
        }catch (IllegalArgumentException e){
            throw new VoyageImpossibleException("La ville de départ ou d'arrivée n'est pas desservie par notre réseau !");
        }
    }

    private void chargerDonnees(String chemin){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(chemin));
            String ligne;
            while((ligne = br.readLine()) != null){
                if (!ligne.startsWith("Depart") && !ligne.trim().isEmpty()){
                    String[] champs = ligne.split(";");
                    String nomDepart = champs[0];
                    String nomArrivee = champs[1];
                    ModaliteTransport mod = ModaliteTransport.valueOf(champs[2].toUpperCase());
                    double prix = Double.parseDouble(champs[3]);
                    double co2 = Double.parseDouble(champs[4]);
                    double duree = Double.parseDouble(champs[5]);
                    if (prix >= 0 && co2 >= 0 && duree >= 0){    
                        Lieu depart = chercherOuCreerLieu(nomDepart);
                        Lieu arrivee = chercherOuCreerLieu(nomArrivee);
                        Route aller = new Route(depart, arrivee, mod, duree, prix, co2);
                        Route retour = new Route(arrivee, depart, mod, duree, prix, co2);
                        connexions.add(aller); 
                        connexions.add(retour);
                    }
                }
            }
        }catch(IOException e){
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        }finally{
            if(br != null){
                try{
                    br.close();
                }catch(IOException e){
                    System.err.println("Impossible de fermer le fichier.");
                }
            }
        }
    }

    public List<String> pointInteret(Chemin c){
        List<String> etapes = new ArrayList<>();
        List<Connexion> routes = c.aretes();
        if(routes.isEmpty()){
            return etapes;
        }
        Connexion premiere = routes.get(0);
        etapes.add("Départ : " + premiere.getDepart() + " [" + premiere.getModalite() + "]");
        for(int i = 1; i <routes.size();i++){
            Connexion avant = routes.get(i - 1);
            Connexion mtn = routes.get(i);
            if(avant.getModalite() != mtn.getModalite()){
                etapes.add("Changement à " + mtn.getDepart() + " : " + avant.getModalite() + " -> " +mtn.getModalite());
            }
        }
        Connexion derniere = routes.get(routes.size() -1);
        etapes.add("Arrivée : " + derniere.getArrivee());
        return etapes;
    }
}