package ihm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PrincipalIHM extends Application {
    private BorderPane racine;
    private VueBarreLaterale barreLaterale;
    private VueRecherche vueRecherche;
    private VueHistorique vueHistorique;
    private final List<String[]> listeHistoriqueFixe = new ArrayList<>();

    public void start(Stage fenetrePrincipale) {
        fenetrePrincipale.setTitle("MonItinéraire");
        racine = new BorderPane();
        racine.setStyle("-fx-background-color: " + Style.FOND_GRIS + ";");
        VueEnTete enTete = new VueEnTete();
        barreLaterale = new VueBarreLaterale();
        vueRecherche = new VueRecherche();
        vueHistorique = new VueHistorique();
        vueRecherche.setPrincipalIHM(this);
        listeHistoriqueFixe.add(new String[]{"Lille", "Paris", "25", "1h02", "3"});
        listeHistoriqueFixe.add(new String[]{"Paris", "Lyon", "45", "1h57", "5"});
        listeHistoriqueFixe.add(new String[]{"Lille", "Londres", "59", "1h22", "6"});
        listeHistoriqueFixe.add(new String[]{"Bruxelles", "Paris", "39", "1h25", "4"});
        listeHistoriqueFixe.add(new String[]{"Paris", "Bordeaux", "55", "2h04", "5"});
        vueHistorique.mettreAJourHistorique(listeHistoriqueFixe);
        racine.setTop(enTete);
        racine.setLeft(barreLaterale);
        racine.setCenter(vueRecherche);
        barreLaterale.getBoutonRecherche().setOnAction(ev -> {
            racine.setCenter(vueRecherche);
            barreLaterale.activerBoutonRecherche();
        });
        barreLaterale.getBoutonHistorique().setOnAction(ev -> {
            racine.setCenter(vueHistorique);
            barreLaterale.activerBoutonHistorique();
        });
        enTete.getBoutonCsv().setOnAction(ev -> {
            ouvrirSelecteurFichier(fenetrePrincipale);
        });
        enTete.getChampRecherche().textProperty().addListener((obs, vieuxTexte, nouveauTexte) -> {
            filtrerEtAfficherHistorique(nouveauTexte);
        });
        Scene scene = new Scene(racine, 1200, 750);
        fenetrePrincipale.setScene(scene);
        fenetrePrincipale.setMaximized(true);
        fenetrePrincipale.show();
    }

    private void ouvrirSelecteurFichier(Stage fenetre) {
        FileChooser selecteurFichier = new FileChooser();
        selecteurFichier.setTitle("Charger un fichier CSV");
        selecteurFichier.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
        File fichierSelectionne = selecteurFichier.showOpenDialog(fenetre);
        if (fichierSelectionne != null){
            try {
                BufferedReader lecteur = new BufferedReader(new FileReader(fichierSelectionne));
                List<String[]> catalogueRecherche = new ArrayList<>();
                String ligne;
                lecteur.readLine();
                while ((ligne = lecteur.readLine()) != null) {
                    String[] colonnes = ligne.split(";");
                    if (colonnes.length == 5) {
                        catalogueRecherche.add(colonnes);
                    }
                }
                lecteur.close();
                vueRecherche.setCatalogueTrajets(catalogueRecherche);
                vueRecherche.rafraichirDonnees();
            }catch(Exception ex){
                System.out.println("Erreur de lecture du catalogue : " + ex.getMessage());
            }
        }
    }

    private void filtrerEtAfficherHistorique(String recherche) {
        String terme = recherche.toLowerCase().trim();
        List<String[]> listeFiltree = new ArrayList<>();
        
        // On filtre uniquement sur la base de nos 5 trajets fixes
        for (String[] trajet : listeHistoriqueFixe) {
            String depart = trajet[0].toLowerCase();
            String arrivee = trajet[1].toLowerCase();
            if (depart.contains(terme) || arrivee.contains(terme)) {
                listeFiltree.add(trajet);
            }
        }
        vueHistorique.mettreAJourHistorique(listeFiltree);
    }

    public void ajouterTrajetReserve(String depart, String arrivee, String prix, String temps, String co2){
        listeHistoriqueFixe.add(0, new String[]{depart, arrivee, prix, temps, co2});
        vueHistorique.mettreAJourHistorique(listeHistoriqueFixe);
    }
    public static void main(String[] arguments) {
        launch(arguments);
    }
}