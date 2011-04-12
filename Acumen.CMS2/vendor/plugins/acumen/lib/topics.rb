#!/usr/bin/env ruby
#--
#
# Author:   Guy J. Murphy
# Date:     19/01/2010
# Version:  0.1
#++
#
# = TopicStore - Ruby wrapper/extension of underlying Java implementation
#

import 'acumen.map.model.Association'
import 'acumen.map.model.Occurence'

module Topics
  
  include Behaviour
  
  private
  
  # This method is used by Topics#generate_sitemap
  #
  # A recursive function that takes a topic whose
  # associations are processed and the referenced topic
  # fecthed and placed inline. A bag is included of all
  # topics that can be fetched from, and a guard of
  # topics that have been processed to avoid circular references.
  def process_topic_for_sitemap(topic, bag, guard)
    if !guard.include?(topic)
      topic.get_associations.values.each do |assoc|
        referenced_topic = bag.find{|t| t.get_id == assoc.get_reference}
        if referenced_topic != nil
          guard.push(topic)
          assoc.set_resolved_topic(referenced_topic)
          process_topic_for_sitemap(referenced_topic,bag,guard)
        end
      end
    end
  end
  
  # We test if the string provided
  # starts with the prefix provided.
  def str_starts_with(str, prefix)
    prefix = prefix.to_s
    str[0, prefix.length] == prefix
  end
  
  protected
  
  # We get the topic from the store
  # of the specified identity, filtered
  # by the *current* scope and language.
  #
  # This method takes the parameter:-
  # :resolved => [true|false]
  # which determines whether or not the
  # occurences should be resolved.
  #
  # :assoc => [associaction_id]
  # which designates that the association
  # of the specified ID should be selected
  # from the fetched topic and placed into
  # the view_state.
  def get_topic(id, opts={})
    puts "#> get topic: #{id}"
    @topic_store.start
    # get the topic and resolve the occurences if appropriate
    topic = @topic_store.get_topic_with(id, @current_language, @current_scope)
    resolve_occurences!(topic, @current_map) unless opts[:resolve] == false
    @view_state[:topic] = topic
    # do we need to fetch an association?
    if opts.has_key?(:assoc)
      assoc = topic.get_associations.values.find do |a| 
        a.get_id == opts[:assoc]
      end
      @view_state[:association] = assoc unless assoc.nil?
    end
    return topic
  ensure
    @topic_store.stop
  end
  
  # We get the topic from the store
  # of the specified identity in a form
  # suitable for editing.
  #
  # TODO: Refactor with #get_topic
  def get_topic_for_edit(id)
    puts "#> get topic for edit: #{id}"
    @topic_store.start
    topic = @topic_store.get_topic_with(id, @current_language, @current_scope)
    resolve_raw_occurences!(topic, @current_map)
    topic.set_pretty_print(true)
    @view_state[:topic] = topic
    return topic
  ensure
    @topic_store.stop
  end
  
  # We get the topic from the store of
  # the specified identity, *unfiltered*.
  #
  # This method takes the parameter:-
  # :resolved => [true|false]
  # which determines whether or not the
  # occurences should be resolved.
  def get_complete_topic(id, opts)
    puts "#> get complete topic: #{id}"
    @topic_store.start
    topic = @topic_store.get_topic(id)
    resolve_occurences!(topic, @current_map) unless opts[:resolve] == false
    topic.set_pretty_print(true)    
    @view_state[:complete_topic] = topic
    return topic
  ensure
    @topic_store.stop
  end
  
  # We get a topic-map from the store of related topics
  # for the topic of the specified identity.
  #
  # We do NOT open and close the store
  # for this method, as it should be called
  # from a method that is already taking
  # care of this.
  def get_related_for_topic(topic, view = true)
    # Do we have a cache available to us?
    # If so then check that cache for our
    # product first.
    key = "#related_topics::#{topic.get_id}"
    if not @tmp_cache.nil?
      if @tmp_cache.key? key
        puts "#> get related for: #{topic.get_id}, obtained from local cache."
        return @tmp_cache[key]
      end
    end
    # If we're still here we were unable to get the product from
    # a local cache, so we need to produce it as per normal.
    puts "#> get related for: #{topic.get_id}"
    related = @topic_store.get_related_topics_with(topic, @current_language, @current_scope)
    related.update_related
    #related.update_pointing
    related.set_pretty_print true
    # If there's a local cache available we need
    # to cache the product.
    if not @tmp_cache.nil?
      @tmp_cache[key] = related
    end
    # Place the product in the view state to
    # carry downstream, and then return it.
    @view_state[:related] = related if view
    return related 
  end
  
  # We get a topic-map from the store of related topics
  # for the topic of the specified identity.
  # If this is a topic we've already fecthed
  # for the view-state we should consider using
  # Topics#get_related_for_current
  def get_related_for(id)
    @topic_store.start
    topic = @topic_store.get_topic_with(id, @current_language, @current_scope)
    return get_related_for_topic(topic)
  ensure
    @topic_store.stop
  end
  
  # We get a topic-map from the store of related topics
  # for the current topic in the view state.
  def get_related_for_current (view = true)
    @topic_store.start
    current = @view_state[:topic]
    if current.nil?
      raise 'There is no current topic to fetch related for.'
    else
      return get_related_for_topic(current, view)
    end
  ensure
    @topic_store.stop
  end
  
  # We get a topic-map from the store of pointing topics
  # for the topic of the specified identity.
  #
  # We do NOT open and close the store
  # for this method, as it should be called
  # from a method that is already taking
  # care of this.
  def get_pointing_for_topic(topic, view = true)
    # Do we have a cache available to us?
    # If so then check that cache for our
    # product first.
    key = "#pointing_topics::#{topic.get_id}"
    if not @tmp_cache.nil?
      if @tmp_cache.key? key
        puts "#> get pointing for: #{topic.get_id}, obtained from local cache."
        return @tmp_cache[key]
      end
    end
    # If we're still here we were unable to get the product from
    # a local cache, so we need to produce it as per normal.
    puts "#> get pointing for: #{topic.get_id}"
    pointing = @topic_store.get_pointing_topics_with(topic, @current_language, @current_scope)
    #pointing.update_related
    pointing.update_pointing
    pointing.set_pretty_print true
    # If there's a local cache available we need
    # to cache the product.
    if not @tmp_cache.nil?
      @tmp_cache[key] = pointing
    end
    # Place the product in the view state to
    # carry downstream, and then return it.
    @view_state[:pointing] = pointing if view
    return pointing 
  end
  
  # We get a topic-map from the store of pointing topics
  # for the topic of the specified identity.
  # If this is a topic we've already fecthed
  # for the view-state we should consider using
  # Topics#get_related_for_current
  def get_pointing_for(id)
    @topic_store.start
    topic = @topic_store.get_topic_with(id, @current_language, @current_scope)
    return get_pointing_for_topic(topic)
  ensure
    @topic_store.stop
  end
  
  # We get a topic-map from the store of pointing topics
  # for the current topic in the view state.
  def get_pointing_for_current (view = true)
    @topic_store.start
    current = @view_state[:topic]
    if current.nil?
      raise 'There is no current topic to fetch pointing for.'
    else
      return get_pointing_for_topic(current, view)
    end
  ensure
    @topic_store.stop
  end
  
  # We get a list from the store of all topics
  # to the specified topic with associations of
  # the type and role specified.
  #
  # We do NOT open and close the store
  # for this method, as it should be called
  # from a method that is already taking
  # care of this.
  def get_leaves_for (topic, resolve_occurences, assoc_type, assoc_role, language, scope, size)
    # Do we have a cache available to us?
    # If so then check that cache for our
    # product first.
    key = "#leaves_for::#{topic.get_id}::#{language}::#{scope}::#{assoc_type}::#{assoc_role}"
    if not @tmp_cache.nil?
      if @tmp_cache.key? key
        puts "#> get leaves for: #{topic.get_id}, obtained from local cache."
        return @tmp_cache[key]
      end
    end
    # If we're still here we were unable to get the product from
    # a local cache, so we need to produce it as per normal.
    puts "#> get leaves for: #{topic.get_id}"
    leaves = @topic_store.get_leaves_for(topic, size, resolve_occurences, assoc_type, assoc_role, language, scope)
    leaves.set_pretty_print true
    # If there's a local cache available we need
    # to cache the product.
    if not @tmp_cache.nil?
      @tmp_cache[key] = leaves
    end
    return leaves 
  end

  # We get all the topics pointing at the current topic
  # with associations that have the type and role specified.
  def get_leaves_for_current (assoc_type, assoc_role, size=5)
    @topic_store.start
    current = @view_state[:topic]
    if current.nil?
      raise 'There is no current topic to get leaves for.'
    else
      leaves = self.get_leaves_for(current, true, assoc_type, assoc_role, @current_language, @current_scope, size)
      leaves.each{|leaf| resolve_occurences!(leaf, @current_map)}
      return leaves
    end
  ensure
    @topic_store.stop
  end
  
  # We generate a site map using the identity
  # of Globals::DEFAULT_ID to determine the root topic.
  def generate_sitemap
    begin
      puts "#> generating sitemap"
      @topic_store.start
      # get all the topics that have active members
      # for this scope and language
      bag = @topic_store.get_topics_with(@current_language, @current_scope).select{|topic| topic.get_meta.size != 0 and topic.get_occurences.size != 0}
      guard = []
      # now establish the root topics
      # 1. use the current topic in the view state if it is the root
      root = @view_state[:topic] if @view_state[:topic] != nil && @view_state[:topic].get_id == Globals::DEFAULT_ID
      # 2. check the bag if the current topic is not root
      root = bag.find{|topic| topic.get_id == Globals::DEFAULT_ID} if root.nil?
      # 3. if we still can't find the root, we're screwed
      if not root.nil?
        root.set_current_language(@current_language)
        root.set_current_scope(@current_scope)
        # now build the actual map
        process_topic_for_sitemap(root,bag,guard)
      end
      return root
    ensure
      @topic_store.stop
    end    
  end
  
    # We create a topic, append the appropriate metadata to it
  # and then add it to the topic store.
  def create_topic(id, label)
    puts "#> create topic: #{id}"
    @topic_store.start
    topic = @topic_store.create_topic(id)
    # set up the topic metadata
    topic.set_meta_data('label', @current_language, @current_scope, label)
    creation_metadata(topic)
    # add the initial wiki occurence
    occurence = topic.create_occurence(topic.get_id, 'wiki', @current_language, @current_scope, 'Page')
    occurence.set_content("This is the wiki for the topic '#{topic.get_id}', created by '#{@user.get_name}', on #{DateTime.now.to_s}.\n\nYou may now edit this topic if you wish.")
    create!(occurence, @current_map, :user_id => @user.get_id)
    # add the finished topic to the store
    @topic_store.add_topic(topic)
    return topic
  ensure
    @topic_store.stop
  end
  
  def creation_metadata(topic)
    topic.set_meta_data('created-by', @current_language, @current_scope, @user.get_name)
    topic.set_meta_data('created-by-uid', @current_language, @current_scope, @user.get_id)
    topic.set_meta_data('created-on', @current_language, @current_scope, DateTime.now.to_s)
    topic.set_meta_data('timestamp', @current_language, @current_scope, Time.now.strftime("%y%m%d%H%M%S"))
  end
  
  def edit_metadata(topic)
    topic.set_meta_data('edited-by', @current_language, @current_scope, @user.get_name)
    topic.set_meta_data('edited-by-uid', @current_language, @current_scope, @user.get_id)
    topic.set_meta_data('edited-on', @current_language, @current_scope, DateTime.now.to_s)
    topic.set_meta_data('timestamp', @current_language, @current_scope, Time.now.strftime("%y%m%d%H%M%S"))
  end
  
  # We update the topic wiki of the specified identity
  # with the wiki text provided.`
  def update_wiki (id, wikitext)
    puts "#> update wiki for: #{id}"
    # get the topic that we're doing an update for
    @topic_store.start
    topic = @topic_store.get_topic_with(id, @current_language, @current_scope)
    # now find the occurence that we're interested in
    # note this will return the last eligible occurence
    occurences = topic.get_occurences.find_all{|o| o.get_behaviour == 'wiki' and o.get_role == 'Page'}
    
    occurence = case occurences.length
      when 0 # we have no occurence so must make one
        #occurence = Occurence.new(topic, id, 'wiki', @current_language, @current_scope, 'Page') # TODO: change this its bugged
        occurence = topic.create_occurence(topic.get_id, 'wiki', @current_language, @current_scope, 'Page')
        occurence.set_content("This is the wiki for the topic '#{topic.get_id}', created by '#{@user.get_name}', on #{DateTime.now.to_s}.\n\nYou may now edit this topic if you wish.")
      when 1 # we have the occurence that we need
        occurence = occurences[0]
      else # we have too many occurences
        raise "There is more than one wiki/page occurence for: #{id}"
    end
    
    update_occurence!(occurence, @current_map, wikitext)
    
    edit_metadata(topic)    
    topic_store.add_topic(topic)
    
  ensure
    topic_store.stop
  end
  
  # We take a 'path expression' identifying metadata
  # to be updated with the value provided.
  def update_topic_metadata_from_path(path, value)
    parts = path.split('::') # break the path down into its component parts
    if parts[0] == 'assoc'
      set_assoc_metadata(parts[1], parts[2], parts[3], parts[4], value)
    else
      set_topic_metadata(parts[0], parts[1], parts[2], parts[3], value)
    end
  end
  
  # We update the metadata specified with the provided value.
  # see: #set_metadata
  def set_topic_metadata(id, name, language, scope, value)
    set_metadata(id, name, language, scope, value, 'topic')
  end
  
  # We update the metadata specified with the provided value.
  # see: #set_metadata
  def set_assoc_metadata(id, name, language, scope, value)
    set_metadata(id, name, language, scope, value, 'assoc')
  end
  
  # We update the metadata specified with the provided value.
  def set_metadata(id, name, language, scope, value, type='topic')
    @topic_store.start
    @topic_store.set_meta_data(id, name, value, language, scope, type)
  ensure
    @topic_store.stop
  end
  
  # We remove the metadata with the parameters specified.
  def remove_metadata(id, name, language, scope)
    @topic_store.start
    @topic_store.remove_meta_data(id, name, language, scope)
  ensure
    @topic_store.stop
  end
  
  # We take a 'path expression' identifying an occurence
  # to be updated with the value provided.
  def update_topic_occurence_from_path (path, reference)
    parts = path.split('::') # break the path down into its component parts
    set_topic_occurence(parts[0], parts[1], parts[2], parts[3], parts[4], reference)
  end
  
  # We update the occurence specified with the reference provided.
  def set_topic_occurence(id, behaviour, role, language, scope, reference)
    @topic_store.start
    @topic_store.set_occurence(id, reference, behaviour, language, scope, role)
  ensure
    @topic_store.stop
  end
  
  # We remove the occurence with the parameters specified.
  def remove_topic_occurence(id, behaviour, role, language, scope)
    @topic_store.start
    @topic_store.remove_occurence(id, behaviour, role, language, scope)
  ensure
    @topic_store.stop
  end
  
  # We take a 'path expression' identifying an association
  # property to be updated with the value provided.
  def update_association_from_path (path, value)
    parts = path.split('::') # break the path down into its component parts
    assoc_id = parts[0]
    # we're given as the path EITHER
    # assoc_id::label::label_value OR
    # assoc_id::reference::reference_value
    # What is not provided in the path is provided
    # as the value, so if we're provided the label in
    # the path then the value is for the reference
    # and visa-versa.
    if (parts[1] == 'label')
      label = parts[2]
      reference = value
    else
      reference = parts[2]
      label = value
    end
    update_association(assoc_id, reference, label)
  end
  
  # We remove the association that matches
  # the parameters provided.
  def remove_assoc (from, to, type, role)
    @topic_store.start
    # first we need to obtain the assoc that we're concerned with
    topic = @topic_store.get_topic(from)
    assoc = topic.get_associations.values.find do |a| 
      a.get_reference == to and
      a.get_type == type and
      a.get_role == role
    end
    @topic_store.remove_association(assoc.get_id) unless assoc.nil?
  ensure
    @topic_store.stop
  end
  
  # We update the specified association
  # with the reference and label provided.
  def update_association(id, reference, label)
    @topic_store.start
    assoc = @topic_store.get_association(id)
    assoc.set_reference(reference)
    assoc.set_label(label)
    @topic_store.add_association(assoc)
  ensure
    @topic_store.stop
  end
  
  # We add a new association with the
  # source, reference, label, type, and role provided.
  # If the label is equal to nil, then the label
  # of the topic being pointed to will be used.
  #
  # TODO: consider refactoring the parameter order of this method
  # as its a bit crap. Stick label on the end as optional.
  def add_association(source, reference, label, type, role)
    @topic_store.start
    assoc = @topic_store.create_association(source, reference, type, role, @current_scope)
    if label == nil
      other_topic = @topic_store.get_topic(reference)
      label = other_topic.get_label
    end
    @topic_store.set_meta_data(assoc.get_id, 'label', label, @current_language, @current_scope, 'assoc')
  ensure
    @topic_store.stop
  end
  
  # We remove the specified association.
  def remove_association(id)
    @topic_store.start
    @topic_store.remove_association(id)
  ensure
    @topic_store.stop
  end
  
  # We produce a UUID and add it to the
  # view state under :uuid or the index
  # provided.
  def produce_uuid (state=:uuid)
    @view_state[state] = gen_uuid
  end
  
  # We produce a list of all topic ids.
  def get_topic_ids
    @topic_store.start
    @topic_store.get_topic_ids
  ensure
    @topic_store.stop
  end
  
end
