package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro položení předmětu.
 * 
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandPut implements ICommand
{
    private static final String NAME = "poloz";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandPut(GamePlan plan)
    {
        this.plan = plan;
    }

    /**
     * Metoda se pokusí, aby hráč vzal předmět. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme předmět, který má položit)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce položit více předmětů
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je předmětem <i>(objektem třídy {@link Item})</i>.
     * Poté zkontroluje, zda je předmět v inventáři.
     * Pokud ano, předmět se položí do aktuální lokace.
     * 
     * @param parameters parametry příkazu
     * @return výsledek zpracování <i>(informace pro hráče, které se vypíšou na konzoli)</i>
     */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nevím, co mám položit, musíš zadat název předmětu.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím položit více předmětů současně.";
        }

        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();
        Item item = plan.getItem(itemName);

        if (!(item instanceof Item)){
            return "Je tohle vůbec předmět?!";
        }        

        if (!inventory.containsItem(itemName)) {
            return "Předmět '" + itemName + "' nelze položit, protože ho nemám v inventáři.";
        }

        inventory.removeItem(itemName);
        area.addItem(item);

        return "Předmět '" + itemName + "' jsi položil do lokace (místnosti) '" + area.getName() + "'.";
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
