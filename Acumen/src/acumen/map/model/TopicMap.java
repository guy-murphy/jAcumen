package acumen.map.model;

import acumen.util.AcumenDictionary;
import acumen.util.AcumenDoubleKeyDictionary;

import acumen.data.xml.XmlWriter;

import java.io.IOException;
import java.util.Map;

/**
 * User: gmurphy
 * Date: 01-Oct-2009
 * Time: 12:15:17
 */
public class TopicMap extends NodeBase {

    public static void createBidirectionalLink (ITopic t1, String t1Type, String t1Role, ITopic t2, String t2Type, String t2Role) {
        t1.getAssociations().setAssociation(t2.getId(), t2Type, t2Role);
        t2.getAssociations().setAssociation(t1.getId(), t1Type, t1Role);
    }

    private ITopicDictionary _topics;
    private IAssociationDictionary _associations;
    private AcumenDoubleKeyDictionary<String,String, AcumenDictionary<String,ITopic>> _related;
    private AcumenDoubleKeyDictionary<String,String, AcumenDictionary<String,ITopic>> _pointing;
    
    public AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> getRelated () {
    	return this.getRelated(true);
    }

    public AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> getRelated (boolean update) {
        if (_related == null) {
            _related = new AcumenDoubleKeyDictionary<String, String, AcumenDictionary<String, ITopic>>();
            if (update) this.updateRelated();
        }
        return _related;
    }
    
    public AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> getPointing () {
    	return this.getPointing(true);
    }

    public AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> getPointing (boolean update) {
        if (_pointing == null) {
            _pointing = new AcumenDoubleKeyDictionary<String, String, AcumenDictionary<String, ITopic>>();
            if (update) this.updatePointing();
        }
        return _pointing;
    }

    public ITopicDictionary getTopics () {
        if (_topics == null) {
            _topics = new TopicDictionary(this);
        }
        return _topics;
    }

    public IAssociationDictionary getAssociations () {
        if (_associations == null) {
            _associations = new AssociationDictionary(this);
        }
        return _associations;
    }

    public void updateAssociationBinding () {
        for (IAssociation assoc: this.getAssociations().values()) {
            ITopic sourceTopic = this.getTopics().get(assoc.getSource());
            if (sourceTopic != null) {
                sourceTopic.addAssociation(assoc);
            }
        }
    }
    
    public void updateRelated () {
    	//_updateRelations(this.getRelated(false), false);
    	// getRelated will call back to here if we don't specify false for the update
    	AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> related = this.getRelated(false);
    	String currentScope = this.getCurrentScope();
    	for (IAssociation assoc: this.getAssociations().values()) {
    		String assocScope = assoc.getCurrentScope();
            if (assocScope.equals(currentScope)) {
            	String assocType = assoc.getType();
            	String assocRole = assoc.getRole();
            	if (related.get(assocType, assocRole) == null) {
            		related.add(assocType, assocRole, new AcumenDictionary<String,ITopic>());
            	}
            	related.get(assocType, assocRole).put(assoc.getId(), this.getTopics().get(assoc.getReference())); // this is the point of divergence
            }
    	}
    }

    public void updatePointing () {
    	//_updateRelations(this.getPointing(false), false);
    	// getPointing will call back to here if we don't specify false for the update
    	AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> pointing = this.getPointing(false);
    	String currentScope = this.getCurrentScope();
        for (IAssociation assoc: this.getAssociations().values()) {
        	String assocScope = assoc.getCurrentScope();
        	if (assocScope.equals(currentScope)) {
        		String assocType = assoc.getType();
            	String assocRole = assoc.getRole();
	            if (pointing.get(assocType,assocRole) == null) {
	            	pointing.add(assocType,assocRole, new AcumenDictionary<String,ITopic>());
	            }
	            pointing.get(assocType,assocRole).put(assoc.getId(), this.getTopics().get(assoc.getSource())); // this is the point of divergence
        	}
        }
    }
    
//    private void _updateRelations (AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> relations, boolean outbound) {
//    	// getPointing will call back to here if we don't specify false for the update
//    	String currentScope = this.getCurrentScope();
//        for (IAssociation assoc: this.getAssociations().values()) {
//        	String assocScope = assoc.getCurrentScope();
//        	if (assocScope.equals(currentScope)) {
//        		String assocType = assoc.getType();
//            	String assocRole = assoc.getRole();
//	            if (relations.get(assocType,assocRole) == null) {
//	            	relations.add(assocType,assocRole, new AcumenDictionary<String,ITopic>());
//	            }
//	            String address = (outbound) ? assoc.getReference() : assoc.getSource(); // this is the point of divergence
//	            relations.get(assocType,assocRole).put(assoc.getId(), this.getTopics().get(address));
//        	}
//        }
//    }

    public ITopic getTopic (String id) {
        return this.getTopics().get(id);
    }

    public void addTopic (ITopic topic) {
        topic.setParent(this);
        this.getTopics().add(topic);
    }

    public IAssociation getAssociation (String id) {
        return this.getAssociations().get(id);        
    }

    public void addAssociation (IAssociation association) {
        this.getAssociations().add(association);
    }
    
    private static void _relationsToXml(XmlWriter xml, String container, AcumenDoubleKeyDictionary<String,String,AcumenDictionary<String,ITopic>> topics) throws IOException {
    	xml.writeEntity(container);
		for (Map.Entry<String, AcumenDictionary<String, AcumenDictionary<String,ITopic>>> outer: topics.entrySet()) {
			xml.writeEntity("related-by");
			xml.writeAttribute("type", outer.getKey());
			for (Map.Entry<String, AcumenDictionary<String, ITopic>> inner: outer.getValue().entrySet()) {
				xml.writeEntity("related-by");
				xml.writeAttribute("role", inner.getKey());
				for (Map.Entry<String, ITopic> topic: inner.getValue().entrySet()) {
					if (topic.getValue() != null) {
						xml.writeEntity("topic");
						xml.writeAttribute("label", topic.getValue().getLabel());
						xml.writeAttribute("id", topic.getValue().getId());
						topic.getValue().getMeta().toXml(xml);
						xml.endEntity();
					}
				}
				xml.endEntity();
			}
			xml.endEntity();
		}
		xml.endEntity();
    }

	public void toXml(XmlWriter xml) throws IOException {
		xml.writeEntity("topic-map");
		this.getMeta().toXml(xml);

		_relationsToXml(xml, "related", this.getRelated());
		_relationsToXml(xml, "pointing", this.getPointing());

		xml.writeEntity("topics");
		for (ITopic topic : this.getTopics().values()) {
			topic.toXml(xml);
		}
		xml.endEntity();
		xml.writeEntity("associations");
		for (IAssociation association : this.getAssociations().values()) {
			association.toXml(xml);
		}
		xml.endEntity();
		xml.endEntity();
	}

}