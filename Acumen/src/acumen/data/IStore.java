/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: IStore.java
 */
package acumen.data;

/**
 * Service based interface for all stores that
 * are used within Acumen.
 * 
 * @author Guy Murphy
 */
public interface IStore {
    StoreType getStoreType();
    Boolean isOpen();

    void start() throws StoreException;
    void stop() throws StoreException;
    void resume() throws StoreException;
    void pause() throws StoreException;
    void dispose() throws StoreException;
}
