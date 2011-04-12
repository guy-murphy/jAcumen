require 'xslt_template_handler'

ActionView::Template.register_template_handler :xsl, Acumen::Templates::XsltTemplateHandler
#ActionView::Template.exempt_from_layout :xsl
