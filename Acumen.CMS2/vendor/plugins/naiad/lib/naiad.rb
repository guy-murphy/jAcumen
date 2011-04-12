#!/usr/bin/env ruby
#--
# Naiad, a simple service locator
# This work is based in a large part
# on the work of Jim Weirich and his DIM module.
# http://onestepback.org/articles/depinj/dim/dim_rb.html
#
# Author:   Guy J. Murphy
# Date:     18/01/2010
# Version:  0.1
#++
#
# = Naiad - A lightweight service container
#
#--
#RAILS_ROOT = '/home/guy/workspace/Acumen.CMS2'
jars=""
Dir[File.join(Rails.root, "lib", "jars") +"/*.jar"].each { |jar| jars+=jar+" "}

require 'java'
Dir[File.join(Rails.root, "lib", "jars") +"/*.jar"].each { |jar| require jar };

import 'acumen.web.view.ViewState'
import 'acumen.map.store.MySqlTopicStore2'
import 'acumen.resource.store.MySqlFileStore'
import 'acumen.user.store.MySqlUserStore'

require 'singleton'
require '/home/guy/workspace/Acumen.CMS2/vendor/plugins/acumen/lib/wiki_behaviour'
require '/home/guy/workspace/Acumen.CMS2/vendor/plugins/acumen/lib/acumen_parser'

# A lightweight service container which indexes blocks
# that when executed deliver a service object.
module Naiad
  
  # Thrown when the container is unable
  # to find a service.
  class ServiceNotFoundError < StandardError
  end
  
  # Thrown when a service is registered
  # under a name already registered with
  # the container
  class NameExistsError < StandardError
  end
  
  # Accessor for the service container.
  def Naiad.services
    ServiceContainer.instance
  end
  
  # Accessor for the service container.
  def services
    Naiad.services
  end
  
  # Defines the service container
  # to be used by the Naiad module.
  class ServiceContainer
    
    include Singleton
    
    public
    
    def key(key1, key2)
      if key2.nil?
        return key1
      end
      return "Naiad::#{key1}::#{key2}"
    end    
    
    # Instantiate the service container.
    def initialize
      @services = {}
      @cache = {}
    end
    
    # We place the block provided into the service
    # container indexed under either one or two
    # provided keys.
    def register_service(key1, key2=nil, &block)
      if @services[key(key1,key2)] # check if the block has already been indexed as provided
        fail NameExistsError, "A service is already under the name '#{key(key1,key2)}'."
      end
      @services[key(key1,key2)] = block
    end
    
    # We retrieve a block which provides a service
    # that is indexed under either the one or two
    # provided keys.
    def obtain_service_block(key1, key2=nil)
      service = @services[self.key(key1,key2)]
      if service.nil?  
        raise ServiceNotFoundError, "No service could be found registered under the name '#{key(key1,key2)}'." 
      end
      service # return
    end
    
    # TODO: remove this method?
    def ServiceContainer.obtain_service_block(key1, key2=nil)
      raise ServiceNotFoundError, "No service could be found registered under the name '#{key(key1,key2)}'."
    end
    
    # We retrieve the service block that is indexed
    # under either the one or two provided keys. First
    # we check the cache for the service object that
    # was created from the block, if that is not found
    # then we retrieve the service block, execute is
    # and cache the result.
    def lookup_service(key1, key2=nil)
      k = key(key1,key2)
      puts "#> looking up service for '#{k}'"
      @cache[k] ||= obtain_service_block(key1, key2).call(self)
    end
    
    # We provide a simple indexing notation
    # which in turn calls #lookup_service, this uses
    # a local cache of created service objects
    # so there's no need to apply local caching
    # of services at a higher level. Doing so will
    # probably hurt not improce performance.
    def [](key1, key2=nil)
      self.lookup_service(key1, key2)
    end
    
    # We iterate over each block
    # held by the service container, noting
    # that we don't visit the cache of the
    # block products.
    def each (&block)
      @services.each(&block)
    end
    
  end
  
end

if __FILE__ == $0
  
  puts 'Naiad informal test...'
  
  include Naiad
  
  Naiad::services.register_service :view_state do
    ViewState.new
  end
  
  Naiad::services.register_service :wiki_parser do
    # this is a thin ruby wrapper around
    # acumen.parser.scanner.WikiParser
    AcumenParser.new
  end
  
  Naiad::services.register_service :topic_store, :london do
    MySqlTopicStore2.new('leviathan.acumen.es', 'acumen_london', 'root', 'notnow')
  end
  Naiad::services.register_service :topic_store, :default do
    MySqlTopicStore2.new('leviathan.acumen.es', 'acumen', 'root', 'notnow')
  end
  #Different maps implementation for file store
  Naiad::services.register_service :file_store, :london do
    store = MySqlFileStore.new('leviathan.acumen.es', 'acumen_london', 'root', 'notnow')
    store.set_namespace 'DevWiki' 
    store # return
  end
  Naiad::services.register_service :file_store, :default do
    store = MySqlFileStore.new('leviathan.acumen.es', 'acumen', 'root', 'notnow')
    store.set_namespace 'DevWiki' 
    store # return
  end
  #Different maps implementation for user store
  Naiad::services.register_service :user_store, :london do
    MySqlUserStore.new('leviathan.acumen.es', 'acumen_london', 'root', 'notnow')
  end
  Naiad::services.register_service :user_store, :default do
    MySqlUserStore.new('leviathan.acumen.es', 'acumen', 'root', 'notnow')
  end
  
  # Behaviours
  Naiad::services.register_service :wiki_behaviour, :london do
    Behaviour::WikiBehaviour.new(Naiad::services[:file_store, :london])
  end
  Naiad::services.register_service :wiki_behaviour, :default do
    Behaviour::WikiBehaviour.new(Naiad::services[:file_store, :default])
  end
  
  #services.each do |k,v|
  # puts "#{k}: #{v}"
  #end
  
  filestore = Naiad::services[:file_store, :default]
  topicstore = Naiad::services[:topic_store, :default]
  topicstore.start
  begin
    topic = topicstore.get_topic_with('GuyMurphy', 'any', '*')
    topic.set_pretty_print true
    
    topic.get_occurences.each do |occurence|
      if occurence.get_behaviour == 'wiki'
        behaviour = services[:wiki_behaviour, :default]
        behaviour.resolve!(occurence)
      end
    end
    
    puts topic.to_xml
  ensure
    topicstore.stop
  end
  
end