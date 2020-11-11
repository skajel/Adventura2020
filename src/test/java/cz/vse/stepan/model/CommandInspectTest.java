package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;

import cz.vse.stepan.model.CommandInspect;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandInspect}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandInspectTest
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
    public void testCommandInspect(){
        String expected = null;
        CommandInspect cmd = new CommandInspect(plan);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        expected = "Tomu nerozumím, neumím prozkoumat více předmětů současně.";
        param2[0]="pes";
        param2[1]="kocka";
        assertEquals(expected, cmd.process(param2));
        game.processCommand("prozkoumej pes kocka");
        
        expected = "Nevím, co mám prozkoumat, musíš zadat název předmětu.";
        assertEquals(cmd.process(), expected);
        game.processCommand("prozkoumej ");
        
        expected = "Je tohle vůbec předmět?!";
        param[0]= "obyvak";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej obyvak");
        
        expected = plan.getCurrentArea().getItem("telefon").getDescription();
        param[0]= "telefon";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej telefon");
        
        expected = "Předmět 'klice' nikde nevidím.";
        param[0]= "klice";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej klice");
        
        expected = plan.getCurrentArea().getItem("sako").getDescription();
        param[0]= "sako";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej sako");
        
        game.processCommand("jdi pracovna");
        
        expected = plan.getCurrentArea().getItem("klice").getDescription() + "\n"
              + "Předmět váží: "+ plan.getItem("klice").getWeight() + " kg.";
        param[0]= "klice";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej klice");
        
        game.processCommand("vezmi klice");
        
        expected = plan.getInventory().getItem("klice").getDescription() + "\n"
                    + "Předmět váží: "+ plan.getItem("klice").getWeight() + " kg.";
        assertEquals(expected, cmd.process(param));
        game.processCommand("prozkoumej klice");
    }
}
