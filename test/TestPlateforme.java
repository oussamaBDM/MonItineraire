package test;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import poo.VoyageImpossibleException;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.Chemin;
import graph.Sommet;
import poo.Plateforme;
import java.util.List;

public class TestPlateforme{

    @Test
    public void testChargementEtRejetDonnees(){
        Plateforme plateforme = new Plateforme("data/exemple-reseau.csv");
        assertFalse(plateforme.getLieux().isEmpty());
        assertFalse(plateforme.getToutesConnexions().isEmpty());
    }

    @Test
    public void testFiltrageModalite() {
        Plateforme plateforme = new Plateforme("data/exemple-reseau.csv");
        List<Connexion> trains = plateforme.getConnexionsFiltrees(ModaliteTransport.TRAIN);
        for(Connexion c : trains) {
            assertEquals(ModaliteTransport.TRAIN, c.getModalite());
        }
    }

    @Test
    public void testRechercheItineraireValide() throws VoyageImpossibleException {
        Plateforme plateforme = new Plateforme("data/exemple-reseau.csv");
        Lieu depart = new Sommet("D");
        Lieu arrivee = new Sommet("A");     
        List<Chemin> resultats = plateforme.chercherItineraire(depart, arrivee, 1);
        assertFalse(resultats.isEmpty());
    }

    @Test
    public void testVoyageImpossibleException(){
        Plateforme plateforme = new Plateforme("data/exemple-reseau.csv");
        Lieu depart = new Sommet("D"); 
        Lieu arrivee = new Sommet("Brest");
        boolean erreurBienLevee = false;
        try{
            plateforme.chercherItineraire(depart, arrivee, 1);
        }catch(VoyageImpossibleException e){
            erreurBienLevee = true;
        }
        assertTrue(erreurBienLevee);
    }

    @Test
    public void testAffichagePointsInteret() throws VoyageImpossibleException {
        Plateforme plateforme = new Plateforme("exemple-reseau.csv");
        Lieu depart = new Sommet("D");
        Lieu arrivee = new Sommet("A");     
        List<Chemin> resultats = plateforme.chercherItineraire(depart, arrivee, 1);
        
        if(!resultats.isEmpty()) {
            List<String> resume = plateforme.pointInteret(resultats.get(0));
            assertFalse(resume.isEmpty());
            assertTrue(resume.get(0).startsWith("Départ :"));
            assertTrue(resume.get(resume.size() - 1).startsWith("Arrivée :"));
        }
    }
}