# Author:: Daniel Enriquez
# Copyright:: Acumen CMS
# Date:: 09-03-2010

require 'naiad'

import 'acumen.map.model.IOccurence'
import 'acumen.resource.store.MySqlFileStore'

class Resolver
  include Naiad
  
  attr_accessor :relative_path, :root, :occurence, :current_map, :full_path
  
  def initialize(occurence, map)
    @occurence = occurence
    @current_map = map
    # Init paths
    @relative_path = @occurence.reference
    case @occurence.get_behaviour
      when 'images'
        @root = services[:images_resolver, @current_map]
      when 'attachments'
        @root = services[:attachments_resolver, @current_map]
    end
    @full_path = "#{Rails.root}/public/#{@root}/#{@relative_path}"
  end
  
  def exists?
    File.directory?(@full_path)
  end
  
  def files
    allowed = case @occurence.get_behaviour
      when 'images' then Globals::ALLOWED_IMAGES
      when 'attachments' then  Globals::ALLOWED_ATTACHMENTS
    end
    files = Dir.entries(@full_path)
    # remove the '.' and '..' in the collection
    files.delete_if{|file| file == '.' or file == '..'}
    # filter out files not permitted
    files.delete_if{|file| (not allowed.include?(File.extname(file).downcase))}
    return files
  end
  
end
