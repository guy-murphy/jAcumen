package test.acumen.map.model;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

import acumen.map.model.Association;
import acumen.map.model.Topic;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 15:03:09
 */
public class AssociationTest {

    @Test
    public void toXml () throws IOException {
        Topic topic = new Topic("test");
        Association association = new Association(topic, "test-assoc", "AboutUs", "navigation", "child");
        association.getMeta().set("label", "A Test Association");
        association.setResolvedTopic(new Topic("AboutUs"));
        String actual = association.toXml();

        String expected = "<association id=\"test-assoc\" label=\"A Test Association\" source=\"test\" reference=\"AboutUs\" type=\"navigation\" role=\"child\" scope=\"*\"><metadata><item name=\"label\" value=\"A Test Association\" language=\"Any\" scope=\"*\"/></metadata><topic id=\"AboutUs\" label=\"AboutUs\"><metadata/><occurences/><associations/></topic></association>";
        assertEquals(expected, actual);
    }
    
    @Test
    public void createAssociation () throws IOException {
    	Association assoc = new Association();
    	assoc.setSource("TestTopic");
    	assoc.setReference("GuyMurphy");
    	assoc.setLabel("Guy Murphy");
    	assoc.setType("test");
    	assoc.setRole("author");
    	
    	assertEquals("TestTopic", assoc.getSource());
    	assertEquals("GuyMurphy", assoc.getReference());
    	assertEquals("Guy Murphy", assoc.getLabel());
    	assertEquals("test", assoc.getType());
    	assertEquals("author", assoc.getRole());
    	
    }

}
