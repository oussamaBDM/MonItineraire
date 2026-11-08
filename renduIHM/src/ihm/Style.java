package ihm;
 
public class Style {
 
    public static final String BLEU_APP   = "#2B579A";
    public static final String BLEU_CLAIR = "#E8EEF4";
    public static final String FOND_GRIS  = "#f9f9f9";
 
    public static final String BLEU_NUIT    = "#1A2B4A";
    public static final String BLEU_VIVO    = "#4A90D9";
    public static final String BLEU_PALE    = "#EBF4FF";
    public static final String VERT         = "#2ECC71";
    public static final String AMBRE        = "#F5A623";
    public static final String ROUGE_CO2    = "#E74C3C";
    public static final String GRIS_TEXTE   = "#8A9BB0";
    public static final String BLANC        = "#FFFFFF";
 
    public static final String CARD_STYLE =
        "-fx-background-color: white;" +
        "-fx-background-radius: 10;" +
        "-fx-effect: dropshadow(gaussian, #00000018, 12, 0, 0, 3);";
 
    public static final String SECTION_TITLE_STYLE =
        "-fx-font-size: 13px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + BLEU_NUIT + ";" +
        "-fx-font-family: 'Segoe UI';";
 
    public static final String LABEL_MUTED_STYLE =
        "-fx-font-size: 11px;" +
        "-fx-text-fill: " + GRIS_TEXTE + ";" +
        "-fx-font-family: 'Segoe UI';";
}