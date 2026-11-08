package graph;

import fr.ulille.but.sae_s2_2026.*;
import java.util.List;

public class GraphesV1{
    public static void main(String[] args){
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();

        Lieu p = new Sommet("P");
        Lieu l = new Sommet("L"); 
        Lieu m = new Sommet("M"); 
        Lieu b = new Sommet("B"); 
        Lieu s = new Sommet("S");

        graphe.ajouterSommet(p);
        graphe.ajouterSommet(l);
        graphe.ajouterSommet(m);
        graphe.ajouterSommet(b);
        graphe.ajouterSommet(s);

        ajouterLienTrain(graphe, p, l, 120, 80, 3);
        ajouterLienTrain(graphe, p, m, 200, 100, 4);
        ajouterLienTrain(graphe, p, b, 130, 85, 3);
        ajouterLienTrain(graphe, p, s, 110, 70, 2);
        ajouterLienTrain(graphe, l, m, 100, 45, 1.5);
        ajouterLienTrain(graphe, l, s, 115, 65, 2);
        ajouterLienTrain(graphe, b, s, 250, 105, 5);
    
        int k = 4;
        List<Chemin> resultats = AlgorithmeKPCC.kpcc(graphe, p, m, k);

        if (resultats.isEmpty()){
            System.out.println("Aucun itinéraire trouvé.");
        }else{
            for(int i = 0; i < resultats.size(); i++){
                Chemin chemin = resultats.get(i);
                double totalCo2 = 0;
                double totalPrix = 0;
                for (Connexion arete : chemin.aretes()){
                    Route route = (Route) arete;
                    totalCo2 += route.getCo2();
                    totalPrix += route.getPrix();
                }

                System.out.println((i + 1) + ") Itinéraire : " + formatChemin(chemin));
                System.out.println("Durée : " + chemin.poids() + " min | CO2 : " + totalCo2 + " kg | Prix : " + totalPrix + " €\n");
            }
        }
    }

    private static void ajouterLienTrain(MultiGrapheOrienteValue g, Lieu a, Lieu b, double duree, double prix, double co2){
        Route aller = new Route(a, b, ModaliteTransport.TRAIN, duree, prix, co2);
        Route retour = new Route(b, a, ModaliteTransport.TRAIN, duree, prix, co2);
        g.ajouterArete(aller, duree);
        g.ajouterArete(retour, duree);
    }

    private static String formatChemin(Chemin chemin){
        StringBuilder sb = new StringBuilder();
        List<Connexion> aretes = chemin.aretes();
        if (aretes.isEmpty()) return "";
        sb.append(aretes.get(0).getDepart().toString());
        for (Connexion c : aretes) {
            sb.append(" - ").append(c.getArrivee().toString());
        }
        sb.append(" (Train)");
        return sb.toString();
    }
}