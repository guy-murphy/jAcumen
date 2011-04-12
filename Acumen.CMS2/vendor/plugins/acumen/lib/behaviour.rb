# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1
#
# Behaviour - A module for resolving resources with behaviours.

require 'naiad'

# We provide an adaptor interface that looks
# up the appropriate behaviour service for
# the type of resource represented by the
# occurence being acted upon. The analagous
# method on the service is then invoked.
module Behaviour
  
  include Naiad # The service container.
  
  def resolve_occurence!(occurence, map)
    service_name = :none # default service for resolution
    case occurence.get_behaviour
      when 'wiki' # Wiki behavior
        service_name = :wiki_behaviour
      when 'images' # Gallery behavior
        service_name = :gallery_behaviour
#      when 'attachments' # Attachments behavior
#        service_name = :attachements_behaviour
    end
    # now get the behaviour, and use it to resolve the occurence
    unless service_name == :none
      behaviour = services[service_name, map]
      behaviour.resolve!(occurence)
    end
  end
  
  def resolve_occurences!(topic, map)
    #TODO put in a guard on the type
    topic.get_occurences.each do |o|
      resolve_occurence!(o, map)
      o.get_content # why did I add this?
    end
  end
  
  def resolve_raw_occurence!(occurence, map)
    if occurence.get_behaviour == 'wiki'
      behaviour = services[:wiki_behaviour, map]
      behaviour.resolve_raw!(occurence)
    end
  end
  
  def resolve_raw_occurences!(topic, map)
    topic.get_occurences.each do |o|
      resolve_raw_occurence!(o, map)
      o.get_content # why did I add this?
    end
  end
  
  def update_occurence!(occurence, map, options={})
    if occurence.get_behaviour == 'wiki'
      behaviour = services[:wiki_behaviour, map]
      behaviour.update_occurence!(occurence, options[:wiki_text])
    end
  end
  
  def create!(occurence, map, options={})
    if occurence.get_behaviour == 'wiki'
      behaviour = services[:wiki_behaviour, map]
      behaviour.create(occurence, options)
    end
  end
  
end
