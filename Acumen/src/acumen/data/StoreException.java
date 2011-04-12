/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: StoreException.java
 */
package acumen.data;

/**
 * An exception that has been thrown by a store.
 * 
 * @author Guy J. Murphy
 */
public class StoreException extends Exception {
    
	private static final long serialVersionUID = 7261975720973999291L;

	public StoreException (String message) {
        super(message);        
    }
}
