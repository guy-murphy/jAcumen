#require 'utils'
def require_jars(path)
  require 'java'
  Dir[File.join(path, "lib", "jars") +"/*.jar"].each { |jar| require jar };
end
require_jars '/home/guy/workspace/Acumen.CMS'

import 'acumen.map.store.MySqlTopicStore2'

puts '#> Simple Acumen test script, for whatever purpose.'

def starts_with?(str, prefix)
  prefix = prefix.to_s
  str[0, prefix.length] == prefix
end

begin
	store = MySqlTopicStore2.new('leviathan.acumen.es', 'acumen', 'root', 'notnow')
	store.start
	ids = store.get_topic_ids
	filtered = ids.select{|id| starts_with?(id, 'F') }
	result = filtered.join("\n")
	puts result
ensure
	store.stop
end