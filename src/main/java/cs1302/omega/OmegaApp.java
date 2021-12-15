package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.net.URLEncoder;
import java.net.URL;
import java.io.InputStreamReader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Region;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import static cs1302.api.Tools.get;
import static cs1302.api.Tools.getJson;
import static cs1302.api.Tools.UTF8;
import static java.net.URLEncoder.encode;
import java.io.IOException;
import java.util.Optional;
import java.nio.charset.Charset;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import com.google.gson.Gson;
import javafx.util.Pair;

/**
 * This is the class for my PokeDex app. It can provide information on any pokemon
 * and it can also provide information on all cards associated with the pokemon that
 * is searched.
 */
public class OmegaApp extends Application {

    VBox vbox;



    //menu
    HBox menu;
    public Button exit;

    //tool bar
    HBox toolBar;
    Label searchQ;
    TextField searchField;
    public Button update;

    //PokeInfo
    HBox pokeInfo;
    HBox pokeLabels;
    TextArea pokeStats;
    TextArea pokeBooks;
    Label stats;
    Label books;

    private Gson gson = new Gson();
    private Gson gson2 = new Gson();
    InputStreamReader reader1;

    private String sUrl;
    private String sUrl2;
    private URL cUrl;
    private URL pUrl;
    private InputStreamReader reader;
    private String term;

    //JsonElement je = JsonParser.parseReader(reader1);
    //JsonObject root = je.getAsJsonObject();

    //String pokeAbs = "";

    //apikey
    final String apiKey1 = "988de148-516b-4a9f-93e3-5cd8094e6051";

    //Pairs
    Pair<String, String> apiHost = new Pair<>("X-Api-Host", "REPLACE");
    Pair<String, String> apiKey = new Pair<>("X-Api-Key", "REPLACE");
    //JsonElement rootCards = Tools.getJson(url, "GET", apiHost, apiKey);

    private static final int DEF_WIDTH = 1280;
    private static final int DEF_HEIGHT = 720;

    private String pokeEnd = "https://pokeapi.co/api/v2/pokemon/";
    private String pokeCardEnd = "https://api.pokemontcg.io/v2/cards?api_key=" + apiKey1;

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        vbox = new VBox();
        //exit
        menu = new HBox(10);
        exit = new Button("Exit");
        menu.getChildren().add(exit);
        //search bar
        toolBar = new HBox(10);
        searchQ = new Label("Search Pokemon:");
        HBox.setHgrow(searchQ, Priority.ALWAYS);
        searchField = new TextField();
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setMaxWidth(Double.MAX_VALUE);
        update = new Button("Search");
        HBox.setHgrow(update, Priority.ALWAYS);
        update.setMaxWidth(Double.MAX_VALUE);
        toolBar.getChildren().addAll(searchQ,searchField,update);
        pokeInfo = new HBox(10);
        pokeLabels = new HBox(100);
        //stats
        stats = new Label("Stats:");
        HBox.setHgrow(stats, Priority.ALWAYS);
        stats.setMaxWidth(Double.MAX_VALUE);
        pokeStats = new TextArea();
        pokeStats.setWrapText(true);
        HBox.setHgrow(pokeStats, Priority.ALWAYS);
        //cards
        books = new Label("Pokemon Card Information:");
        HBox.setHgrow(books, Priority.ALWAYS);
        books.setMaxWidth(Double.MAX_VALUE);
        pokeBooks = new TextArea();
        pokeBooks.setWrapText(true);
        HBox.setHgrow(pokeBooks, Priority.ALWAYS);
        pokeInfo.getChildren().addAll(pokeStats,pokeBooks);
        pokeLabels.getChildren().addAll(stats,books);
        vbox.getChildren().addAll(menu,toolBar,pokeLabels,pokeInfo);
        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getSource() == exit) {
                    System.exit(0);
                }
                if (e.getSource() == update) {
                    term = searchField.getText();
                    term = term.toLowerCase();
                    sUrl = pokeEnd + term + "/";
                    try {
                        pUrl = new URL(sUrl);
                        try {
                            reader1 = new InputStreamReader(pUrl.openStream());
                        } catch (IOException iOe1) {
                            System.err.println("IO Exception");
                        }
                    } catch (MalformedURLException m) {
                        System.err.println("Malformed URL");
                    }
                    getStats();
                    getCards();
                }
            }
        };
        exit.setOnMouseClicked(handler);
        update.setOnMouseClicked(handler);
        Scene scene = new Scene(vbox);
        stage.setMaxWidth(DEF_WIDTH);
        stage.setMaxHeight(DEF_HEIGHT);
        stage.setTitle("PokeDex!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * This method gets the Card Info.
     */
    public void getCards() {
        sUrl2 = pokeCardEnd + "&q=name:" + term;
        try {
            cUrl = new URL(sUrl2);
            try {
                reader = new InputStreamReader(cUrl.openStream());
            } catch (IOException iOe2) {
                System.err.println("IO Exception");
            }
        } catch (MalformedURLException m1) {
            System.err.println("Malformed URL");
        }

        String pokeCards = "Cards:\n";

        JsonElement je1 = JsonParser.parseReader(reader);
        //JsonElement rootCards = Tools.getJson(url, "GET", apiHost, apiKey);
        JsonObject rootCard = je1.getAsJsonObject();


        //Data
        JsonArray data  = rootCard.getAsJsonArray("data");
        int numData = data.size();
        for (int k = 0; k < numData; k++) {
            pokeCards += (k + 1) + ".\n\t";
            //id
            JsonObject id = data.get(k).getAsJsonObject();
            JsonElement cardId = id.get("id");
            String cIdentity = gson2.fromJson(cardId,String.class);
            pokeCards += "Card Id:\n\t\t" + cIdentity + "\n\t";
            //cardName
            JsonObject cName = data.get(k).getAsJsonObject();
            JsonElement cardN = cName.get("name");
            String cardName = gson2.fromJson(cardN,String.class);
            pokeCards += "Card Name:\n\t\t" + cardName + "\n\t";
            //hp
            JsonObject health = data.get(k).getAsJsonObject();
            JsonElement healthP = health.get("hp");
            String hp = gson2.fromJson(healthP,String.class);
            pokeCards += "HP:\n\t\t" + hp + "\n\t";
            //rarity
            JsonObject rare = data.get(k).getAsJsonObject();
            JsonElement rareC = rare.get("rarity");
            String rarity = gson2.fromJson(rareC,String.class);
            pokeCards += "Rarity:\n\t\t" + rarity + "\n\t";
            //level
            JsonObject lvl = data.get(k).getAsJsonObject();
            JsonElement lvlC = lvl.get("level");
            String level = gson2.fromJson(lvlC,String.class);
            pokeCards += "Level:\n\t\t" + level + "\n";
        }


        pokeBooks.setText(pokeCards);
        //System.out.println(cIdentity);

    }

    /**
     * This method gets the pokemon stats.
     */
    public void getStats() {
        JsonElement je = JsonParser.parseReader(reader1);
        JsonObject root = je.getAsJsonObject();
        //abilities
        JsonArray abilities = root.getAsJsonArray("abilities");
        int numAbilities = abilities.size();
        String[] pAbilities = new String[numAbilities];
        for (int i = 0; i < numAbilities; i++) {
            JsonObject ability = abilities.get(i).getAsJsonObject();
            JsonObject abName = ability.getAsJsonObject("ability");
            JsonElement name = abName.get("name");
            String abilityName = gson.fromJson(name,String.class);
            pAbilities[i] = (i + 1) + ". " + abilityName;
        }
        String pokeAbs = "Abilities:\n\t";
        for (int j = 0; j < numAbilities; j++) {
            if (j < numAbilities - 1) {
                pokeAbs += (pAbilities[j] + "\n\t");
            } else {
                pokeAbs += (pAbilities[j] + "\n");
            }
        }
        //getAbilities();
        //height
        JsonElement height = root.get("height");
        int pokHeight = height.getAsInt();
        pokeAbs += "Height:\n\t" + pokHeight + " decimeters\n";
        //weight
        JsonElement weight = root.get("weight");
        int pokWeight = weight.getAsInt();
        pokeAbs += "Weight:\n\t" + pokWeight + " hectograms\n";
        //types
        JsonArray types = root.getAsJsonArray("types");
        int numTypes = types.size();
        String[] pTypes = new String[numTypes];
        for (int l = 0; l < numTypes; l++) {
            JsonObject type = types.get(l).getAsJsonObject();
            JsonObject typeName = type.getAsJsonObject("type");
            JsonElement tName = typeName.get("name");
            String typeNames = gson.fromJson(tName,String.class);
            pTypes[l] = (l + 1) + ". " + typeNames;
        }
        pokeAbs += "Types:\n\t";
        for (int k = 0; k < numTypes; k++) {
            if (k < numTypes - 1) {
                pokeAbs += (pTypes[k] + "\n\t");
            } else {
                pokeAbs += (pTypes[k] + "\n");
            }
        }
        //moves
        JsonArray moves = root.getAsJsonArray("moves");
        int numMoves = moves.size();
        String[] pMoves = new String[numMoves];
        for (int m = 0; m < numMoves; m++) {
            JsonObject move = moves.get(m).getAsJsonObject();
            JsonObject moveName = move.getAsJsonObject("move");
            JsonElement mName = moveName.get("name");
            String moveNames = gson.fromJson(mName,String.class);
            pMoves[m] = (m + 1) + ". " + moveNames;
        }
        pokeAbs += "Moves:\n\t";
        for (int t = 0; t < numMoves; t++) {
            if (t < numMoves - 1) {
                pokeAbs += (pMoves[t] + "\n\t");
            } else {
                pokeAbs += (pMoves[t] + "\n");
            }
        }
        pokeStats.setText(pokeAbs);
    }

} // OmegaApp
