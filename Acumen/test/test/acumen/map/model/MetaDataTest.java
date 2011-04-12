package test.acumen.map.model;

import static junit.framework.TestCase.assertEquals;

import org.junit.*;

import acumen.map.model.MetaData;
import acumen.map.model.Language;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 14:29:07
 */
public class MetaDataTest {

    @Test
    public void toXml () throws IOException {
        String expected = "<item name=\"name\" value=\"Value\" language=\"Any\" scope=\"*\"/>";
        MetaData meta = new MetaData("home", "name", Language.Any, "*", "Value");
        String actual = meta.toXml();
        assertEquals(expected, actual);
    }

}
