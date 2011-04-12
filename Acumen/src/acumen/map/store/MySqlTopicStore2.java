/**
 * User: Guy J. Murphy
 * Date: Feb 8, 2010
 * Time: 2:53:23 PM
 * File: MySqlTopicStore2.java
 */
package acumen.map.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import acumen.data.StoreException;
import acumen.map.model.Association;
import acumen.map.model.IAssociation;
import acumen.map.model.ITopic;
import acumen.map.model.Language;
import acumen.map.model.MetaData;
import acumen.map.model.NodeTimestampComparator;
import acumen.map.model.Topic;
import acumen.map.model.TopicMap;
import acumen.util.AcumenList;

import acumen.util.IAcumenList;
import acumen.util.Is;

/**
 * @author Guy J. Murphy
 */
public class MySqlTopicStore2 extends MySqlTopicStore {
	
	private static String _topicQuerySqlTemplateConstrained;
	private static String _topicViewQueryConstrained;
	
	protected static String getTopicQuerySqlTemplateConstrained (String topicid, Language language, String scope) {
		
		//if (Is.NullOrEmpty(_topicQuerySqlTemplateConstrained)) {
			String languageCode = Integer.toString(language.getValue());
			
			_topicQuerySqlTemplateConstrained = "select  T.act_code,\n" + 
					"        M.acm_name topic_meta_name,\n" + 
					"        M.acm_value topic_meta_value,\n" + 
					"        M.acm_lang topic_meta_language,\n" + 
					"        M.acm_scope topic_meta_scope,\n" + 
					"        O.aco_reference occurence_reference,\n" + 
					"        O.aco_behaviour occurence_behaviour,\n" + 
					"        O.aco_lang occurence_language, \n" + 
					"        O.aco_scope occurence_scope, \n" + 
					"        O.aco_role occurence_role,\n" + 
					"        A.aca_code assoc_id,\n" + 
					"        A.aca_source assoc_source,\n" + 
					"        A.aca_reference assoc_reference,\n" + 
					"        A.aca_type assoc_type,\n" + 
					"        A.aca_role assoc_role,\n" + 
					"        A.aca_scope assoc_scope,\n" + 
					"        AM.acm_name assoc_meta_name, \n" + 
					"        AM.acm_value assoc_meta_value, \n" + 
					"        AM.acm_lang assoc_meta_language,\n" + 
					"        AM.acm_scope assoc_meta_scope\n" + 
					"from topic T\n" + 
					"\n" + 
					"left join metadata M \n" + 
					"on \n" + 
					"        T.act_code = M.acm_ref and M.acm_node_type = 'topic' and " + String.format("M.acm_scope = '%s' and M.acm_lang = %s", scope, languageCode) +
					"\n\n" + 
					"left join occurence O\n" + 
					"on\n" + 
					"        T.act_code = O.aco_actref and " + String.format("O.aco_scope = '%s' and O.aco_lang = %s", scope, languageCode) +
					"\n\n" + 
					"left join association A\n" + 
					"on\n" + 
					"        T.act_code = A.aca_source and " + String.format("A.aca_scope = '%s' and A.aca_scope = %s", scope, languageCode) +
					"\n\n" + 
					"left join metadata AM\n" + 
					"on\n" + 
					"        A.aca_code=AM.acm_ref and AM.acm_node_type = 'assoc' and " + String.format("AM.acm_scope = '%s' and AM.acm_scope = %s", scope, languageCode) + 
					"\n\n%s\n\n" + 
					"order by T.act_code\n";
		//}
		String whereClause = Is.NotNullOrEmpty(topicid) ? String.format("where T.act_code = '%s'", topicid) : "";
		return String.format(_topicQuerySqlTemplateConstrained, whereClause);
	}
	
	protected static String getTopicViewQueryConstrained (Language language, String scope) {
		//if (Is.NullOrEmpty(_topicViewQueryConstrained)) {
			String languageCode = Integer.toString(language.getValue());
			
			_topicViewQueryConstrained = "select\n" + 
					"	A.aca_code,\n" + 
					"	A.aca_source as assoc_source,\n" + 
					"	A.aca_reference as assoc_ref,\n" + 
					"	A.aca_type as assoc_type,\n" + 
					"	A.aca_role as assoc_role,\n" + 
					"	A.aca_scope as assoc_scope,\n" + 
					"	AM.acm_ref as assoc_meta_for,\n" + 
					"	AM.acm_lang as assoc_meta_lang,\n" + 
					"	AM.acm_scope as assoc_meta_scope,\n" + 
					"	AM.acm_name as assoc_meta_name,\n" + 
					"	AM.acm_value as assoc_meta_value,\n" + 
					"	T.act_code,\n" + 
					"	TM.acm_ref as topic_meta_for,\n" + 
					"	TM.acm_lang as topic_meta_lang,\n" + 
					"	TM.acm_scope as topic_meta_scope,\n" + 
					"	TM.acm_name as topic_meta_name,\n" + 
					"	TM.acm_value as topic_meta_value,\n" + 
					"	O.aco_actref as occur_for,\n" + 
					"	O.aco_lang as occur_lang,\n" + 
					"	O.aco_scope as occur_scope,\n" + 
					"	O.aco_behaviour as occur_behaviour,\n" + 
					"	O.aco_reference as occur_ref,\n" + 
					"	O.aco_role as occur_role,\n" + 
					"	TA.aca_code as topic_assoc_id,\n" + 
					"	TA.aca_source as topic_assoc_source,\n" + 
					"	TA.aca_reference as topic_assoc_ref,\n" + 
					"	TA.aca_type as topic_assoc_type,\n" + 
					"	TA.aca_role as topic_assoc_role,\n" + 
					"	TA.aca_scope as topic_assoc_scope,\n" + 
					"	TAM.acm_ref as topic_assoc_meta_for,\n" + 
					"	TAM.acm_lang as topic_assoc_meta_lang,\n" + 
					"	TAM.acm_scope as topic_assoc_meta_scope,\n" + 
					"	TAM.acm_name as topic_assoc_meta_name,\n" + 
					"	TAM.acm_value as topic_assoc_meta_value\n" + 
					"from\n" + 
					"	association A\n" + 
					"left join\n" + 
					"	 metadata AM\n" + 
					"on\n" + 
					"   A.aca_code = AM.acm_ref and " + String.format("AM.acm_scope = '%s' and AM.acm_lang = %s", scope, languageCode) +
					"\nleft join\n" + 
					"	 topic T\n" + 
					"on\n" + 
					"   %s\n" + // topic clause 
					"left join\n" + 
					"	 metadata TM\n" + 
					"on\n" + 
					"  T.act_code = TM.acm_ref and " + String.format("TM.acm_scope = '%s' and TM.acm_scope = %s", scope, languageCode) +  
					"\nleft join\n" + 
					"	 occurence O\n" + 
					"on\n" + 
					"  T.act_code = O.aco_actref and " + String.format("O.aco_scope = '%s' and O.aco_lang = %s", scope, languageCode) +
					"\nleft join\n" + 
					"	 association TA\n" + 
					"on\n" + 
					"  T.act_code = TA.aca_source and " + String.format("TA.aca_scope = '%s'", scope) + 
					"\nleft join\n" + 
					"	 metadata TAM\n" + 
					"on \n" + 
					"  TA.aca_code = TAM.acm_ref and " + String.format("TAM.acm_scope = '%s' and TAM.acm_lang = %s", scope, languageCode) +
					"\n";
					// where clause
					//"where A.aca_source = 'home' and A.aca_scope = 'chy'";
		//}
		return _topicViewQueryConstrained;
	}
	
	
	
	public MySqlTopicStore2 (String server, String database, String user, String password) {
        super(server, database, user, password);
    }
	
	// TODO: remove at some point, this method is only to test a certain approach
	public ITopic getTopic_Experimental (String id) throws StoreException {
    	// do we have a topic?
    	if (this.topicExists(id)) {
    		Topic topic = new Topic(id);
    		// get the topic metadata
    		_resolveMetaDataFor(true, topic);
    		// resolve the occurences
    		this.resolveOccurencesFor(true, topic);
    		// now resolve the associations
    		this.resolveAssociationsFor(true, topic);
    		return topic;
    	} else {
    		return null;
    	}
    }
	
	public ITopic getTopicWith (String id, Language language, String scope) throws StoreException {
		String sql = getTopicQuerySqlTemplateConstrained(id, language, scope);
        if (this.topicExists(id)) {
            //ITopic topic = new Topic(id);
            IAcumenList<ITopic> topics = _processTopicQuery(sql);
            ITopic topic = (topics.size() > 0) ? topics.get(0) : null;
            topic.setCurrentLanguage(language);
            topic.setCurrentScope(scope);
            return topic;
        } else {
            return null;
        }
	}
		
	public ITopic getTopicWith (String id, Language language) throws StoreException {
		return this.getTopicWith(id, language, Topic.DEFAULT_SCOPE);
	}
	
	public ITopic getTopicWith (String id, String scope) throws StoreException {
		return this.getTopicWith (id, Topic.DEFAULT_LANGUAGE, scope);
	}
	
	public ITopic getTopicWith (String id, String language, String scope) throws StoreException {
		return this.getTopicWith(id, Language.parseLanguageCode(language), scope);
	}

	public IAcumenList<ITopic> getTopicsWith (Language language, String scope) throws StoreException {
		String sql = getTopicQuerySqlTemplateConstrained("", language, scope);
		IAcumenList<ITopic> topics = _processTopicQuery(sql);
		// we need to set the actual current language and scope of each topic
		for (ITopic topic: topics) {
			topic.setCurrentLanguage(language);
			topic.setCurrentScope(scope);
		}
		return topics;
	}
	
	public IAcumenList<ITopic> getTopicsWith (String language, String scope) throws StoreException {
		return this.getTopicsWith(Language.parseLanguageCode(language), scope);
	}
	
	private TopicMap _getRelatedTopicsQuery (ITopic topic, String topicClause, String whereClause, Language language, String scope) throws StoreException {
		return _getRelatedTopicsQuery(topic, topicClause, whereClause, language, scope, true);
	}

    private TopicMap _getRelatedTopicsQuery (ITopic topic, String topicClause, String whereClause, Language language, String scope, boolean processOccurences) throws StoreException {
        TopicMap map = new TopicMap();
        // If we don't set the current language and scope
        // then when we come to #updateRelated and #updatePointing
        // we won't actually resolve any relationships as they'll
        // be examining associations with 'Any' language and '*' scope.
        map.setCurrentLanguage(language);
        map.setCurrentScope(scope);

        String assoc_id, topic_id, topic_assoc_id;

        IAssociation currentAssoc = null;
        ITopic currentTopic = null;
        IAssociation currentTopicAssoc;

        String sql = String.format(getTopicViewQueryConstrained(language, scope), topicClause) + whereClause;

        ResultSet reader = null;
        try {
            try {
                reader = this.read(sql);
                while (reader.next()) {
                    // 1. are we reading a new association?
                    assoc_id = reader.getString("aca_code");
                    if (currentAssoc == null || !currentAssoc.getId().equals(assoc_id)) {
                        // yes we have a new association
                        if (map.getAssociations().containsKey(assoc_id)) {
                            currentAssoc = map.getAssociations().get(assoc_id);
                        } else {
                            currentAssoc = new Association(topic, assoc_id);
                            map.addAssociation(currentAssoc);
                        }
                    }
                    // 1.1 read the assoc properties
                    if (topic == null) {
                        currentAssoc.setSource(reader.getString("assoc_source"));
                    }
                    if (currentAssoc.getReference() == null) currentAssoc.setReference(reader.getString("assoc_ref"));
                    if (currentAssoc.getType() == null) currentAssoc.setType(reader.getString("assoc_type"));
                    if (currentAssoc.getRole() == null) currentAssoc.setRole(reader.getString("assoc_role"));
                    if (currentAssoc.getScope() == null) currentAssoc.setScope(reader.getString("assoc_scope"));
                    // 1.2 read the assoc metadata
                    currentAssoc.getMeta().setMetaData(
                        reader.getString("assoc_meta_name"),
                        Language.getLanguage(reader.getInt("assoc_meta_lang")),
                        reader.getString("assoc_meta_scope"),
                        reader.getString("assoc_meta_value")
                    );

                    // 2. are we reading a new topic?
                    topic_id = reader.getString("act_code");
                    if (topic_id != null && (currentTopic == null || !currentTopic.getId().equals(topic_id))) {
                        // yes we have a new topic
                        if (map.getTopics().containsKey(topic_id)) {
                            currentTopic = map.getTopics().get(topic_id);
                        } else {
                            currentTopic = new Topic(topic_id);
                            map.addTopic(currentTopic);
                        }
                        // 2.1 read the topic metadata
                        currentTopic.getMeta().setMetaData(
                            reader.getString("topic_meta_name"),
                            Language.getLanguage(reader.getInt("topic_meta_lang")),
                            reader.getString("topic_meta_scope"),
                            reader.getString("topic_meta_value")
                        );
                        // 2.2 read the topic occurence data
                        if (processOccurences) {
                            String occurenceReference = reader.getString("occur_ref");
                            if (occurenceReference != null) {
                                String occurenceBehaviour = reader.getString("occur_behaviour");
                                Language occurenceLanguage = Language.getLanguage(reader.getInt("occur_lang"));
                                String occurenceScope = reader.getString("occur_scope");
                                String occurenceRole = reader.getString("occur_role");
                                currentTopic.getOccurences().setOccurence(occurenceReference, occurenceBehaviour, occurenceLanguage, occurenceScope, occurenceRole);
                            }
                        }
                    } else { // no it'value possibly still the same topic
                        // check if we're reading any topic data for this
                        // association at all
                        if (currentTopic != null) {
                            currentTopic.getMeta().setMetaData(
                                reader.getString("topic_meta_name"),
                                Language.getLanguage(reader.getInt("topic_meta_lang")),
                                reader.getString("topic_meta_scope"),
                                reader.getString("topic_meta_value")
                            );
                            // 2.2 read the topic occurence data
                            if (processOccurences) {
                                String occurenceReference = reader.getString("occur_ref");
                                if (occurenceReference != null) {
                                    String occurenceBehaviour = reader.getString("occur_behaviour");
                                    Language occurenceLanguage = Language.getLanguage(reader.getInt("occur_lang"));
                                    String occurenceScope = reader.getString("occur_scope");
                                    String occurenceRole = reader.getString("occur_role");
                                    currentTopic.getOccurences().setOccurence(occurenceReference, occurenceBehaviour, occurenceLanguage, occurenceScope, occurenceRole);
                                }
                            }
                        }
                    }
                    // 2.2 read the topic associations... these are cousins to the first level of associations
                    topic_assoc_id = reader.getString("topic_assoc_id");

                    // Modified by BK - 080717
                    if (topic_assoc_id != null && currentTopic != null) {
                        if (currentTopic.getAssociations().containsKey(topic_assoc_id)) {
                            currentTopicAssoc = currentTopic.getAssociations().get(topic_assoc_id);
                        } else {
                            currentTopicAssoc = new Association(currentTopic, topic_assoc_id);
                            currentTopic.getAssociations().add(currentTopicAssoc);
                        }
                        // 2.3 read the topic associations properties
                        if (currentTopicAssoc.getReference() == null) currentTopicAssoc.setReference(reader.getString("topic_assoc_ref"));
                        if (currentTopicAssoc.getType() == null) currentTopicAssoc.setType(reader.getString("topic_assoc_type"));
                        if (currentTopicAssoc.getRole() == null) currentTopicAssoc.setRole(reader.getString("topic_assoc_role"));
                        if (currentTopicAssoc.getScope() == null) currentTopicAssoc.setScope(reader.getString("topic_assoc_scope"));

                        // Modified by BK - 080717 (Acumen/C#)
                        /*
                        currentTopicAssoc.Meta.SetMetaData(
                                ReadString(reader, "topic_assoc_meta_name"),
                                (Language)ReadInt(reader, "topic_assoc_meta_lang"),
                                ReadString(reader, "topic_assoc_meta_scope"),
                                ReadString(reader, "topic_assoc_meta_value")
                            );
                        */

                        // 2.4 read the topic associations metadata
                        MetaData metaDatum = new MetaData(
                            currentTopicAssoc.getId(),
                            reader.getString("topic_assoc_meta_name"),
                            Language.getLanguage(reader.getInt("topic_assoc_meta_lang")),
                            reader.getString("topic_assoc_meta_scope"),
                            reader.getString("topic_assoc_meta_value")
                        );

                        if (!currentTopicAssoc.getMeta().contains(metaDatum)) {
                            currentTopicAssoc.getMeta().add(metaDatum);
                        }
                    }
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
        return map;
    }
    
    public TopicMap getRelatedTopicsWith (ITopic topic, String language, String scope) throws StoreException {
    	return this.getRelatedTopicsWith(topic, Language.parseLanguageCode(language), scope);
    }
    
    public TopicMap getRelatedTopicsWith (ITopic topic, Language language, String scope) throws StoreException {
		String topicClause = "T.act_code = A.aca_reference";
		String whereClause = String.format("where A.aca_source = '%s' and A.aca_scope = '%s'", topic.getId(), scope);
		return _getRelatedTopicsQuery(topic, topicClause, whereClause, language, scope);
	}
    
    public TopicMap getPointingTopicsWith(ITopic topic, String language, String scope) throws StoreException {
    	return this.getPointingTopicsWith(topic, Language.parseLanguageCode(language), scope);
    }
    
    public TopicMap getPointingTopicsWith(ITopic topic, Language language, String scope) throws StoreException {
    	String topicClause = "T.act_code = A.aca_source";
		String whereClause = String.format("where A.aca_reference = '%s' and A.aca_scope = '%s'", topic.getId(), scope);
		return _getRelatedTopicsQuery(null, topicClause, whereClause, language, scope);
    }
    
    public AcumenList<ITopic> getLeavesFor (ITopic topic, int limit, boolean resolveOccurences, String typeName, String roleName, String language, String scope) throws StoreException {
    	return this.getLeavesFor(topic, limit, resolveOccurences, typeName, roleName, Language.parseLanguageCode(language), scope);
    }
    
    public AcumenList<ITopic> getLeavesFor (ITopic topic, int limit, boolean resolveOccurences, String typeName, String roleName, Language language, String scope) throws StoreException {
		// Get a map of the topics pointing at the primary topic provided.
		TopicMap leafMap = Is.NullOrEmpty(roleName) ? this.getPointingTopics(topic, typeName, resolveOccurences) : this.getPointingTopics(topic, typeName, roleName, scope, resolveOccurences);
		leafMap.getTopics().filterByLanguage(language);
		AcumenList<ITopic> entries = new AcumenList<ITopic>(leafMap.getTopics().values());
		Collections.sort(entries, new NodeTimestampComparator());
		int size = (entries.size() <= limit) ? entries.size() : limit;
		AcumenList<ITopic> result = new AcumenList<ITopic>(entries.subList(0, size));
		return result;
	}

}
