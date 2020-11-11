package cz.vse.stepan.model;

import java.util.*;

/**
 * Třída představuje lokaci <i>(místo, místnost, prostor)</i> ve scénáři hry.
 * Každá lokace má název, který ji jednoznačně identifikuje. Lokace může mít
 * sousední lokace, do kterých z ní lze odejít. Odkazy na všechny sousední
 * lokace jsou uložené v kolekci.
 *
 * @author Michael Kölling
 * @author Luboš Pavlíček
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class Area
{
    private String name;
    private String description;
    private Set<Area> exits;  // Obsahuje sousední lokace, do kterých lze z této odejít
    private Map<String, Item> items;  // Obsahuje předměty v lokaci
    private Map<String, Person> people;
    /**
     * Konstruktor třídy. Vytvoří lokaci se zadaným názvem a popisem.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     */
    public Area(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.exits = new HashSet<>();
        this.items = new HashMap<>();
        this.people = new HashMap<>();
    }

    public Map<String, Item> getItemList() {return items;}

    public Collection<Area> getAreaList() {return Collections.unmodifiableCollection(exits);}

    public Map<String, Person> getPeopleList() {return people;}

    public String getDescription()
    {
        return description;
    }

    /**
     * Metoda vrací název lokace, který byl zadán při vytváření instance jako
     * parametr konstruktoru. Jedná se o jednoznačný identifikátor lokace <i>(ve
     * hře nemůže existovat více lokací se stejným názvem)</i>. Aby hra správně
     * fungovala, název lokace nesmí obsahovat mezery, v případě potřeby můžete
     * více slov oddělit pomlčkami, použít camel-case apod.
     *
     * @return název lokace
     */
    public String getName()
    {
        return name;
    }

    /**
     * Metoda přidá další východ z této lokace do lokace předané v parametru.
     * <p>
     * Vzhledem k tomu, že pro uložení sousedních lokací se používá {@linkplain Set},
     * může být přidán pouze jeden východ do každé lokace <i>(tzn. nelze mít dvoje
     * 'dveře' do stejné sousední lokace)</i>. Druhé volání metody se stejnou lokací
     * proto nebude mít žádný efekt.
     * <p>
     * Lze zadat též cestu do sebe sama.
     *
     * @param area lokace, do které bude vytvořen východ z aktuální lokace
     */
    public void addExit(Area area)
    {
        exits.add(area);
    }

    /**
     * Metoda vrací detailní informace o lokaci. Výsledek volání obsahuje název
     * lokace, podrobnější popis a seznam sousedních lokací, do kterých lze
     * odejít.
     *
     * @return detailní informace o lokaci
     */
    public String getFullDescription()
    {
        String exitNames = "Východy:";
        for (Area exitArea : exits) {
            exitNames += " " + exitArea.getName();
        }

        String itemNames = "Předměty:";
        if (items.isEmpty()){
            itemNames += " nejsou tady žádné předměty";    
        }
        else {
            for (String itemName : items.keySet()) {
                itemNames += " " + itemName;
            }
        }
        String personNames = "Osoby:";
        if (people.isEmpty()){
            personNames += " nejsou tady žádné osoby";    
        }
        else {
            for (String personName : people.keySet()) {
                personNames += " " + personName;
            }
        }

        return "Nacházíš se v lokaci (místnosti) " + name + ".\n"
        + description + "\n\n"
        + exitNames + "\n"
        + itemNames + "\n"
        + personNames;   
    }

    /**
     * Metoda vrací lokaci, která sousedí s aktuální lokací a jejíž název
     * je předán v parametru. Pokud lokace s daným jménem nesousedí s aktuální
     * lokací, vrací se hodnota {@code null}.
     * <p>
     * Metoda je implementována pomocí tzv. 'lambda výrazu'. Pokud bychom chtěli
     * metodu implementovat klasickým způsobem, kód by mohl vypadat např. tímto
     * způsobem:
     * <pre> for (Area exitArea : exits) {
     *     if (exitArea.getName().equals(areaName)) {
     *          return exitArea;
     *     }
     * }
     *
     * return null;</pre>
     *
     * @param areaName jméno sousední lokace <i>(východu)</i>
     * @return lokace, která se nachází za příslušným východem; {@code null}, pokud aktuální lokace s touto nesousedí
     */
    public Area getExitArea(String areaName)
    {
        return exits.stream()
        .filter(exit -> exit.getName().equals(areaName))
        .findAny().orElse(null);
    }

    /**
     * Metoda porovnává dvě lokace <i>(objekty)</i>. Lokace jsou shodné,
     * pokud mají stejný název <i>(atribut {@link #name})</i>. Tato metoda
     * je důležitá pro správné fungování seznamu východů do sousedních
     * lokací.
     * <p>
     * Podrobnější popis metody najdete v dokumentaci třídy {@linkplain Object}.
     *
     * @param o objekt, který bude porovnán s aktuálním
     * @return {@code true}, pokud mají obě lokace stejný název; jinak {@code false}
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o)
    {
        // Ověříme, že parametr není null
        if (o == null) {
            return false;
        }

        // Ověříme, že se nejedná o stejnou instanci (objekt)
        if (this == o) {
            return true;
        }

        // Ověříme, že parametr je typu (objekt třídy) Area
        if (!(o instanceof Area)) {
            return false;
        }

        // Provedeme 'tvrdé' přetypování
        Area area = (Area) o;

        // Provedeme porovnání názvů, statická metoda equals z pomocné třídy
        // java.util.Objects porovná hodnoty obou parametrů a vrátí true pro
        // stejné názvy a i v případě, že jsou oba názvy null; jinak vrátí
        // false
        return Objects.equals(this.name, area.name);
    }

    /**
     * Metoda vrací číselný identifikátor instance, který se používá
     * pro optimalizaci ukládání v dynamických datových strukturách
     * <i>(např.&nbsp;{@linkplain HashSet})</i>. Při překrytí metody
     * {@link #equals(Object) equals} je vždy nutné překrýt i tuto
     * metodu.
     * <p>
     * Podrobnější popis pravidel pro implementaci metody najdete
     * v dokumentaci třídy {@linkplain Object}.
     *
     * @return číselný identifikátor instance
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    /**
     * Metoda přidá předmět <i>(objekt třídy {@link Item})</i> do lokace.
     * 
     * @param item předmět, který bude do lokace přidán
     */
    public void addItem(Item item)
    {
        items.put(item.getName(), item);
    }

    /**
     * Metoda odebere předmět <i>(objekt třídy {@link Item})</i> z lokace.
     * 
     * @param itemName předmět, který bude z lokace odebrán
     * @return předmět, který byl z lokace odebrán
     */
    public Item removeItem(String itemName)
    {
        return items.remove(itemName);
    }

    /**
     * Metoda vrací odkaz na předmět <i>(objekt třídy {@link Item})</i>, který se nachází v lokaci.
     * 
     * @param itemName název předmětu, na který chceme získát odkaz
     * @return předmět v lokaci, na který získáme odkaz
     */
    public Item getItem(String itemName)
    {
        return items.get(itemName);
    }

    /**
     * Metoda zjištuje, zda se v lokaci nachází předmět <i>(objekt třídy {@link Item})</i>.
     * 
     * @param itemName název předmětu, u kterého chceme zjistit, zda se nachází v lokaci.
     * @return {@code true}, pokud se předmět nachází v lokaci; jinak {@code false}
     */
    public boolean containsItem(String itemName)
    {
        return items.containsKey(itemName);
    }

    /**
     * Metoda přidá osobu <i>(objekt třídy {@link Person})</i> do lokace.
     * 
     * @param person osoba, která bude do lokace přidán
     */
    public void addPerson(Person person)
    {
        people.put(person.getName(), person);
    }

    /**
     * Metoda odebere osobu <i>(objekt třídy {@link Person})</i> z lokace.
     * 
     * @param personName osoba, která bude z lokace odebrána
     * @return osoba, která byla z lokace odebrána
     */
    public Person removePerson(String personName)
    {
        return people.remove(personName);
    }

    /**
     * Metoda vrací odkaz na osobu <i>(objekt třídy {@link Person})</i>, která se nachází v lokaci.
     * 
     * @param personName název osoby, na který chceme získát odkaz
     * @return osoba v lokaci, na kterou získáme odkaz
     */
    public Person getPerson(String personName)
    {
        return people.get(personName);
    }

    /**
     * Metoda zjištuje, zda se v lokaci nachází osoba <i>(objekt třídy {@link Person})</i>.
     * 
     * @param personName název osoby, u které chceme zjistit, zda se nachází v lokaci.
     * @return {@code true}, pokud se osoba nachází v lokaci; jinak {@code false}
     */
    public boolean containsPerson(String personName)
    {
        return people.containsKey(personName);
    }
}

