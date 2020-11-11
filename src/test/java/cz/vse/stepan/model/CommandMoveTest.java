package cz.vse.stepan.model;

import static org.junit.Assert.assertEquals;

import cz.vse.stepan.model.CommandMove;
import cz.vse.stepan.model.Game;
import cz.vse.stepan.model.GamePlan;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovací třída pro komplexní otestování třídy {@link CommandMove}.
 *
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class CommandMoveTest
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
    public void testCommandMove(){
        String expected = null;
        CommandMove cmd = new CommandMove(plan, game);
        String[] param2 = new String[2];
        String[] param = new String[1];
        
        assertEquals("loznice", plan.getCurrentArea().getName());
        
        expected = "Nechápu, co po mě chceš. Neumím se 'rozdvojit' a jít na více míst současně.";
        param2[0]="pracovna";
        param2[1]="loznice";
        assertEquals(expected, cmd.process(param2));
        game.processCommand("jdi pracovna loznice");
        assertEquals("loznice", plan.getCurrentArea().getName());
        
        expected = "Nechápu, kam mám jít. Musíš mi zadat nějaký cíl.";
        assertEquals(cmd.process(), expected);
        game.processCommand("jdi ");
        assertEquals("loznice", plan.getCurrentArea().getName());
        
        expected = "Tam se ale odsud jít nedá!";
        param[0]= "garaz";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi garaz");
        assertEquals("loznice", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("pracovna").getFullDescription();
        param[0]= "pracovna";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi pracovna");
        assertEquals("pracovna", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("chodba").getFullDescription();
        param[0]= "chodba";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi chodba");
        assertEquals("chodba", plan.getCurrentArea().getName());
        
        expected = "Nemůžeš přejít do místnosti (lokace) 'garaz', protože máš žízeň. \n"
                        + "Musíš se vrátit zpět do 'kuchyne', aby ses napil.";
        param[0]= "garaz";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi garaz");
        assertEquals("chodba", plan.getCurrentArea().getName());
        
        expected = "Nemůžeš přejít do místnosti (lokace) 'sklep', protože máš žízeň. \n"
                        + "Musíš se vrátit zpět do 'kuchyne', aby ses napil.";
        param[0]= "sklep";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi sklep");
        assertEquals("chodba", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("obyvak").getFullDescription();
        param[0]= "obyvak";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi obyvak");
        assertEquals("obyvak", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("kuchyne").getFullDescription();
        param[0]= "kuchyne";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi kuchyne");
        assertEquals("kuchyne", plan.getCurrentArea().getName());
        game.processCommand("vezmi voda");
        
        expected = plan.getCurrentArea().getExitArea("obyvak").getFullDescription();
        param[0]= "obyvak";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi obyvak");
        assertEquals("obyvak", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("chodba").getFullDescription();
        param[0]= "chodba";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi chodba");
        assertEquals("chodba", plan.getCurrentArea().getName());
        
        expected = plan.getCurrentArea().getExitArea("garaz").getFullDescription();
        param[0]= "garaz";
        assertEquals(expected, cmd.process(param));
        game.processCommand("jdi garaz");
        assertEquals("garaz", plan.getCurrentArea().getName());
    }
}
