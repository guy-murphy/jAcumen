# Author:   Guy J. Murphy
# Date:     20/01/2010
# Version:  0.1

require 'topics'
require 'authentication'
require 'authorisation'

import 'acumen.util.PropertySet'
import 'acumen.map.model.Language'
import 'acumen.map.model.Occurence'

class AcumenController < ApplicationController
  
  attr_reader :view_state, :topic_store, :file_store, :user_store, :current_scope, :current_language, :current_map, :initialised, :tmp_cache
  # @tmp_cache - The purpose of this is to provide a cache with a lifetime of the CURRENT REQUEST
  #              ...this is desirable to avoid repeated visits to the database for data we've only just fetched.
  
  include Naiad           # The service container.
  include Topics          # Broad high level API for topic store.
  include Authentication  # Implements authentication.
  include Authorisation   # Implements authorisation.
  
  # see: Authentication#authenticate, Authorisation#authorise
  # see: topics#generate_site_map
  before_filter :filter_params, :init_stores, :authenticate, :authorise
  
  protected
  
  # We overrride the ApplicationController#render method
  # so that we can check whether or not this should be
  # handled as an XML dump. If not we carry on with
  # default behaviour.  
  def render(options = nil, extra_options = {}, &block)
    if params[:format] == 'xml' # dump the view state as XML
      put_xml @view_state
    else
      super options
    end
  end
  
  # Outputs the oject provided as XML to the current response
  # stream.
  def put_xml(obj = nil, status = 200, append_response = false)
    @performed_render = true
    
    obj.set_pretty_print(true)
    text = obj.to_xml
    
    response.content_type = 'text/xml'
    response.status = status
    
    if append_response
      response.body ||= ''
      response.body << text.to_s
    else
      response.body = case text
        when Proc then text
        when nil  then " " # Safari doesn't pass the headers of the return if the response is zero length
      else           
        text.to_s
      end
    end
  end
  
  public
  
  # We pull the standard stores from the service container
  # and make them available via convenient accessors, noting
  # that this doesn't actually start or open any of the stores.
  def initialize
    super
    @view_state = services[:view_state]
    @tmp_cache = {}
    @initialised = false
  end
  
  # We initialise the stores that we're going to use.
  def init_stores
    if not @initialised
      @topic_store = services[:topic_store, @current_map]
      @file_store = services[:file_store, @current_map]
      @user_store = services[:user_store, @current_map]
      @initialised = true
    end
  end
  
  # We establish the params that the controller will use
  def filter_params
    if not @params_filtered
      # these values are automatically persisted
      # via the session
      @current_map = select_param :map, :symbol => true
      @current_language = select_param :lang
      @current_scope = select_param :scope
      @current_id = select_param :id
      # update the view state with values that must be communicated down-stream
      properties = PropertySet.new()
      properties[:language] = @current_language
      properties[:scope] = @current_scope
      properties[:map] = @current_map
      properties[:home] = Globals::DEFAULT[:id]
      @view_state[:properties] = properties
      @view_state[:languages] = Globals::LANGUAGES
      @view_state[:scopes] = Globals::SCOPES
      @view_state[:maps] = Globals::MAPS
      @view_state[:notice] = PropertySet.new()
      @view_state[:error] = PropertySet.new()
      @view_state[:success] = PropertySet.new()
      @params_filtered = true
    end
  end
  
  def notice
    @view_state[:notice]
  end
  
  def error
    @view_state[:error]
  end
  
  def success
    @view_state[:success]
  end
  
  # We favour values from the params, session, and then defaults
  # in that order. We check if we have a params value, and then
  # check the others if it's not available. Once we have selected
  # a value we store it in the session as a new soft default.
  def select_param(name, opts={})
    value = params[name]
    if value.nil?
      value = session[name]
      if value.nil?
        value = Globals::DEFAULT[name]
      end
    end
    set_param(name,value)
    return opts[:symbol] ? value.to_sym : value
  end
  
  # We set the named parameter with the
  # value provided, which will set both the params
  # hash and the session hash.
  def set_param(name, value)
    session[name] = value
    params[name] = value
    return value
  end
  
  # We obtain a hierachical map of the site
  # suitable for navigation in something like a footer.
  # This is not a good way to produce a flat sitemap
  # for the likes of Google, and one should produce
  # a formatted flat topic list for that.  
  def fetch_sitemap
    sitemap = Rails.cache.read('sitemap')
    
    if sitemap.nil?
      puts "#> sitemap MISSED cache"
      sitemap = generate_sitemap
      @view_state[:sitemap] = sitemap
      Rails.cache.write('sitemap', sitemap)
    else
      puts "#> sitemap HIT cache"
      @view_state[:sitemap] = sitemap
    end
  end
  
  # We fetch the blogs pointing to the current topic.
  def fetch_blogs (size = Globals::DEFAULT_BLOGS_FETCHED)
    @view_state[:blogs] = get_leaves_for_current('blog', 'page', size)
  end
  
  # We fetch the comments pointing to the current topic
  def fetch_comments (size = Globals::DEFAULT_COMMENTS_FETCHED)
    @view_state[:comments] = get_leaves_for_current('comment', 'page', size)
  end
  
  public
  
  # These are controller actions from AcumenController
  # Override where appropriate.
  
  # Handle as a 'view' of the default topic identity.
  def index
    redirect_to :action => 'view', :id => Globals::DEFAULT_ID 
  end
  
  # We present the details for the current topic.
  # This can be overriden if the view provides
  # more details than this.
  def view
    get_topic(@current_id, :resolve => false)
    get_complete_topic(@current_id, :resolve => false)
    get_related_for_current
  end
  
end
