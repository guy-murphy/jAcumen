#!/usr/bin/env ruby
#--
#
# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1
#++
#
# = WikiBehaviour - A behaviour for resolving wiki text.
#

require 'naiad'

import 'acumen.map.model.IOccurence'
import 'acumen.resource.store.MySqlFileStore'
import 'acumen.resource.model.ResourceException'
import 'acumen.util.StringUtil'

module Behaviour
  
  class WikiBehaviour
    
    include Naiad
    
    attr_accessor :store
    
    def initialize(file_store=nil)
      @store = file_store
    end
    
    def create(occurence, opts={})
      puts occurence.to_xml
      @store.start
      unless @store.resource_exists(occurence.reference)
        wiki_text = occurence.get_content || 'no data'
        @store.create_resource(occurence.get_reference, opts[:user_id], 'wiki', wiki_text)
      end
    ensure
      @store.stop
    end
    
    # We get the raw underlying textual representation
    # of the data being pointed to by the occurence.
    #
    # NOTE: This is not doable for all forms of occurence data.
    # This is mainly of use for Wikis and other types of DSL
    # that have a textual representation used for editting.
    def get_raw(occurence, version=nil)
      @store.start
      resource = nil
      if version.nil?
        resource = @store.get_most_recent_instance(occurence.get_reference)
      else
        resource = @store.get_resource_instance(occurence.get_reference, version)
      end
      resource == nil ? '*unable to find wiki for topic*' : resource.string_data
    ensure
      @store.stop
    end
    
    def get_version_info(occurence)
      @store.start
      
    ensure
      @store.stop
    end
    
    # We get the raw data being pointed to by
    # the occurence and then process it into
    # an 'end form' XML view and then place
    # it in the occurences content.
    def resolve!(occurence, version=nil)     
      wiki_text = self.get_raw(occurence, version)
      unless wiki_text == nil
        parser = services[:wiki_parser]
        begin
          parser.text = wiki_text
          occurence.set_content '<content>'+ parser.parse + '</content>'
        end
      end
    end
    
    # We get a raw representation of the data being
    # pointed to by the occurence but we don't
    # process it into an XML view, we just set the occurences
    # content to the raw data for. We do however wrap the content
    # as CDATA so that any characters that are a problem in valid XML
    # don't blow up.
    #
    # NOTE: This is not doable for all forms of occurence data.
    # This is mainly of use for Wikis and other types of DSL
    # that have a textual representation used for editting.
    def resolve_raw!(occurence, version=nil)
      unparsed_text = self.get_raw(occurence,version)
      occurence.set_content("<![CDATA[#{unparsed_text}]]>")
    end
    
    def update_occurence!(occurence, wikitext, opts={})      
      begin
        @store.start
        begin
          resource = @store.open_resource_for_update(occurence.get_reference, 'some user id')
          if resource != nil
            puts "#> updating resource with (#{wikitext.class}): #{wikitext}"
            resource.set_string_data(wikitext)
            @store.update_resource(resource)
          else
            # The resource does not exist, so we must create it instead.
            @store.create_resource(occurence.get_reference, opts[:user_id], 'wiki', wikitext)
          end
        rescue ResourceException
          # The resource has either been locked or marked as deleted.
          if resource.get_locked_by.length == 'DELETED'
            # It has been marked as deleted.
            raise 'This wiki has been deleted.'
          else
            # It has been locked by somebody else.
            puts "#> unable to update wiki as it is locked"
            @store.create_resource(occurence.get_reference, 'some user id', 'wiki', wikitext)
            lock_info = PropertySet.new # acumen.util.PropertySet
            lock_info.put('id', resource.get_id)
            lock_info.put('name', resource.get_name)
            lock_info.put('locked_by', resource.get_locked_by)
            occurence.get_resolved_objects.put('lock', lock_info)
          end
        end
      ensure
        @store.stop
      end
    end 
    
  end

end
