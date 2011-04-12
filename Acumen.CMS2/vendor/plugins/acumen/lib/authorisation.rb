require 'ap'

module Authorisation

  private
  
  protected
  
  # We ensure the current user is allowed to 'execute'
  # the current request.
  def authorise
    puts '#> authorising'
    controller_spec = Globals::AUTHORITY[controller_name.to_sym]
    action_spec = controller_spec[action_name.to_sym]
    restrict_to_roles action_spec
  end
  
  # We perform whatever action is deemed necessary
  # as a consequence of the user lacking authorisation.
  def not_authorised
    render :file => "403.html", :status => status
  end
  
  def has_role(role)
    @user.get_roles.get_role(role)
  end
    
  def restrict_to_roles(roles)
    if roles != nil and roles.length != 0
      not_authorised unless  @user.get_roles.has_any_role(roles.map{|i| i.to_s})
    else
      not_authorised
    end
  end
  
end
