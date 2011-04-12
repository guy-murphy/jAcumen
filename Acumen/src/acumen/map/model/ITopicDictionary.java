package acumen.map.model;

/**
 * User: gmurphy
 * Date: 01-Oct-2009
 * Time: 13:11:47
 */
public interface ITopicDictionary extends INodeDictionaryBase<ITopic> {
    public void filterByLanguageAndScope (Language language, String scope);
    public void filterByLanguage ();
    public void filterByLanguage (Language language);
    public void filterByScope ();
    public void filterByScope (String scope);
}
