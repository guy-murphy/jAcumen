# We are concerned with high level operations
# on metadata such as viewing and editing it.
class MetadataController < AcumenController

  public
      
  # We update metadata in a manner
  # suitable for inline AJAX editing.
  def inline
    update_topic_metadata_from_path(params[:path], params[:update_value])
    render :text => params[:update_value]
  #rescue
  #  render :text => params[:original_html]
  end
  
  # We update or add the provided metadata.  
  # If params[:type] is 'assoc' then this will be treated as association metadata;
  # otherwise it will be treated as topic metadata.
  def update
    if params[:type] == 'assoc'
      set_assoc_metadata(params[:assoc], params[:name], @current_language, @current_scope, params[:value])
      redirect_to :action => 'assoc', :id => @current_id, :assoc => params[:assoc]
    else
      set_topic_metadata(@current_id, params[:name], @current_language, @current_scope, params[:value])
      redirect_to :action => 'view', :id => @current_id
    end
  end
  
  # We remove the specified metadata.
  def remove
    if params[:type] == 'assoc'
      remove_metadata(params[:assoc], params[:name], @current_language, @current_scope)
      redirect_to :action => 'assoc', :id => @current_id, :assoc => params[:assoc]
    else
      remove_metadata(@current_id, params[:name], @current_language, @current_scope)
      redirect_to :action => 'view', :id => @current_id
    end
  end
  
  def assoc
    get_topic(@current_id, :resolve => false, :assoc => params[:assoc])
  end
  
end
