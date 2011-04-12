/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: SqlStoreBase.java
 */
package acumen.data;

import java.sql.*;

import acumen.util.Is;

/**
 * The base class for any store that is backing
 * onto a SQL RDBMS.
 * 
 * The basic functionality that this class provides
 * is handling of credentials and server connection,
 * along with a service based API of
 * start, stop, pause and resume.
 * 
 * Standard interaction functionality is provided
 * via read and exec methods that are common across
 * all SQL based stores.
 * 
 * @author Guy J. Murphy
 */
public abstract class SqlStoreBase implements IStore {

    private Boolean _isDisposed;
    private StoreState _state;
    private Connection _connection;
    private String _server;
    private String _database;
    private String _userName;
    private String _password;

    public abstract String getDriver();
    public abstract String getUrl();

    /**
     * Instantiates a SqlStoreBase object without any
     * server, database, or credentials specified.
     * 
     * This constructor cannot be used directly.
     */
    protected SqlStoreBase () {
        this(null,null,null,null);
    }

    /**
     * Instantiates a new SqlStoreBase object with the server,
     * database, and credenttials specified.
     * 
     * This constructor cannot be used directly.
     * 
     * @param server The server that is going to be connected to.
     * @param database The specific database that is going to be used.
     * @param user The username used for authentication.
     * @param password The password used for authentication.
     */
    protected SqlStoreBase (String server, String database, String user, String password) {
        _isDisposed = false;
        _state = StoreState.Closed;
        _connection = null;
        this.setServer(server);
        this.setDatabase(database);
        this.setUserName(user);
        this.setPassword(password);
    }
    
    /**
     * Finalizes the store ensuring it's in a fit state
     * to throw away.
     */
    protected void finalize () throws Throwable {
        super.finalize();
        this.dispose();
    }

    /**
     * Closes the specified reader if it is not null and open.
     * 
     * @param reader The reader to close.
     * @throws StoreException A rethrow of any exception that occurs as a
     * result of trying to close the reader.
     */
    protected static void closeReader (ResultSet reader) throws StoreException {
    	try {
    		if (reader != null && !reader.isClosed()) reader.close();
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    }

    /**
     * Makes the store ready to be disposed.
     */
    public void dispose () throws StoreException {
        if (!_isDisposed) {
            this.stop();
            _isDisposed = true;
        }
    }

    /**
     * Determines whether or not the store is currently open.
     */
    public Boolean isOpen () {
        return (_state == StoreState.Open);
    }

    /**
     * Retrieves the current StoreType of this store.
     * 
     * @return 
     */
    public StoreType getStoreType () {
        return StoreType.Connecting;
    }

    /**
     * 
     * @return
     */
    public StoreState getState () {
        return _state;
    }

    public String getUserName () {
        return _userName;
    }

    public void setUserName (String name) {
        _userName = name;
    }

    public String getPassword () {
        return _password;
    }

    public void setPassword (String password) {
        _password = password;
    }

    public void setServer (String server) {
        _server = server;
    }

    public String getServer () {
        return _server;
    }

    public String getDatabase () {
        return _database;
    }

    public void setDatabase (String database) {
        _database = database;
    }

    public void assertIsOpen () throws StoreException {
        if (_state == StoreState.Closed) {
            throw new StoreException("The store you are attempting to use is not open.");
        } else if (_state == StoreState.Paused) {
            throw new StoreException("The store you are attempting to use has been paused.");
        }
    }

    public void assertIsClosed () throws StoreException {
        if (_state == StoreState.Open) {
                throw new StoreException("The store you are attempting to open is already open");
            } else if (_state == StoreState.Paused) {
                throw new StoreException("The store you are attempting to start has been paused.");
            }
    }

     private Connection getConnection () throws StoreException {
        if (_connection != null) {
            return _connection;
        } else {
            try {
                Class.forName(this.getDriver()).newInstance();
                _connection = DriverManager.getConnection(this.getUrl(), this.getUserName(), this.getPassword());

                return _connection;

            } catch (ClassNotFoundException err) {
                throw new StoreException(String.format("The driver %s is unknown.", this.getDriver()));
            } catch (SQLException err) {
                throw new StoreException("Unable to open a connection to the database.");
            } catch (IllegalAccessException err) {
                throw new StoreException(String.format("Unable to access the driver %s.", this.getDriver()));
            } catch (InstantiationException err) {
                throw new StoreException(String.format("There was a problem instantiating the driver %s.", this.getDriver()));
            }
        }
    }

    public void start () throws StoreException {
        assertIsClosed();

        if (!_isDisposed) {
            _connection = this.getConnection();
            _state = StoreState.Open;
        }
    }

    public void stop () throws StoreException {
        if (_state != StoreState.Closed) {
            try {
                if (_connection != null) {
                    _connection.close();
                    _connection = null;
                }
            } catch (SQLException err) {
                throw new StoreException("There was a problem cleaning up ther resources for this store.");    
            }
            _state = StoreState.Closed;
        }
    }

    public void pause () throws StoreException {
        assertIsOpen();
        _state = StoreState.Paused;
    }

    public void resume () throws StoreException {
        if (_state == StoreState.Closed) {
            throw new StoreException("The store you are trying to resume is closed.");
        } else if (_state == StoreState.Paused) {
            _state = StoreState.Open;
        }
    }

    public boolean exists (String sql) throws StoreException {
        try {
            ResultSet reader = null;
            try {
                reader = this.read(sql);
                return reader.next();
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }

    public boolean exists (String sql, Object ... params) throws StoreException {
        try {
            ResultSet reader = null;
            try {
                reader = this.read(sql, params);
                return reader.next();
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }
    
    public boolean existsWithNames (String sql, Object ... params) throws StoreException {
        try {
            ResultSet reader = null;
            try {
                reader = this.readWithNames(sql, params);
                return reader.next();
            } finally {
                closeReader(reader);
            }
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }
    
    public String getString (String sql) throws StoreException {
    	return this.getString(sql, null);
    }
    
    public String getString(String sql, String columnName) throws StoreException {
    	ResultSet reader = this.read(sql);
    	String result = null;
    	try {
	    	try {
	    		while (reader.next()) {
		    		if (Is.NotNullOrEmpty(columnName)) {
		    			result = reader.getString(columnName);
		    		} else {
		    			result = reader.getString(0);
		    		}
		    	}
	    	} finally {
	    		closeReader(reader);
	    	}
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    	return result;
    }
    
    public int getInt (String sql, String columnName) throws StoreException {
    	ResultSet reader = this.read(sql);
    	int result = 0;
    	try {
    		try {
		    	while (reader.next()) {
		    		if (Is.NotNullOrEmpty(columnName)) {
		    			result = reader.getInt(columnName);
		    		} else {
		    			result = reader.getInt(0);
		    		}
		    	}
    		} finally {
    			closeReader(reader);
    		}
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    	return result;
    }
    
    public int getInt(String sql) throws StoreException {
    	return this.getInt(sql, null);
    }

    public void exec (String sql) throws StoreException {
        assertIsOpen();
        try {
            Statement statement = _connection.createStatement();
            statement.executeUpdate(sql);
            //_connection.commit();
        } catch (SQLException outer) {
            throw new StoreException("Unable to execute the provided queary against the database.");
        }
    }

    private PreparedStatement _prepareStatement (String sql, Object ... params) throws StoreException {
        assertIsOpen();
        try {
            PreparedStatement preparedStatement = _connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                //preparedStatement.setString(i+1, params[i]);
            	preparedStatement.setObject(i+1, params[i]);
            }
            return preparedStatement;
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }
    
    private NamedParameterStatement _prepareNamedParameterStatement (String sql, Object ... params) throws StoreException {
    	assertIsOpen();
    	try {
    		NamedParameterStatement statement = new NamedParameterStatement(_connection, sql);
    		for (int i = 0; i < params.length; i++) {
    			if (i % 2 == 1) {
    				statement.setObject(params[i-1].toString(), params[i]);
    			}
    		}
    		return statement;
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    }

    public void exec (String sql, Object ... params) throws StoreException {
        assertIsOpen();
        try {
            PreparedStatement statement = _prepareStatement(sql, params);
            statement.executeUpdate();
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }
    
    public void execWithNames (String sql, Object ... params) throws StoreException {
    	assertIsOpen();
    	try {
    		NamedParameterStatement statement = _prepareNamedParameterStatement(sql, params);
    		statement.executeUpdate();
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    }

    public ResultSet read (String sql) throws StoreException {
        assertIsOpen();
        try {
            Statement statement = _connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }

    public ResultSet read (String sql, Object ... params) throws StoreException {
        assertIsOpen();
        try {
            PreparedStatement statement = _prepareStatement(sql, params);
            return statement.executeQuery();
        } catch (SQLException err) {
            throw new StoreException(err.getMessage());
        }
    }
    
    public ResultSet readWithNames (String sql, Object ... params) throws StoreException {
    	assertIsOpen();
    	try {
    		NamedParameterStatement statement = _prepareNamedParameterStatement(sql, params);
    		return statement.executeQuery();
    	} catch (SQLException err) {
    		throw new StoreException(err.getMessage());
    	}
    }
}
