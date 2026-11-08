package ihm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class VueBarreLaterale extends VBox {
    private Button boutonRecherche;
    private Button boutonHistorique;

    public VueBarreLaterale() {
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setPrefWidth(220);
        this.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 1 0 0;");
        this.boutonRecherche = creerBouton("📍 Recherche de trajet");
        this.boutonHistorique = creerBouton("📊 Historique & Profil");
        activerBoutonRecherche();
        this.getChildren().addAll(boutonRecherche, boutonHistorique);
    }

    private Button creerBouton(String texte) {
        Button bouton = new Button(texte);
        bouton.setPrefWidth(200);
        bouton.setAlignment(Pos.CENTER_LEFT);
        bouton.setOnMouseEntered(ev -> {
            if (!bouton.getStyle().contains(Style.BLEU_APP)) {
                bouton.setStyle("-fx-background-color: " + Style.BLEU_CLAIR + "; -fx-text-fill: #666; -fx-padding: 10; -fx-border-radius: 4;");
            }
        });
        bouton.setOnMouseExited(ev -> {
            if (!bouton.getStyle().contains(Style.BLEU_APP)) {
                bouton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; -fx-padding: 10; -fx-border-radius: 4;");
            }
        });
        return bouton;
    }

    public void activerBoutonRecherche() {
        boutonRecherche.setStyle("-fx-background-color: " + Style.BLEU_APP + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-border-radius: 4;");
        boutonHistorique.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; -fx-padding: 10; -fx-border-radius: 4;");
    }

    public void activerBoutonHistorique() {
        boutonHistorique.setStyle("-fx-background-color: " + Style.BLEU_APP + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-border-radius: 4;");
        boutonRecherche.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; -fx-padding: 10; -fx-border-radius: 4;");
    }

    public Button getBoutonRecherche() {
        return boutonRecherche;
    }

    public Button getBoutonHistorique() {
        return boutonHistorique;
    }
}