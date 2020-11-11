package cz.vse.stepan.model;

/**
 * Hlavní třída logiky aplikace. Třída vytváří instanci třídy {@link GamePlan},
 * která inicializuje lokace hry, a vytváří seznam platných příkazů a instance
 * tříd provádějících jednotlivé příkazy.
 *
 * Během hry třída vypisuje uvítací a ukončovací texty a vyhodnocuje jednotlivé
 * příkazy zadané uživatelem.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class Game implements IGame
{
    private ListOfCommands listOfCommands;
    private GamePlan gamePlan;
    private boolean gameOver;

    /**
     * Konstruktor třídy. Vytvoří hru, inicializuje herní plán udržující
     * aktuální stav hry a seznam platných příkazů.
     */
    public Game()
    {
        gameOver = false;
        gamePlan = new GamePlan();
        listOfCommands = new ListOfCommands();

        listOfCommands.addCommand(new CommandHelp(listOfCommands, gamePlan));
        listOfCommands.addCommand(new CommandTerminate(this));
        listOfCommands.addCommand(new CommandMove(gamePlan, this));
        listOfCommands.addCommand(new CommandPick(gamePlan));
        listOfCommands.addCommand(new CommandInspect(gamePlan));
        listOfCommands.addCommand(new CommandSteer(gamePlan));
        listOfCommands.addCommand(new CommandInventory(gamePlan));
        listOfCommands.addCommand(new CommandTalk(gamePlan));
        listOfCommands.addCommand(new CommandPut(gamePlan));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrologue()
    {
        return "Vítejte v textové adventuře 'Nebezpečí v továrně!'\n"
        + "Píše se rok 1933. Nacházíš se ve svém bytě na ulici James St. ve městě Birmingham.\n"
        + "Zrovna ses chystal ke spánku, když vrátný naléhavě volá, že se spustil alarm v jedné z tvých továren.\n"
        + "Je to planý poplach, nebo se jedná o zloděje?! Prověř celou situaci. \n\n"
        + "Napiš 'napoveda', pokud si nevíš rady, jak hrát dál.\n"
        + "\n"
        + gamePlan.getCurrentArea().getFullDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEpilogue()
    {
        String epilogue = "Díky, že sis zahrál.";

        if (gamePlan.isVictorious()) {
            epilogue = "Narazil jsi na zloděje. Sice se snažil utéct, ale tobě se podařilo zloděje zastavit výstřelem ze zbraně. \n"
            + "ZVÍTĚZIL JSI!\n" + epilogue;
        }
        else {
            epilogue = "Narazil jsi na zloděje, který byl ozbrojený, ty však nemáš zbraň, kterou ho můžeš zabít.\n"
            + "PROHRÁL JSI, ZKUS HRU ZNOVA!\n"+ epilogue;
        }

        return epilogue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver()
    {
        return gameOver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String processCommand(String line)
    {
        String[] words = line.split("[ \t]+");

        String cmdName = words[0];
        String[] cmdParameters = new String[words.length - 1];

        for (int i = 0; i < cmdParameters.length; i++) {
            cmdParameters[i] = words[i + 1];
        }

        String result = null;
        if (listOfCommands.checkCommand(cmdName)) {
            ICommand command = listOfCommands.getCommand(cmdName);
            result = command.process(cmdParameters);
        } else {
            result = "Nechápu, co po mně chceš. Tento příkaz neznám.";
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GamePlan getGamePlan()
    {
        return gamePlan;
    }

    /**
     * Metoda nastaví příznak indikující, že nastal konec hry. Metodu
     * využívá třída {@link CommandTerminate}, mohou ji ale použít
     * i další implementace rozhraní {@link ICommand}.
     *
     * @param gameOver příznak indikující, zda hra již skončila
     */
    void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }

}
