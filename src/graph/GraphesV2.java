package graph;

import fr.ulille.but.sae_s2_2026.*;
import java.util.List;


public class GraphesV2 {
    public static void main(String[] args){
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        Lieu n = new Sommet("Nantes");
        Lieu r = new Sommet("Rennes");
        Lieu t = new Sommet("Toulouse");
        Lieu c = new Sommet("Clermont-Ferrand");
        Lieu v = new Sommet("Valence");
        graphe.ajouterSommet(n);
        graphe.ajouterSommet(r);
        graphe.ajouterSommet(t);
        graphe.ajouterSommet(c);
        graphe.ajouterSommet(v);
        ajouterLien(graphe,n, r, ModaliteTransport.TRAIN, 75, 15, 2);
        ajouterLien(graphe, n, r, ModaliteTransport.BUS, 100, 5, 8);
        ajouterLien(graphe,n, t, ModaliteTransport.AVION, 70, 90, 85);
        ajouterLien(graphe, n,t, ModaliteTransport.BUS, 480, 35, 45);
        ajouterLien(graphe, n, c, ModaliteTransport.TRAIN, 300, 60, 8);
        ajouterLien(graphe, n, c, ModaliteTransport.BUS,360, 25, 35);
        ajouterLien(graphe, r, t,ModaliteTransport.AVION,80, 100, 90);
        ajouterLien(graphe, r, t,ModaliteTransport.TRAIN, 400, 80, 10);
        ajouterLien(graphe, r, c,ModaliteTransport.TRAIN,360, 70, 9);
        ajouterLien(graphe, t, c,ModaliteTransport.TRAIN, 240, 45, 6);
        ajouterLien(graphe, t, c,ModaliteTransport.BUS,300, 20, 30);
        ajouterLien(graphe, t, v, ModaliteTransport.TRAIN,210, 50, 5);
        ajouterLien(graphe, t, v, ModaliteTransport.BUS,270, 25, 25);
        ajouterLien(graphe, c, v, ModaliteTransport.TRAIN,150, 30, 4);
        ajouterLien(graphe, c, v, ModaliteTransport.BUS,200, 15, 20);
        int k = 4;
        List<Chemin> resultats = AlgorithmeKPCC.kpcc(graphe,n,v,k);
        if(resultats.isEmpty()){
            System.out.println("Aucun itinéraire trouvé.");
        }else{
            System.out.println("=== Scénario Oussama : De Nantes à Valence (Optimisation Temps) ===\n");
            for(int i = 0;i<resultats.size();i++){
                Chemin chemin = resultats.get(i);
                afficherDetailItineraire(i+1,chemin);
            }
        }
    }
    private static void ajouterLien(MultiGrapheOrienteValue g, Lieu a, Lieu b, ModaliteTransport mode, double duree, double prix, double co2) {
        Route aller = new Route(a, b,mode,duree,prix,co2);
        Route retour = new Route(b, a, mode,duree,prix,co2);
        g.ajouterArete(aller, duree);
        g.ajouterArete(retour, duree);
    }

    private static void afficherDetailItineraire(int classement, Chemin chemin){
        List<Connexion> aretes = chemin.aretes();
        if (aretes.isEmpty()) return;
        double totalDuree = 0;
        double totalPrix = 0;
        double totalCo2 = 0;
        String trace = "";
        ModaliteTransport modePrecedent = null;
        Lieu villePrecedente = null;
        for (Connexion c : aretes){
            Route route = (Route) c;
            ModaliteTransport modeActuel = route.getModalite();
            Lieu villeActuelle = route.getDepart();
            if(modePrecedent != null && modePrecedent != modeActuel){
                double penalite = getPenalite(villeActuelle.toString(),modePrecedent, modeActuel);
                totalDuree += penalite;
                trace += " [Transfert " + villeActuelle.toString() + " : +" + (int)penalite + " min] ";
            }
            totalDuree += route.getDuree();
            totalPrix += route.getPrix();
            totalCo2 += route.getCo2();
            trace += villeActuelle.toString() + " (" + modeActuel + ") ";
            modePrecedent = modeActuel;
            villePrecedente = route.getArrivee();
        }
        trace += villePrecedente.toString(); 
        System.out.println(classement + ") Itinéraire : " + trace);
        System.out.println("   Temps total : " + totalDuree + " min | Prix : " + totalPrix + " € | CO2 : " + totalCo2 + " kg\n");
    }

    private static double getPenalite(String ville, ModaliteTransport m1, ModaliteTransport m2){
        if (m1 == m2) return 0;
        boolean isTrainBus = (m1 == ModaliteTransport.TRAIN && m2 == ModaliteTransport.BUS) || (m1 == ModaliteTransport.BUS && m2 == ModaliteTransport.TRAIN);
        boolean isTrainAvion = (m1 == ModaliteTransport.TRAIN && m2 == ModaliteTransport.AVION) || (m1 == ModaliteTransport.AVION && m2 == ModaliteTransport.TRAIN);
        boolean isBusAvion = (m1 == ModaliteTransport.BUS && m2 == ModaliteTransport.AVION) || (m1 == ModaliteTransport.AVION && m2 == ModaliteTransport.BUS);
        if(ville.equals("Nantes")){
            if (isTrainBus) return 15; 
            if (isTrainAvion || isBusAvion)return 60;
        }else if(ville.equals("Rennes")){
            if(isTrainBus) return 10; 
            if (isTrainAvion || isBusAvion) return 45;    
        }else if(ville.equals("Toulouse")) {
            if (isTrainBus)return 20;
            if (isTrainAvion || isBusAvion)return 50;
        } else if(ville.equals("Clermont-Ferrand")){
            if (isTrainBus) return 10;
            if (isTrainAvion || isBusAvion) return 40;
        }else if(ville.equals("Valence")){
            if(isTrainBus) return 15;
        }
        return 0; 
    }
}