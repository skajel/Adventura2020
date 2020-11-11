package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cz.vse.stepan.model.CommandPick;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;
import java.text.DecimalFormat;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandPick}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandPickTest
{
    private Game game;
    private GamePlan plan;
    DecimalFormat decimal = new DecimalFormat("0.0");

    @Before
    public void setUp()
    {
        game = new Game();
        plan = game.getGamePlan();
    }

    @Test
    public void testCommandPick(){
        String expected = null;
        CommandPick cmd = new CommandPick(plan);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        assertFalse(plan.getMainCharacter().isDrunk());
        assertTrue(plan.getMainCharacter().isThirsty());
        
        expected = "Tomu nerozumím, neumím sebrat více předmětů současně.";
        param2[0]="sako";
        param2[1]="klice";
        assertEquals(cmd.process(param2), expected);
        game.processCommand("vezmi sako klice");
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.0));
        
        expected = "Nevím, co mám sebrat, musíš zadat název předmětu.";
        assertEquals(cmd.process(), expected);
        game.processCommand("vezmi ");
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.0));
        expected = "Nevím, co mám sebrat, musíš zadat název předmětu.";
        assertEquals(cmd.process(), expected);
        
        game.processCommand("vezmi sako");
        assertFalse(plan.getCurrentArea().containsItem("sako"));
        assertFalse(plan.getInventory().containsItem("sako"));
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.0));
        
        game.processCommand("jdi pracovna");
        
        expected = "Je tohle vůbec předmět?!";
        param[0]="pracovna";
        assertEquals(cmd.process(param), expected);
        game.processCommand("vezmi pracovna");
        assertFalse(plan.getCurrentArea().containsItem("pracovna"));
        assertFalse(plan.getInventory().containsItem("pracovna"));
        
        expected = "Předmět 'stul' fakt neuneseš.";
        param[0]="stul";
        assertEquals(cmd.process(param), expected);
        game.processCommand("vezmi stul");
        assertTrue(plan.getCurrentArea().containsItem("stul"));
        assertFalse(plan.getInventory().containsItem("stul"));
        
        expected = "Předmět 'zebrik' tady není.";
        param[0]="zebrik";
        assertEquals(cmd.process(param), expected);
        game.processCommand("vezmi zebrik");
        assertFalse(plan.getCurrentArea().containsItem("zebrik"));
        assertFalse(plan.getInventory().containsItem("zebrik"));
        
        game.processCommand("vezmi klice");
        assertFalse(plan.getCurrentArea().containsItem("klice"));
        assertTrue(plan.getInventory().containsItem("klice"));
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.4));
        
        game.processCommand("jdi chodba");
        game.processCommand("jdi obyvak");
        game.processCommand("jdi kuchyne");
        
        expected = "Vypil jsi 'voda'.";
        param[0]="voda";
        assertEquals(cmd.process(param), expected);
        game.processCommand("vezmi voda");
        assertFalse(plan.getCurrentArea().containsItem("voda"));
        assertFalse(plan.getInventory().containsItem("voda"));
        assertFalse(plan.getMainCharacter().isDrunk());
        assertFalse(plan.getMainCharacter().isThirsty());
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.4));
        
        expected = "Vypil jsi 'pivo'.";
        param[0]="pivo";
        assertEquals(cmd.process(param), expected);
        game.processCommand("vezmi pivo");
        assertFalse(plan.getCurrentArea().containsItem("pivo"));
        assertFalse(plan.getInventory().containsItem("pivo"));
        assertTrue(plan.getMainCharacter().isDrunk());
        assertFalse(plan.getMainCharacter().isThirsty());
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.4));
        
        game.processCommand("jdi obyvak");
        game.processCommand("jdi chodba");
        game.processCommand("jdi garaz");
        
        game.processCommand("vezmi zebrik");
        assertTrue(plan.getCurrentArea().containsItem("zebrik"));
        assertFalse(plan.getInventory().containsItem("zebrik"));
        assertEquals(plan.getInventory().getCurrentLoad(), decimal.format(0.4));
    }
}
