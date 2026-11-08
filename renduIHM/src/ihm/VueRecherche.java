package ihm;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class VueRecherche extends BorderPane {

    private final StringProperty selectedTransport = new SimpleStringProperty("TRAIN");
    private final DoubleProperty priorityPrix  = new SimpleDoubleProperty(80);
    private final DoubleProperty priorityTemps = new SimpleDoubleProperty(20);
    private final DoubleProperty priorityCO2   = new SimpleDoubleProperty(0);
    private VBox resultsSection;
    private Label routeSummaryLabel;
    private TextField champDepart;
    private TextField champArrivee;
    private VBox rightContent;
    private List<String[]> catalogueTrajets = new ArrayList<>();
    private PrincipalIHM principalIHM;
    private boolean ajustementAuto = false;

    public VueRecherche() {
        this.setStyle("-fx-background-color: " + Style.FOND_GRIS + ";");
        VBox leftPanel = new VBox(0);
        leftPanel.setPrefWidth(380);
        leftPanel.setStyle("-fx-background-color: " + Style.BLANC + ";" +"-fx-border-color: #E0E8F4;" +"-fx-border-width: 0 1 0 0;");
        leftPanel.getChildren().addAll(buildSearchPanel(), buildConfigPanel());
        ScrollPane rightScroll = new ScrollPane();
        rightScroll.setFitToWidth(true);
        rightScroll.setStyle("-fx-background-color: " + Style.FOND_GRIS + ";" +"-fx-background: " + Style.FOND_GRIS + ";");
        resultsSection = buildResultsSection();
        resultsSection.setVisible(false);
        resultsSection.setManaged(false);
        this.rightContent = new VBox(0);
        rightContent.setStyle("-fx-background-color: " + Style.FOND_GRIS + ";");
        rightContent.getChildren().add(buildRightPlaceholder());
        rightContent.getChildren().add(resultsSection);
        rightScroll.setContent(rightContent);
        this.setLeft(leftPanel);
        this.setCenter(rightScroll);
        this.setupPrioritesMax100();
    }

    public void setCatalogueTrajets(List<String[]> catalogue) {
        this.catalogueTrajets = catalogue;
    }

    public void setPrincipalIHM(PrincipalIHM principalIHM) {
        this.principalIHM = principalIHM;
    }

    private VBox buildSearchPanel(){
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #F0F4F8; -fx-border-color: #C0D4EE; -fx-border-width: 0 0 1 0;");
        Label title = new Label("Rechercher un trajet");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");
        VBox departBox = new VBox(5);
        Label departLabel = new Label("DÉPART");
        departLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + Style.GRIS_TEXTE + ";");
        champDepart = new TextField();
        champDepart.setPromptText("Ex: Lille");
        champDepart.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-prompt-text-fill: #95A5A6; -fx-border-color: #C0D4EE; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 8;");
        addAutoComplete(champDepart);
        departBox.getChildren().addAll(departLabel, champDepart);
        Button swapBtn = new Button("⇄  Inverser");
        swapBtn.setStyle("-fx-background-color: #EBF5FB; -fx-text-fill: #2980B9; -fx-border-color: #AED6F1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 4 10; -fx-cursor: hand;");
        swapBtn.setOnAction(e -> {
            String tmp = champDepart.getText();
            champDepart.setText(champArrivee.getText());
            champArrivee.setText(tmp);
        });
        VBox arriveeBox = new VBox(5);
        Label arriveeLabel = new Label("ARRIVÉE");
        arriveeLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + Style.GRIS_TEXTE + ";");  
        champArrivee = new TextField();
        champArrivee.setPromptText("Ex: Paris");
        champArrivee.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-prompt-text-fill: #95A5A6; -fx-border-color: #C0D4EE; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 8;");
        addAutoComplete(champArrivee);
        arriveeBox.getChildren().addAll(arriveeLabel, champArrivee);
        Button searchBtn = new Button("🔍   Calculer l'itinéraire");
        searchBtn.setMaxWidth(Double.MAX_VALUE);
        String styleNormal = "-fx-background-color: " + Style.AMBRE + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 4; -fx-cursor: hand;";
        String styleHover  = "-fx-background-color: #E8960F; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 4; -fx-cursor: hand;";
        searchBtn.setStyle(styleNormal);
        searchBtn.setOnMouseEntered(e -> {
            searchBtn.setStyle(styleHover);
        });
        searchBtn.setOnMouseExited(e -> {
            searchBtn.setStyle(styleNormal);
        });
        searchBtn.setOnAction(e -> {
            triggerSearch(searchBtn);
        });
        panel.getChildren().addAll(title, departBox, swapBtn, arriveeBox, searchBtn);
        return panel;
    }

    private VBox buildConfigPanel() {
        VBox panel = new VBox(24);
        panel.setPadding(new Insets(20, 20, 20, 20));
        panel.getChildren().addAll(
            buildTransportSelector(),
            buildSeparator(),
            buildPrioritySliders()
        );
        VBox.setVgrow(panel, Priority.ALWAYS);
        return panel;
    }

    private Region buildSeparator() {
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #E0E8F4;");
        return sep;
    }

    private VBox buildTransportSelector() {
        VBox box = new VBox(12);
        Label title = new Label("Modes de transport");
        title.setStyle(Style.SECTION_TITLE_STYLE);
        HBox cards = new HBox(10);
        cards.setAlignment(Pos.CENTER_LEFT);
        TransportCard train = new TransportCard("🚄", "Train",  "Confort",   Style.AMBRE,     "TRAIN");
        TransportCard bus = new TransportCard("🚌", "Bus",    "Eco",       Style.VERT,      "BUS");
        TransportCard avion = new TransportCard("✈",  "Avion",  "Rapide",    Style.BLEU_VIVO, "AVION");
        train.setSelected(true);
        selectedTransport.set("TRAIN");
        train.setOnMouseClicked(e -> {
            train.setSelected(true); bus.setSelected(false); avion.setSelected(false);
            selectedTransport.set("TRAIN");
        });
        bus.setOnMouseClicked(e -> {
            train.setSelected(false); bus.setSelected(true); avion.setSelected(false);
            selectedTransport.set("BUS");
        });
        avion.setOnMouseClicked(e -> {
            train.setSelected(false); bus.setSelected(false); avion.setSelected(true);
            selectedTransport.set("AVION");
        });
        cards.getChildren().addAll(train, bus, avion);
        box.getChildren().addAll(title, cards);
        return box;
    }

    private VBox buildPrioritySliders(){
        VBox box = new VBox(12);
        Label title = new Label("Choissisez vos critères");
        title.setStyle(Style.SECTION_TITLE_STYLE);
        Label hint = new Label("Ajustez l'importance de chaque critère les uns par rapport aux autres.");
        hint.setStyle("-fx-font-size: 10px; -fx-text-fill: " + Style.GRIS_TEXTE + "; -fx-font-style: italic;");
        box.getChildren().addAll(title,hint,buildPriorityRow("💰", "Prix",Style.AMBRE,priorityPrix),buildPriorityRow("⏱", "Temps",Style.BLEU_VIVO, priorityTemps),buildPriorityRow("🌿", "Écologie", Style.VERT,priorityCO2));
        return box;
    }

    private void setupPrioritesMax100() {
        priorityPrix.addListener((o, ov, nv) -> gererChangementPriorite(priorityPrix, priorityTemps, priorityCO2, nv.doubleValue()));
        priorityTemps.addListener((o, ov, nv) -> gererChangementPriorite(priorityTemps, priorityPrix, priorityCO2, nv.doubleValue()));
        priorityCO2.addListener((o, ov, nv) -> gererChangementPriorite(priorityCO2, priorityPrix, priorityTemps, nv.doubleValue()));
    }

    private void gererChangementPriorite(DoubleProperty master, DoubleProperty slave1, DoubleProperty slave2, double nouvelleValeur) {
        if (ajustementAuto) return;
        ajustementAuto = true;
        double totalAutres = slave1.get() + slave2.get();
        double depassement = (nouvelleValeur + totalAutres) - 100;
        if(depassement > 0){
            if(totalAutres > 0){
                double ratio1 = slave1.get() / totalAutres;
                double ratio2 = slave2.get() / totalAutres;
                slave1.set(Math.max(0, slave1.get() - (depassement * ratio1)));
                slave2.set(Math.max(0, slave2.get() - (depassement * ratio2)));
            }else{
                master.set(100);
            }
        }
        ajustementAuto = false;
    }

    private HBox buildPriorityRow(String icon, String label, String color, DoubleProperty prop){
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        Label ico = new Label(icon + " " + label);
        ico.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Style.BLEU_NUIT + "; -fx-min-width: 80px;");
        Slider slider = new Slider(0, 100, prop.get());
        slider.setShowTickLabels(false);
        slider.setStyle("-fx-accent: " + color + ";");
        slider.valueProperty().bindBidirectional(prop);
        HBox.setHgrow(slider, Priority.ALWAYS);
        Label val = new Label((int) prop.get() + "%");
        val.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-min-width: 36px;");
        prop.addListener((o, ov, nv) -> val.setText((int) nv.doubleValue() + "%"));
        row.getChildren().addAll(ico, slider, val);
        return row;
    }

    private VBox buildRightPlaceholder(){
        VBox ph = new VBox(12);
        ph.setAlignment(Pos.CENTER);
        ph.setPadding(new Insets(80, 40, 80, 40));
        Label ico = new Label("🗺");
        ico.setStyle("-fx-font-size: 48px;");
        Label msg = new Label("Saisissez un départ et une arrivée\npuis cliquez sur Calculer");
        msg.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Style.GRIS_TEXTE + "; -fx-text-alignment: center;");
        msg.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        ph.getChildren().addAll(ico, msg);
        return ph;
    }

    private VBox buildResultsSection(){
        VBox section = new VBox(0);
        section.setStyle("-fx-background-color: " + Style.FOND_GRIS + ";");
        HBox header = new HBox(16);
        header.setPadding(new Insets(20, 24, 12, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + Style.BLANC + "; -fx-border-color: #E0E8F4; -fx-border-width: 0 0 1 0;");
        routeSummaryLabel = new Label("3 itinéraires trouvés");
        routeSummaryLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");
        header.getChildren().add(routeSummaryLabel);
        VBox cards = new VBox(12);
        cards.setPadding(new Insets(16, 24, 24, 24));
        cards.setId("resultsCards");
        section.getChildren().addAll(header, cards);
        return section;
    }
    
    private void triggerSearch(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(0.96); 
        st.setToY(0.96);
        st.setAutoReverse(true); 
        st.setCycleCount(2);
        st.play();
        String dep = champDepart.getText().trim().isEmpty()  ? "Départ"  : champDepart.getText().trim();
        String arr = champArrivee.getText().trim().isEmpty() ? "Arrivée" : champArrivee.getText().trim();
        VBox cards = (VBox) resultsSection.lookup("#resultsCards");
        cards.getChildren().clear();
        List<ItineraryData> itineraries = buildDynamicData(dep, arr);
        computeScores(itineraries);
        itineraries.sort((d1, d2) -> Integer.compare(d2.score, d1.score));
        String preferred = selectedTransport.get();
        routeSummaryLabel.setText(itineraries.size() + " itinéraires trouvés · " + dep + " → " + arr);
        for (int i = 0; i < itineraries.size(); i++) {
            ItineraryData data = itineraries.get(i);
            boolean isBest = (i == 0);
            boolean isPreferred = data.transportId.equals(preferred);
            cards.getChildren().add(buildResultCard(data, isBest, isPreferred));
        }
        resultsSection.setVisible(true);
        resultsSection.setManaged(true);
        fadeIn(resultsSection);
        if (!(rightContent.getChildren().get(0) instanceof Carte)) {
            Carte carte = new Carte();
            carte.setPrefHeight(280);
            rightContent.getChildren().set(0, carte);
            fadeIn(carte);
        }
    }
    
    private void computeScores(List<ItineraryData> list){
        double wPrix  = priorityPrix.get();
        double wTemps = priorityTemps.get();
        double wCO2   = priorityCO2.get();
        double total  = wPrix + wTemps + wCO2;
        if (total <= 0){ 
            wPrix = wTemps = wCO2 = 100.0 / 3.0; 
            total = 100.0; 
        }
        wPrix /= total;
        wTemps/= total;
        wCO2 /= total;
        double minPrix = list.stream().mapToDouble(d -> d.prixVal).min().orElse(0);
        double maxPrix = list.stream().mapToDouble(d -> d.prixVal).max().orElse(1);
        double minDuree = list.stream().mapToDouble(d -> d.dureeMin).min().orElse(0);
        double maxDuree = list.stream().mapToDouble(d -> d.dureeMin).max().orElse(1);
        double minCO2 = list.stream().mapToDouble(d -> d.co2Val).min().orElse(0);
        double maxCO2 = list.stream().mapToDouble(d -> d.co2Val).max().orElse(1);
        double rangePrix = maxPrix - minPrix;
        double rangeDuree = maxDuree- minDuree;
        double rangeCO2 = maxCO2 - minCO2;
        for (ItineraryData d : list){
            double normPrix  = rangePrix  > 0  ? (d.prixVal - minPrix) / rangePrix   : 0;
            double normDuree = rangeDuree > 0 ? (d.dureeMin - minDuree) / rangeDuree : 0;
            double normCO2   = rangeCO2   > 0  ? (d.co2Val - minCO2) / rangeCO2     : 0;
            double penalty = (normPrix * wPrix + normDuree * wTemps + normCO2 * wCO2) * 100;
            d.score = (int) Math.round(100 - penalty);
        }
    }

    private List<ItineraryData> buildDynamicData(String dep, String arr){
        List<ItineraryData> list = new ArrayList<>();
        String departRecherche = dep.trim();
        String arriveeRecherche = arr.trim();
        if (catalogueTrajets == null || catalogueTrajets.isEmpty()) {
            return list;
        }
        for (String[] row : catalogueTrajets) {
            if (row.length >= 5) {
                String csvDep = row[0].trim();
                String csvArr = row[1].trim();
                if (csvDep.equalsIgnoreCase(departRecherche) && csvArr.equalsIgnoreCase(arriveeRecherche)) {
                    try {
                        int basePrix = Integer.parseInt(row[2].trim().replaceAll("[^0-9]", ""));
                        String baseDur = row[3].trim();
                        int baseCO2 = Integer.parseInt(row[4].trim().replaceAll("[^0-9]", ""));
                        int baseMin = 60;
                        if (baseDur.contains("h")) {
                            String[] parts = baseDur.split("h");
                            int heures = Integer.parseInt(parts[0].trim().replaceAll("[^0-9]", ""));
                            int minutes = parts.length > 1 ? Integer.parseInt(parts[1].trim().replaceAll("[^0-9]", "")) : 0;
                            baseMin = (heures * 60) + minutes;
                        }
                        list.add(new ItineraryData("Train", "TRAIN", csvDep, csvArr, baseDur, basePrix, baseCO2));
                        int busPrix = Math.max(7, basePrix / 2);
                        int busMin = (int) (baseMin * 2.2);
                        int busCO2 = (int) (baseCO2 * 0.4);
                        list.add(new ItineraryData("Bus", "BUS", csvDep, csvArr, formatDuration(busMin), busPrix, busCO2));
                        int avionPrix = basePrix * 3;
                        int avionMin = Math.max(50, (int) (baseMin * 0.4));
                        int avionCO2 = baseCO2 * 12;
                        list.add(new ItineraryData("Avion", "AVION", csvDep, csvArr, formatDuration(avionMin), avionPrix, avionCO2));
                        break;
                    } catch (Exception e) {
                        System.out.println("⚠️ Erreur d'analyse sur la ligne du CSV : " + e.getMessage());
                    }
                }
            }
        }
        return list;
    }

    private String formatDuration(int totalMin) {
        return totalMin / 60 + "h" + String.format("%02d", totalMin % 60);
    }


    private VBox buildResultCard(ItineraryData data, boolean isBest, boolean isPreferred) {
        VBox card = new VBox(0);
        String icon = "🚄";
        String color = Style.AMBRE;
        if ("BUS".equals(data.transportId)) {
            icon = "🚌";
            color = Style.VERT;
        }else if("AVION".equals(data.transportId)){
            icon = "✈";
            color = Style.BLEU_VIVO;
        }
        String baseStyle = "-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, #00000015, 8, 0, 0, 2);";
        String borderColor = isBest ? Style.BLEU_APP : (isPreferred ? color : "transparent");
        card.setStyle(baseStyle + " -fx-border-color: " + borderColor + "; -fx-border-width: 2; -fx-border-radius: 8;");
        HBox main = new HBox(16);
        main.setPadding(new Insets(16, 20, 16, 20));
        main.setAlignment(Pos.CENTER_LEFT);
        Label transportBadge = new Label(icon + "  " + data.transport);
        transportBadge.setStyle("-fx-background-color: " + color + "18; -fx-text-fill: " + color + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 5 12; -fx-background-radius: 6; -fx-min-width: 90;");
        VBox routeInfo = new VBox(3);
        Label horaires = new Label(data.departure + "  →  " + data.arrival);
        horaires.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");
        Label dureeLabel = new Label("⏱ " + data.duration);
        dureeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Style.GRIS_TEXTE + ";");
        routeInfo.getChildren().addAll(horaires, dureeLabel);
        HBox.setHgrow(routeInfo, Priority.ALWAYS);
        HBox metrics = new HBox(20);
        metrics.setAlignment(Pos.CENTER);
        metrics.getChildren().addAll(
            buildMetricBox("💰", data.prixVal + " €", Style.AMBRE),
            buildMetricBox("🌿", data.co2Val + " kg", Style.VERT),
            buildMetricBox("⏱", data.duration, Style.BLEU_VIVO)
        );
        VBox scoreBox = new VBox(2);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setMinWidth(56);
        String scoreColor = data.score >= 70 ? Style.VERT : data.score >= 40 ? Style.AMBRE : Style.ROUGE_CO2;
        Label scoreVal = new Label(data.score + "%");
        scoreVal.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + scoreColor + ";");
        Label scoreLbl = new Label("Score");
        scoreLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: " + Style.GRIS_TEXTE + ";");
        scoreBox.getChildren().addAll(scoreVal, scoreLbl);
        Button bookBtn = new Button("Réserver →");
        bookBtn.setStyle("-fx-background-color: " + (isBest ? Style.BLEU_APP : Style.BLEU_NUIT) + "; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 14; -fx-cursor: hand;");
        bookBtn.setOnAction(e -> showBookingPopup(data));
        HBox badgeRow = new HBox(6);
        badgeRow.setAlignment(Pos.CENTER_LEFT);
        if (isBest) {
            Label bestBadge = new Label("✓ Meilleur score");
            bestBadge.setStyle("-fx-background-color: " + Style.BLEU_APP + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 10; -fx-background-radius: 4;");
            badgeRow.getChildren().add(bestBadge);
        }
        if (isPreferred && !isBest) {
            Label prefBadge = new Label("★ Votre mode préféré");
            prefBadge.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 10; -fx-background-radius: 4;");
            badgeRow.getChildren().add(prefBadge);
        }
        if ("TRAIN".equals(data.transportId)) {
            Label ecoBadge = new Label("🌱 Option Écoresponsable");
            ecoBadge.setStyle("-fx-background-color: " + Style.VERT + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 10; -fx-background-radius: 4;");
            badgeRow.getChildren().add(ecoBadge);
        }
        if (!badgeRow.getChildren().isEmpty()) {
            VBox topWrapper = new VBox(0);
            topWrapper.setPadding(new Insets(12, 20, 0, 20));
            topWrapper.getChildren().add(badgeRow);
            card.getChildren().addAll(topWrapper, main);
        }else{
            card.getChildren().add(main);
        }
        main.getChildren().addAll(transportBadge, routeInfo, metrics, scoreBox, bookBtn);
        return card;
    }

    private VBox buildMetricBox(String icon, String value, String color) {
        VBox v = new VBox(2);
        v.setAlignment(Pos.CENTER);
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 13px;");
        Label val = new Label(value);
        val.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Style.BLEU_NUIT + ";" +
            "-fx-font-family: 'Segoe UI';"
        );
        v.getChildren().addAll(ico, val);
        return v;
    }

    private void showBookingPopup(ItineraryData data){
        Stage popup = new Stage();
        popup.setTitle("Confirmer la réservation");
        VBox content = new VBox(18);
        content.setPadding(new Insets(28));
        content.setStyle("-fx-background-color: white;");
        content.setMinWidth(400);
        Label title = new Label("Réserver votre trajet");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");

        String icon = "🚄";
        if ("BUS".equals(data.transportId)) {
            icon = "🚌";
        } else if ("AVION".equals(data.transportId)) {
            icon = "✈";
        }

        VBox recap = new VBox(10);
        recap.setStyle("-fx-background-color: " + Style.BLEU_PALE + "; -fx-padding: 14; -fx-background-radius: 8;");
        recap.getChildren().addAll(
            recapRow("Transport", icon + " " + data.transport),
            recapRow("Horaires",  data.departure + " → " + data.arrival),
            recapRow("Durée",     data.duration),
            recapRow("Prix",      data.prixVal + " €"),
            recapRow("CO₂",       data.co2Val + " kg"),
            recapRow("Score",     data.score + "%")
        );

        Label passLabel = new Label("Nombre de passagers :");
        passLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Style.BLEU_NUIT + ";");
        Spinner<Integer> spinner = new Spinner<>(1, 9, 1);
        spinner.setPrefWidth(100);

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_RIGHT);

        Button cancel = new Button("Annuler");
        cancel.setStyle("-fx-background-color: #F0F4F8; -fx-text-fill: " + Style.BLEU_NUIT + "; -fx-cursor: hand; -fx-padding: 9 18; -fx-background-radius: 6;");
        cancel.setOnAction(e -> popup.close());

        Button confirm = new Button("Confirmer la réservation");
        confirm.setStyle("-fx-background-color: " + Style.BLEU_APP + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 9 18; -fx-background-radius: 6;");
        confirm.setOnAction(e -> { 
            popup.close(); 
            showToast("✓  Réservation confirmée ! Bon voyage 🎉", Style.VERT); 
            
            if (principalIHM != null) {
                principalIHM.ajouterTrajetReserve(
                    data.departure,
                    data.arrival,
                    data.prixVal + " €",
                    data.duration,
                    data.co2Val + " kg"
                );
            }
        });
        btns.getChildren().addAll(cancel, confirm);
        content.getChildren().addAll(title, recap, passLabel, spinner, btns);
        popup.setScene(new Scene(content));
        popup.show();
    }

    private HBox recapRow(String key, String value){
        HBox row = new HBox(8);
        Label k = new Label(key + " :");
        k.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Style.GRIS_TEXTE + "; -fx-min-width: 80;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");
        row.getChildren().addAll(k, v);
        return row;
    }

    private void showToast(String message, String color) {
        Stage toast = new Stage();
        toast.initStyle(javafx.stage.StageStyle.UNDECORATED);
        HBox box = new HBox();
        box.setPadding(new Insets(12, 22, 12, 22));
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        box.setAlignment(Pos.CENTER);
        Label lbl = new Label(message);
        lbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: white;");
        box.getChildren().add(lbl);
        toast.setScene(new Scene(box));
        toast.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> toast.close());
        pause.play();
    }

    private void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(350), node);
        ft.setToValue(1.0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(280), node);
        tt.setFromY(12); tt.setToY(0);
        new ParallelTransition(ft, tt).play();
    }

    private void addAutoComplete(TextField field) {
        List<String> cities = List.of("Lille", "Paris", "Lyon", "Marseille", "Bordeaux", "Toulouse", "Nantes", "Strasbourg", "Rennes", "Nice", "Londres", "Bruxelles", "Amsterdam", "Berlin");
        ContextMenu menu = new ContextMenu();
        field.textProperty().addListener((obs, ov, nv) -> {
            menu.getItems().clear();
            if(nv != null && nv.trim().length() >= 2){
                for (String city : cities) {
                    if(city.toLowerCase().contains(nv.toLowerCase())) {
                        MenuItem item = new MenuItem(city);
                        item.setOnAction(e -> field.setText(city));
                        menu.getItems().add(item);
                    }
                }
            }
            if (!menu.getItems().isEmpty()) menu.show(field, Side.BOTTOM, 0, 0);
            else menu.hide();
        });
    }

    public TextField getChampDepart(){ return champDepart; }
    public TextField getChampArrivee() { return champArrivee; }
    public String getSelectedTransport(){ return selectedTransport.get(); }
    public double getPriorityPrix(){ return priorityPrix.get(); }
    public double getPriorityTemps(){ return priorityTemps.get(); }
    public double getPriorityCO2(){ return priorityCO2.get(); }

    public void rafraichirDonnees() {
        this.showToast("✓ Données du fichier CSV chargées avec succès !", Style.VERT);
    }

    private static class TransportCard extends VBox{
        private boolean selected;
        private final String color;

    TransportCard(String icon, String name, String tagline, String color, String id){
            this.color = color;
            setAlignment(Pos.CENTER);
            setPadding(new Insets(10, 14, 10, 14));
            setSpacing(3);
            setPrefWidth(95);
            setCursor(Cursor.HAND);
            setStyle(styleNormal());
            Label ico = new Label(icon);
            ico.setStyle("-fx-font-size: 20px;");
            Label nameLbl = new Label(name);
            nameLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + Style.BLEU_NUIT + ";");
            Label tag = new Label(tagline);
            tag.setStyle("-fx-font-size: 10px; -fx-text-fill: " + Style.GRIS_TEXTE + ";");
            getChildren().addAll(ico, nameLbl, tag);
            setOnMouseEntered(e -> { if (!selected) setStyle(styleHover()); });
            setOnMouseExited(e -> { if (!selected) setStyle(styleNormal()); });
        }

        void setSelected(boolean sel) {
            selected = sel;
            setStyle(sel ? styleSelected() : styleNormal());
        }

    private String styleNormal(){
        return "-fx-background-color: " + Style.FOND_GRIS + "; -fx-background-radius: 8; -fx-border-color: #E0EAF5; -fx-border-radius: 8; -fx-border-width: 2;";
    }

    private String styleHover() {
        return "-fx-background-color: " + color + "18; -fx-background-radius: 8; -fx-border-color: " + color + "88; -fx-border-radius: 8; -fx-border-width: 2;";
    }

    private String styleSelected() {
        return "-fx-background-color: " + color + "22; -fx-background-radius: 8; -fx-border-color: " + color + "; -fx-border-radius: 8; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, " + color + "55, 8, 0, 0, 2);";
    }
    }
    
    private static class ItineraryData{
        final String transport;
        final String transportId;
        final String departure;
        final String arrival;
        final String duration;
        final int prixVal;
        final int co2Val;
        int score;
        final int dureeMin;

        ItineraryData(String transport, String transportId, String dep, String arr, String dur, int prix, int co2) {
            this.transport = transport;
            this.transportId = transportId;
            this.departure = dep;
            this.arrival = arr;
            this.duration = dur;
            this.prixVal = prix;
            this.co2Val = co2;
            int m = 0;
            try {
                String[] parts = dur.split("h");
                m = Integer.parseInt(parts[0].trim()) * 60 + (parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0);
            }catch(Exception ignored){}
            this.dureeMin = m;
        }
    }
}