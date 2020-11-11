package cz.vse.stepan.main;

import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.IGame;
import cz.vse.stepan.model.TextUI;

/**
 * Hlavní třída určená pro spuštění hry. Obsahuje pouze statickou metodu
 * {@linkplain #main(String[]) main}, která vytvoří instance logiky hry
 * a uživatelského rozhraní, propojí je a zahájí hru.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @version LS 2020
 */
public final class Start
{
    /**
     * Metoda pro spuštění celé aplikace.
     *
     * @param args parametry aplikace z příkazového řádku
     */
    public static void main(String[] args)
    {
        IGame game = new Game();
        TextUI textUI = new TextUI(game);
        
        if (args.length == 0) {
            textUI.play();
        } else {
            textUI.play(args[0]);
        }
    }

    private Start() {}

}
