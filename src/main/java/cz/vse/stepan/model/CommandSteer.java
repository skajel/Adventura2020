package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro řízení dopravního prostředku.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandSteer implements ICommand
{
    private static final String NAME = "rid";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandSteer(GamePlan plan){
        this.plan = plan;
    }

    /**
     * Metoda se pokusí hráči umožnit, aby řídit dopravní prostředek. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme cíl cesty)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce jet více dopravními prostředky
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je dopravní prostředek předmětem <i>(objektem třídy {@link Item})</i>.
     * Poté zkontroluje, zda se jedná o auto a jestli je v aktuální lokaci.
     * Dále ověří, zda je možné auto řídit <i>(tj. hráč má v inventáři klíče, není opilý 
     * (pokud ano, potřebuje Arthura))</i>.
     * Nakonec zjistí, jestli jede z garáže do vrátnice nebo naopak.
     * Poté provede přesun hráče do cílové lokace a vrátí její popis.
     * 
     * @param parameters parametry příkazu
     * @return výsledek zpracování <i>(informace pro hráče, které se vypíšou na konzoli)</i>
     */
    @Override
    public String process(String... parameters){
        if (parameters.length == 0) {
            return "Nevím, co mám řídit, musíš zadat název prostředku.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, viděl jsi někoho řídit více věcí naráz?.";
        }

        String itemName = parameters[0];
        Area area = plan.getCurrentArea();
        Inventory inventory = plan.getInventory();
        Item item = area.getItem(itemName);
        Area vratnice = plan.getArea(plan.VRATNICE);
        Area garaz = plan.getArea(plan.GARAZ);
        Item items = plan.getItem(itemName);

        if (!(items instanceof Item)){
            return "Je tohle vůbec předmět?!.";
        }

        if (!itemName.equals(GamePlan.VEHICLE)){
            return "Tento předmět nelze řídit.";
        }

        if (!area.containsItem(itemName)) {
            return "Dopravní prostředek '" + itemName + "' tady není.";
        }

        if (!inventory.containsItem(plan.KLICE)){   
            return "Nemůžeš řídit '" + itemName + "', protože sis zapomněl '" + plan.KLICE + "'.\n";
        }

        if (area.getName().equals(plan.VRATNICE)){
            vratnice.removeItem(itemName);
            plan.setCurrentArea(garaz);
            garaz.addItem(item);
            return "Nastoupil(a) jsi do '" + itemName + "' a přijel jsi k " + plan.GARAZ + ". \n"
            + plan.getArea(plan.GARAZ).getFullDescription() + "\n";
        }

        MainCharacter main = plan.getMainCharacter();

        if (main.isDrunk()) {
            if (main.isWithArthur()){   
                garaz.removeItem(itemName);
                plan.setCurrentArea(vratnice);
                vratnice.addItem(item);

                return "Nemůžeš řídit, protože jsi vypil '" + plan.PIVO + "', ještě že máš s sebou '" + plan.ARTHUR + "'.\n"
                + "Nastoupil jsi do '" + itemName + "' a přijel jsi k " + plan.VRATNICE + ". \n\n"
                + plan.getArea(plan.VRATNICE).getFullDescription() + "\n";
            }
            else{
                return "Nemůžeš řídit '" + itemName + "', protože jsi vypil '" + plan.PIVO + "'.\n"
                + "Vrať se zpět do '" + plan.OBYVAK + "' a promluv si s '" + plan.ARTHUR + "', aby tě svezl.\n";
            }
        }  

        garaz.removeItem(itemName);
        plan.setCurrentArea(vratnice);
        vratnice.addItem(item);

        return "Nastoupil jsi do '" + itemName + "' a přijel jsi k " + plan.VRATNICE + ". \n\n"
        + plan.getArea(plan.VRATNICE).getFullDescription() + "\n";
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
