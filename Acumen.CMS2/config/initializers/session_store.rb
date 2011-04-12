# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key    => '_Acumen.CMS2_session',
  :secret => '6664f102f8b23de6cdd7f8b2e1fdb0dac3a47786d63d5f103348975e2525e60bf638a37ea904e2a363fcea6aa582b67a0f2cd53fa2cc74bd8db0e24aa00d77b5'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
