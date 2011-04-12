package acumen.map.model;

import acumen.util.AcumenDictionary;
import acumen.util.Assert;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 13:51:40
 */
public abstract class NodeDictionaryBase<T extends INode> extends AcumenDictionary<String,T> implements INodeDictionaryBase<T> {

	private static final long serialVersionUID = 5981459639189457351L;
	
	private INode _parent;
    private Language _currentLanguage;
    private String _currentScope;

    public NodeDictionaryBase () {
        super();
    }

    public NodeDictionaryBase (INodeDictionaryBase<T> dictionary) {
        super(dictionary);
        _parent = dictionary.getParent();
    }

    public NodeDictionaryBase (INode parent) {
        super();
        Assert.NotNull(parent);
        _parent = parent;
    }

    public NodeDictionaryBase (AcumenDictionary<String,T> dictionary) {
        super(dictionary);
    }

    public NodeDictionaryBase (INode parent, AcumenDictionary<String,T> dictionary) {
        this(dictionary);
        _parent = parent;
    }

    public INode getParent () {
        return _parent;
    }

    public void setParent (T parent) {
        Assert.NotNull(parent);
        _parent = parent;
    }

    public Language getCurrentLanguage () {
        return _currentLanguage;
    }

    public void setCurrentLanguage (Language language) {
        _currentLanguage = language;
    }

    public String getCurrentScope() {
        return _currentScope;
    }

    public void setCurrentScope (String scope) {
        _currentScope = scope;
    }

    public boolean isRoot () {
        return (_parent == null);
    }

    public void add (T node) {
        if (node.getParent() == null) {
            node.setParent(this.getParent());
        }
        super.put(node.getId(), node);
    }

    public void remove (T node) {
        super.remove(node.getId());
    }

}
