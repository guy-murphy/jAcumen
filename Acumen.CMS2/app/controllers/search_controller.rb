require 'behaviour'
require 'naiad'

# We provide search functionality for finding
# topics withint the CMS.
class SearchController < AcumenController

  # Initially we simply jump to the topic
  # that has been specified in params[:goto]
  def topic
    redirect_to :controller => 'topic', :action => 'view', :id => params[:goto], :status => 303 # HTTP See Other
  end
  
  def autocomplete
    topic_ids = get_topic_ids
    filtered = topic_ids.select{|str| str_starts_with(str, params[:q])}
    render :text => filtered.join("\n")
  end

end
