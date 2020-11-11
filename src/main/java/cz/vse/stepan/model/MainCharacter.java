package cz.vse.stepan.model;

/**
 * Třída představuje hlavní postavu. 
 * Třída funguje jako zjednodušení ochovávání stavu hlavní postavy.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class MainCharacter
{
    private boolean drunk;
    private boolean thirsty;
    private boolean withArthur;

    /**
     * Konstruktor třídy. Vytvoří hlavní postavu, kterou nastaví do výchozí pozice. 
     *
     * @param drunk uchovává stav hry, kdy je hlavní postava opilá
     * @param thirsty uchovává stav hry, kdy je hlavní postava žíznivá
     * @param withArthur uchovává stav hry, kdy je Arthur s hlavní postavou
     */
    public MainCharacter(boolean drunk, boolean thirsty, boolean withArthur)
    {
        this.drunk = drunk;
        this.thirsty = thirsty;
        this.withArthur = withArthur;
    }

    /**
     * Metoda vrací informaci o hlavní postavě, jestli je opilá. 
     *
     * @return {@code true} pokud je hlavní postava opilá; jinak {@code false}
     */
    public boolean isDrunk()
    {
        return drunk;
    }

    /**
     * Metoda nastaví informaci o hlavní postavě, že je opilá. 
     */
    public void setDrunk()
    {
        drunk = true;
    }

    /**
     * Metoda vrací informaci o hlavní postavě, jestli je žíznivá. 
     *
     * @return {@code true} pokud je hlavní postava žíznivá; jinak {@code false}
     */
    public boolean isThirsty()
    {
        return thirsty;
    }

    /**
     * Metoda nastaví informaci o hlavní postavě, že už není žíznivá. 
     */
    public void setThirsty()
    {
        thirsty = false;
    }

    /**
     * Metoda vrací informaci o hlavní postavě, jestli je s ní Arthur. 
     *
     * @return {@code true} pokud je Arthur s hlavní postavou; jinak {@code false}
     */
    public boolean isWithArthur()
    {
        return withArthur;
    }

    /**
     * Metoda nastaví informaci o hlavní postavě, že je s ní Arthur. 
     */
    public void setWithArthur()
    {
        withArthur = true;
    }
}

