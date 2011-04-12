def require_jars(path)
  require 'java'
  Dir[File.join(path, "lib", "jars") +"/*.jar"].each { |jar| require jar };
end
require_jars '/home/guy/workspace/Acumen.CMS'

require 'date'

puts '#> Simple Acumen test script, for whatever purpose.'

puts DateTime.now.to_s