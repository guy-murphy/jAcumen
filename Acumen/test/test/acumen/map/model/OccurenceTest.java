package test.acumen.map.model;

import static junit.framework.TestCase.assertEquals;

import org.junit.*;
import java.io.IOException;

import acumen.map.model.Language;
import acumen.map.model.Occurence;
import acumen.map.model.Topic;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 14:52:23
 */
public class OccurenceTest {

    @Test
    public void toXml () throws IOException {
        Topic topic = new Topic("test");
        Occurence occurence = new Occurence(topic, "test.wiki", "wiki", Language.Any, "*", "Page");
        occurence.setContent("To be or not to be that is the question...");
        String expected = "<occurence reference=\"test.wiki\" behaviour=\"wiki\" language=\"Any\" scope=\"*\" role=\"Page\">To be or not to be that is the question...</occurence>";
        String actual = occurence.toXml();
        assertEquals(expected, actual);
    }
}
