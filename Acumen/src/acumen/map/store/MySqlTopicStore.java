package acumen.map.store;

import acumen.data.*;
import acumen.util.AcumenDictionary;
import acumen.util.AcumenList;
import acumen.util.IAcumenDictionary;
import acumen.util.IAcumenList;
import acumen.map.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.util.Map;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 13:46:32
 */
public class MySqlTopicStore extends MySqlStoreBase {

    protected static String TOPIC_TABLE = "topic";

    private static String _topicViewQuery;
    protected static String getTopicViewQuery () {
        //if (Is.NullOrEmpty(_topicViewQuery)) {
            StringBuffer buf = new StringBuffer();
            buf.append("\nselect");
            buf.append("\n	A.aca_code,");
            buf.append("\n	A.aca_source as assoc_source,");
            buf.append("\n	A.aca_reference as assoc_ref,");
            buf.append("\n	A.aca_type as assoc_type,");
            buf.append("\n	A.aca_role as assoc_role,");
            buf.append("\n	A.aca_scope as assoc_scope,");
            buf.append("\n	AM.acm_ref as assoc_meta_for,");
            buf.append("\n	AM.acm_lang as assoc_meta_lang,");
            buf.append("\n	AM.acm_scope as assoc_meta_scope,");
            buf.append("\n	AM.acm_name as assoc_meta_name,");
            buf.append("\n	AM.acm_value as assoc_meta_value,");
            buf.append("\n	T.act_code,");
            buf.append("\n	TM.acm_ref as topic_meta_for,");
            buf.append("\n	TM.acm_lang as topic_meta_lang,");
            buf.append("\n	TM.acm_scope as topic_meta_scope,");
            buf.append("\n	TM.acm_name as topic_meta_name,");
            buf.append("\n	TM.acm_value as topic_meta_value,");
            buf.append("\n	O.aco_actref as occur_for,");
            buf.append("\n	O.aco_lang as occur_lang,");
            buf.append("\n	O.aco_scope as occur_scope,");
            buf.append("\n	O.aco_behaviour as occur_behaviour,");
            buf.append("\n	O.aco_reference as occur_ref,");
            buf.append("\n	O.aco_role as occur_role,");
            buf.append("\n	TA.aca_code as topic_assoc_id,");
            buf.append("\n	TA.aca_source as topic_assoc_source,");
            buf.append("\n	TA.aca_reference as topic_assoc_ref,");
            buf.append("\n	TA.aca_type as topic_assoc_type,");
            buf.append("\n	TA.aca_role as topic_assoc_role,");
            buf.append("\n	TA.aca_scope as topic_assoc_scope,");
            buf.append("\n	TAM.acm_ref as topic_assoc_meta_for,");
            buf.append("\n	TAM.acm_lang as topic_assoc_meta_lang,");
            buf.append("\n	TAM.acm_scope as topic_assoc_meta_scope,");
            buf.append("\n	TAM.acm_name as topic_assoc_meta_name,");
            buf.append("\n	TAM.acm_value as topic_assoc_meta_value");
            buf.append("\nfrom");
            buf.append("\n	association A");
            buf.append("\nleft join");
            buf.append("\n	 metadata AM");
            buf.append("\non");
            buf.append("\n   A.aca_code = AM.acm_ref");
            buf.append("\nleft join");
            buf.append("\n	 topic T");
            buf.append("\non");
            buf.append("\n   %s");
            buf.append("\nleft join");
            buf.append("\n	 metadata TM");
            buf.append("\non");
            buf.append("\n  T.act_code = TM.acm_ref");
            buf.append("\nleft join");
            buf.append("\n	 occurence O");
            buf.append("\non");
            buf.append("\n  T.act_code = O.aco_actref");
            buf.append("\nleft join");
            buf.append("\n	 association TA");
            buf.append("\non");
            buf.append("\n  T.act_code = TA.aca_source");
            buf.append("\nleft join");
            buf.append("\n	 metadata TAM");
            buf.append("\non TA.aca_code = TAM.acm_ref \n");
            _topicViewQuery = buf.toString();
        //}
        return _topicViewQuery;
    }

    private static String _topicQuerySqlTemplate;
    protected static String getTopicQuerySqlTemplate() {
        //if (Is.NullOrEmpty(_topicQuerySqlTemplate)) {
            StringBuffer buf = new StringBuffer();
            buf.append("\n/* 4.. finally add the metadata for associations */");
            buf.append("\nselect");
            buf.append("\n	  act_code, topic_meta_name, topic_meta_value, topic_meta_language, topic_meta_scope,");
            buf.append("\n	  occurence_reference, occurence_behaviour, occurence_language, occurence_scope, occurence_role,");
            buf.append("\n	  assoc_id, assoc_source, assoc_reference, assoc_type, assoc_role, assoc_scope,");
            buf.append("\n	  AM.acm_name assoc_meta_name, ");
            buf.append("\n	  AM.acm_value assoc_meta_value, ");
            buf.append("\n	  AM.acm_lang assoc_meta_language,");
            buf.append("\n	  AM.acm_scope assoc_meta_scope");
            buf.append("\nfrom");
            buf.append("\n	(");
            buf.append("\n	 /* 3.. add the association data to to the topic/metadata/occurence data */");
            buf.append("\n		select ");
            buf.append("\n			   act_code, topic_meta_name, topic_meta_value, topic_meta_language, topic_meta_scope,");
            buf.append("\n			   occurence_reference, occurence_behaviour, occurence_language, occurence_scope, occurence_role,");
            buf.append("\n			   A.aca_code assoc_id,");
            buf.append("\n			   A.aca_source assoc_source,");
            buf.append("\n			   A.aca_reference assoc_reference,");
            buf.append("\n			   A.aca_type assoc_type,");
            buf.append("\n			   A.aca_role assoc_role,");
            buf.append("\n			   A.aca_scope assoc_scope			   ");
            buf.append("\n		from");
            buf.append("\n			(");
            buf.append("\n			 /* 2.. add the topic occurence data to the topic/metadata */");
            buf.append("\n				select");
            buf.append("\n					  act_code, topic_meta_name, topic_meta_value, topic_meta_language, topic_meta_scope,");
            buf.append("\n					  O.aco_reference occurence_reference,");
            buf.append("\n					  O.aco_behaviour occurence_behaviour,");
            buf.append("\n					  O.aco_lang occurence_language,");
            buf.append("\n					  O.aco_scope occurence_scope,");
            buf.append("\n					  O.aco_role occurence_role");
            buf.append("\n				from");
            buf.append("\n					(");
            buf.append("\n					 /* 1.. topic and topic metadata */ 		");
            buf.append("\n					   select");
            buf.append("\n					   		 T.act_code,");
            buf.append("\n							 M.acm_name topic_meta_name,");
            buf.append("\n							 M.acm_value topic_meta_value,");
            buf.append("\n							 M.acm_lang topic_meta_language,");
            buf.append("\n							 M.acm_scope topic_meta_scope ");
            buf.append("\n					   from topic T");
            buf.append("\n					   	   left join metadata M");
            buf.append("\n					   	   on T.act_code = M.acm_ref and M.acm_node_type = 'topic'");
            buf.append("\n                            %s"); // {0}
            buf.append("\n					) TM");
            buf.append("\n					left join occurence O");
            buf.append("\n					on TM.act_code = O.aco_actref");
            buf.append("\n					%s"); // {1}
            buf.append("\n			) TMO");
            buf.append("\n			left join association A");
            buf.append("\n			on TMO.act_code = A.aca_source");
            buf.append("\n	) TMOA");
            buf.append("\n	left join metadata AM");
            buf.append("\n	on TMOA.assoc_id = AM.acm_ref and AM.acm_node_type = 'assoc'");
            buf.append("\n	order by act_code");
            _topicQuerySqlTemplate = buf.toString();
        //}
        return _topicQuerySqlTemplate;
    }

    public MySqlTopicStore (String server, String database, String user, String password) {
        super(server, database, user, password);
    }

    protected IAcumenList<ITopic> _processTopicQuery (String sql, Object ... params) throws StoreException {
        ITopic currentTopic = null;
        IAssociation currentAssociation = null;
        AcumenList<ITopic> topics = new AcumenList<ITopic>();
        ResultSet reader = null;
        try {
            try {
                reader = this.read(sql, params);
                while (reader.next()) {
                    // get the topic id that the current record concerns
                    String topicId = reader.getString("act_code");
                    // then check if we're concerned with a new topic
                    if (topicId != null && (currentTopic == null || !currentTopic.getId().equals(topicId))) {
                        // we have a new topic
                        currentTopic = new Topic(topicId);
                        topics.add(currentTopic);
                        currentAssociation = null;
                    }
                    // now it's time to check if we're dealing with a new association
                    String associationId = reader.getString("assoc_id");
                    if (associationId != null && ((currentAssociation != null && !currentAssociation.getId().equals(associationId) || currentAssociation == null))) {
                        // we have a new association
                        String reference = reader.getString("assoc_reference");
                        String type = reader.getString("assoc_type");
                        String role = reader.getString("assoc_role");
                        String scope = reader.getString("assoc_scope");
                        currentAssociation = new Association(currentTopic, associationId, reference, type, role, scope);
                        currentTopic.addAssociation(currentAssociation);
                    }
                    // note: it is safe to add duplicate metadata and occurences as the underlying
                    //          collection checks if an item already exists with the specified name, value, scope
                    //          parameters and removes it if so...
                    //          _although_ this is not necessarily the most efficient way in which to handle this process
                    //          the overhead is extremely light, and it's logically simpler
                    // check for occurence data
                    String occurenceReference = reader.getString("occurence_reference");
                    if (occurenceReference != null) {
                        String occurenceBehaviour = reader.getString("occurence_behaviour");
                        Language occurenceLanguage = Language.getLanguage(reader.getInt("occurence_language"));
                        String occurenceScope = reader.getString("occurence_scope");
                        String occurenceRole = reader.getString("occurence_role");
                        currentTopic.getOccurences().setOccurence(occurenceReference,occurenceBehaviour,occurenceLanguage,occurenceScope,occurenceRole);
                    }
                    // check if we have any topic metadata
                    String topicMetaName = reader.getString("topic_meta_name");
                    if (topicMetaName != null) {
                        // we have metadata
                        String topicMetaValue = reader.getString("topic_meta_value");
                        Language topicMetaLanguage = Language.getLanguage(reader.getInt("topic_meta_language"));
                        String topicMetaScope = reader.getString("topic_meta_scope");
                        currentTopic.getMeta().setMetaData(topicMetaName,topicMetaLanguage,topicMetaScope,topicMetaValue);
                    }
                    // and lastly check if we have any association metadata
                    String assocMetaName = reader.getString("assoc_meta_name");
                    if (assocMetaName != null) {
                        // we have association metadata
                        String assocMetaValue = reader.getString("assoc_meta_value");
                        Language assocMetaLanguage = Language.getLanguage(reader.getInt("assoc_meta_language"));
                        String assocMetaScope = reader.getString("assoc_meta_scope");
                        currentAssociation.getMeta().setMetaData(assocMetaName,assocMetaLanguage,assocMetaScope,assocMetaValue);
                    }
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }

        return topics;
    }

    private TopicMap _getRelatedTopicsQuery (ITopic topic, String topicClause, String whereClause) throws StoreException {
		return _getRelatedTopicsQuery(topic, topicClause, whereClause, false);
	}

    private TopicMap _getRelatedTopicsQuery (ITopic topic, String topicClause, String whereClause, boolean processOccurences) throws StoreException {
        TopicMap map = new TopicMap();

        String assoc_id, topic_id, topic_assoc_id;

        IAssociation currentAssoc = null;
        ITopic currentTopic = null;
        IAssociation currentTopicAssoc;

        String sql = String.format(getTopicViewQuery(), topicClause) + whereClause;

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

                        // Modified by BK - 080717
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

    public String[] getTopicIds () throws StoreException {
        try {
            ResultSet reader = null;
            try {
                reader = this.read("select act_code from topic");
                ArrayList<String> result = new ArrayList<String>();
                while (reader.next()) {
                    result.add(reader.getString("act_code"));    
                }
                return result.toArray(new String[result.size()]);
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }

    public boolean topicExists (String id) throws StoreException {
        return this.exists("select act_code from topic where act_code=?", id);
    }

    public ITopic createTopic (String id) throws StoreException {
        if (!this.topicExists(id)) {
            this.exec("insert into topic (act_code) values (?)", id);
            return new Topic(id);
        } else {
            throw new StoreException("The topic you are trying to create already exists.");
        }
    }

    public ITopic createTopic () throws StoreException {
        return this.createTopic(UUID.randomUUID().toString());
    }

    public void removeTopic (String id) throws StoreException {
	    this.exec("delete from metadata where acm_ref=?", id);
		this.exec("delete from occurence where aco_actref=?", id);
		this.exec("delete from metadata where acm_ref in (select aca_code from association where aca_source=?)", id);
		this.exec("delete from association where aca_source=?", id);
		this.exec("delete from topic where act_code=?", id);
    }

    public void addTopic (ITopic topic) throws StoreException {
			// do we have a previous version of this topic that needs cleaned up?
			if (this.topicExists(topic.getId())) {
				// clean up previous topic
				this.removeTopic(topic.getId());
			}
			// create the new topic
			this.createTopic(topic.getId());
			// add the topics metadata
			for (IMetaData meta: topic.getMeta()) {
				this.setMetaData(meta.getParentId(), meta.getName(), meta.getValue(), meta.getLanguage(), meta.getScope(), "topic");
			}
			// add the topics occurences
			for (IOccurence occurence: topic.getOccurences()) {
				this.setOccurence(occurence.getParent().getId(), occurence.getReference(), occurence.getBehaviour(), occurence.getLanguage(), occurence.getScope(), occurence.getRole(), false);
			}
			// add the topics associations
			for (IAssociation association: topic.getAssociations().values()) {
				this.addAssociation(association);
			}
		}

    public ITopic getTopic (String id) throws StoreException {
        String sql = String.format(getTopicQuerySqlTemplate(), "where T.act_code=?", "");
        if (this.topicExists(id)) {
            //ITopic topic = new Topic(id);
            IAcumenList<ITopic> topics = _processTopicQuery(sql, id);
            return (topics.size() > 0) ? topics.get(0) : null;
        } else {
            return null;
        }
    }

    public IAcumenList<ITopic> getTopics () throws StoreException {
		String sql = String.format(getTopicQuerySqlTemplate(), "", "");
		return _processTopicQuery(sql);
	}

    public IAcumenList<ITopic> getTopicsLike (String pattern) throws StoreException {
		String sqlPattern = pattern.replace("*", "%");
		String sql = String.format(getTopicQuerySqlTemplate(), "where T.act_code like ?", "");
		return _processTopicQuery(sql, sqlPattern);
	}

    public IAcumenList<ITopic> getOrphans () throws StoreException {
		String sql = String.format(getTopicQuerySqlTemplate(), "where T.act_code not in (SELECT aca_reference FROM association)", "");
		return _processTopicQuery(sql);
    }

    public IAcumenList<ITopic> getTopicsByMetadata (String name, String value) throws StoreException {
		String sql = String.format(getTopicQuerySqlTemplate(), "", "WHERE TM.act_code IN (SELECT acm_ref FROM metadata WHERE acm_name=? AND acm_value=?)");
		return _processTopicQuery(sql, name, value);
	}

    public IAcumenList<ITopic> getTopicsByMetadataLike (String name, String pattern) throws StoreException {
		String sql = String.format(getTopicQuerySqlTemplate(), "", "WHERE TM.act_code IN (SELECT acm_ref FROM metadata WHERE acm_name=? AND acm_value LIKE ?)");
		return _processTopicQuery(sql, name, pattern);
	}

    private IAcumenList<ITopic> getTopicsByMetadataAsDate (String name, Date value, String predicate) {
        /*** IMPLEMENT ***/
		return null;
	}

    public IAcumenList<ITopic> getTopicsByMetadataAsDate (String name, Date value) {
		return this.getTopicsByMetadataAsDate(name, value, "=");
	}

    public IAcumenList<ITopic> getTopicsByMetadataAsGreaterDate (String name, Date value) {
		return this.getTopicsByMetadataAsDate(name, value, ">");
	}

    public IAcumenList<ITopic> getTopicsByMetadataAsLessDate (String name, Date value) {
		return this.getTopicsByMetadataAsDate(name, value, "<");
	}

    public AcumenList<ITopic> getTopicsByMetadataAsBetweenDates (String name, Date start, Date end) {
		/*** IMPLEMENT ***/
		return null;
	}

    public TopicMap getRelatedTopics (ITopic topic) throws StoreException {
		String topicClause = "T.act_code = A.aca_reference";
		String whereClause = String.format("where A.aca_source = '%s'", topic.getId());
		return _getRelatedTopicsQuery(topic, topicClause, whereClause);
	}

    public TopicMap getRelatedTopics (ITopic topic, String assocType) throws StoreException {
		String whereClause = String.format("where A.aca_source = '%s' and A.aca_type = '%s'", topic.getId(), assocType);
		String topicClause = "T.act_code = A.aca_reference";
		return _getRelatedTopicsQuery(topic, topicClause, whereClause);
	}

    public TopicMap getRelatedTopics (ITopic topic, String assocType, String assocRole) throws StoreException {
		String whereClause = String.format("where A.aca_source = '%s' and A.aca_type = '%s' and A.aca_role = '%s'", topic.getId(), assocType, assocRole);
		String topicClause = "T.act_code = A.aca_reference";
		return _getRelatedTopicsQuery(topic, topicClause, whereClause);
	}

    public TopicMap getCorelatedTopics (ITopic topic, String assocType, String assocRole1, String assocRole2) throws StoreException {
		return this.getCorelatedTopics(topic, assocType, assocRole1, assocType, assocRole2);
	}

	public TopicMap getCorelatedTopics (ITopic topic, String assocType1, String assocRole1, String assocType2, String assocRole2) throws StoreException {
		return this.getCorelatedTopics(topic, assocType1, assocRole1, assocType2, assocRole2, false);
	}

    public TopicMap getCorelatedTopics (ITopic topic, String assocType1, String assocRole1, String assocType2, String assocRole2, boolean biDirectional) throws StoreException {
		String topicClause = "T.act_code = A.aca_reference";
		String whereClause;
		if (biDirectional) {
			whereClause = String.format("where A.aca_source in (select aca_reference from association where aca_source = '%s' and aca_type = '%s' and aca_role = '%s' or aca_source = '%s' and aca_type = '%s' and aca_role = '%s')",
				topic.getId(),
				assocType1,
				assocRole1,
                topic.getId(),
                assocType1,
		        assocRole2
			);
		} else {
			whereClause = String.format("where A.aca_source in (select aca_reference from association where aca_source = '%s' and aca_type = '%s' and aca_role = '%s') and A.aca_type = '%s' and A.aca_role = '%s'",
		    	topic.getId(),
				assocType1,
				assocRole1,
				assocType2,
				assocRole2
			);
		}
		return _getRelatedTopicsQuery(topic, topicClause, whereClause);
	}

    public TopicMap getPointingTopics (ITopic topic) throws StoreException {
		String topicClause = "T.act_code = A.aca_source";
		String whereClause = String.format("where A.aca_reference = '%s'", topic.getId());
		return _getRelatedTopicsQuery(null, topicClause, whereClause);
	}

    public IAcumenList<IAssociation> getPointingAssociations (ITopic topic) throws StoreException {
        TopicMap map = this.getPointingTopics(topic);
        AcumenList<IAssociation> results = new AcumenList<IAssociation>();
        for (IAssociation assoc: map.getAssociations().values()) {
            if (assoc.getReference().equals(topic.getId())) {
                results.add(assoc);
            }
        }
        return results;
    }

    public TopicMap getPointingTopics (ITopic topic, String assocType) throws StoreException {
		String topicClause = "T.act_code = A.aca_source";
		String whereClause = String.format("where A.aca_reference = '%s' and A.aca_type = '%s'", topic.getId(), assocType);
		return _getRelatedTopicsQuery(null, topicClause, whereClause);
	}

    public TopicMap getPointingTopics (ITopic topic, String assocType, String assocRole) throws StoreException {
		return this.getPointingTopics(topic, assocType, assocRole, "*", false);
	}

    public TopicMap getPointingTopics (ITopic topic, String assocType, String assocRole, boolean processOccurences) throws StoreException {
	    return this.getPointingTopics(topic, assocType, assocRole, "*", processOccurences);
	}

    public TopicMap getPointingTopics (ITopic topic, String assocType, boolean processOccurences) throws StoreException {
		String topicClause = "T.act_code = A.aca_source";
		String whereClause = String.format("where A.aca_reference = '%s' and A.aca_type = '%s'", topic.getId(), assocType);
		return _getRelatedTopicsQuery(null, topicClause, whereClause, processOccurences);
	}

    public TopicMap getPointingTopics (ITopic topic, String assocType, String assocRole, String assocScope, boolean processOccurences) throws StoreException {
		String topicClause = "T.act_code = A.aca_source";
		String whereClause = String.format("where A.aca_reference = '%s' and A.aca_type = '%s' and A.aca_role = '%s' and A.aca_scope = '%s'", topic.getId(), assocType, assocRole, assocScope);
		return _getRelatedTopicsQuery(null, topicClause, whereClause, processOccurences);
	}

    public void addAssociation (IAssociation assoc) throws StoreException {
		// remove the association
		this.removeAssociation(assoc.getId());
		// add the association
		this.createAssociation(assoc.getId(), assoc.getSource(), assoc.getReference(), assoc.getType(), assoc.getRole(), (assoc.getScope() == null ? assoc.getCurrentScope() : assoc.getScope()));
		// add metadata for association
		for (IMetaData meta: assoc.getMeta()) {
			// we call the optomised method because we know we don't have
			// to check for existing metadata before inserting
			this.setMetaData(meta.getParentId(), meta.getName(), meta.getValue(), meta.getLanguage(), meta.getScope(), "assoc");
		}
	}

    public void removeAssociation (String assocId) throws StoreException {
		this.exec("delete from metadata where acm_ref=?", assocId);
		this.exec("delete from association where aca_code=?", assocId);
	}

    public boolean associationExists (String id) throws StoreException {
        return this.exists("select aca_code from association where aca_code=?", id);
	}

    public IAssociation getAssociation (String id) throws StoreException {
		String sql = "select * from association where aca_code=?";
		ResultSet reader = null;
		String source = null;
		String reference = null;
		String type = null;
		String role = null;
		String scope = null;
        try {
            try {
            	reader = this.read(sql, id);
                while (reader.next()) {
                    source = reader.getString("aca_source");
                    reference = reader.getString("aca_reference");
                    type = reader.getString("aca_type");
                    role = reader.getString("aca_role");
                    scope = reader.getString("aca_scope");
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());   
        }
		if (reference == null) {
			return null;
		} else {
			IAssociation assoc = new Association(null, id, reference, type, role, scope);
			assoc.setSource(source);
			this.inlineMetaDataFor(assoc);
			return assoc;
		}
	}

    protected IAssociationDictionary resolveAssociationsFor (boolean inline, ITopic parent, String sql, Object ... params) throws StoreException {
	    IAssociationDictionary assocs = (inline) ? parent.getAssociations() : new AssociationDictionary(parent);
		ResultSet reader = null;
        try {
            try {
            	reader = this.read(sql, params);
                while (reader.next()) {
                    String id = reader.getString("aca_code");
                    String reference = reader.getString("aca_reference");
                    String type = reader.getString("aca_type");
                    String role = reader.getString("aca_role");
                    String scope = reader.getString("aca_scope");
                    IAssociation assoc = new Association(parent, id, reference, type, role, scope);
                    assocs.add(assoc);
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
		return assocs;
	}

    protected IAssociationDictionary resolveAssociationsFor (boolean inline, ITopic parent) throws StoreException {
		return this.resolveAssociationsFor(inline, parent, "select * from association where aca_source=?", parent.getId());
	}

    public IAssociationDictionary getAssociationsFor (ITopic topic) throws StoreException {
		return this.resolveAssociationsFor(false, topic);
	}

    public void inlineAssociationsFor (ITopic topic) throws StoreException {
		this.resolveAssociationsFor(true, topic);
	}

    public IAssociationDictionary getBlindLinks (ITopic parent) throws StoreException {
		String sql = "select * from association where aca_source=? and aca_reference not in (select act_code from topic)";
		return this.resolveAssociationsFor(false, parent, sql, parent.getId());
	}
    
    public IAssociation createAssociation (String assocId, String source, String reference, String type, String role, String scope) throws StoreException {
    	this.removeAssociation(assocId);
		String sql = "insert into association (aca_code, aca_source, aca_reference, aca_type, aca_role, aca_scope) values (?, ?, ?, ?, ?, ?)";
		this.exec(sql, assocId, source, reference, type, role, scope);
		IAssociation assoc = new Association(assocId);
		assoc.setSource(source);
		assoc.setReference(reference);
		assoc.setType(type);
		assoc.setRole(role);
		assoc.setScope(scope);
		return assoc;
	}

    public IAssociation createAssociation (String source, String reference, String type, String role, String scope) throws StoreException {
        String assocId = UUID.randomUUID().toString();
		return this.createAssociation(assocId, source, reference, type, role, scope);
	}

    public IAssociation createAssociation (ITopic parent, String assocId, String reference, String type, String role, String scope) throws StoreException {
		return this.createAssociation(assocId, parent.getId(), reference, type, role, scope);
	}

    public IAssociation createAssociation (ITopic parent, String reference, String type, String role, String scope) throws StoreException {
		return this.createAssociation(parent.getId(), reference, type, role, scope);
	}

    public IAssociation createAssociation (ITopic parent, String reference, String type, String role) throws StoreException {
		return this.createAssociation(parent.getId(), reference, type, role, parent.getCurrentScope());
	}

    protected IMetaDataList resolveMetaDataFor (boolean inline, INode parent, String sql, Object ... params) throws StoreException {
        IMetaDataList data = (inline) ? parent.getMeta() : new MetaDataList(parent);
        ResultSet reader = null;
        try {
            try {
            	reader = this.read(sql, params);
                while (reader.next()) {
                    String name = reader.getString("acm_name");
                    String value = reader.getString("acm_value");
                    String scope = reader.getString("acm_scope");
                    Language lang = Language.getLanguage(reader.getInt("acm_lang"));
                    data.setMetaData(name, lang, scope, value);
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
        return data;
    }

    public IAcumenList<IMetaData> getMetaDataFor (String nodeId) throws StoreException {
        String sql = "select * from metadata where acm_ref=?";
        IAcumenList<IMetaData> data = new AcumenList<IMetaData>();
        ResultSet reader = null;
        try {
            try {
            	reader = this.read(sql, nodeId);
                while (reader.next()) {
                    String name = reader.getString("acm_name");
                    String value = reader.getString("acm_value");
                    String scope = reader.getString("acm_scope");
                    Language lang = Language.getLanguage(reader.getInt("acm_lang"));
                    MetaData item = new MetaData(nodeId, name, lang, scope, value);
                    data.add(item);
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
        return data;
    }

    public IAcumenDictionary<String, String> getValueIndexFor (IAcumenList<String> topicids) throws StoreException {
		return this.getValueIndexFor(topicids, Language.Any);
	}

    public IAcumenDictionary<String, String> getValueIndexFor (IAcumenList<String> topicids, Language language) throws StoreException {
        IAcumenDictionary<String, String> results = new AcumenDictionary<String, String>();
        if (topicids.size() > 0) {
            String sql = "select acm_ref, acm_value from metadata where acm_ref in (%s) and acm_lang=?";
            StringBuilder topicList = new StringBuilder();
            for (int i = 0; i < topicids.size(); i++) {
                topicList.append(String.format("'%s'", topicids.get(i)));
                if (i < topicids.size() - 1) {
                    topicList.append(",");
                }
            }
            try {
                ResultSet reader = null;
                try {
                	reader = this.read(String.format(sql, topicList.toString()), Integer.toString(language.getValue())); // TODO: test this, I'm not sure about it
                    while (reader.next()) {
                        String forNode = reader.getString("acm_ref");
                        String value = reader.getString("acm_value");
                        results.put(forNode, value);
                    }
                } finally {
                    closeReader(reader);
                }
            } catch (SQLException err) {
                throw new StoreException(err.getMessage());
            }
        }
        return results;
    }

    protected IMetaDataList _resolveMetaDataFor (INode parent, String sql, Object ... params) throws StoreException {
		return this.resolveMetaDataFor(false, parent, sql, params);
	}

    protected IMetaDataList _resolveMetaDataFor (boolean inline, INode parent) throws StoreException {
		String sql = "select * from metadata where acm_ref=?";
		return resolveMetaDataFor(inline, parent, sql, parent.getId());
	}

    public IMetaDataList getMetaDataFor (INode parent) throws StoreException {
		return _resolveMetaDataFor(false, parent);
	}

    public void inlineMetaDataFor (INode parent) throws StoreException {
		_resolveMetaDataFor(true, parent);
	}

    protected IMetaDataList resolveMetaDataFor (boolean inline, INode parent, Language language, String scope) throws StoreException {
        String sql = "select * from metadata where acm_ref=? and acm_scope=? and acm_lang=?";
        return resolveMetaDataFor(inline, parent, sql, parent.getId(), scope, Integer.toString(language.getValue()));
    }

    public IMetaDataList getMetaDataFor (INode parent, Language language, String scope) throws StoreException {
        return resolveMetaDataFor(false, parent, language, scope);
    }

    public void inlineMetaDataFor (INode parent, Language language, String scope) throws StoreException {
        this.resolveMetaDataFor(true, parent, language, scope);
    }

    protected IMetaDataList resolveMetaDataFor (boolean inline, INode parent, Language language) throws StoreException {
        String sql = "select * from metadata where acm_ref=? and acm_lang=?";
        return resolveMetaDataFor(inline, parent, sql, parent.getId(), Integer.toString(language.getValue()));
    }

    public void inlineMetaDataFor (INode parent, Language language) throws StoreException {
        this.resolveMetaDataFor(true, parent, language);
    }

    protected IMetaDataList resolveMetaDataFor (boolean inline, INode parent, String scope) throws StoreException {
        String sql = "select * from metadata where acm_ref=? and acm_name=?";
        return resolveMetaDataFor(inline, parent, sql, parent.getId(), scope);
    }

    public IMetaDataList getMetaDataFor (INode parent, String scope) throws StoreException {
        return this.resolveMetaDataFor(false, parent, scope);
    }

    public void inlineMetaDataFor (INode parent, String scope) throws StoreException {
        this.resolveMetaDataFor(true, parent, scope);
    }

    protected IMetaDataList _resolveSpcificMetaDataFor (boolean inline, INode parent, String name, String scope) throws StoreException {
        String sql = "select * from metadata where acm_ref=? and acm_name=? and acm_scope=?";
        return resolveMetaDataFor(inline, parent, sql, parent.getId(), name, scope);
    }

    public IMetaDataList getMetaDataFor (INode parent, String name, String scope) throws StoreException {
        return this.resolveMetaDataFor(false, parent, name, scope);
    }

    public void inlineMetaDataFor (INode parent, String name, String scope) throws StoreException {
        this.resolveMetaDataFor(true, parent, name, scope);
    }

    protected IMetaDataList _resolveMetaDataFor (boolean inline, INode parent, String name, Language language) throws StoreException {
        String sql = "select * from metadata where acm_ref=? and acm_name=? and acm_lang=?";
        return resolveMetaDataFor(inline, parent, sql, parent.getId(), name, Integer.toString(language.getValue()));
    }

    public IMetaDataList getMetaDataFor (INode parent, String name, Language language) throws StoreException {
		return this.resolveMetaDataFor(false, parent, name, language);
	}

    public void inlineMetaDataFor (INode parent, String name, Language language) throws StoreException {
		this.resolveMetaDataFor(true, parent, name, language);
	}

    public IMetaData getMetaDataFor (String topicId, String name, Language language, String scope) throws StoreException {
    	String sql = "select * from metadata where acm_ref=? and acm_name=? and acm_lang=? and acm_scope=?";
        MetaData meta = null;
        ResultSet reader = null;
        try {
            try {
            	reader = this.read(sql, topicId, name, Integer.toString(language.getValue()), scope);
                while (reader.next()) {
                    if (meta == null) {
                        meta = new MetaData(topicId, name, language, scope, "");
                    }
                    meta.setValue(reader.getString("acm_value"));
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
        return meta;
    }
    
	public void setMetaData (String forNode, String name, String value, String language, String scope, String nodeType) throws StoreException {
    	this.setMetaData(forNode, name, value, Language.parseLanguageCode(language), scope, nodeType);
    }

    private void setMetaData (String forNode, String name, String value, Language language, String scope, String nodeType) throws StoreException {
        String insertSql = "insert into metadata (acm_ref, acm_name, acm_value, acm_lang, acm_scope, acm_node_type) values (?, ?, ?, ?, ?, ?)";

        String lang = Integer.toString(language.getValue());
        Object[] parameters = new Object[] {forNode,name,value,lang,scope,nodeType};

        this.removeMetaData(forNode, name, language, scope);
        this.exec(insertSql, parameters);
    }

    public void setTopicMetaData (MetaData data) throws StoreException {
		this.setMetaData(data.getParentId(), data.getName(), data.getValue(), data.getLanguage(), data.getScope(), "topic");
	}

	public void setAssociationMetaData (MetaData data) throws StoreException {
		this.setMetaData(data.getParentId(), data.getName(), data.getValue(), data.getLanguage(), data.getScope(), "assoc");
	}

	public void setTopicMetaData (String forNode, String name, String value, Language language, String scope) throws StoreException {
		this.setMetaData(forNode, name, value, language, scope, "topic");
	}

	public void setAssociationMetaData (String forNode, String name, String value, Language language, String scope) throws StoreException {
		this.setMetaData(forNode, name, value, language, scope, "assoc");
	}
	
	public void removeMetaData (String forNode, String name, String language, String scope) throws StoreException {
		this.removeMetaData(forNode, name, Language.parseLanguageCode(language), scope);
	}

    public void removeMetaData (String forNode, String name, Language language, String scope) throws StoreException {
        this.exec("delete from metadata where acm_ref=? and acm_name=? and acm_lang=? and acm_scope=?", forNode, name, Integer.toString(language.getValue()), scope);
    }

    public void removeMetaData (String forNode, String name, Language language) throws StoreException {
		this.exec("delete from metadata where acm_ref=? and acm_name=? and acm_lang=?", forNode, name, Integer.toString(language.getValue()));
	}

    public void removeMetaData (String forNode, String name, String scope) throws StoreException {
        this.exec("delete from metadata where acm_ref=? and acm_name=? and acm_scope=?", forNode, name, scope);
    }

    public void removeMetaData (String forNode, String name) throws StoreException {
        this.exec("delete from metadata where acm_ref=? and acm_name=? and acm_lang=?language and acm_scope=?", forNode, name);
    }

    public void removeMetaData (String forNode) throws StoreException {
        this.exec("delete from metadata where acm_ref=?", forNode);
    }

    public IAcumenList<ITopic> getTopicsByOccurence (String behaviour, String reference) throws StoreException {
        String sql = String.format(getTopicQuerySqlTemplate(), "", "WHERE Lower(O.aco_reference)=Lower(?) AND O.aco_behaviour=?");
        return _processTopicQuery(sql, reference, behaviour);
    }

    public void addOccurence (IOccurence occurence) throws StoreException {
		this.setOccurence(occurence.getParent().getId(), occurence.getReference(), occurence.getBehaviour(), occurence.getLanguage(), occurence.getScope(), occurence.getRole(), true);
	}

    protected IOccurenceList resolveOccurencesFor (boolean inline, ITopic parent, String sql, Object ... params) throws StoreException {
        IOccurenceList data = (inline) ? parent.getOccurences() : new OccurenceList(parent);
        ResultSet reader = null;
        try {
            try {
            	reader = this.read(sql, params);
                while (reader.next()) {
                    String reference = reader.getString("aco_reference");
                    String behaviour = reader.getString("aco_behaviour");
                    Language lang = Language.getLanguage(reader.getInt("aco_lang"));
                    String scope = reader.getString("aco_scope");
                    String role = reader.getString("aco_role");
                    data.setOccurence(reference, behaviour, lang, scope, role);
                }
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
        return data;
    }

    protected IOccurenceList resolveOccurencesFor (ITopic parent, String sql, Object ... params) throws StoreException {
		return this.resolveOccurencesFor(false, parent, sql, params);
	}

    protected IOccurenceList resolveOccurencesFor (boolean inline, ITopic parent) throws StoreException {
        String sql = "select * from occurence where aco_actref=?";
        return this.resolveOccurencesFor(inline, parent, sql, parent.getId());
    }

    public IOccurenceList getOccurencesFor (ITopic parent) throws StoreException {
        return this.resolveOccurencesFor(false, parent);
    }

    public void inlineOccurencesFor (ITopic parent) throws StoreException {
        this.resolveOccurencesFor(true, parent);
    }
    
    public void setOccurence (String topicId, String reference, String behaviour, String language, String scope, String role) throws StoreException {
    	this.setOccurence(topicId, reference, behaviour, Language.parseLanguageCode(language), scope, role, true);
    }

    private void setOccurence (String topicId, String reference, String behaviour, Language language, String scope, String role, boolean check) throws StoreException {
        if (check) {
        	this.removeOccurence(topicId, behaviour, role, language, scope);
        }
        String sql = "insert into occurence (aco_actref, aco_reference, aco_behaviour, aco_lang, aco_scope, aco_role) values (?, ?, ?, ?, ?, ?)";
        this.exec(sql,
            topicId,
            reference,
            behaviour,
            Integer.toString(language.getValue()),
            scope,
            role
       );
    }
    
    public void removeOccurence(String topicId, String behaviour, String role, String language, String scope) throws StoreException {
    	this.removeOccurence(topicId, behaviour, role, Language.parseLanguageCode(language), scope);
    }
    
    public void removeOccurence(String topicId, String behaviour, String role, Language language, String scope) throws StoreException {
    	this.exec(
    		"delete from occurence where aco_actref=? and aco_behaviour=? and aco_lang=? and aco_scope=? and aco_role=?",
    		topicId, behaviour, Integer.toString(language.getValue()), scope, role
    	);
    }

    protected IOccurenceList resolveOccurencesFor (boolean inline, ITopic parent, Language language, String scope) throws StoreException {
        String sql = "select * from occurence where aco_actref=? and aco_lang=? and aco_scope=?";
        return this.resolveOccurencesFor(inline, parent, sql, parent.getId(), Integer.toString(language.getValue()), scope);
    }

    public IOccurenceList getOccurencesFor (ITopic parent, Language language, String scope) throws StoreException {
        return this.resolveOccurencesFor(false, parent, language, scope);
    }

    public void inlineOccurencesFor (ITopic parent, Language language, String scope) throws StoreException {
        this.resolveOccurencesFor(true, parent, language, scope);
    }

    protected IOccurenceList resolveOccurencesFor (boolean inline, ITopic parent, Language language) throws StoreException {
        String sql = "select * from occurence where aco_actref=? and aco_lang=?";
        return this.resolveOccurencesFor(inline, parent, sql, parent.getId(), Integer.toString(language.getValue()));
    }

    public IOccurenceList getOccurencesFor (ITopic parent, Language language) throws StoreException {
        return this.resolveOccurencesFor(false, parent, language);
    }

    public void inlineOccurencesFor (ITopic parent, Language language) throws StoreException {
        this.resolveOccurencesFor(true, parent, language);
    }

    protected IOccurenceList resolveOccurencesFor (boolean inline, ITopic parent, String scope) throws StoreException {
        String sql = "select * from occurence where aco_actref=? and aco_scope=?";
        return this.resolveOccurencesFor(inline, parent, sql, parent.getId(), scope);
    }

    public IOccurenceList getOccurencesFor (ITopic parent, String scope) throws StoreException {
        return this.resolveOccurencesFor(false, parent, scope);
    }

    public void inlineOccurencesFor (ITopic parent, String scope) throws StoreException {
        this.resolveOccurencesFor(true, parent, scope);
    }

    public IAcumenDictionary<String, String> replaceIdentity (IAcumenDictionary<String, String> ids) throws StoreException {
        AcumenDictionary<String, String> result = new AcumenDictionary<String, String>();
        for (Map.Entry<String,String> entry: ids.entrySet()) {
            if (!this.topicExists(entry.getKey())) {
                result.put(entry.getKey(), String.format("[%s] -> [%s] - NOT DONE, topic does not exist", entry.getKey(), entry.getValue()));
            } else {
                String newTopicId = entry.getValue();
                String oldTopicId = entry.getKey();
                this.exec("update topic set act_code=? where act_code=?", newTopicId, oldTopicId);
                this.exec("update association set aca_source=? where aca_source=?", newTopicId, oldTopicId);
                this.exec("update association set aca_reference=? where aca_reference=?", newTopicId, oldTopicId);
                this.exec("update metadata set acm_ref=? where acm_ref=?", newTopicId, oldTopicId);
                this.exec("update occurence set aco_actref=? where aco_actref=?", newTopicId, oldTopicId);
                result.put(entry.getKey(), String.format("[%s] -> [%s] - done", entry.getKey(), entry.getValue()));
            }
        }
        return result;
    }

    public void nukeEverythingInTheStore () throws StoreException {
        this.exec("truncate topic");
		this.exec("truncate occurence");
		this.exec("truncate association");
		this.exec("truncate metadata");
    }

}
