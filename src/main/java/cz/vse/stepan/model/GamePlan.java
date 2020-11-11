package cz.vse.stepan.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Třída představující aktuální stav hry. Veškeré informace o stavu hry
 * <i>(mapa lokací, inventář, vlastnosti hlavní postavy, informace o plnění
 * úkolů apod.)</i> by měly být uložené zde v podobě datových atributů.
 * <p>
 * Třída existuje především pro usnadnění potenciální implementace ukládání
 * a načítání hry. Pro uložení rozehrané hry do souboru by mělo stačit uložit
 * údaje z objektu této třídy <i>(např. pomocí serializace objektu)</i>. Pro
 * načtení uložené hry ze souboru by mělo stačit vytvořit objekt této třídy
 * a vhodným způsobem ho předat instanci třídy {@link Game}.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class GamePlan
{
    public static final String VEHICLE = "auto";

    public static final String ARTHUR = "arthur";
    public static final String VRATNY = "vratny";

    public static final String PIVO = "pivo";
    public static final String SAKO = "sako";
    public static final String VODA = "voda";
    public static final String ZBRAN = "zbran";
    public static final String KLICE = "klice";

    public static final String CHODBA = "chodba";
    public static final String GARAZ = "garaz";
    public static final String KUCHYNE = "kuchyne";
    public static final String SKLEP = "sklep";
    public static final String PRACOVNA = "pracovna";
    public static final String LOZNICE = "loznice";
    public static final String OBYVAK = "obyvak";
    public static final String VRATNICE = "vratnice";
    public static final String TOVARNA = "tovarna";

    public static final String FINAL_SLOGAN = "Došel jsme do továrny, ale nikoho zatím nevidím.\n"
        + "Aaaa, už ho vidím!!!";

    private Area currentArea;
    private Inventory inventory;
    private Map<String, Area> locations;
    private Map<String, Item> items;
    private Map<String, Person> people;
    private MainCharacter tommy;

    /**
     * Konstruktor třídy. Pomocí metody {@link #prepareWorldMap() prepareWorldMap}
     * vytvoří jednotlivé lokace a propojí je pomocí východů.
     */
    public GamePlan()
    {
        prepareWorldMap();
    }

    /**
     * Metoda vytváří jednotlivé lokace a propojuje je pomocí východů. Jako
     * výchozí aktuální lokaci následně nastaví ložnice, ve kterém spí hlavní postava.
     */
    private void prepareWorldMap()
    {  
        inventory = new Inventory();
        tommy = new MainCharacter(false, true, false);

        // Vytvoříme jednotlivé lokace
        Area pracovna = new Area(PRACOVNA,"Útulná místnost ... kdo by tady nechtěl pracovat.");
        Area loznice = new Area(LOZNICE, "Nemůžu se dočkat, až půjdu spát.");
        Area obyvak = new Area(OBYVAK, "Obývací pokoj v plné své kráse. Jen se podívejte na tu pohovku.");
        Area chodba = new Area(CHODBA,"Vždy se musím podívat do zrcadla, abych zkontroloval, jak vypadám.");
        Area kuchyne = new Area(KUCHYNE,"Vždy když dojdu do kuchyně, dostanu žížeň, jak je to možné...?");
        Area sklep = new Area(SKLEP,"Tady je ale nepořádek ... měl bych tady uklidit.");
        Area garaz = new Area(GARAZ,"Tady kdysi byla dílna, teď už tady můžeš nalézt maximálně mé '" + VEHICLE + "'.");
        Area vratnice = new Area(VRATNICE,"Vstupní brána do továrny, kterou hlídá '" + VRATNY + "'.");
        Area tovarna = new Area(TOVARNA,FINAL_SLOGAN);

        locations = new HashMap<>();

        // Přídáme lokace do pomocné mapy
        locations.put(pracovna.getName(), pracovna);
        locations.put(loznice.getName(), loznice);
        locations.put(obyvak.getName(), obyvak);
        locations.put(chodba.getName(), chodba);
        locations.put(kuchyne.getName(), kuchyne);
        locations.put(sklep.getName(), sklep);
        locations.put(garaz.getName(), garaz);
        locations.put(vratnice.getName(), vratnice);
        locations.put(tovarna.getName(), tovarna);

        // Nastavíme průchody mezi lokacemi (sousední lokace)
        pracovna.addExit(loznice);
        loznice.addExit(pracovna);

        loznice.addExit(obyvak);
        obyvak.addExit(loznice);

        pracovna.addExit(chodba);
        chodba.addExit(pracovna);

        kuchyne.addExit(obyvak);
        obyvak.addExit(kuchyne);

        obyvak.addExit(chodba);
        chodba.addExit(obyvak);

        chodba.addExit(sklep);
        sklep.addExit(chodba);

        chodba.addExit(garaz);
        garaz.addExit(chodba);

        vratnice.addExit(tovarna);

        // Vytvoříme jednotlivé předměty
        Item telefon = new Item("telefon", "Pevná linka.", 2.2, false, false);
        Item pohovka = new Item("pohovka", "Béžová pohovka, ideální pro chvíli odpočinku.");
        Item sako = new Item(SAKO, "Černé společenské sako, které zvyšuje nosnost inventáře.", 0.0, true);
        Item auto = new Item(VEHICLE, "Zelené auto značky Minerva z roku 1930.");
        Item pivo = new Item(PIVO, "Vychlazená Plzeň ... když mě vypiješ, nebudeš moci řídit.", 0.0, true);
        Item zbran = new Item(ZBRAN, "Německý samopal MP 18, ideální proti ozbrojenému přepadení.", 4.2);
        Item voda = new Item(VODA, "Vychlazená voda z kohoutku v skleničce.", 0.0, true);
        Item klice = new Item(KLICE, "Klíče od auta, můžou se hodit pokud budeš řídit auto.", 0.4);
        Item dokument = new Item("dokument", "Smlouva o nakoupení bezpečnostního systému továrny.", 0.2);
        Item brambora = new Item("brambora", "Osamocená brambora, která zbyla po dnešním obědě.", 1.0);
        Item stul = new Item("stul", "Dřevěný psací stůl.");
        Item zrcadlo = new Item("zrcadlo", "Ne, neodpovím ti na otázku 'kdo je na světě nejkrásnější'.");
        Item zebrik = new Item("zebrik", "Dřevěný žebřík vysoký 2.5 metru.", 7.0);

        items = new HashMap<>();

        // Přidáme předměty do pomocné mapy
        items.put(telefon.getName(), telefon);
        items.put(pohovka.getName(), pohovka);
        items.put(sako.getName(), sako);
        items.put(auto.getName(), auto);
        items.put(pivo.getName(), pivo);
        items.put(zbran.getName(), zbran);
        items.put(voda.getName(), voda);
        items.put(klice.getName(), klice);
        items.put(dokument.getName(), dokument);
        items.put(brambora.getName(), brambora);
        items.put(stul.getName(), stul);
        items.put(zrcadlo.getName(), zrcadlo);
        items.put(zebrik.getName(), zebrik);

        // Přidáme předměty do lokací
        loznice.addItem(telefon);
        obyvak.addItem(pohovka);
        loznice.addItem(sako);
        garaz.addItem(auto);
        kuchyne.addItem(pivo);
        sklep.addItem(zbran);
        kuchyne.addItem(voda);
        pracovna.addItem(klice);
        pracovna.addItem(dokument);
        sklep.addItem(brambora);
        pracovna.addItem(stul);
        chodba.addItem(zrcadlo);
        garaz.addItem(zebrik);

        // Vytvoříme jednotlivé postavy
        Person arthur = new Person(ARTHUR, "To mě nenecháš si ani na chvilku odpočinout?\n"
                + "Ale samozřejmě, že tě v tom nenechám bratře! Jdu s tebou." );
        Person vratny = new Person(VRATNY, "Ještě že jste přijel tak rychle, čekal jsem tady na Vás! \n"
                + "Viděl jsem ho, zloděje! Nemohl jsem proti němu nic udělat, byl ozbrojený! \n"
                + "Budeš nejspíš potřebovat '" + ZBRAN + "'.");

        people = new HashMap<>();

        // Přidáme předměty do pomocné mapy
        people.put(arthur.getName(), arthur);
        people.put(vratny.getName(), vratny);

        // Přidáme postavy do lokací
        obyvak.addPerson(arthur);
        vratnice.addPerson(vratny);

        // Hru začneme v ložnici
        currentArea = loznice;
    }

    /**
     * Metoda vrací odkaz na inventář <i>(objekt třídy {@link Inventory})</i>.
     * 
     * @return inventář, na který získáme odkaz
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * Metoda vrací odkaz na hlavní postavu <i>(objekt třídy {@link MainCharacter})</i>.
     * 
     * @return hlavní postava, na kterou získáme odkaz
     */
    public MainCharacter getMainCharacter(){
        return tommy;
    }

    /**
     * Metoda vrací odkaz na lokaci <i>(objekt třídy {@link Area})</i>.
     * 
     * @param areaName název lokace, na kterou chceme získát odkaz
     * @return lokaci, na který získáme odkaz
     */
    public Area getArea(String areaName){
        return locations.get(areaName);
    }

    /**
     * Metoda vrací odkaz na předmět <i>(objekt třídy {@link Item})</i>.
     * 
     * @param itemName název předmětu, na kterou chceme získát odkaz
     * @return předmět, na který získáme odkaz
     */
    public Item getItem(String itemName){
        return items.get(itemName);
    }

    /**
     * Metoda vrací odkaz na osobu <i>(objekt třídy {@link Person})</i>.
     * 
     * @param personName název osoby, na kterou chceme získát odkaz
     * @return osoba, na kterou získáme odkaz
     */
    public Person getPerson(String personName){
        return people.get(personName);
    }

    /**
     * Metoda vrací odkaz na aktuální lokaci, ve které se hráč právě nachází.
     *
     * @return aktuální lokace
     */
    public Area getCurrentArea()
    {
        return currentArea;
    }

    /**
     * Metoda nastaví aktuální lokaci, používá ji příkaz {@link CommandMove}
     * při přechodu mezi lokacemi.
     *
     * @param area lokace, která bude nastavena jako aktuální
     */
    public void setCurrentArea(Area area)
    {
        currentArea = area;
    }

    /**
     * Metoda vrací informaci o stavu hry, jestli je vítězná. 
     *
     * @return {@code true} pokud hráč vyhrál; jinak {@code false}
     */
    public boolean isVictorious()
    {
        if (!inventory.containsItem(ZBRAN)){
            return false;
        }
        return true;
    }
}
