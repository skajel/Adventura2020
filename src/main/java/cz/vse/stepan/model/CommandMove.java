package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro pohyb mezi herními lokacemi.
 *
 * @author Jarmila Pavlíčková
 * @author Luboš Pavlíček
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandMove implements ICommand
{
    private static final String NAME = "jdi";
    private Game game;
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     * @param game odkaz na aktuální hru, kvůli nastavení konce hry
     */
    public CommandMove(GamePlan plan, Game game)
    {
        this.plan = plan;
        this.game = game;
    }

    /**
     * Metoda se pokusí přesunout hráče do jiné lokace. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme cíl cesty)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce jít na více míst
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je lokace <i>(objektem třídy {@link Area})</i> a jestli
     * s aktuální lokací sousedí jiná lokace s daným názvem <i>(tj.
     * zda z aktuální lokace lze jít do požadovaného cíle)</i>. 
     * Při přechodu z chodby do garáže nebo sklepa zkontroluje, zda je hráč žíznivý.
     * Pokud ano, vrátí chybové hlášení. Pokud všechny kontroly proběhnou v pořádku, 
     * provede přesun hráče do cílové lokace a vrátí její popis.
     * Pokud je cílová lokace továrna, hra je ukončena.
     *
     * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
     * @return informace pro hráče, které se vypíšou na konzoli
     */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nechápu, kam mám jít. Musíš mi zadat nějaký cíl.";  // Pokud hráč nezadá žádný parametr (cíl cesty)
        } else if (parameters.length > 1) {
            return "Nechápu, co po mě chceš. Neumím se 'rozdvojit' a jít na více míst současně.";  // Pokud hráč zadá více parametrů
        }

        // Název cílové lokace si uložíme do pomocné proměnné
        String exitName = parameters[0];

        // Zkusíme získat odkaz na cílovou lokaci
        Area exitArea = plan.getCurrentArea().getExitArea(exitName);
        Area areas = plan.getArea(exitName);

        if (!(areas instanceof Area)){
            return "Je tohle vůbec lokace?!"; // Pokud hráč zadal něco jiného než lokaci
        }

        if (exitArea == null) {
            return "Tam se ale odsud jít nedá!";  // Pokud hráč zadal název lokace, která nesousedí s aktuální
        }

        Area area = plan.getCurrentArea();
        MainCharacter main = plan.getMainCharacter();

        if (area.getName().equals(plan.CHODBA) && (main.isThirsty())){
            if (exitName.equals(plan.GARAZ)){
                return "Nemůžeš přejít do místnosti (lokace) '" + plan.GARAZ + "', protože máš žízeň. \n"
                + "Musíš se vrátit zpět do '" + plan.KUCHYNE + "', aby ses napil.";
            }
            if (exitName.equals(plan.SKLEP)){
                return "Nemůžeš přejít do místnosti (lokace) '" + plan.SKLEP + "', protože máš žízeň. \n"
                + "Musíš se vrátit zpět do '" + plan.KUCHYNE + "', aby ses napil.";
            }
        }

        if (exitName.equals(plan.TOVARNA)){
            plan.setCurrentArea(exitArea);
            game.setGameOver(true);
            return plan.FINAL_SLOGAN;
        }

        // Změníme aktuální lokaci (přesuneme hráče) a vrátíme popis nové lokace
        plan.setCurrentArea(exitArea);
        return exitArea.getFullDescription();
    }

    /**
     * Metoda vrací název příkazu tj.&nbsp; slovo {@value NAME}.
     *
     * @return název příkazu
     */
    @Override
    public String getName()
    {
        return NAME;
    }

}
