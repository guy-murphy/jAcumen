package test.acumen.map.model;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;

import org.junit.*;

import acumen.map.model.MetaData;
import acumen.map.model.Language;
import acumen.map.model.Topic;
import acumen.map.model.MetaDataList;

/**
 * User: gmurphy
 * Date: 13-Oct-2009
 * Time: 11:40:31
 */
public class MetaDataListTest {

    @Test
    public void contains () {
        MetaData m1 = new MetaData ("TestTopic", "label", Language.Any, "test", "A Label");
        MetaData m2 = new MetaData ("TestTopic", "description", Language.Any, "test", "Some Description");
        MetaData m3 = new MetaData ("TestTopic", "created-by", Language.Any, "test", "Guy Murphy");

        MetaDataList list = new MetaDataList(new Topic("TestTopic"));
        list.add(m1);
        list.add(m2);
        list.add(m3);
        
        boolean result = list.contains(m1);
        System.out.println(result);

        assertTrue(list.contains(m1));
        assertTrue(list.contains(m2));
        assertTrue(list.contains(m3));

        MetaData m4 = new MetaData("TestTopic", "label", Language.Spanish, "test", "En Espanol");
        assertFalse(list.contains(m4));
    }
}
