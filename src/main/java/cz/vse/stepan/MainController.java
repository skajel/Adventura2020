package cz.vse.stepan;

import cz.vse.stepan.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.Collection;

/**
 * Třída představuje ovládání GUI verze adventury.
 *
 * @author Ondřej Štěpán
 * @version ZS 2020
 */

public class MainController {

    public TextArea textOutput;
    public TextField textInput;
    private IGame game;
    public Label locationName;
    public Label locationDescription;
    public VBox exits;
    public VBox items;
    public VBox people;
    public VBox inventory;
    public ImageView imageTop;
    public ImageView imageMid;
    public ImageView imageBot;
    public ImageView settings;
    public StackPane settingsMenu;
    public Button btnNo;
    public Button btnYes;
    public ImageView map;
    public Circle hackBtn;
    public CheckMenuItem on;
    public CheckMenuItem off;
    public MenuItem winnable;
    private static int running = 0;
    private static Stage stage;

    /**
     * Metoda vytváří samotnoou GUI hru s výchozím textem.
     */
    public void init(IGame game) {
        this.game = game;
        update();
        textOutput.appendText(game.getPrologue() + "\n\n");

    }

    /**
     * Metoda aktualizuje vizuální stránku hry pomocí odkazů na jednotlivé akttualizace.
     * Pokud hra je ukončena, nastaví závěrečný text. Hráč dále nemůže psát jednotlivé příkazy.
     */
    private void update() {
        String location = getCurrentArea().getName();
        locationName.setText(location.toUpperCase());

        String description = getCurrentArea().getDescription();
        locationDescription.setText(description);

        updateExits();
        updateItems();
        updateInventory();
        updatePeople();
        updateBackground();
        openSettings();
        updateMap();

        if (game.isGameOver()) {
            textOutput.appendText(game.getEpilogue());
            textInput.setVisible(false);
        }
    }

    /**
     * Metoda v GUI v nastavení <i>hacking</i> se přepne na <i>zapnuto</i> a obarví adekvátně kruh <i>zelenou</i>, který je vedle menu.
     */
    public void turnHacksOn() {
        if (off.isSelected()) {
            off.setSelected(false);
        }
        hackBtn.setFill(Color.rgb(0, 255, 0));
        winnable.setVisible(true);
    }

    /**
     * Metoda v GUI v nastavení <i>hacking</i> se přepne na <i>vypnuto</i> a obarví adekvátně kruh <i>červenou</i>, který je vedle menu.
     */
    public void turnHacksOff() {
        if (on.isSelected()) {
            on.setSelected(false);
        }
        hackBtn.setFill(Color.rgb(255, 0, 0));
        winnable.setVisible(false);
    }

    /**
     * Metoda v GUI aktualizuje pozici hlavní postavy a Arthura na mapě.
     */
    private void updateMap() {
        String location = getCurrentArea().getName();
        InputStream stream;
        if (!game.getGamePlan().getMainCharacter().isWithArthur()) {
            stream = getClass().getClassLoader().getResourceAsStream(location + ".jpg");
        } else {
            stream = getClass().getClassLoader().getResourceAsStream(location + "-a.jpg");
        }
        assert stream != null;
        Image img = new Image(stream);
        map.setImage(img);
    }

    /**
     * Metoda v GUI aktualizuje obrázek jako pozadí podle velikosti rozlišení hry.
     */
    private void updateBackground() {
        InputStream str;
        String location = getCurrentArea().getName();
        if (imageMid.getFitWidth() == 1920) {
            str = getClass().getClassLoader().getResourceAsStream(location + "3.jpg");
        } else if (imageMid.getFitWidth() == 1600) {
            str = getClass().getClassLoader().getResourceAsStream(location + "2.jpg");
        } else {
            str = getClass().getClassLoader().getResourceAsStream(location + "1.jpg");
        }
        assert str != null;
        Image img = new Image(str);
        imageMid.setImage(img);
    }

    /**
     * Metoda v GUI otevře nastavení hry, kde se ukončuje hra.
     */
    private void openSettings() {
        settings.setCursor(Cursor.HAND);
        if (!settingsMenu.isVisible()) {
            settings.setOnMouseClicked(event -> {
                settingsMenu.setVisible(true);
                update();
            });
        }
        if (settingsMenu.isVisible()) {
            btnNo.setOnAction(event -> {
                settingsMenu.setVisible(false);
                update();
            });

            btnYes.setOnAction(event1 -> System.exit(0));
        }

    }

    /**
     * Metoda v GUI aktualizuje předměty třídy {@link Item}.
     * Pokud je předměty nepřenositelný, při přejetí myší se tento fakt vypíše.
     */
    private void updateItems() {
        Collection<Item> itemList = getCurrentArea().getItemList().values();
        items.getChildren().clear();

        for (Item item : itemList) {
            String itemName = item.getName();
            Label itemLabel = new Label(itemName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(itemName + ".jpg");
            assert stream != null;
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            itemLabel.setGraphic(imageView);

            game.getGamePlan();
            if (itemName.equals(GamePlan.VEHICLE)) {
                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> executeCommand("rid " + itemName))
                ;
            } else if (item.isMoveable()) {
                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> executeCommand("vezmi " + itemName))
                ;

            } else {
                itemLabel.setTooltip(new Tooltip("Tato věc je nepřenositelná"));
            }

            items.getChildren().add(itemLabel);
        }
    }

    /**
     * Metoda v GUI aktualizuje východy třídy {@link Area}.
     */
    private void updateExits() {
        Collection<Area> exitList = getCurrentArea().getAreaList();
        exits.getChildren().clear();

        for (Area area : exitList) {
            String exitName = area.getName();
            Label exitLabel = new Label(exitName);
            exitLabel.setTooltip(new Tooltip(area.getDescription()));

            InputStream stream = getClass().getClassLoader().getResourceAsStream(exitName + "3.jpg");
            assert stream != null;
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(50);
            imageView.setFitHeight(30);
            exitLabel.setGraphic(imageView);

            exitLabel.setCursor(Cursor.HAND);
            exitLabel.setOnMouseClicked(event -> executeCommand("jdi " + exitName));

            exits.getChildren().add(exitLabel);
        }
    }

    /**
     * Metoda v GUI aktualizuje osoby třídy {@link Person}.
     */
    private void updatePeople() {
        Collection<Person> peopleList = getCurrentArea().getPeopleList().values();
        people.getChildren().clear();

        for (Person person : peopleList) {
            String personName = person.getName();
            Label personLabel = new Label(personName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(personName + ".png");
            assert stream != null;
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            personLabel.setGraphic(imageView);


            personLabel.setCursor(Cursor.HAND);
            personLabel.setOnMouseClicked(event -> executeCommand("promluv " + personName));

            people.getChildren().add(personLabel);
        }
    }

    /**
     * Metoda v GUI aktualizuje předměty třídy {@link Item} v inventáři třídy {@link Inventory}.
     * Jestliže je konec hry, předměty se zamrazí.
     */
    private void updateInventory() {
        Collection<Item> itemList = game.getGamePlan().getInventory().getItemsInventory().values();
        inventory.getChildren().clear();

        for (Item item : itemList) {
            String itemName = item.getName();
            Label itemLabel = new Label(itemName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(itemName + ".jpg");
            assert stream != null;
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            itemLabel.setGraphic(imageView);

            if (!getCurrentArea().getName().equals(GamePlan.TOVARNA)){
            itemLabel.setCursor(Cursor.HAND);
            itemLabel.setOnMouseClicked(event -> executeCommand("poloz " + itemName))
            ;}


            inventory.getChildren().add(itemLabel);
        }


    }

    /**
     * Metoda vrací odkaz na lokaci <i>(objekt třídy {@link Area})</i>.
     *
     * @return lokaci, na kterou získáme odkaz
     */
    private Area getCurrentArea() {
        return game.getGamePlan().getCurrentArea();
    }

    /**
     * Metoda provede příkaz z rozhraní {@link ICommand}
     *
     * @param command příkaz, který chceme provést
     */
    private void executeCommand(String command) {
        String result = game.processCommand(command);
        textOutput.appendText(result + "\n\n");
        update();
    }

    /**
     * Metoda v GUI odesílá jednotlivé textové příkazy k vykonání.
     *
     * @param keyEvent zmáčknutí klávesy, která má provést metodu
     */
    public void onInputKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            executeCommand(textInput.getText());
            textInput.setText("");
        }
    }

    /**
     * Metoda otevře URL stránku (nápovědu k textové adventuře) v prohlížeči.
     */
    public void getHint() {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://online.fliphtml5.com/vwlig/efia/#p=1");
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda ukončí původní a spustí novou hru v novém okně v rozlišení <i>1920x1080</i>.
     */
    public void set1920() throws IOException {
        executeCommand("konec\n\n");
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene3.fxml");
        makeWindow(str);
    }

    /**
     * Metoda ukončí původní a spustí novou hru v novém okně v rozlišení <i>1600x900</i>.
     */
    public void set1600() throws IOException {
        executeCommand("konec\n\n");
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene2.fxml");
        makeWindow(str);
    }

    /**
     * Metoda ukončí původní a spustí novou hru v novém okně v rozlišení <i>1280x720</i>.
     */
    public void set1280() throws IOException {
        executeCommand("konec\n\n");
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene1.fxml");
        makeWindow(str);
    }

    /**
     * Metoda zavře původní hru a vytvoří novou. Výsledkem metody je otevření GUI.
     *
     * @param str InputStream, který obsahuje danou scénu ve formátu fxml
     */
    public static void makeWindow(InputStream str) throws IOException {
        if (running == 1) {
            stage.close();
        }
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Nebezpečí v továrně");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(str);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        IGame hra = new Game();
        controller.init(hra);
        primaryStage.show();
        running = 1;
        stage = primaryStage;
    }

    /**
     * Metoda vrátí hlavní postavu třídy {@link MainCharacter} do výchozí lokace třídy {@link Area}
     */
    public void winGame() {
        while (!getCurrentArea().getName().equals(GamePlan.LOZNICE)) {
            String location = getCurrentArea().getName();
            switch (location) {
                case GamePlan.TOVARNA:
                    executeCommand("jdi " + GamePlan.VRATNICE);
                    break;
                case GamePlan.VRATNICE:
                    executeCommand("jdi " + GamePlan.GARAZ);
                    break;
                case GamePlan.GARAZ:
                case GamePlan.SKLEP:
                    executeCommand("jdi " + GamePlan.CHODBA);
                    break;
                case GamePlan.CHODBA:
                    executeCommand("jdi " + GamePlan.PRACOVNA);
                    break;
                case GamePlan.KUCHYNE:
                    executeCommand("jdi " + GamePlan.OBYVAK);
                    break;
                case GamePlan.PRACOVNA:
                case GamePlan.OBYVAK:
                    executeCommand("jdi " + GamePlan.LOZNICE);
                    break;
            }
        }
        dropItems();
        letsWin();

    }

    /**
     * Metoda přečte ze souboru příkazy, které se provedou, aby hlavní postava vyhrála hru.
     */
    public void letsWin() {
        try {
            BufferedReader read = new BufferedReader(new FileReader("vyhra.txt"));
            String line = read.readLine();
            while (line != null) {
                executeCommand(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metoda položí první objekt třídy {@link Item} z inventáře do lokace třídy {@link Area}
     */
    public void dropItems() {
        Collection<Item> itemList = game.getGamePlan().getInventory().getItemsInventory().values();

        for (Item item : itemList) {
            String itemName = item.getName();
            switch (itemName) {
                default:
                    executeCommand("poloz " + itemName);
                    break;
                case GamePlan.ZBRAN:
                case GamePlan.KLICE:
                    break;
            }
            break;
        }
    }

}


