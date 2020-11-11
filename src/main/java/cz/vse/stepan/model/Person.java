package cz.vse.stepan.model;
import java.util.Objects;

/**
 * Třída představuje osobu. 
 * Osoba se ve hře nachází v určité lokaci.
 * Každá osoba má název, který ji jednoznačně identifikuje.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class Person
{
    private String name;
    private String monolog;

    /**
     * Konstruktor třídy. Vytvoří osobu se zadaným názvem a monologem.
     *
     * @param name název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param monolog po příkazu {@link CommandTalk} postava pronese monolog
     */
    public Person(String name, String monolog)
    {
        this.name = name;
        this.monolog = monolog;
    }

    /**
     * Metoda vrací název osoby, která byl zadána při vytváření instance jako
     * parametr konstruktoru. Jedná se o jednoznačný identifikátor osoby <i>(ve
     * hře nemůže existovat více osob se stejným názvem)</i>.
     *
     * @return název osoby
     */
    public String getName()
    {
        return name;
    }

    /**
     * Metoda vrací monolog osoby, který pronese po příkazu {@link CommandTalk}. 
     *
     * @return monolog osoby
     */
    public String getMonolog()
    {
        return monolog;
    }

    /**
     * Metoda porovnává dvě postavy <i>(objekty)</i>. Postavy jsou shodné,
     * pokud mají stejný název <i>(atribut {@link #name})</i>.
     * <p>
     * Podrobnější popis metody najdete v dokumentaci třídy {@linkplain Object}.
     *
     * @param o objekt, který bude porovnán s aktuálním
     * @return {@code true}, pokud mají obě postavy stejný název; jinak {@code false}
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

        // Ověříme, že parametr je typu (objekt třídy) Person
        if (!(o instanceof Person)) {
            return false;
        }

        // Provedeme 'tvrdé' přetypování
        Person person = (Person) o;

        // Provedeme porovnání názvů, statická metoda equals z pomocné třídy
        // java.util.Objects porovná hodnoty obou parametrů a vrátí true pro
        // stejné názvy a i v případě, že jsou oba názvy null; jinak vrátí
        // false
        return Objects.equals(this.name, person.name);
    }

    /**
     * Metoda vrací číselný identifikátor instance, který se používá
     * pro optimalizaci ukládání v dynamických datových strukturách. 
     * Při překrytí metody {@link #equals(Object) equals} 
     * je vždy nutné překrýt i tuto metodu.<p>
     * 
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
