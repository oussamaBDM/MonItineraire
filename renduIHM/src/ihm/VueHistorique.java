package ihm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class VueHistorique extends ScrollPane {
    private VBox panneauHistorique;

    public VueHistorique() {
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: " + Style.FOND_GRIS + "; -fx-background: " + Style.FOND_GRIS + ";");

        VBox contenu = new VBox(20);
        contenu.setPadding(new Insets(30));

        Label titrePrincipal = new Label("Votre Profil et Historique :");
        titrePrincipal.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titrePrincipal.setTextFill(Color.web(Style.BLEU_APP));

        this.panneauHistorique = new VBox(12);
        this.panneauHistorique.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-border-radius: 8;");

        DropShadow ombre = new DropShadow();
        ombre.setColor(Color.web("#00000019"));
        ombre.setOffsetY(2);
        ombre.setRadius(4);
        this.panneauHistorique.setEffect(ombre);

        Label texteAttenteHistorique = new Label("Aucun historique pour le moment.");
        texteAttenteHistorique.setTextFill(Color.GRAY);
        this.panneauHistorique.getChildren().add(texteAttenteHistorique);
        contenu.getChildren().addAll(titrePrincipal, this.panneauHistorique);
        this.setContent(contenu);
    }

    public void mettreAJourHistorique(List<String[]> listeTrajets) {
        this.panneauHistorique.getChildren().clear();
        for (String[] trajet : listeTrajets) {
            HBox ligne = new HBox(15);
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setPadding(new Insets(12, 20, 12, 20));
            ligne.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;");
            Label villes = new Label("🌍  " + trajet[0] + "  →  " + trajet[1]);
            villes.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            villes.setTextFill(Color.web(Style.BLEU_NUIT));
            Region espaceur = new Region();
            HBox.setHgrow(espaceur, Priority.ALWAYS);
            Label prix = new Label("💰 " + trajet[2] + " €");
            prix.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            prix.setTextFill(Color.web(Style.AMBRE));
            Label temps = new Label("⏱ " + trajet[3]);
            temps.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            temps.setTextFill(Color.web(Style.BLEU_VIVO));
            Label co2 = new Label("🌿 " + trajet[4] + " kg CO₂");
            co2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            co2.setTextFill(Color.web(Style.VERT));
            ligne.getChildren().addAll(villes, espaceur, prix, temps, co2);
            this.panneauHistorique.getChildren().add(ligne);
        }
    }
}