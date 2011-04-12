# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1

jars=""
Dir[File.join(RAILS_ROOT, "lib", "jars") +"/*.jar"].each { |jar| jars+=jar+" "}

require 'java'
Dir[File.join(RAILS_ROOT, "lib", "jars") +"/*.jar"].each { |jar| require jar };

import 'acumen.web.view.ViewState'
import 'acumen.map.store.MySqlTopicStore2'
import 'acumen.resource.store.MySqlFileStore'
import 'acumen.user.store.MySqlUserStore'
import 'acumen.user.store.MySqlUserStore'

require 'naiad'
require 'wiki_behaviour'
require 'gallery_behaviour'
#require 'attachments_behaviour'

Naiad::services.register_service :view_state do
  ViewState.new # return
end

Naiad::services.register_service :wiki_parser do
  # this is a thin ruby wrapper around
  # acumen.parser.scanner.WikiParser
  AcumenParser.new # return
end

# Stores

#Different maps implementation for topic store
Naiad::services.register_service :topic_store, :london do
  MySqlTopicStore2.new('leviathan.acumen.es', 'acumen_london', 'root', 'redacted') # return
end
Naiad::services.register_service :topic_store, :default do
  MySqlTopicStore2.new('leviathan.acumen.es', 'acumen', 'root', 'redacted') # return
end
#Different maps implementation for file store
Naiad::services.register_service :file_store, :london do
  store = MySqlFileStore.new('leviathan.acumen.es', 'acumen_london', 'root', 'redacted')
  store.set_namespace 'DevWiki' 
  store # return
end
Naiad::services.register_service :file_store, :default do
  store = MySqlFileStore.new('leviathan.acumen.es', 'acumen', 'root', 'redacted')
  store.set_namespace 'DevWiki' 
  store # return
end
#Different maps implementation for user store
Naiad::services.register_service :user_store, :london do
  MySqlUserStore.new('leviathan.acumen.es', 'acumen_london', 'root', 'redacted') # return
end
Naiad::services.register_service :user_store, :default do
  MySqlUserStore.new('leviathan.acumen.es', 'acumen', 'root', 'redacted') # return
end

# Behaviours
Naiad::services.register_service :wiki_behaviour, :london do
  Behaviour::WikiBehaviour.new(Naiad::services[:file_store, :london])
end
Naiad::services.register_service :wiki_behaviour, :default do
  Behaviour::WikiBehaviour.new(Naiad::services[:file_store, :default])
end

Naiad::services.register_service :gallery_behaviour, :default do
  Behaviour::GalleryBehaviour.new(Naiad::services[:file_store, :default], :default)
end
Naiad::services.register_service :gallery_behaviour, :london do
  Behaviour::GalleryBehaviour.new(Naiad::services[:file_store, :london], :london)
end

#Resolvers
Naiad::services.register_service :images_resolver, :default do
  'repositories/default/images' # return
end
Naiad::services.register_service :images_resolver, :london do
  'repositories/london/images' # return
end

