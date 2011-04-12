# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1

require 'behaviour'

# We are concerned with high level operations
# on topics such as viewing them and editting them.
# Although one might argue that these operations
# aren't actually on topic entities but instead
# occurences etc., this is from a logical API
# perspective, and not aiming to establish a "truth".
class TopicController < AcumenController
  
  attr_reader :sitemap
  
  public

  # We present the topic with its
  # resources resolved, in a form
  # suitable to read.
  def view
    get_topic(@current_id)
    get_related_for_current
    fetch_blogs
    fetch_comments
  end
  
  # Similar to 'view' but gets
  # only the basic topic details.
  def peek
    get_topic(@current_id)
  end
  
  # We present the topic with its
  # wiki text ready to edit.
  #
  # NOTE: We are displaying for edit
  # only, not actually modifying any data.
  # For that see TopicController#update
  def edit
    get_topic_for_edit(@current_id)
  end

  # We update the topics primary
  # wiki text with that provided.
  def update
    update_wiki(@current_id, :wiki_text => params[:wikitext])
    redirect_to :action => 'edit', :id => @current_id
  end
  
  # We present a form
  # to add a new topic.
  def add
    # Prepare a GUID for a default Topic ID
    produce_uuid # @view_state[:uuid]
  end
  
  # We create the topic with the id
  # and label provided and then we
  # redirect to editting the topic.
  def create
    create_topic(params[:id], params[:label])
    redirect_to :action => 'edit', :id => @current_id
  end
  
  # We remove the current topic.
  def remove
  end

end
