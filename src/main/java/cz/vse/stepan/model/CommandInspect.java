package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro prozkoumání předmětu.
 * 
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandInspect implements ICommand
{
    private static final String NAME = "prozkoumej";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandInspect(GamePlan plan)
    {
        this.plan = plan;
    }

    /**
     * Metoda se pokusí prozkoumat předmět hráčem. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme předmět, který má prozkoumat)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce prozkoumat více předmětů
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je předmětem <i>(objektem třídy {@link Item})</i>.
     * Poté zkontroluje, zda je předmět v lokaci či inventáři.
     * Pokud předmět můžeme sebrat a dát ho i do inventáře. Vrátí se informace o předmětu
     * i s váhou. V opačném případě bez váhy.
     * 
     * @param parameters parametry příkazu
     * @return výsledek zpracování <i>(informace pro hráče, které se vypíšou na konzoli)</i>
     */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nevím, co mám prozkoumat, musíš zadat název předmětu.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím prozkoumat více předmětů současně.";
        }

        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();
        Item item = plan.getItem(itemName);

        if (!(item instanceof Item)){
            return "Je tohle vůbec předmět?!";
        }

        if (!area.containsItem(itemName) && !inventory.containsItem(itemName))  {
            return "Předmět '" + itemName + "' nikde nevidím.";
        }

        if (item.isMoveable() && item.isTakeable()){

            if (inventory.containsItem(itemName)) {
                return inventory.getItem(itemName).getDescription() + "\n"
                + "Předmět váží: "+ item.getWeight() + " kg.";
            }

            if (area.containsItem(itemName)) {
                return area.getItem(itemName).getDescription() + "\n"
                + "Předmět váží: "+ item.getWeight() + " kg.";
            }
        }

        return area.getItem(itemName).getDescription();
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
