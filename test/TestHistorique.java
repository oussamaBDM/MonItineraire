package test;

import poo.GestionnaireHistorique;
import poo.PreferencesMultiCriteres;
import poo.Recherche;
import poo.TypeCout; 

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestHistorique {

    private final String FICHIER_TEST = "test_historique.dat";

    @BeforeEach
    public void preparerTest(){
        File fichier = new File(FICHIER_TEST);
        if (fichier.exists()){
            fichier.delete();
        }
    }

    @AfterEach
    public void nettoyerTest() {
        File fichier = new File(FICHIER_TEST);
        if(fichier.exists()){
            fichier.delete();
        }
    }

    @Test
    public void testFichierInexistant() throws Exception{
        List<Recherche> historique = GestionnaireHistorique.charger(FICHIER_TEST);
        assertNotNull(historique);
        assertTrue(historique.isEmpty());
    }

    @Test
    public void testSauvegardeEtChargement() throws Exception{
        PreferencesMultiCriteres prefTemps = new PreferencesMultiCriteres();
        prefTemps.setImportance(TypeCout.TEMPS, 1.0);
        PreferencesMultiCriteres prefPrix = new PreferencesMultiCriteres();
        prefPrix.setImportance(TypeCout.PRIX, 1.0);
        List<Recherche> historiqueASauvegarder = new ArrayList<>();
        historiqueASauvegarder.add(new Recherche("Lille", "Paris", prefTemps));
        historiqueASauvegarder.add(new Recherche("Marseille", "Lyon", prefPrix));

        GestionnaireHistorique.sauvegarder(historiqueASauvegarder, FICHIER_TEST);
        File fichier = new File(FICHIER_TEST);
        assertTrue(fichier.exists());

        List<Recherche> historiqueCharge = GestionnaireHistorique.charger(FICHIER_TEST);
        assertEquals(2, historiqueCharge.size());
        
        Recherche trajet1 = historiqueCharge.get(0);
        assertEquals("Lille", trajet1.getVilleDepart());
        assertEquals("Paris", trajet1.getVilleArrivee());
        assertEquals(1.0, trajet1.getPreferences().getImportance(TypeCout.TEMPS));
    }
}