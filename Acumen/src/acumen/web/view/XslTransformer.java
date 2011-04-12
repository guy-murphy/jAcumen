package acumen.web.view;

import acumen.util.Is;
import acumen.util.PropertySet;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 * User: gmurphy
 * Date: 21-Oct-2009
 * Time: 12:28:27
 */
public class XslTransformer implements IXslTransformer {
	
	private boolean _cache;
	
	public static Source getSourceFromPath (String path) {
        File file = new File(path);
        Source source = new StreamSource(file);
        source.setSystemId(path);
        return source;
    }

    public static Source getSourceFromText (String systemId, String text) {
        StringReader reader = new StringReader(text);
        Source source = new StreamSource(reader);
        source.setSystemId(systemId);
        return source;
    }
    
    private Map<String,Transformer> _xslCache;
    private TransformerFactory _factory;
    private String _root;
    private ResourceResolver _resolver;

    public XslTransformer(Map<String,Transformer> xslCache, String root) {
    	_cache = true;
        _xslCache = (xslCache == null) ? new HashMap<String,Transformer>() :_xslCache;
        _factory = TransformerFactory.newInstance();
        _root = root;
        _resolver = new ResourceResolver(_root);
        _factory.setURIResolver(_resolver);
    }
    
    public XslTransformer(String root) {
    	this(null, root);
    }

    public XslTransformer() {
        this(null,"");
    }
    
    public void turnOnCaching () {
    	_cache = true;
    }
    
    public void turnOffCaching () {
    	_cache = false;
    }
    
    protected String getRoot () {
    	return _root;
    }
    
    protected ResourceResolver getResolver () {
    	return _resolver;
    }

    protected Map<String,Transformer> getXslCache () {
        return _xslCache;
    }

    public void cacheStylesheet (String key, Transformer stylesheet) {
        _xslCache.put(key, stylesheet);
    }

    public boolean isCached (String key) {
        return _xslCache.containsKey(key);
    }

    public Transformer retrieveStylesheet (String key) {
        Transformer transformer = _xslCache.get(key);
        if (transformer != null) {
        	System.out.println(String.format("#> XSL Cache HIT for '%s'.", key));
        } else {
        	System.out.println(String.format("#> XSL Cache MISS for '%s'.", key));
        }
        return transformer;
    }

    public Transformer removeStylesheet (String key) {
        return _xslCache.remove(key);
    }

    public Transformer getStylesheet (Source source) throws XslTransformerException {
        if (Is.NullOrEmpty(source.getSystemId())) {
            throw new XslTransformerException("All sources used for transformations must have a system id.");
        }
        Transformer transformer = (_cache) ? this.retrieveStylesheet(source.getSystemId()) : _makeStylesheet(source);
        if (transformer == null) {
        	if (_cache) {
        		transformer = _makeStylesheet(source);
        	} else {
        		throw new XslTransformerException(String.format("Unable to make stylesheet for %s.", source.getSystemId()));
        	}
        }
        return transformer;
    }
    
    private Transformer _makeStylesheet (Source source) throws XslTransformerException {
    	try {
	    	Transformer transformer = _factory.newTransformer(source);
	        transformer.setURIResolver(_resolver);
	        if (_cache) this.cacheStylesheet(source.getSystemId(), transformer);
	        return transformer;
    	} catch (TransformerConfigurationException err) {
            throw new XslTransformerException(err.getMessage());
        }
    }

    public Transformer getStylesheetFromPath (String path) throws XslTransformerException {
        return this.getStylesheet(XslTransformer.getSourceFromPath(path));
    }

    public Transformer getStylesheetFromText (String systemId, String text) throws XslTransformerException {
        return this.getStylesheet(XslTransformer.getSourceFromText(systemId, text));
    }

    public void transform (Source xml, Transformer xsl, Writer writer, PropertySet params) throws XslTransformerException {
        Result result = new StreamResult(writer);
        try {
        	for (Entry<String,String> entry: params.entrySet()) {
            	xsl.setParameter(entry.getKey(), entry.getValue());
            }
            xsl.transform(xml, result);
            xsl.clearParameters();
        } catch (TransformerException err) {
            throw new XslTransformerException(err.getMessage());
        }
    }

    public void transformText (String xmlId, String xmlText, String xslId, String xslText, Writer writer, PropertySet params) throws XslTransformerException {
        Source xml = XslTransformer.getSourceFromText(xmlId, xmlText);
        Source xsl = XslTransformer.getSourceFromText(xslId, xslText);
        this.transformSources(xml, xsl, writer, params);
    }

    public String transformText (String xmlId, String xmlText, String xslId, String xslText, PropertySet params) throws XslTransformerException {
    	// This is the most common entry-point from the rails stack.
        StringWriter writer = new StringWriter();
        this.transformText(xmlId, xmlText, xslId, xslText, writer, params);
        return writer.toString();
    }

    public void transformSources (Source xml, Source xsl, Writer writer, PropertySet params) throws XslTransformerException {
        Transformer transformer = this.getStylesheet(xsl);
        this.transform(xml, transformer, writer, params);
    }

    public void transformResources (String xmlPath, String xslPath, Writer writer, PropertySet params) throws XslTransformerException {
        Source xml = XslTransformer.getSourceFromPath(xmlPath);
        Transformer xsl = this.getStylesheetFromPath(xslPath);
        this.transform(xml, xsl, writer, params);
    }

}
