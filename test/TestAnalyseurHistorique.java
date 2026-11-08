package test;

import poo.AnalyseurHistorique;
import poo.PreferencesMultiCriteres;
import poo.Recherche;
import poo.TypeCout; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TestAnalyseurHistorique {
    private AnalyseurHistorique analyseur;

    @BeforeEach
    public void preparerTest() {
        PreferencesMultiCriteres prefPrix = new PreferencesMultiCriteres();
        prefPrix.setImportance(TypeCout.PRIX, 1.0);
        
        PreferencesMultiCriteres prefTemps = new PreferencesMultiCriteres();
        prefTemps.setImportance(TypeCout.TEMPS, 1.0);
        
        PreferencesMultiCriteres prefCO2 = new PreferencesMultiCriteres();
        prefCO2.setImportance(TypeCout.CO2, 1.0);
        List<Recherche> fauxHistorique = new ArrayList<>();
        fauxHistorique.add(new Recherche("Lille", "Paris", prefPrix));
        fauxHistorique.add(new Recherche("Marseille", "Lyon", prefTemps));
        fauxHistorique.add(new Recherche("Lille", "Bordeaux", prefPrix));
        fauxHistorique.add(new Recherche("Lille", "Paris", prefPrix));
        fauxHistorique.add(new Recherche("Paris", "Lyon", prefCO2));
        analyseur = new AnalyseurHistorique(fauxHistorique);
    }

    @Test
    public void testVilleDepartFavorite() {
        assertEquals("Lille", analyseur.getVilleDepartFavorite());
    }

    @Test
    public void testTrajetFavori() {
        assertEquals("Lille ---> Paris", analyseur.getTrajetFavori());
    }

    @Test
    public void testCritereFavori() {
        assertEquals(TypeCout.PRIX, analyseur.getCritereFavori());
    }

    @Test
    public void testDernieresRecherchesNormal() {
        List<Recherche> dernieres = analyseur.getDernieresRecherches(2);
        assertEquals(2, dernieres.size());
        Recherche toutDernier = dernieres.get(1);
        assertEquals("Paris", toutDernier.getVilleDepart());
        assertEquals("Lyon", toutDernier.getVilleArrivee());
        assertEquals(1.0, toutDernier.getPreferences().getImportance(TypeCout.CO2));
    }

    @Test
    public void testBouclierSecurite() {
        List<Recherche> toutesLesDernieres = analyseur.getDernieresRecherches(10);
        assertEquals(5, toutesLesDernieres.size());
    }
    
    @Test
    public void testHistoriqueVide() {
        AnalyseurHistorique analyseurVide = new AnalyseurHistorique(new ArrayList<>());
        assertEquals("Rien", analyseurVide.getVilleDepartFavorite());
        assertEquals("Aucun trajet", analyseurVide.getTrajetFavori());
        assertNull(analyseurVide.getCritereFavori());
        assertTrue(analyseurVide.getDernieresRecherches(5).isEmpty());
    }
}