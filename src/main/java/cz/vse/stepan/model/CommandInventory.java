package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro zobrazení inventáře ke hře.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandInventory implements ICommand
{
    private static final String NAME = "inventar";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandInventory(GamePlan plan)
    {
        this.plan = plan;
    }

    /**
     * V případě, že je metoda zavolána bez parametrů <i>(hráč na konzoli zadá
     * pouze slovo {@value NAME})</i>, zobrazí inventář. Jinak vrátí chybovou zprávu
     * a hra pokračuje.
     *
     * @param parameters parametry příkazu <i>(očekává se prázdné pole)</i>
     * @return informace pro hráče, která se vypíše na konzoli
     */
    @Override
    public String process(String... parameters){
        if (parameters.length > 0) {
            return "Nechápu, jak mám otevřít" + NAME + ". Příkaz '" + NAME + "' se používá bez parametrů a zobrazí inventář.";
        }

        Inventory inventory = plan.getInventory();

        return inventory.getAllItems() + "\n"
        + "Zbývající kapacita inventáře je " + inventory.getRemainingSpace() + " kg z celkových "
        + inventory.getTotalLoad() + " kg.";
    }

    /**
     * Metoda vrací název příkazu tj.&nbsp; slovo {@value NAME}.
     *
     * @return název příkazu
     */
    @Override
    public String getName(){
        return NAME;
    }
}
