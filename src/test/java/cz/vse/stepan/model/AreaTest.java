package cz.vse.stepan.model;

import cz.vse.stepan.model.Area;
import cz.vse.stepan.model.Item;
import cz.vse.stepan.model.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Testovací třída pro komplexní otestování třídy {@link Area}.
 *
 * @author Jarmila Pavlíčková
 * @author Jan Říha
 * @author Ondřej Štěpán
 * @version LS 2020
 */
public class AreaTest
{
    @Test
    public void testAreaExits()
    {
        Area area1 = new Area("hala", "Toto je vstupní hala budovy VŠE na Jižním městě.");
        Area area2 = new Area("bufet", "Toto je bufet, kam si můžete zajít na svačinu.");

        area1.addExit(area2);
        area2.addExit(area1);

        assertEquals(area1, area2.getExitArea(area1.getName()));
        assertEquals(area2, area1.getExitArea(area2.getName()));

        assertNull(area1.getExitArea("pokoj"));
        assertNull(area2.getExitArea("pokoj"));
    }

    @Test
    public void testItems()
    {
        Area area = new Area("hala", "Toto je vstupní hala budovy VŠE na Jižním městě.");

        Item item1 = new Item("stul", "Těžký dubový stůl.", 1.25, false);
        Item item2 = new Item("rum", "Láhev vyzrálého rumu.", 0.5);

        assertFalse(area.containsItem(item1.getName()));
        assertFalse(area.containsItem(item2.getName()));
        assertFalse(area.containsItem("pc"));

        area.addItem(item1);
        area.addItem(item2);

        assertEquals(item1, area.getItem(item1.getName()));
        assertEquals(item2, area.getItem(item2.getName()));
        assertNull(area.getItem("pc"));

        assertTrue(area.containsItem(item1.getName()));
        assertTrue(area.containsItem(item2.getName()));
        assertFalse(area.containsItem("pc"));

        assertEquals(item1, area.removeItem(item1.getName()));
        assertEquals(item2, area.removeItem(item2.getName()));
        assertNull(area.removeItem("pc"));

        assertFalse(area.containsItem(item1.getName()));
        assertFalse(area.containsItem(item2.getName()));
        assertFalse(area.containsItem("pc"));
    }   

    @Test
    public void testPeople()
    {
        Area area = new Area("hala", "Toto je vstupní hala budovy VŠE na Jižním městě.");

        Person person1 = new Person("pomocnik", "Co by vám tak pověděl.");
        Person person2 = new Person("cestovatel", "Jednou procestuji celý svět.");

        assertFalse(area.containsPerson(person1.getName()));
        assertFalse(area.containsPerson(person2.getName()));
        assertFalse(area.containsPerson("programator"));

        area.addPerson(person1);
        area.addPerson(person2);

        assertEquals(person1, area.getPerson(person1.getName()));
        assertEquals(person2, area.getPerson(person2.getName()));
        assertNull(area.getPerson("programator"));

        assertTrue(area.containsPerson(person1.getName()));
        assertTrue(area.containsPerson(person2.getName()));
        assertFalse(area.containsPerson("programator"));

        assertEquals(person1, area.removePerson(person1.getName()));
        assertEquals(person2, area.removePerson(person2.getName()));
        assertNull(area.removePerson("programator"));

        assertFalse(area.containsPerson(person1.getName()));
        assertFalse(area.containsPerson(person2.getName()));
        assertFalse(area.containsPerson("programator"));
    } 
}
