import 'acumen.util.StringUtil'

def require_jars(path)
  require 'java'
  Dir[File.join(path, "lib", "jars") +"/*.jar"].each { |jar| require jar };
  puts "#> utils.rb, require_jars #{path}, jars loaded"
end

def gen_uuid
  StringUtil.generate_uuid
end
