package cz.vse.stepan;

import cz.vse.stepan.model.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.InputStream;
import java.util.Collection;


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
    public ChoiceBox resolution;

    public void init(IGame game){
        this.game = game;
        update();
        textOutput.setText(game.getPrologue() + "\n\n");

//        resolution.getItems().add("1280x720");
//        resolution.getItems().add("1600x900");
//        resolution.getItems().add("1920x1080");
//        resolution.setValue("1280x720");

    }

//    private void updateResolution(){
//        String location = getCurrentArea().getName();
//        int st[] = {720,900,1080};
//
//        resolution.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//            InputStream stream =  getClass().getClassLoader().getResourceAsStream(location + observable +".jpg");
//            Image img = new Image(stream);
//            imageMid.setFitWidth(st[newValue.intValue()]);
//            imageMid.setFitHeight(st[newValue.intValue()]-400);
//            imageMid.setImage(img);
//            imageTop.setFitWidth(st[newValue.intValue()]);
//            imageBot.setFitWidth(st[newValue.intValue()]);
//            update();
//            }
//        });
//    }

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
//        updateResolution();

        if(game.isGameOver()){
        textOutput.appendText(game.getEpilogue());
        textInput.setVisible(false);
    }

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
        String location = getCurrentArea().getName();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(location + "1.jpg");
        Image img = new Image(stream);
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
            exitLabel.setCursor(Cursor.HAND);
            exitLabel.setTooltip(new Tooltip(area.getDescription()));

            InputStream stream = getClass().getClassLoader().getResourceAsStream(exitName + "1.jpg");
            Image img = new Image(stream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(50);
            imageView.setFitHeight(30);
            exitLabel.setGraphic(imageView);

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

    public void startNewGame(ActionEvent actionEvent){
        init(new Game());
    }

    public void getHint(ActionEvent actionEvent) {
    }

}