package ihm;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Carte extends StackPane {

    public Carte() {
        WebView navigateurInterne = new WebView();
        WebEngine moteurWeb = navigateurInterne.getEngine();
        String urlCarte = "https://www.openstreetmap.org/export/embed.html?bbox=-5.0,42.0,10.0,51.0&layer=mapnik";
        moteurWeb.load(urlCarte);
        this.getChildren().add(navigateurInterne);
        this.setStyle(Style.CARD_STYLE);
    }
}