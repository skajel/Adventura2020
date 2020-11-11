package cz.vse.stepan.model;

import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testovací třída pro komplexní otestování herního příběhu.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class GameTest
{
    private Game game;
    private GamePlan plan;

    @Before
    public void setUp()
    {
        game = new Game();
        plan = game.getGamePlan();
    }

    @Test
    public void testWinnable()
    {
        assertEquals("loznice", plan.getCurrentArea().getName());
        assertFalse(plan.getMainCharacter().isWithArthur());
        assertFalse(plan.getMainCharacter().isDrunk());
        assertTrue(plan.getMainCharacter().isThirsty());

        game.processCommand("vezmi sako");
        assertFalse(plan.getCurrentArea().containsItem("sako"));
        assertFalse(game.isGameOver());

        game.processCommand("jdi pracovna");
        assertEquals("pracovna", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("vezmi klice");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(game.isGameOver());

        game.processCommand("jdi chodba");
        assertEquals("chodba", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi garaz");
        assertEquals("chodba", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi obyvak");
        assertEquals("obyvak", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsPerson("arthur"));
        assertFalse(game.isGameOver());

        game.processCommand("promluv arthur");
        assertTrue(plan.getMainCharacter().isWithArthur());
        assertFalse(plan.getCurrentArea().containsPerson("arthur"));
        assertFalse(game.isGameOver());

        game.processCommand("jdi kuchyne");
        assertEquals("kuchyne", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("vezmi pivo");
        assertFalse(plan.getCurrentArea().containsItem("pivo"));
        assertFalse(plan.getMainCharacter().isThirsty());
        assertTrue(plan.getMainCharacter().isDrunk());
        assertFalse(game.isGameOver());

        game.processCommand("jdi obyvak");
        assertEquals("obyvak", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi chodba");
        assertEquals("chodba", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi sklep");
        assertEquals("sklep", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("vezmi brambora");
        assertTrue(plan.getInventory().containsItem("brambora"));
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getInventory().containsItem("pivo"));
        assertFalse(game.isGameOver());

        game.processCommand("vezmi zbran");
        assertFalse(plan.getInventory().containsItem("zbran"));
        assertFalse(game.isGameOver());

        game.processCommand("poloz brambora");
        assertFalse(plan.getInventory().containsItem("brambora"));
        assertTrue(plan.getCurrentArea().containsItem("brambora"));
        assertFalse(game.isGameOver());

        game.processCommand("vezmi zbran");
        assertTrue(plan.getInventory().containsItem("zbran"));
        assertFalse(game.isGameOver());

        game.processCommand("jdi chodba");
        assertEquals("chodba", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("jdi garaz");
        assertEquals("garaz", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("vezmi zebrik");
        assertFalse(plan.getInventory().containsItem("zebrik"));
        assertTrue(plan.getCurrentArea().containsItem("zebrik"));
        assertFalse(game.isGameOver());

        game.processCommand("rid auto");
        assertEquals("vratnice", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsPerson("vratny"));

        game.processCommand("promluv vratny");
        assertTrue(plan.getCurrentArea().containsPerson("vratny"));
        assertFalse(game.isGameOver());

        game.processCommand("jdi tovarna");
        assertEquals("tovarna", plan.getCurrentArea().getName());
        assertTrue(game.isGameOver());
    }

    @Test
    public void testPlayerQuit(){
        assertEquals("loznice", plan.getCurrentArea().getName());

        game.processCommand("jdi pracovna");
        assertEquals("pracovna", plan.getCurrentArea().getName());
        assertFalse(game.isGameOver());

        game.processCommand("konec");
        assertTrue(game.isGameOver());
    }

}
