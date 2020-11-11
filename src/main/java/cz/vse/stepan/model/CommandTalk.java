package cz.vse.stepan.model;

/**
 * Třída implementující příkaz pro promluvení s osobou.
 * 
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandTalk implements ICommand
{
    private static final String NAME = "promluv";
    private GamePlan plan;

    /**
     * Konstruktor třídy.
     *
     * @param plan odkaz na herní plán s aktuálním stavem hry
     */
    public CommandTalk(GamePlan plan){
        this.plan = plan;
    }

    /**
     * Metoda se pokusí, aby hráč promluvil s osobou. Nejprve zkontroluje počet
     * parametrů. Pokud nebyl zadán žádný parametr <i>(tj. neznáme postavu, s kterou má promluvit)</i>,
     * nebo bylo zadáno dva a více parametrů <i>(tj. hráč chce mluvit s více postavami
     * současně)</i>, vrátí chybové hlášení. Pokud byl zadán právě jeden parametr,
     * zkontroluje, zda je parametr postavou<i>(objektem třídy {@link Person})</i>.
     * Poté zkontroluje, zda je postava v aktuální lokaci.
     * Pokud ano, hráč promluví s postavou a postava odpoví mologem. 
     * Jedná-li se o Arthura, smaže ho z aktuální lokace a nastaví hráči, že je s Arthurem.
     * 
     * @param parameters parametry příkazu
     * @return výsledek zpracování <i>(informace pro hráče, které se vypíšou na konzoli)</i>
     */
    @Override
    public String process(String... parameters){
        if (parameters.length == 0) {
            return "Nevím, s kým mám promluvit, musíš zadat název osoby.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím. Umím vést pouze dialog";
        }

        String personName = parameters[0];
        Area area = plan.getCurrentArea();
        Person person = plan.getPerson(personName);

        if (!(person instanceof Person))
        {
            return "Je tohle vůbec osoba?!";
        }

        if (!area.containsPerson(personName)) {
            return "Osoba '" + personName + "' tady není.";
        }

        MainCharacter main = plan.getMainCharacter();

        if (personName.equals(plan.ARTHUR)){
            main.setWithArthur();
            area.removePerson(personName);
        }

        return person.getMonolog();
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
