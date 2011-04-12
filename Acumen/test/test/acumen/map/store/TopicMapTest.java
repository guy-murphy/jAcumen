/**
 * User: Guy J. Murphy
 * Date: Feb 15, 2010
 * Time: 12:20:25 PM
 * File: TopicMapTest.java
 */
package test.acumen.map.store;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.*;

import acumen.map.store.MySqlTopicStore2;
import acumen.data.StoreException;
import acumen.map.model.*;
import acumen.util.IAcumenList;
import acumen.util.Is;

/**
 * @author Guy J. Murphy
 */
public class TopicMapTest {
	
	private MySqlTopicStore2 _store;

    public TopicMapTest () throws StoreException {
        _reset();
    }

    private void _reset() throws StoreException {
        if (_store != null) {
            if (_store.isOpen()) {
                _store.stop();
            }
        }
        _store = new MySqlTopicStore2("leviathan.acumen.es", "acumen_london", "root", "redacted");
    }
    
    @Test
    public void getRelated () throws StoreException, IOException {
    	_reset();
    	try {
    		_store.start();
    		String language = "any";
    		String scope = "chy";
    		ITopic topic = _store.getTopicWith("home", language, scope);
    		TopicMap map = _store.getRelatedTopicsWith(topic, language, scope);
    		map.updateRelated();
    		map.updatePointing();
    		map.setPrettyPrint(true);
    		System.out.println(map.toXml());
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void getPointing () throws StoreException, IOException {
    	_reset();
    	try {
    		_store.start();
    		String language = "any";
    		String scope = "chy";
    		ITopic topic = _store.getTopicWith("employeestories", language);
    		TopicMap map = _store.getPointingTopicsWith(topic, language, scope);
    		map.updateRelated();
    		map.updatePointing();
    		map.setPrettyPrint(true);
    		System.out.println(map.toXml());
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void getTopics () throws StoreException, IOException {
    	_reset();
    	try {
    		_store.start();
    		String language = "any";
    		String scope = "chy";
    		IAcumenList<ITopic> topics = _store.getTopicsWith(language, scope);
    		topics.setPrettyPrint(true);
    		System.out.println(topics.toXml());
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void getTopic_getLabel () throws StoreException, IOException {
    	// Problem: The topic label output from XML appears to be
    	// falling back to the topics id rather than using the label
    	// metadata provided.
    	// See: TopicTest.getLabel()
    	_reset();
    	try {
    		_store.start();
    		String language = "any";
    		String scope = "chy";
    		ITopic topic = _store.getTopicWith("home", language, scope);
    		topic.setPrettyPrint(true);
    		System.out.println(topic.toXml());
    		
    		String expected = "Home";
    		String actual = topic.getLabel();
    		
    		assertEquals(expected, actual);
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void getTopics_getLabel () throws StoreException, IOException {
    	// Problem: The topic label output from XML appears to be
    	// falling back to the topics id rather than using the label
    	// metadata provided.
    	// See: TopicTest.getLabel()
    	_reset();
    	try {
    		_store.start();
    		Language language = Language.Any;
    		String scope = "chy";
    		IAcumenList<ITopic> topics = _store.getTopicsWith(language,scope);
    		
    		for (ITopic topic: topics) {
    			if (topic.getMeta().size() > 0) {
    				String expected = topic.getMeta().get("label", language, scope);
    				String actual = topic.getLabel();
    				if (Is.NotNullOrEmpty(expected)) {
    					topic.setPrettyPrint(true);
    					System.out.println(topic.toXml());
    					assertEquals(expected, actual);
    				}
    			}
    		}
    		
    	} finally {
    		_store.stop();
    	}
    }

}
