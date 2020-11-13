package cz.vse.stepan;

import cz.vse.stepan.model.*;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

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
    public ImageView imageVw;

    public void init(IGame game){
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


        if(game.isGameOver()){
        textOutput.setText(game.getEpilogue());
    }
    }

    private void updateBackground(){
        String location = getCurrentArea().getName();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(location + "1.jpg");
        Image img = new Image(stream);
        imageVw.setImage(img);
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

//            InputStream stream = getClass().getClassLoader().getResourceAsStream(exitName + ".jpg");
//            Image img = new Image(stream);
//            ImageView imageView = new ImageView(img);
//            imageView.setFitWidth(60);
//            imageView.setFitHeight(40);
//            exitLabel.setGraphic(imageView);

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
//            InputStream stream = getClass().getClassLoader().getResourceAsStream(personName + ".jpg");
//            Image img = new Image(stream);
//            ImageView imageView = new ImageView(img);
//            imageView.setFitWidth(60);
//            imageView.setFitHeight(60);
//            itemLabel.setGraphic(imageView);


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
}
