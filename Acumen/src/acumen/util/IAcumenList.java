package acumen.util;


import java.util.List;
import java.util.Collection;
import java.io.IOException;

import acumen.data.xml.XmlWriter;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 13:12:36
 */
public interface IAcumenList<T> extends List<T>, IDataType {
	public void push (T item);
    public T pop ();
    public T peek (int offset);
    public T peek ();
    public int lastIndex ();
    public IAcumenList<T> unionMerge (Collection<T> collection);
    public void toXml (XmlWriter xml, String label) throws IOException;
}
