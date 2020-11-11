package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro sebrání předmětu.
 * 
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandPick implements ICommand
{
    private static final String NAME = "vezmi";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandPick(GamePlan plan)
    {
        this.plan = plan;
    }

    /**
     * Metoda se pokusí, aby hráč vzal předmět. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme předmět, který má vzít)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce sebrat více předmětů
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je předmětem <i>(objektem třídy {@link Item})</i>.
     * Poté zkontroluje, zda je předmět v aktuální lokaci, jestli je přenositelný a jestli jej
     * můžeme vložit do inventáře.
     * Dále ověří, zda je v inventáři místo a přidá předmět do inventáře, případně je jej jen sebere 
     * <i>(jestliže není možné jej uložit do inventáře)</i>.
     * 
     * @param parameters parametry příkazu
     * @return výsledek zpracování <i>(informace pro hráče, které se vypíšou na konzoli)</i>
     */
    @Override
    public String process(String... parameters)
    {
        if (parameters.length == 0) {
            return "Nevím, co mám sebrat, musíš zadat název předmětu.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím sebrat více předmětů současně.";
        }

        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Item item = plan.getItem(itemName);
        Inventory inventory = plan.getInventory();

        if (!(item instanceof Item)){
            return "Je tohle vůbec předmět?!";
        }

        if (!area.containsItem(itemName)) {
            return "Předmět '" + itemName + "' tady není.";
        }

        if (!item.isMoveable()) {
            return "Předmět '" + itemName + "' fakt neuneseš.";
        }

        if (!item.isTakeable()){
            MainCharacter main = plan.getMainCharacter();
            if (itemName.equals(plan.SAKO)){
                inventory.expandLoad();
                area.removeItem(itemName);
                return "Sebral jsi '" + plan.SAKO +"', které ti zvětšilo nosnost inventáře o " 
                + inventory.getExpand() + " kg na "
                + inventory.getTotalLoad() + " kg.";
            }

            else if (itemName.equals(plan.PIVO)){
                area.removeItem(itemName);
                main.setDrunk();
                main.setThirsty();
                return "Vypil jsi '" + plan.PIVO +"'.";
            }

            else if (itemName.equals(plan.VODA)){
                area.removeItem(itemName);
                main.setThirsty();
                return "Vypil jsi '" + plan.VODA +"'.";
            }
        }

        if (item.getWeight()>inventory.getFreeSpace()){
            return "Tvůj inventář je plný. Předmět '" + itemName + "' nebylo možné sebrat.\n"
            + "Zbývající kapacita tvého inventáře je pouze " + inventory.getRemainingSpace() + " kg,"
            + " ale předmět '" + itemName + "' váží " + item.getWeight() + " kg.";
        }

        inventory.addItem(item);
        area.removeItem(itemName);

        return "Sebral jsi předmět '" + itemName + "' a uložil jsi ho do inventáře. \n"
        + "Celková váha tvého invenáře je " + inventory.getCurrentLoad() + " kg. \n" 
        + "Ještě můžeš naložit " + inventory.getRemainingSpace() + " kg.";
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
