#require 'utils'
def require_jars(path)
  require 'java'
  Dir[File.join(path, "lib", "jars") +"/*.jar"].each { |jar| require jar };
end
require_jars '/home/guy/workspace/Acumen.CMS'

import 'acumen.parser.scanner.WikiParser'

class AcumenParser
  
  attr_accessor :text, :result
  
  def initialize
    @text = ''
    @result = 'Wiki not parsed. Call AcumenParser#parse'
  end
  
  def parse
    parser = WikiParser.new(@text)
    parser.wiki
    parser.set_pretty_print(true)
    @result = parser.to_xml # return
  rescue
    @result = '<message>Unable to parse wiki.</message>' # return
  end
  
end

if __FILE__ == $0 
  data = "To be or not to be\n\nthat is the question"
  
  parser = AcumenParser.new
  parser.text = data
  parser.parse
end