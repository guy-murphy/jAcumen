require 'behaviour'
require 'naiad'

# We provide search functionality for working
# with topics linked to other topics as blogs.
class BlogController < AcumenController

  # We present details of the current topic
  # and topics linked to it as blogs with
  # an interface suitable for managing them.
  def manage
    get_topic(@current_id)
    get_related_for_current
    fetch_blogs
    # Prepare a GUID for a new blog should it be needed
    produce_uuid # @view_state[:uuid]
  end
  
  # We add an association between params[:id]
  # and params[:from] where 'from' recieves
  # the association and becomes the blog entry
  # to 'id'
  def link
    add_association(params[:from], params[:id], nil, 'blog', 'page')
    redirect_to :action => 'manage'
  end
  
  # We remove the association binding one
  # topic to another as a blog entry.
  def delink
    remove_assoc(params[:from], params[:id], 'blog', 'page')
    redirect_to :action => 'manage'    
  end
  
  # We create a new topic, and then add an association to
  # it linking it as a blog to the current topic.
  def create
    create_topic(params[:from], params[:label])
    add_association(params[:from], params[:id], params[:label], 'blog', 'page')
    redirect_to :action => 'manage'
  end

end
