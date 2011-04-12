/**
 * User: Guy J. Murphy
 * Date: Feb 15, 2010
 * Time: 5:17:40 PM
 * File: LayoutResolver.java
 */
package acumen.web.view;

import javax.xml.transform.*;

/**
 * @author Guy J. Murphy
 */
public class ResourceResolver implements URIResolver {
	
	private String _root;
	
	public ResourceResolver (String root) {
		_root = root;
	}

	/* (non-Javadoc)
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
	 */
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		System.out.println(href);
		System.out.println(base);
		if (href.startsWith("layout:")) {
			String reference = href.substring(7);
			String path = String.format("%s/app/views/layouts/%s", _root, reference);
			return XslTransformer.getSourceFromPath(path);
		} else {
			return null;
		}
	}

}
