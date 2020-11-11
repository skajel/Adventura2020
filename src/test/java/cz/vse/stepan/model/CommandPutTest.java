package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cz.vse.stepan.model.CommandPut;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandPut}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandPutTest
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
    public void testCommandPut(){
        String expected = null;
        CommandPut cmd = new CommandPut(plan);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        game.processCommand("jdi pracovna");
        assertFalse(plan.getInventory().containsItem("klice"));
        assertTrue(plan.getCurrentArea().containsItem("klice"));
        
        game.processCommand("vezmi klice");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        
        expected = "Tomu nerozumím, neumím položit více předmětů současně.";
        param2[0]="klice";
        param2[1]="klice";
        assertEquals(expected, cmd.process(param2));
        game.processCommand("poloz klice klice");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        
        expected = "Nevím, co mám položit, musíš zadat název předmětu.";
        assertEquals(cmd.process(), expected);
        game.processCommand("poloz ");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        
        expected = "Je tohle vůbec předmět?!";
        param[0]= "obyvak";
        assertEquals(expected, cmd.process(param));
        game.processCommand("poloz obyvak");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        
        expected = "Předmět 'stul' nelze položit, protože ho nemám v inventáři.";
        param[0]= "stul";
        assertEquals(expected, cmd.process(param));
        game.processCommand("poloz stul");
        assertTrue(plan.getInventory().containsItem("klice"));
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        
        expected = "Předmět 'klice' jsi položil do lokace (místnosti) '" + plan.getCurrentArea().getName() + "'.";
        param[0]= "klice";
        assertEquals(expected, cmd.process(param));
        game.processCommand("poloz klice");
        assertFalse(plan.getInventory().containsItem("klice"));
        assertTrue(plan.getCurrentArea().containsItem("klice"));
    }
}
