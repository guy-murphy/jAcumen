package acumen.web.view;

import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import acumen.util.PropertySet;

import java.io.Writer;

/**
 * User: gmurphy
 * Date: 21-Oct-2009
 * Time: 16:21:57
 */
public interface IXslTransformer {
    void cacheStylesheet (String key, Transformer stylesheet);

    boolean isCached (String key);

    Transformer retrieveStylesheet (String key);

    Transformer removeStylesheet (String key);

//    Source getSourceFromPath (String path);
//
//    Source getSourceFromText (String systemId, String text);

    Transformer getStylesheet (Source source) throws XslTransformerException;

    Transformer getStylesheetFromPath (String path) throws XslTransformerException;

    Transformer getStylesheetFromText (String systemId, String text) throws XslTransformerException;

    void transform (Source xml, Transformer xsl, Writer writer, PropertySet params) throws XslTransformerException;

    void transformText (String xmlId, String xmlText, String xslId, String xslText, Writer writer, PropertySet params) throws XslTransformerException;

    String transformText (String xmlId, String xmlText, String xslId, String xslText, PropertySet params) throws XslTransformerException;

    void transformSources (Source xml, Source xsl, Writer writer, PropertySet params) throws XslTransformerException;

    void transformResources (String xmlPath, String xslPath, Writer writer, PropertySet params) throws XslTransformerException;
    
    void turnOnCaching ();
    void turnOffCaching ();
}
