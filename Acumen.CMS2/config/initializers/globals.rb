require 'utils'
require_jars Rails.root

require 'encryption'

import 'acumen.web.view.XslTransformer'
import 'acumen.util.PropertySet'
import 'acumen.map.model.Language'

module Globals
  XSL_TRANSFORMER = XslTransformer.new(Rails.root.to_s)
  XSL_TRANSFORMER.turn_off_caching # ensures edits to XSL stylesheets are automatically reflected
  #XSL_TRANSFORMER.turn_on_caching # ensures that XSL stylesheets are compiled once then cached 
  
  DEFAULT_SCOPE = '*'
  DEFAULT_LANGUAGE = 'any'
  DEFAULT_ID = 'FrontPage'
  DEFAULT_MAP = :default
  DEFAULT_USER_ID = 'anon'
  WIKI_NAMESPACE = 'DevWiki'
  
  DEFAULT = {}
  DEFAULT[:scope] = DEFAULT_SCOPE
  DEFAULT[:language] = DEFAULT_LANGUAGE
  DEFAULT[:id] = DEFAULT_ID
  DEFAULT[:map] = DEFAULT_MAP
  DEFAULT[:user_id] = DEFAULT_USER_ID
  
  DEFAULT_BLOGS_FETCHED = 5
  DEFAULT_COMMENTS_FETCHED = 10
  
  LANGUAGES = PropertySet.new()
  LANGUAGES['any'] = 'English'
  LANGUAGES['es'] = 'Spanish'
  LANGUAGES['de'] = 'German'
    
  SCOPES = PropertySet.new()
  SCOPES['*'] = 'Universal'
  SCOPES['chy'] = 'Charity'
  SCOPES['rpc'] = 'RP. Corp.'
    
  MAPS = PropertySet.new()
  MAPS['london'] = 'London'
  MAPS['default'] = 'Acumen'
  
  # Encryption
  CRYPT = Encryption::Crypt.new(ActionController::Base.cookie_verifier_secret)
  
  # Resources
  
  ALLOWED_IMAGES = [".png",".jpg",".jpeg",".gif"]
  ALLOWED_ATTACHMENTS = [".png",".jpg",".jpeg",".gif",".doc",".pdf",".xls",".xml",".docx",".pptx",".xlsx",".ppt",".sql",".txt",".html"]
  
  # Security
  
  TRUSTED_USERS = [:Owner, :Admin, :Moderator, :Developer, :Trusted]
  PRIVILEGED_USERS = [:Owner, :Admin]
  
  AUTHORITY = {
    :topic => {
      :view         => [:Anon],
      :peek         => [:Anon],
      :edit         => TRUSTED_USERS,
      :update       => TRUSTED_USERS,
      :add          => TRUSTED_USERS,
      :create       => TRUSTED_USERS,
      :remove       => PRIVILEGED_USERS
    },
    :metadata       => {
      :view         => TRUSTED_USERS,
      :inline       => TRUSTED_USERS,
      :update       => TRUSTED_USERS,
      :remove       => TRUSTED_USERS,
      :assoc        => TRUSTED_USERS
    },
    :association    => {
      :view         => TRUSTED_USERS,
      :inline       => TRUSTED_USERS,
      :update       => TRUSTED_USERS,
      :add          => TRUSTED_USERS,
      :remove       => TRUSTED_USERS
    },
    :occurence      => {
      :view         => TRUSTED_USERS,
      :update       => TRUSTED_USERS,
      :inline       => TRUSTED_USERS,
      :remove       => TRUSTED_USERS
    },
    :parse          => {
      :wiki         => [:Anon]
    },
    :search         => {
      :topic        => TRUSTED_USERS,
      :autocomplete => TRUSTED_USERS
    },
    :blog           => {
      :manage       => TRUSTED_USERS,
      :delink       => TRUSTED_USERS,
      :link         => TRUSTED_USERS,
      :create       => TRUSTED_USERS
    },
    :comment        => {
      :manage       => TRUSTED_USERS,
      :delink       => TRUSTED_USERS,
      :link         => TRUSTED_USERS,
      :create       => TRUSTED_USERS
    },
    :gallery        => {
      :manage       => TRUSTED_USERS,
      :repository   => TRUSTED_USERS
    },
    :user           => {
      :login        => [:Anon]
    }
  }
  
end

