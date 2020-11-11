package cz.vse.stepan.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.text.DecimalFormat;

/**
 * Třída představuje inventář, do kterého se ukládají předměty <i>(objekty třídy {@link Item})</i>.
 * Inventář má svou kapacitu v kilogramech, která lze rozšířit.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class Inventory
{
    private Map<String, Item> inventory;
    private double currentLoad;
    private double totalLoad;
    private double expand;
    DecimalFormat decimal = new DecimalFormat("0.0");

    /**
     * Konstruktor třídy. Vytvoří inventář, který má kapacitu 4.0 kg. 
     * Lze rozšířit oblečením saka na 5.0 kg.
     * Aktuální váha je nastavena na 1.0 kg. 
     */
    public Inventory(){
        this.inventory = new HashMap<>();
        this.totalLoad = 4.0;
        this.expand = 1.0;
        this.currentLoad = 0.0;
    }

    /**
     * Metoda vrací celkovou kapacitu inventáře ve formátu <i>#.#</i>.
     *
     * @return celková kapacita inventáře
     */
    public String getTotalLoad(){
        return decimal.format(totalLoad);
    } 

    /**
     * Metoda vrací kapacitu rozšíření inventáře ve formátu<i>#.#</i>.
     *
     * @return celková kapacita inventáře
     */
    public String getExpand(){
        return decimal.format(expand);
    }

    /**
     * Metoda rozšiřuje inventář na větší kapacitu.
     */
    public void expandLoad(){
        totalLoad = totalLoad + expand;
    }

    /**
     * Metoda vrací aktuální naložení inventáře ve formátu <i>#.#</i>.
     *
     * @return aktuální naložení inventáře
     */
    public String getCurrentLoad(){
        return decimal.format(currentLoad);
    }

    /**
     * Metoda vrací seznam všech předmětů v inventáři.
     * Používá s v metodě {@link CommandInventory}
     *
     * @return všechny předměty v inventáři
     */
    public String getAllItems(){
        String answer = "";  
        Set<String> keys = inventory.keySet();
        if (keys.isEmpty()){
            return "Inventář je prázdný.";
        }
        else {
            for (String k : keys){
                answer += k + " ";
            }
        }
        return "V inventáři se nachází: " + answer;
    }

    /**
     * Metoda přidá předmět <i>(objekt třídy {@link Item})</i> do inventáře.
     * 
     * @param item předmět, který bude přidán do inventáře
     */
    public void addItem (Item item){
        inventory.put(item.getName(), item);
        currentLoad=currentLoad+item.getWeight();
    }

    /**
     * Metoda odebere předmět <i>(objekt třídy {@link Item})</i> z inventáře.
     * 
     * @param itemName předmět, který bude z inventáře odebrán
     * @return předmět, který byl z lokace odebrán
     */
    public Item removeItem (String itemName){
        currentLoad=currentLoad-getItem(itemName).getWeight();
        return inventory.remove(itemName);
    }

    /**
     * Metoda zjištuje, zda se v inventáři nachází předmět <i>(objekt třídy {@link Item})</i>.
     * 
     * @param itemName název předmětu, u kterého chceme zjistit, zda se nachází v inventáři.
     * @return {@code true}, pokud se předmět nachází v inventáři; jinak {@code false}
     */
    public boolean containsItem(String itemName){
        return inventory.containsKey(itemName);
    }

    /**
     * Metoda vrací odkaz na předmět <i>(objekt třídy {@link Item})</i>, který se nachází v inventáři.
     * 
     * @param itemName název předmětu, na který chceme získát odkaz
     * @return předmět v inventáři, na který získáme odkaz
     */
    public Item getItem (String itemName){
        return inventory.get(itemName);
    }

    /**
     * Metoda vrací odkaz na zbývající místo v inventáři ve formátu <i>#.#</i>.
     * 
     * @return zbývající misto v inventáři ve formátu <i>#.#</i>.
     */
    public String getRemainingSpace () {
        return decimal.format(totalLoad-currentLoad);
    }

    /**
     * Metoda vrací odkaz na zbývající místo v inventáři ve formátu <i>#.#</i>.
     * 
     * @return zbývající misto v inventáři
     */
    public double getFreeSpace () {
        return totalLoad-currentLoad;
    } 
}  

