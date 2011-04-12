require 'behaviour'
require 'naiad'

import 'acumen.util.AcumenText'

# We expose language parsers as a Web service
# that recieve encoded text, parse it, then pass back the result.
class ParseController < AcumenController

  # We parse the text provided by ?data
  # as Acumen wiki text, and then provide it
  # with the minimal processing to view it.
  #
  # Note that in order to get an XML view of
  # the parse as one would with a topic, it
  # is necessary so pass a dummy ID this...
  # http://host/parse/wiki/dummy.xml?data=abc
  # This dummy value can be anything so just as good is...
  # http://host/parse/wiki/result.xml?data=abc
  def wiki
    parser = services[:wiki_parser]
    parser.text = params[:data]
    @view_state[:wiki] = AcumenText.new(parser.parse)
  end

end
