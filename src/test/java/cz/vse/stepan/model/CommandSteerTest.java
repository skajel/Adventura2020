package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cz.vse.stepan.model.CommandSteer;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandSteer}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandSteerTest
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
    public void testCommandSteer(){
        String expected = null;
        CommandSteer cmd = new CommandSteer(plan);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        game.processCommand("jdi pracovna");
        game.processCommand("vezmi klice");
        game.processCommand("jdi chodba");
        game.processCommand("jdi obyvak");
        game.processCommand("jdi kuchyne");
        game.processCommand("vezmi pivo");
        game.processCommand("jdi obyvak");
        game.processCommand("jdi chodba");
        game.processCommand("jdi garaz");
        
        expected = "Nevím, co mám řídit, musíš zadat název prostředku.";
        assertEquals(cmd.process(), expected);
        game.processCommand("rid");
        assertEquals("garaz", plan.getCurrentArea().getName());
        
        expected = "Tomu nerozumím, viděl jsi někoho řídit více věcí naráz?.";
        param2[0]="auto";
        param2[1]="letadlo";
        assertEquals(cmd.process(param2), expected);
        game.processCommand("rid auto letadlo");
        assertEquals("garaz", plan.getCurrentArea().getName());
        
        expected="Je tohle vůbec předmět?!.";
        param[0]="letadlo";
        assertEquals(cmd.process(param), expected);
        game.processCommand("rid letadlo");
        assertEquals("garaz", plan.getCurrentArea().getName());
        
        expected="Nemůžeš řídit 'auto', protože jsi vypil 'pivo'.\n"
            + "Vrať se zpět do 'obyvak' a promluv si s 'arthur', aby tě svezl.\n";
        param[0]="auto";
        assertEquals(cmd.process(param), expected);
        game.processCommand("rid auto");
        assertEquals("garaz", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsItem("auto"));
        
        game.processCommand("jdi chodba");
        game.processCommand("jdi obyvak");
        game.processCommand("promluv arthur");
        game.processCommand("jdi chodba");
        game.processCommand("jdi garaz");
        
        game.processCommand("poloz klice");
        param[0]="auto";
        expected="Nemůžeš řídit 'auto', protože sis zapomněl 'klice'.\n";
        assertEquals(cmd.process(param), expected);
        game.processCommand("rid auto");
        assertEquals("garaz", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsItem("auto"));
        
        game.processCommand("vezmi klice");
        game.processCommand("rid auto");
        assertEquals("vratnice", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsItem("auto"));
        
        game.processCommand("rid auto");
        assertEquals("garaz", plan.getCurrentArea().getName());
        assertTrue(plan.getCurrentArea().containsItem("auto"));
    }
}
