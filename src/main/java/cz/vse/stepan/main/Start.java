package cz.vse.stepan.main;

import cz.vse.stepan.MainController;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.IGame;
import cz.vse.stepan.textUI.TextUI;
import javafx.application.Application;
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
 * @author Ondřej Štěpán
 * @version ZS 2020
 */
public class Start extends Application {
    /**
     * Metoda pro spuštění celé aplikace.
     *
     * @param args parametry aplikace z příkazového řádku
     */
    public static void main(String[] args) {
        List<String> vstup = Arrays.asList(args);

        if (vstup.contains("text")) {

            IGame game = new Game();
            TextUI textUI = new TextUI(game);
            textUI.play();

        } else {
            launch();
        }
    }

    /**
     * Metoda přepisuje metodu start v třídě {@link Application} a spustí novou hru v GUI v defaultním rozlišení.
     *
     * @param primaryStage stage, která se otevře
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("scene.fxml");
        MainController.makeWindow(stream);
    }

}
