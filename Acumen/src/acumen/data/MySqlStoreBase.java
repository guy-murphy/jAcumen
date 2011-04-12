/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: MySqlStoreBase.java
 */
package acumen.data;

/** 
 * Base class for stores that are backing onto MySQL.
 * This class is only really ensures the correct JDBC URI.
 * 
 * @author Guy J. Murphy
 */
public class MySqlStoreBase extends SqlStoreBase {

	/**
	 * Instantiates a new MySqlStoreBase object, leaving
	 * the setting of credentials and server details to be done.
	 */
    public MySqlStoreBase() {
        super();
    }

    /**
     * Instantiates a new MySqlStoreBase object with
     * the spec provided.
     * 
     * @param server The server that is going to be connected to.
     * @param database The specific database that is going to be used.
     * @param user The username used for authentication.
     * @param password The password used for authentication.
     */
    public MySqlStoreBase(String server, String database, String user, String password) {
        super(server,database,user,password);
    }
    
    /**
     * Gets the specific JDBC driver that will be
     * used for this store. 
     */
    public String getDriver () {
        return "com.mysql.jdbc.Driver";
    }

    /**
     * Gets the full URL for a JDBC connection that will be
     * used by this store including credentials.
     */
    public String getUrl () {
        // jdbc:mysql://<server>/<database>?user=<name>&password=<password>
        return String.format(
            "jdbc:mysql://%s/%s?user=%s&password=%s", 
            this.getServer(),
            this.getDatabase(),
            this.getUserName(),
            this.getPassword()
        );
    }

}
