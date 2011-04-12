# We are concerned with high level operations
# on occurences such as viewing and editing them.
class OccurenceController < AcumenController
  
  public
  
  # We update an occurence in a manner
  # suitable for inline AJAX editing.
  def inline
    update_topic_occurence_from_path(params[:path], params[:update_value])
    render :text => params[:update_value]
  #rescue
  #  render :text => params[:original_html]
  end

  # We update or add the provided occurence.  
  def update
    set_topic_occurence(@current_id, params[:behaviour], params[:role], @current_language, @current_scope, params[:reference])
    redirect_to :action => 'view', :id => @current_id
  end
  
  # We remove the specified occurence.
  def remove
    remove_topic_occurence(@current_id, params[:behaviour], params[:role], @current_language, @current_scope)
    redirect_to :action => 'view', :id => @current_id
  end

end
