package cz.vse.stepan;

import cz.vse.stepan.main.Start;
import cz.vse.stepan.model.*;
import javafx.event.ActionEvent;
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
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


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


    public void init(IGame game) {
        this.game = game;
        update();
        textOutput.setText(game.getPrologue() + "\n\n");

    }

    private void update () {
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

        if(game.isGameOver()){
        textOutput.appendText(game.getEpilogue());
        textInput.setVisible(false);
    }
    }

    public void turnHacksOn(ActionEvent actionEvent) {
        if (off.isSelected()){
            off.setSelected(false);
        }
        hackBtn.setFill(Color.rgb(0,255,0));
        winnable.setVisible(true);
    }

    public void turnHacksOff(ActionEvent actionEvent) {
        if (on.isSelected()){
            on.setSelected(false);
        }
        hackBtn.setFill(Color.rgb(255,0,0));
        winnable.setVisible(false);
    }


    private void updateMap(){
        String location = getCurrentArea().getName();
        InputStream stream;
        if (!game.getGamePlan().getMainCharacter().isWithArthur()) {
            stream = getClass().getClassLoader().getResourceAsStream(location + ".jpg");
        } else {
            stream = getClass().getClassLoader().getResourceAsStream(location + "-a.jpg");
        }
        Image img = new Image(stream);
        map.setImage(img);
    }

    private void updateBackground(){
        InputStream str;
        String location = getCurrentArea().getName();
        if(imageMid.getFitWidth()==1920) {
            str = getClass().getClassLoader().getResourceAsStream(location + "3.jpg");
        }
        else if (imageMid.getFitWidth()==1600) {
            str = getClass().getClassLoader().getResourceAsStream(location + "2.jpg");
        }else{
            str = getClass().getClassLoader().getResourceAsStream(location + "1.jpg");
        }
        Image img = new Image(str);
        imageMid.setImage(img);
    }

    private void openSettings(){
        settings.setCursor(Cursor.HAND);
        if(!settingsMenu.isVisible()) {
            settings.setOnMouseClicked(event -> {
            settingsMenu.setVisible(true);
            update();
            });}
        if (settingsMenu.isVisible()){
            btnNo.setOnAction(event -> {
                settingsMenu.setVisible(false);
            update();
            });

            btnYes.setOnAction(event1 -> {
                System.exit(0);
            });
        }

    }

    private void updateItems() {
        Collection<Item> itemList = getCurrentArea().getItemList().values();
        items.getChildren().clear();

        for (Item item : itemList){
            String itemName = item.getName();
            Label itemLabel = new Label(itemName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(itemName + ".jpg");
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            itemLabel.setGraphic(imageView);

            if(itemName.equals(game.getGamePlan().VEHICLE)){
                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> {
                    executeCommand("rid " + itemName);
                })
                ;
            }

            else if(item.isMoveable()) {
                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> {
                    executeCommand("vezmi " + itemName);
                        })
                        ;

            } else {
                itemLabel.setTooltip(new Tooltip("Tato věc je nepřenositelná"));
            }

            items.getChildren().add(itemLabel);
        }
    }

    private void updateExits() {
        Collection<Area> exitList = getCurrentArea().getAreaList();
        exits.getChildren().clear();

        for (Area area : exitList) {
            String exitName = area.getName();
            Label exitLabel = new Label(exitName);
            exitLabel.setTooltip(new Tooltip(area.getDescription()));

            InputStream stream = getClass().getClassLoader().getResourceAsStream(exitName + "1.jpg");
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(50);
            imageView.setFitHeight(30);
            exitLabel.setGraphic(imageView);

            exitLabel.setCursor(Cursor.HAND);
            exitLabel.setOnMouseClicked(event -> {
                executeCommand("jdi "+exitName);
            });

            exits.getChildren().add(exitLabel);
        }
    }

    private void updatePeople() {
        Collection<Person> peopleList = getCurrentArea().getPeopleList().values();
        people.getChildren().clear();

        for (Person person : peopleList){
            String personName = person.getName();
            Label personLabel = new Label(personName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(personName + ".png");
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            personLabel.setGraphic(imageView);


            personLabel.setCursor(Cursor.HAND);
            personLabel.setOnMouseClicked(event -> {
                 executeCommand("promluv " + personName);
            });

            people.getChildren().add(personLabel);
        }
    }

    private void updateInventory() {
        Collection<Item> itemList = game.getGamePlan().getInventory().getItemsInventory().values();
        inventory.getChildren().clear();

        for (Item item : itemList){
            String itemName = item.getName();
            Label itemLabel = new Label(itemName);
            InputStream stream = getClass().getClassLoader().getResourceAsStream(itemName + ".jpg");
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            itemLabel.setGraphic(imageView);

                itemLabel.setCursor(Cursor.HAND);
                itemLabel.setOnMouseClicked(event -> {
                    executeCommand("poloz " + itemName);
                })
                ;


            inventory.getChildren().add(itemLabel);
        }


    }

    private Area getCurrentArea() {return game.getGamePlan().getCurrentArea();}

    private void executeCommand(String command) {
        String result = game.processCommand(command);
        textOutput.appendText(result + "\n\n");
        update();
    }

    public void onInputKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            executeCommand(textInput.getText());
            textInput.setText("");
        }
    }

    public void getHint(ActionEvent actionEvent) {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://online.fliphtml5.com/vwlig/efia/#p=1");
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set1920(ActionEvent actionEvent) throws IOException {
        executeCommand("konec\n\n");

        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Adventura");

        FXMLLoader loader = new FXMLLoader();
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene3.fxml");
        Parent root = loader.load(str);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        IGame hra = new Game();
        controller.init(hra);
        primaryStage.show();


    }

    public void set1600(ActionEvent actionEvent) throws IOException {
        executeCommand("konec\n\n");
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Adventura");

        FXMLLoader loader = new FXMLLoader();
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene2.fxml");
        Parent root = loader.load(str);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        IGame hra = new Game();
        controller.init(hra);
        primaryStage.show();
    }

    public void set1280(ActionEvent actionEvent) throws IOException {
        executeCommand("konec\n\n");
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Adventura");

        FXMLLoader loader = new FXMLLoader();
        InputStream str = getClass().getClassLoader().getResourceAsStream("scene.fxml");
        Parent root = loader.load(str);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        IGame hra = new Game();
        controller.init(hra);
        primaryStage.show();
    }

    public void winGame(ActionEvent actionEvent) {
        while (!getCurrentArea().getName().equals(GamePlan.LOZNICE)){
        String location = getCurrentArea().getName();
        switch (location) {
           case GamePlan.TOVARNA:
               executeCommand("jdi " + GamePlan.VRATNICE);
               break;
           case GamePlan.VRATNICE:
               executeCommand("jdi " + GamePlan.GARAZ);
               break;
           case GamePlan.GARAZ:
               executeCommand("jdi " + GamePlan.CHODBA);
               break;
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
               executeCommand("jdi " + GamePlan.LOZNICE);
               break;
           case GamePlan.OBYVAK:
               executeCommand("jdi " + GamePlan.LOZNICE);
               break;
        }
        }
        //dropItems();
        letsWin();

    }

    public void letsWin(){
        try {
            BufferedReader read = new BufferedReader(new FileReader("vyhra.txt"));
            String line = read.readLine();
            while (line != null){
                executeCommand(line);
                line = read.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void dropItems(){
        Collection<Item> itemList = game.getGamePlan().getInventory().getItemsInventory().values();

            for (Item item : itemList) {
                String itemName = item.getName();
                switch (itemName) {
                    default:
                        executeCommand("poloz " + itemName);
                        break;
                    case GamePlan.ZBRAN:
                        break;
                    case GamePlan.KLICE:
                        break;
                }
                break;
            }
        }


    public void dropItems2(){
        Set<String> itemList = game.getGamePlan().getInventory().getItemsInventory().keySet();
        Set<String> dropList = null;
        for (String item : itemList) {
            if (!item.equals(GamePlan.ZBRAN) && !item.equals(GamePlan.KLICE)){
                dropList.add(item);
            }
        }

        for (String item : dropList){
            executeCommand("poloz " +item);
        }

    }

    public void dropItems3(){
        Set<String> itemList = game.getGamePlan().getInventory().getItemsInventory().keySet();
        boolean a = itemList.size()==1 && itemList.contains(GamePlan.ZBRAN);
        boolean b = itemList.size()==1 && itemList.contains(GamePlan.KLICE);
        boolean c = itemList.size()==2 && itemList.contains(GamePlan.KLICE) && itemList.contains(GamePlan.ZBRAN);

        while(!itemList.isEmpty() && !a && !b && !c){
            executeCommand("poloz " );
        }

    }


}


