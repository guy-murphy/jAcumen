package acumen.util;

/**
 * User: gmurphy
 * Date: 01-Oct-2009
 * Time: 11:18:17
 */
public class AcumenDoubleKeyDictionary<TKey1,TKey2,TValue> extends AcumenDictionary<TKey1,AcumenDictionary<TKey2,TValue>> {

	private static final long serialVersionUID = 1269011590338677474L;

	public AcumenDoubleKeyDictionary () {
        super();
    }

    public AcumenDoubleKeyDictionary (AcumenDictionary<TKey1, AcumenDictionary<TKey2, TValue>> dictionary) {
        super(dictionary);
    }

    public TValue get (TKey1 k1, TKey2 k2) {
        if (this.containsKeys(k1,k2)) {
            return this.get(k1).get(k2);    
        } else {
            return null;
        }
    }

    public void add (TKey1 k1, TKey2 k2, TValue value) {
        if (!super.containsKey(k1)) {
            super.put(k1, new AcumenDictionary<TKey2,TValue>());
        }
        super.get(k1).put(k2,value);
    }

    public void remove (TKey1 k1, TKey2 k2) {
        if (this.containsKeys(k1,k2)) {
            super.get(k1).remove(k2);
            if (super.get(k1).size() == 0) {
                super.remove(k1);
            }
        }
    }

    public boolean containsKeys (TKey1 k1, TKey2 k2) {
        if (super.containsKey(k1)) {
            if (super.get(k1).containsKey(k2)) {
                return true;
            }
        }
        return false;
    }

}
