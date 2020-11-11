package cz.vse.stepan.model;
import java.util.Objects;

/**
 * Třída představuje předmět. 
 * Předmět je ve hře uložen v lokaci či inventáři.
 * Každý předmět má název, který jej jednoznačně identifikuje.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class Item
{
    private String name;
    private String description;
    private boolean moveable;
    private double weight;
    private boolean takeable;

    /**
     * Konstruktor třídy. Vytvoří předmět se zadaným názvem, popisem, 
     * váhou, možností sebrat předmět, možností dát si předmět do inventáře.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     * @param weight váha předmětu v kilogramech <i>(zapsáno ve formátu #.#)</i>
     * @param moveable možnost sebrat předmět 
     * {@code true} pokud je možné předmět přenášet; jinak {@code false}
     * @param takeable možnost vzít si předmět do inventáře
     * {@code true} pokud je možné předmět přenášet; jinak {@code false}
     */
    public Item(String name, String description, double weight, boolean moveable, boolean takeable)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.moveable = moveable;
        this.takeable = takeable;
    }

    /**
     * Překrytí konstruktoru třídy. Vytvoří předmět se zadaným názvem, popisem, 
     * váhou, možností sebrat předmět. Možnost dát si předmět do inventáře se nastaví na {@code false}.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     * @param weight váha předmětu v kilogramech <i>(zapsáno ve formátu #.#)</i>
     * @param moveable možnost sebrat předmět 
     * {@code true} pokud je možné předmět přenášet; jinak {@code false}
     */
    public Item(String name, String description, double weight, boolean moveable)
    {
        this(name, description, weight, moveable, false);
    }

    /**
     * Překrytí konstruktoru třídy. Vytvoří předmět se zadaným názvem, popisem, 
     * váhou. Možnost sebrat předmět a dát si předmět do inventáře se nastaví na {@code true}.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     * @param weight váha předmětu v kilogramech <i>(zapsáno ve formátu #.#)</i>
     */
    public Item(String name, String description, double weight)
    {
        this(name, description, weight, true, true);
    }

    /**
     * Překrytí konstruktoru třídy. Vytvoří předmět se zadaným názvem, popisem.
     * Výchozí váha se nastaví na 0.0. <i>Použití pro nepřenositelné předměty.</i>. 
     * Možnost sebrat předmět a dát si předmět do inventáře se nastaví na {@code false}.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     */
    public Item(String name, String description)
    {
        this(name, description, 0.0 , false, false);
    }

    /**
     * Metoda vrací název předmětu, který byl zadán při vytváření instance jako
     * parametr konstruktoru. Jedná se o jednoznačný identifikátor předmětu <i>(ve
     * hře nemůže existovat více předmětů se stejným názvem)</i>.
     *
     * @return název lokace
     */
    public String getName()
    {
        return name;
    }

    /**
     * Metoda vrací popis předmětu po příkazu {@link CommandInspect}
     *
     * @return popis předmětu
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Metoda vrací informaci o předmětu, zda jej můžeme sebrat. 
     *
     * @return {@code true}, pokud předmět můžeme sebrat; jinak {@code false}
     */
    public boolean isMoveable()
    {
        return moveable;
    }

    /**
     * Metoda vrací informaci o předmětu, zda jej můžeme vložit do inventáře. 
     *
     * @return {@code true}, pokud předmět můžeme vložit do inventáře; jinak {@code false}
     */
    public boolean isTakeable()
    {
        return takeable;
    }

    /**
     * Metoda vrací váhu předmětu v kilogramech. <i>(zapsáno ve formátu #.#)</i>
     *
     * @return {@code true}, pokud předmět můžeme vložit do inventáře; jinak {@code false}
     */
    public double getWeight()
    {
        return weight;
    }

    /**
     * Metoda porovnává dva předměty <i>(objekty)</i>. Předměty jsou shodné,
     * pokud mají stejný název <i>(atribut {@link #name})</i>.
     * <p>
     * Podrobnější popis metody najdete v dokumentaci třídy {@linkplain Object}.
     *
     * @param o objekt, který bude porovnán s aktuálním
     * @return {@code true}, pokud mají oba předměty stejný název; jinak {@code false}
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

        // Ověříme, že parametr je typu (objekt třídy) Item
        if (!(o instanceof Item)) {
            return false;
        }

        // Provedeme 'tvrdé' přetypování
        Item item = (Item) o;

        // Provedeme porovnání názvů, statická metoda equals z pomocné třídy
        // java.util.Objects porovná hodnoty obou parametrů a vrátí true pro
        // stejné názvy a i v případě, že jsou oba názvy null; jinak vrátí
        // false
        return Objects.equals(this.name, item.name);
    }

    /**
     * Metoda vrací číselný identifikátor instance, který se používá
     * pro optimalizaci ukládání v dynamických datových strukturách. 
     * Při překrytí metody {@link #equals(Object) equals} je vždy nutné překrýt i tuto
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
}
