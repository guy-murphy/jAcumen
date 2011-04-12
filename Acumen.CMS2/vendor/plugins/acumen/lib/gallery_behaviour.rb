# Author:   Daniel Enriquez, Guy J. Murphy
# Date:     08/03/2010
# Version:  0.1

require 'naiad'
require 'resolver'
require 'ap'

import 'acumen.map.model.IOccurence'
#import 'acumen.resource.store.MySqlFileStore'
#import 'acumen.resource.model.ResourceMetadata'
import 'acumen.service.io.GalleryResolver'


# Behaviour for resolving and manipulating image resources
# collected into galleries.

module Behaviour
  class GalleryBehaviour
    include Naiad
    
    attr_accessor :file_store, :current_map
    
    def initialize(file_store, map = :default)
      @file_store = file_store
      @current_map = map
    end
    
    def resolve!(occurence, version=nil)
      puts '***********************************************************'
      puts '#> resolving occurence'
      scope = occurence.get_scope
      resolver = GalleryResolver.new(Rails.root.to_s, file_store)
      relative_path = 'public/repositories/images/'+@current_map.to_s+'/'+scope+'/' + occurence.get_reference
      resolver.set_relative_path(relative_path)
      puts "relative path: #{relative_path}"
      puts "absolute path: #{resolver.get_full_path}"
      
      # does the repository exist?
      if resolver.exists
        files = resolver.get_files
        puts files.to_xml     
        occurence.set_resolved_objects(files)
      end
      
      puts '***********************************************************'
    end
    
  end
end
