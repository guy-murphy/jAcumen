# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1

require 'behaviour'

class GalleryController < AcumenController

  # We present the topic with its
  # resources resolved, in a form
  # suitable to read.
  def manage
    get_topic(@current_id)
    get_related_for_current
  end
  
  # We check if a repository for this topic
  # exists, and if it doesn't we create it.
  def repository
    reference = @current_scope + '/' + @current_id
    
  end  

end
