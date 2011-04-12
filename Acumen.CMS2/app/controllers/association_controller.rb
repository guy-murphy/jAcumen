# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1

# We are concerned with high level operations
# on associations (links) such as viewing and editing them.
class AssociationController < AcumenController
  
  public
  
  # We update an association in a manner
  # suitable for inline AJAX editing.
  def inline
    update_association_from_path(params[:path], params[:update_value])
    render :text => params[:update_value]
  #rescue
  #  render :text => params[:original_html]
  end

  # We update or add the provided association.  
  def update
    #set_topic_occurence(@current_id, params[:behaviour], params[:role], @current_language, @current_scope, params[:reference])
    #redirect_to :action => 'view', :id => @current_id
  end
  
  def add
    add_association(@current_id, params[:reference], params[:label], params[:type], params[:role])
    redirect_to :action => 'view', :id => @current_id
  end
  
  # We remove the specified association.
  def remove
    remove_association(params[:assoc])
    redirect_to :action => 'view', :id => @current_id
  end

end
