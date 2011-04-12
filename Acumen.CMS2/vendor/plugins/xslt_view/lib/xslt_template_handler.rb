require 'utils'

import 'acumen.web.view.XslTransformer'
import 'acumen.util.PropertySet'

module Acumen
  module Templates
    class XsltTemplateHandler < ActionView::Template::Handler
          
      def initialize(action_view)
        super
      end

      def self.put_source(identifier, source)
        @@sources ||= {}
        @@sources[identifier] = source
      end
      
      def self.get_source(identifier)
        @@sources[identifier]
      end
      
      def self.has_put_already?(identifier)
        @@sources ||= {}
        @@sources.key?(identifier)
      end

      def self.render(view, identifier)
        params = PropertySet.new()
        params.put('root', Rails.root.to_s)
        params.put('controller', view.controller_name)
        params.put('action', view.action_name)
        
        key = "#{view.controller_name}.#{view.action_name}"
        view_state = view.instance_variable_get('@view_state')
        relative_path = view.instance_variable_get('@_virtual_path')
        
        source = get_source(identifier)
        
        Globals::XSL_TRANSFORMER.transform_text(key, view_state.to_xml, identifier, source, params)
      end
      
      def self.compile (template)
        "Acumen::Templates::XsltTemplateHandler.render(self, '#{template.identifier}')"
      end
      
      # This is the entry point into the responsibilities of
      # the template handler. This method gets called from
      # line #39 in action_view/template.rb
      #
      # The expected return from this is a string.
      # The string should be ruby code that can become
      # part of a ruby method declaration and be evaluated
      # at runtime.
      #
      # The 'compiled' method should return the result
      # of the template.
      def self.call(template)
        put_source(template.identifier, template.source) unless has_put_already?(template.identifier)
        compile(template)
      end
      
    end
  end
end
