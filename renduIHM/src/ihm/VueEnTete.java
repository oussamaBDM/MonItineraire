package ihm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VueEnTete extends HBox {
    private Button boutonCsv;
    private TextField champRecherche;

    public VueEnTete(){
        this.setSpacing(20);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        this.setAlignment(Pos.CENTER_LEFT);

        VBox sectionLogo = new VBox();
        Label logo = new Label("MonItinéraire");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logo.setTextFill(Color.web(Style.BLEU_APP));
        Label sousTitre = new Label("Trouvez le meilleur itinéraire pour votre voyage");
        sousTitre.setFont(Font.font("Arial", 12));
        sousTitre.setTextFill(Color.GRAY);
        sectionLogo.getChildren().addAll(logo, sousTitre);

        this.champRecherche = new TextField();
        this.champRecherche.setPromptText("Recherche rapide (ex: Lille)...");
        this.champRecherche.setPrefWidth(300);
        this.champRecherche.setStyle("-fx-padding: 8 12; -fx-border-color: #ddd; -fx-border-radius: 4;");
        Region espaceur = new Region();
        HBox.setHgrow(espaceur, Priority.ALWAYS);
        this.boutonCsv = new Button("📁 Charger CSV");
        String styleBaseCsv = "-fx-background-color: white; -fx-text-fill: " + Style.BLEU_APP + "; -fx-border-color: " + Style.BLEU_APP + "; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 4; -fx-cursor: hand;";
        this.boutonCsv.setStyle(styleBaseCsv);
        this.boutonCsv.setOnMouseEntered(ev -> {
            this.boutonCsv.setStyle("-fx-background-color: " + Style.BLEU_CLAIR + "; -fx-text-fill: " + Style.BLEU_APP + "; -fx-border-color: " + Style.BLEU_APP + "; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 4; -fx-cursor: hand;");
        });
        this.boutonCsv.setOnMouseExited(ev -> {
            this.boutonCsv.setStyle(styleBaseCsv);
        });
        this.getChildren().addAll(sectionLogo, espaceur, this.champRecherche, boutonCsv);
    }

    public Button getBoutonCsv() {
        return boutonCsv;
    }

    public TextField getChampRecherche() {
        return champRecherche;
    }
}