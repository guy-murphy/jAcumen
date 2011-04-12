package acumen.map.model;

import acumen.util.Assert;
import acumen.util.Is;

import java.util.UUID;
import java.io.IOException;

import acumen.data.xml.XmlWriter;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 12:03:12
 */
public class Association extends NodeBase implements IAssociation {

    private String _source;
    private String _reference;
    private String _type;
    private String _role;
    private String _scope;
    private ITopic _resolvedTopic;

    public Association (ITopic parent, String id, String reference, String type, String role, String scope) {
        super(parent, id);
        _reference = reference;
        _type = type;
        _role = role;
        _scope = scope;
        _resolvedTopic = null;
    }

    public Association () {
        super();
    }

    public Association (String id) {
        super(id);
    }

    public Association (IAssociation association) {
        super(association);
        _reference = association.getReference();
        _type = association.getType();
        _role = association.getRole();
        _scope = association.getScope();
        _resolvedTopic = null;
    }

    public Association (ITopic parent, String id) {
        this(parent, id, null, null, null, null);
    }

    public Association (ITopic parent, String id, String reference, String type, String role) {
        super(parent, id);
        _reference = reference;
        _type = type;
        _role = role;
        _scope = (this.getParent() == null) ? NodeBase.DEFAULT_SCOPE : this.getParent().getCurrentScope();
        _resolvedTopic = null;
    }

    public Association (ITopic parent, String reference, String type, String role) {
        this(parent, UUID.randomUUID().toString(), reference, type, role);
    }
    

    private boolean _belongsToTopic () {
        return this.getParent() != null && this.getParent() instanceof ITopic;
    }

    public String getSource () {
        if (_belongsToTopic()) {
            return this.getParent().getId();
        } else {
            return _source;
        }
    }

    public void setSource (String source) {
        if (_belongsToTopic()) {
            throw new IllegalArgumentException("You may not assign a source to an association that has a parent node.");           
        } else {
            Assert.HasValue(source);
            _source = source;
        }
    }

    public String getReference () {
        return _reference;
    }

    public void setReference (String reference) {
        Assert.HasValue(reference);
        _reference = reference;
    }

    public String getType () {
        return _type;
    }

    public void setType (String type) {
        _type = type;
    }

    public String getRole () {
        return _role;
    }

    public void setRole (String role) {
        Assert.HasValue(role);
        _role = role;
    }

    public String getScope () {
        return _scope;
    }

    public void setScope (String scope) {
        Assert.HasValue(scope);
        _scope = scope;
    }

    public ITopic getResolvedTopic () {
        return _resolvedTopic;
    }

    public void setResolvedTopic (ITopic topic) {
        Assert.NotNull(topic);
        _resolvedTopic = topic;
    }

    public String getOrdinal () {
        String ordinal = this.getMeta().get("ordinal");
        return Is.NullOrEmpty(ordinal) ? "0" : ordinal;
    }

    public void setOrdinal (String ordinal) {
        this.getMeta().set("ordinal", ordinal);
    }
//
//    public String getCurrentScope () {
//        return Is.NullOrEmpty(_scope) ? this.getParent().getCurrentScope() : _scope;
//    }
//
//    public void setCurrentScope (String scope) {
//        this.setScope(scope);
//    }

    public void toXml (XmlWriter xml) throws IOException {
        xml.writeEntity("association");
        xml.writeAttribute("id", this.getId());
        xml.writeAttribute("label", this.getLabel());
        xml.writeAttribute("source", this.getSource());
        xml.writeAttribute("reference", this.getReference());
        xml.writeAttribute("type", this.getType());
        xml.writeAttribute("role", this.getRole());
        xml.writeAttribute("scope", this.getScope());
        this.getMeta().toXml(xml);
		if (this.getResolvedTopic() != null) {
			this.getResolvedTopic().toXml(xml);
		}
        xml.endEntity();
    }


}
