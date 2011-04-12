class XsltViewGenerator < Rails::Generator::NamedBase
  def manifest
    record do |m|
        m.directory "Views/"
        m.template 'README.txt', "README.txt"
    end
  end
end
