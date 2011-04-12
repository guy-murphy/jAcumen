package test.acumen.map.store;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertEquals;
import org.junit.*;

import acumen.data.MySqlStoreBase;
import acumen.data.StoreException;
import acumen.data.StoreType;

/**
 * User: gmurphy
 * Date: 23-Sep-2009
 * Time: 16:58:10
 */
public class MySqlStoreBaseTest {

    private MySqlStoreBase _store;

    public MySqlStoreBaseTest () throws StoreException {
        _reset();                
    }

    private void _reset() throws StoreException {
        if (_store != null) {
            if (_store.isOpen()) {
                _store.stop();
            }
        }
        _store = new MySqlStoreBase("leviathan.acumen.es", "acumen_read", "root", "redacted");
    }

    @Test
    public void getDriver () throws StoreException {
        _reset();
        String expected = "com.mysql.jdbc.Driver";
        assertEquals(_store.getDriver(), expected);
    }

    @Test
    public void getUrl () throws StoreException {
        _reset();
        String expected = "jdbc:mysql://localhost/acumen?user=root&password=redacted";
        assertEquals(_store.getUrl(), expected);
    }

    @Test
    public void isOpen () throws StoreException {
        _reset();
        assertFalse(_store.isOpen());
        try {
            _store.start();
            assertTrue(_store.isOpen());
        } finally {
            _store.stop();
        }
    }

    @Test
    public void getStoreType () throws StoreException {
        assertTrue(_store.getStoreType() == StoreType.Connecting);
    }

}
