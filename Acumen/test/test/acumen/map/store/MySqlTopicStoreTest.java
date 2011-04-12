package test.acumen.map.store;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import java.io.IOException;
import org.junit.*;
import org.custommonkey.xmlunit.*;
import org.xml.sax.SAXException;


import acumen.map.store.MySqlTopicStore2;
import acumen.data.StoreException;
import acumen.map.model.*;
import acumen.util.AcumenList;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 15:00:59
 */
public class MySqlTopicStoreTest {

	private MySqlTopicStore2 _store;

    public MySqlTopicStoreTest () throws StoreException {
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
    public void topicExists () throws StoreException {
        _reset();
        try {
            _store.start();
            assertTrue(_store.topicExists("home"));
        } finally {
            _store.stop();
        }
    }

    @Test
    public void getTopic () throws StoreException, IOException, SAXException {
        _reset();
        try {
            _store.start();
            ITopic topic = _store.getTopicWith("home", "any", "chy");
            assertNotNull(topic);
            assertEquals(topic.getId(), "home");
            
            topic.setPrettyPrint(true);
            String result = topic.toXml();
            
            System.out.println(result);
            
            String expected = "";
            
            XMLUnit.setNormalizeWhitespace(true);
            XMLUnit.setNormalize(true);
            Diff diff = new Diff(result, expected);
            DetailedDiff details = new DetailedDiff(diff);

//            if (!details.similar()) {
//                for (Object item: details.getAllDifferences()) {
//                    System.out.println(item.toString());
//                }
//            }
            assertTrue("Get get topic result and expected are similar", details.similar());
            
        } finally {
            _store.stop();
        }
    }
    
    @Test
	public void setMetaData () throws StoreException, IOException {
		_reset();
		try {
			_store.start();
			IMetaData originalMeta = _store.getMetaDataFor("home", "edited-by", Language.parseLanguageCode("any"), "chy");
			String originalValue = originalMeta.getValue();
			System.out.println(String.format("Original value: %s", originalValue));
			_store.setMetaData("home", "edited-by", "Guy Murphy", "any", "chy", "topic");
			IMetaData updatedMeta = _store.getMetaDataFor("home", "edited-by", Language.parseLanguageCode("any"), "chy");
			String newValue = updatedMeta.getValue();
			
			System.out.println(String.format("New value: %s", newValue));
			assertEquals("Guy Murphy", newValue);
			_store.setMetaData("home", "edited-by", originalValue, "any", "chy", "topic");
		} finally {
			_store.stop();
		}
	}

    @Test
    public void getRelatedTopics () throws StoreException, IOException, SAXException {
        _reset();
        try {
            _store.start();
            ITopic topic = _store.getTopicWith("home", "any", "chy");
            assertNotNull(topic);
            TopicMap map = _store.getRelatedTopicsWith(topic, "any", "chy");
            assertNotNull(map);
            map.setPrettyPrint(true);
            String result = map.toXml();
            System.out.println(result);

//            String expected = "";            
//
//            XMLUnit.setNormalizeWhitespace(true);
//            XMLUnit.setNormalize(true);
//            Diff diff = new Diff(result, expected);
//            DetailedDiff details = new DetailedDiff(diff);
//
//            //System.out.println(map.toXml());
//            if (!details.similar()) {
//                for (Object item: details.getAllDifferences()) {
//                    System.out.println(item.toString());
//                }
//            }
//            assertTrue("Get related topics result and expected are similar", details.similar());
            
        } finally {
            _store.stop();
        }
    }
    
    @Test
    public void testCachingProblem () throws StoreException {
    	_reset();
    	try {    		
    	    _store.start();
   	  
    		ITopic t1 = _store.getTopicWith("home","any","chy");    		
    		ITopic t2 = _store.getTopicWith("donations","any","chy"); // acumen_london
    		
    		IOccurence occurence = t2.getOccurences().setOccurence("chy/donations","attachments",null,"chy","Files");     		
    		    		
    	    
    	    IOccurenceList ocurrences1 = t1.getOccurences(); // acumen - 5 occurrences
    	    IOccurenceList ocurrences2 = t2.getOccurences(); // acumen_london - 2 occurrences 
    	        	    
    		System.out.println(t1.getId());
    		System.out.println(t2.getId());
    		
    		//assertFalse("Identities of topics must be differnt.", (t1.getId().equals(t2.getId())));
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void getLeavesFor () throws StoreException, IOException {
    	_reset();
    	try {
    		_store.start();
    		ITopic topic = _store.getTopic("home");
    		AcumenList<ITopic> blogs = _store.getLeavesFor(topic, 3, true, "blog", "page", "any", "chy");
    		blogs.setPrettyPrint(true);
    		System.out.println(blogs.toXml());
    		assertTrue("There should be 3 blogs returned.", (blogs.size() == 3));
    	} finally {
    		_store.stop();
    	}
    }
    
    @Test
    public void updateAssociation () throws StoreException, IOException {
    	_reset();
    	try {
    		_store.start();
    		IAssociation assoc = _store.getAssociation("c9d9779f-86c4-4237-8551-42cf84851613");
    		System.out.println(assoc.toXml());
    	} finally {
    		_store.stop();
    	}
    }

}
