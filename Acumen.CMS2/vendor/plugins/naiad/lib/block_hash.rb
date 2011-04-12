class BlockHash
  def initialize(parent=nil)
    @blocks = {}
    @cache = {}
  end
  
  def add_block(name, &block)
    if @blocks[name]
      fail NameExistsError, "A service is already under the name '#{name}'."
    end
    @services[name] = block
  end
  
  def obtain_block(name)
    @blocks[name]
  end
  
  def get_block(name)
    @cache[name] ||= obtain_block(name).call(self)
  end
  
  def [](name)
    self.lookup_service(name)
  end
  
  def each (&block)
    @blocks.each(&block)
  end
end

