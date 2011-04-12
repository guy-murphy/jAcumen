module Authentication

  attr_accessor :user, :user_id

  private
  
  protected
  
  # We ensure the current request is authenticated.
  def authenticate
    puts '#> authenticating'
    uname = params[:uname]
    upassword = params[:upassword]
    
    # no creadentials
    if uname.nil? or upassword.nil?
      # check if already logged in
      ticket = session[:ticket]
      if not ticket.nil?
        @user_id = decrypt(ticket)
      else
        record_user
      end
    else # check credentials
      get_user_from_credentials(uname, upassword)
    end
    
    if @user.nil? # we haven't got a user yet
      get_user_from_id
    end
    if @user.nil? and @user_id != Globals::DEFAULT[:user_id] # retry 'anon' as we might have picked up a crap cookie
      get_user_from_uid('anon')
    end
    if @user.nil? # FAILURE
      throw 'Authentication failed, unable to find even a default user to authenticate as'
    end
    # add the users details to the view state
    # but ensure that their password isn't exposed while
    # noting that an empty/null password will present
    # 'anon' as the password
    @user.set_password('')
    @view_state[:user] = @user
  end
  
  # We get the user for the identity provided
  # and then record the details.
  def get_user_from_id(id=@user_id)
    @user_store.start
    @user = @user_store.get_user(id)
    record_user
  ensure
    @user_store.stop
  end
  
  # We get the user for the credentials provided
  # and then record the details.
  def get_user_from_credentials(name, password)
    @user_store.start
    @user = @user_store.get_user(name, password)
    record_user
  ensure
    @user_store.stop
  end
  
  # We record the users identity, and a cookie with the
  # identity encrypted.
  def record_user
    @user_id = (@user.nil?) ? Globals::DEFAULT[:user_id] : @user.get_id
    session[:ticket] = encrypt(@user_id)
    return @user
  end
  
  # Encrypt the text provided.
  # See Globals.rb for key setup.
  def encrypt(text)
    #Globals::CRYPT.encrypt text
    text
  end
  
  # Decrypt the text provided.
  # See Globals.rb for key setup.
  def decrypt(text)
    #Globals::CRYPT.decrypt text
    text
  end
  
end
