package cz.vse.stepan.main;

import cz.vse.stepan.MainController;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.IGame;
import cz.vse.stepan.textUI.TextUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Hlavní třída určená pro spuštění hry. Obsahuje pouze statickou metodu
 * {@linkplain #main(String[]) main}, která vytvoří instance logiky hry
 * a uživatelského rozhraní, propojí je a zahájí hru.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @version LS 2020
 */
public class Start extends Application
{
    /**
     * Metoda pro spuštění celé aplikace.
     *
     * @param args parametry aplikace z příkazového řádku
     */
    public static void main(String[] args)
    {
        List<String> vstup = Arrays.asList(args);

        if(vstup.contains("text")) {

            IGame game = new Game();
            TextUI textUI = new TextUI(game);
            textUI.play();

        } else {
            launch();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Adventura");

        FXMLLoader loader = new FXMLLoader();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("scene.fxml");
        Parent root = loader.load(stream);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        MainController controller = loader.getController();
        IGame hra = new Game();
        controller.init(hra);
        primaryStage.show();


}}
