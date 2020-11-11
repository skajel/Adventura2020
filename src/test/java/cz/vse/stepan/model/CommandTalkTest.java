package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cz.vse.stepan.model.CommandTalk;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandTalk}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandTalkTest
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
    public void testCommandTalk(){
        String expected = null;
        CommandTalk cmd = new CommandTalk(plan);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        assertFalse(plan.getMainCharacter().isWithArthur());
        
        game.processCommand("jdi pracovna");
        game.processCommand("vezmi klice");
        game.processCommand("jdi chodba");
        game.processCommand("jdi obyvak");
        
        expected = "Tomu nerozumím. Umím vést pouze dialog";
        param2[0]="arthur";
        param2[1]="arthur";
        assertEquals(expected, cmd.process(param2));
        game.processCommand("promluv arthur arthur");
        
        expected = "Nevím, s kým mám promluvit, musíš zadat název osoby.";
        assertEquals(cmd.process(), expected);
        game.processCommand("promluv ");
        
        expected = "Je tohle vůbec osoba?!";
        param[0]= "pes";
        assertEquals(expected, cmd.process(param));
        game.processCommand("promluv pes");
        
        expected = plan.getCurrentArea().getPerson("arthur").getMonolog();
        param[0]= "arthur";
        assertEquals(expected, cmd.process(param));
        game.processCommand("promluv arthur");
        assertTrue(plan.getMainCharacter().isWithArthur());
        assertFalse(plan.getCurrentArea().containsPerson("arthur"));
        
        expected = "Osoba 'vratny' tady není.";
        param[0]= "vratny";
        assertEquals(expected, cmd.process(param));
        game.processCommand("promluv vratny");
        
        game.processCommand("jdi kuchyne");
        game.processCommand("vezmi voda");
        game.processCommand("jdi obyvak");
        game.processCommand("jdi chodba");
        game.processCommand("jdi garaz");
        game.processCommand("rid auto");
        
        expected = plan.getCurrentArea().getPerson("vratny").getMonolog();
        param[0]= "vratny";
        assertEquals(expected, cmd.process(param));
        game.processCommand("promluv vratny");
        assertTrue(plan.getCurrentArea().containsPerson("vratny"));
    }
}
