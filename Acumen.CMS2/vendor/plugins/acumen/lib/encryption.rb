#!/usr/bin/env ruby
#--
#
# Author:   Guy J Murphy
# Date:     20/04/2010
# Version:  0.1
#++
#
# = Crypto - A module for simple encryption/decryption.
#
# Built upon an original module called AESCrypt, authored by
# I'm not quite sure, but full credit where it's due and happy
# to provide a citation should somebody pop up.
#
# This module is really just some very simple tidying up into
# a fuller module (suitable for our purposes), 
# the core is derived entirely from the existing code.

require 'openssl'

module Encryption
  
  # Decrypts a block of data (encrypted_data) given an encryption key
  # and an initialization vector (iv).  Keys, iv's, and the data 
  # returned are all binary strings.  Cipher_type should be
  # "AES-256-CBC", "AES-256-ECB", or any of the cipher types
  # supported by OpenSSL.  Pass nil for the iv if the encryption type
  # doesn't use iv's (like ECB).
  #:return: => String
  #:arg: encrypted_data => String 
  #:arg: key => String
  #:arg: iv => String
  #:arg: cipher_type => String
  def self.decrypt(encrypted_data, key, iv, cipher_type)
    aes = OpenSSL::Cipher::Cipher.new(cipher_type)
    aes.decrypt
    aes.key = key
    aes.iv = iv if iv != nil
    aes.update(encrypted_data) + aes.final  
  end
  
  # Encrypts a block of data given an encryption key and an 
  # initialization vector (iv).  Keys, iv's, and the data returned 
  # are all binary strings.  Cipher_type should be "AES-256-CBC",
  # "AES-256-ECB", or any of the cipher types supported by OpenSSL.  
  # Pass nil for the iv if the encryption type doesn't use iv's (like
  # ECB).
  #:return: => String
  #:arg: data => String 
  #:arg: key => String
  #:arg: iv => String
  #:arg: cipher_type => String  
  def self.encrypt(data, key, iv, cipher_type)
    aes = OpenSSL::Cipher::Cipher.new(cipher_type)
    aes.encrypt
    aes.key = key
    aes.iv = iv if iv != nil
    aes.update(data) + aes.final      
  end
  
  class Crypt
    
    attr_accessor :key, :iv, :cypher
    
    def initialize (key, iv=nil, cypher='BF-CBC')
      @cypher = cypher
      @iv = iv
      @key = key
    end
    
    def encrypt(text)
      Encryption.encrypt(text, @key, @iv, @cypher)
	  end

      
    def decrypt(text)
      Encryption.decrypt(text, @key, @iv, @cypher) 
    end
  end
end
    
#key = "c9d5d05e0fa8c53bb975a7a71f2fa2760227e67e2044065b9fb6eeaaeefcdb165f97de40fd977b2f8c910a78cc67f6a65fbc82193c05d4d65f543160526c61c6"
#iv = nil
#uid = 'GuyMurphy'
#    
#crypt = Encryption::Crypt.new(key)
#    
#(1..10000).each do |i|
#  puts "#> loop #{i}"
#  encrypted = crypt.encrypt(uid)
#  puts "encrypted #{encrypted}"
#  decrypted = crypt.decrypt(encrypted) 
#  puts "decrypted #{decrypted}"
#end

