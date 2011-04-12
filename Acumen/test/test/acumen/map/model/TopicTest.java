package test.acumen.map.model;

import static junit.framework.TestCase.assertEquals;

import org.junit.*;

import acumen.map.model.Association;
import acumen.map.model.Language;
import acumen.map.model.Topic;
import acumen.map.model.Occurence;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 15:55:29
 */
public class TopicTest {

    @Test
    public void toXml () throws IOException {
        Topic t1 = new Topic("t1");
        t1.setCurrentLanguage(Language.Spanish);
        t1.setCurrentScope("development");
        t1.getMeta().set("label","Topic One");
        t1.getMeta().set("created-by","A. Person");
        t1.addOccurence(new Occurence(t1, "t1.wiki", "wiki", "Page"));
        Association assoc = new Association(t1, "a1", "t2", "navigation", "child");
        assoc.setLabel("xxx");
        t1.addAssociation(assoc);

        String actual = t1.toXml();
        String expected = "<topic id=\"t1\" label=\"Topic One\"><metadata><item name=\"label\" value=\"Topic One\" language=\"Spanish\" scope=\"development\"/><item name=\"created-by\" value=\"A. Person\" language=\"Spanish\" scope=\"development\"/></metadata><occurences><occurence reference=\"t1.wiki\" behaviour=\"wiki\" language=\"Any\" scope=\"*\" role=\"Page\"/></occurences><associations><item name=\"a1\"><association id=\"a1\" label=\"xxx\" source=\"t1\" reference=\"t2\" type=\"navigation\" role=\"child\" scope=\"development\"><metadata><item name=\"label\" value=\"xxx\" language=\"Spanish\" scope=\"development\"/></metadata></association></item></associations></topic>";
        assertEquals(expected, actual);
    }
    
    @Test
    public void getLabel () {
    	// Problem: The topic label output from XML appears to be
    	// falling back to the topics id rather than using the label
    	// metadata provided.
    	// See: TopicMapTest.getTopic_getLabel()
    	Topic t1 = new Topic("t1");
        t1.getMeta().set("label", "scope1", "The First Topic");
        t1.getMeta().set("label", "Topic One");
        t1.getMeta().set("created-by","A. Person");
        t1.setCurrentScope("scope1");
        
        String expected = "The First Topic";
        String actual = t1.getLabel();
        assertEquals(expected, actual);
    }

}
