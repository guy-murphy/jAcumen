package acumen.web.view;

import acumen.util.AcumenDictionary;
import acumen.util.IDataType;

/**
 * User: gmurphy
 * Date: 22-Oct-2009
 * Time: 12:03:18
 */
public class ViewState extends AcumenDictionary<String, IDataType> {

	private static final long serialVersionUID = 8129969175290836867L;

	public ViewState () {
        super("view-state");
    }

}
