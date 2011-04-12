package acumen.resource.store;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import acumen.resource.model.*;

import acumen.data.MySqlStoreBase;
import acumen.data.StoreException;
import acumen.util.AcumenDictionary;
import acumen.util.AcumenList;
import acumen.util.Assert;
import acumen.util.Is;

public class MySqlFileStore extends MySqlStoreBase implements IFileStore {

	private final String DEFAULT_FILE_TYPE = "unknown";

	private Charset _encoding;
	private String _namespace;

	public static Resource readResource(ResultSet reader, boolean readData) throws StoreException {
		try {
			String id = reader.getString("fs_code");
			Resource resource = new Resource(id);
			resource.setBranch(reader.getInt("fs_branch"));
			resource.setOpened(reader.getDate("fs_opened"));
			resource.setCommited(reader.getDate("fs_commited"));
			resource.setCreated(reader.getDate("fs_created"));
			resource.setCreatedBy(reader.getString("fs_createdby"));
			resource.setLockedBy(reader.getString("fs_lockedby"));
			resource.setName(reader.getString("fs_name"));
			resource.setNamespace(reader.getString("fs_namespace"));
			resource.setRights(reader.getInt("fs_rights"));
			resource.setType(reader.getString("fs_type"));
			if (readData) {
				resource.setData(reader.getBytes("fs_data"));
			} else {
				resource.setData(null);
			}
			resource.getMetadata().fromString(reader.getString("fs_meta"));
			return resource;
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR the store barfed
		}
	}

	public static Resource readSingleResource(ResultSet reader) throws StoreException {
		try {
			while (reader.next()) {
				return readResource(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR the store barfed
		}
		return null;
	}

	public static Resource readResource(ResultSet reader) throws StoreException {
		return readResource(reader, true);
	}
	
	public MySqlFileStore (String server, String database, String user, String password) {
		super(server, database, user, password);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getEncoding()
	 */
	public Charset getEncoding() {
		if (_encoding == null) {
			_encoding = Charset.forName("UTF8");
		}
		return _encoding;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#setEncoding(java.nio.charset.Charset)
	 */
	public void setEncoding(Charset encoding) {
		_encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#setNamespace(java.lang.String)
	 */
	public void setNamespace(String namespace) {
		_namespace = namespace;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getNamespace()
	 */
	public String getNamespace() {
		if (Is.NullOrEmpty(_namespace))
			_namespace = "none";
		return _namespace;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getSize()
	 */
	public int getSize() throws StoreException {
		return this.getInt("SELECT count(fs_code) FROM filestore");
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getIds()
	 */
	public String[] getIds() throws StoreException {
		int size = this.getSize();
		String[] ids = new String[size];
		ResultSet reader = null;
		try {
			reader = this.read("SELECT fs_code FROM filestore");
			int idx = 0;
			while (reader.next() && idx < size) { // guard against an overrun of the array
				ids[idx] = reader.getString("fs_code");
				idx++;
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR the store barfed
		}
		return ids;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getResourceNames()
	 */
	public String[] getResourceNames() throws StoreException {
		ArrayList<String> names = new ArrayList<String>();
		ResultSet reader = null;
		try {
			try {
				reader = this.read("SELECT distinct fs_name FROM filestore");
				while (reader.next()) {
					names.add(reader.getString("fs_name"));
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage());
		}
		String[] result = new String[names.size()];
		return names.toArray(result);
	}

	private Resource[] _getResources(boolean getData) throws StoreException {
		int size = this.getSize();
		Resource[] resources = new Resource[size];
		ResultSet reader = null;
		try {
			try {
				reader = this.read("select * from filestore");
				int idx = 0;
				while (reader.next() && idx < size) {
					resources[idx] = readResource(reader, getData);
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR the store barfed
		}
		return resources;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getResources()
	 */
	public Resource[] getResources() throws StoreException {
		return _getResources(true);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getStubs()
	 */
	public Resource[] getStubs() throws StoreException {
		return _getResources(false);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#resourceInstanceExists(java.lang.String)
	 */
	public boolean resourceInstanceExists(String id) throws StoreException {
		String sql = "select count(fs_code) from filestore where fs_code=?";
		ResultSet reader = null;
		try {
			try {
				reader = this.read(sql, id);
				while (reader.next()) {
					return true;
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR underlying SQL error
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#resourceExists(java.lang.String, java.lang.String)
	 */
	public boolean resourceExists(String nameSpace, String name) throws StoreException {
		String sql = "select fs_code from filestore where fs_namespace=? and fs_name=?";
		ResultSet reader = null;
		try {
			try {
				reader = this.read(sql, nameSpace, name);
				while (reader.next()) {
					return true;
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR underlying SQL error
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#resourceExists(java.lang.String)
	 */
	public boolean resourceExists(String name) throws StoreException {
		return this.resourceExists(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#addResource(acumen.resource.model.IResource)
	 */
	public void addResource(IResource resource) throws StoreException {
		this.removeResourceInstance(resource.getId());
		String sql = "insert into filestore\r\n" + "(fs_code, fs_namespace, fs_name, fs_created, fs_opened, fs_commited, fs_branch, fs_rights, fs_type, fs_createdby, fs_lockedby, fs_data, fs_meta)\r\n" + "values\r\n" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		this.exec(sql, resource.getId(), resource.getNamespace(), resource.getName(), resource.getCreated(), resource.getOpened(), resource.getCommited(), resource.getBranch(), resource.getRights(), resource.getType(), resource.getCreatedBy(), resource.getLockedBy(), resource.getData(), resource.getMetadata().toString());
		if (Is.NullOrEmpty(resource.getLockedBy())) {
			this.synchroniseResource(resource);
		} else if (resource.getLockedBy().equals("DELETED")) {
			this.cleanupResource(resource);
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#removeResourceInstance(java.lang.String)
	 */
	public void removeResourceInstance(String id) throws StoreException {
		Resource resource = this.getResourceInstance(id);
		if (resource != null) {
			this.exec("delete from filestore where fs_code = ?", id);

			if (this.hasInstances(resource.getNamespace(), resource.getName())) {
				this.cleanupResource(resource);
			}
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#removeResource(java.lang.String)
	 */
	public void removeResource(String name) throws StoreException {
		this.removeResource(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#removeResource(java.lang.String, java.util.List)
	 */
	public void removeResource(String nameSpace, List<String> names) {
		String[] nameArray = new String[names.size()];
		names.toArray(nameArray);
		this.removeResource(nameSpace, names);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#removeResource(java.lang.String, java.lang.String)
	 */
	public void removeResource(String nameSpace, String... names) throws StoreException {
		for (String name : names) {
			this.removeResource(nameSpace, name);
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#removeResource(java.lang.String, java.lang.String)
	 */
	public void removeResource(String nameSpace, String name) throws StoreException {
		_removeResource(nameSpace, name);
		this.cleanupResource(nameSpace, name);
	}

	private void _removeResource(String nameSpace, String name) throws StoreException {
		this.exec("delete from filestore where fs_namespace = ? and fs_name = ?", nameSpace, name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getResourceInstance(java.lang.String)
	 */
	public Resource getResourceInstance(String id) throws StoreException {
		ResultSet reader = this.read("select * from filestore where fs_code = ?", id);
		try {
			return readSingleResource(reader);
		} finally {
			closeReader(reader);
		}
	}

	private Resource _getInstanceFor(String sql, String nameSpace, String name) throws StoreException {
		ResultSet reader = this.readWithNames(sql, "namespace", nameSpace, "name", name);
		try {
			return readSingleResource(reader);
		} finally {
			closeReader(reader);
		}
	}

	private AcumenDictionary<String, Resource> _getInstancesForNamespace(String sql, String nameSpace) throws StoreException {
		AcumenDictionary<String, Resource> results = new AcumenDictionary<String, Resource>();
		Resource resource = null;
		ResultSet reader = null;
		try {
			try {
				reader = this.read(sql, nameSpace);
				while (reader.next()) {
					resource = readResource(reader, false);
					if (!resource.getLockedBy().equals("DELETED")) {
						results.put(resource.getName(), resource);
					}
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR underlying SQL error
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getLockedInstance(java.lang.String)
	 */
	public Resource getLockedInstance(String name) throws StoreException {
		return this.getLockedInstance(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getLockedInstance(java.lang.String, java.lang.String)
	 */
	public Resource getLockedInstance(String nameSpace, String name) throws StoreException {
		String sql = "select \r\n" + "				* from filestore\r\n" + "			where \r\n" + "				fs_namespace = :namespace and \r\n" + "				fs_name = :name and\r\n" + "				fs_lockedby is NOT NULL and\r\n" + "				fs_opened = (\r\n" + "					select max(fs_opened)\r\n" + "						from filestore\r\n" + "					where\r\n" + "						fs_namespace = :namespace and \r\n" + "						fs_name = :name\r\n" + "				) AND\r\n" + "				  fs_opened > (\r\n" + "					select max(fs_commited)\r\n" + "						from filestore\r\n" + "						where\r\n" + "							fs_namespace = :namespace and \r\n" + "							fs_name = :name and\r\n" + "							fs_lockedby IS NULL\r\n" + "				  )";
		return _getInstanceFor(sql, nameSpace, name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getPreviousLockedInstance(java.lang.String)
	 */
	public Resource getPreviousLockedInstance(String name) throws StoreException {
		return this.getPreviousLockedInstance(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getPreviousLockedInstance(java.lang.String, java.lang.String)
	 */
	public Resource getPreviousLockedInstance(String nameSpace, String name) throws StoreException {
		String sql = "select \r\n" + "				* from filestore \r\n" + "			where \r\n" + "				fs_namespace = :namespace and \r\n" + "				fs_name = :name and \r\n" + "				fs_lockedby is NOT NULL and\r\n" + "				fs_opened = (\r\n" + "					select max(fs_opened)\r\n" + "						from filestore\r\n" + "					where\r\n" + "						fs_namespace = :namespace and \r\n" + "						fs_name = :name\r\n" + "				)";
		return _getInstanceFor(sql, nameSpace, name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#isLocked(java.lang.String)
	 */
	public boolean isLocked(String name) throws StoreException {
		return this.isLocked(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#isLocked(java.lang.String, java.lang.String)
	 */
	public boolean isLocked(String nameSpace, String name) throws StoreException {
		return (this.getLockedInstance(nameSpace, name) != null) ? true : false;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getMostRecentInstance(java.lang.String)
	 */
	public Resource getMostRecentInstance(String name) throws StoreException {
		return this.getMostRecentInstance(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getInstanceCountFor(java.lang.String, java.lang.String)
	 */
	public int getInstanceCountFor(String nameSpace, String name) throws StoreException {
		AcumenList<Resource> instances = this.getInstancesFor(nameSpace, name);
		return instances.size();
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#hasInstances(java.lang.String, java.lang.String)
	 */
	public boolean hasInstances(String nameSpace, String name) throws StoreException {
		return (this.getInstanceCountFor(nameSpace, name) > 0) ? true : false;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getInstancesFor(java.lang.String, java.lang.String)
	 */
	public AcumenList<Resource> getInstancesFor(String nameSpace, String name) throws StoreException {
		String sql = "select * from filestore where\r\n" + "				fs_namespace = ? and\r\n" + "				fs_name = ?";
		AcumenList<Resource> results = new AcumenList<Resource>();
		ResultSet reader = null;
		try {
			try {
				reader = this.read(sql, nameSpace, name);
				while (reader.next()) {
					results.add(readResource(reader));
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage()); // ERROR underlying SQL error
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getMostRecentInstance(java.lang.String, java.lang.String)
	 */
	public Resource getMostRecentInstance(String nameSpace, String name) throws StoreException {
		String sql = "SELECT * FROM filestore WHERE \r\n" + "				fs_namespace = :namespace and\r\n" + "				fs_name = :name and\r\n" + "				fs_commited = (SELECT Max(fs_commited) FROM filestore WHERE fs_namespace = :namespace AND fs_name = :name)";
		Resource resource = _getInstanceFor(sql, nameSpace, name);
		if (resource != null) {
			return (resource.getLockedBy() == null) ? resource : null;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getOriginalInstance(java.lang.String)
	 */
	public Resource getOriginalInstance(String name) throws StoreException {
		return this.getOriginalInstance(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getOriginalInstance(java.lang.String, java.lang.String)
	 */
	public Resource getOriginalInstance(String nameSpace, String name) throws StoreException {
		String sql = "SELECT * FROM filestore WHERE fs_namespace = :namespace AND fs_name = :name AND fs_opened IS NULL";
		return _getInstanceFor(sql, nameSpace, name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getMostRecentForNamespace(java.lang.String)
	 */
	public AcumenDictionary<String, Resource> getMostRecentForNamespace(String nameSpace) throws StoreException {
		String sql = "select\r\n" + "				a.fs_commited,\r\n" + "				a.fs_name,a.fs_code,a.fs_namespace,a.fs_created, a.fs_opened, a.fs_branch,a.fs_rights, a.fs_type, a.fs_createdby, a.fs_lockedby, a.fs_meta\r\n" + "				from \r\n" + "					filestore a,\r\n" + "					(select \r\n" + "						fs_name,\r\n" + "						max(fs_commited) fs_commited\r\n" + "					from \r\n" + "						filestore where fs_namespace = ?\r\n" + "					group by fs_name) b\r\n" + "				where \r\n" + "					a.fs_name = b.fs_name and a.fs_commited = b.fs_commited";
		return _getInstancesForNamespace(sql, nameSpace);
	}

	public static void assertCreationInvariance(IResource resource) throws IllegalArgumentException {
		Assert.NotNull("id", resource.getId());
		Assert.NotNull("namespace", resource.getNamespace());
		Assert.NotNull("name", resource.getName());
		Assert.NotNull("created", resource.getCreated());
		Assert.NotNull("commited", resource.getCommited());
		Assert.NotNull("created-by", resource.getCreatedBy());
		Assert.NotNull("type", resource.getType());
		Assert.NotNull("data", resource.getData());
		Assert.IsNull("opened", resource.getOpened());
		Assert.IsNull("locked-by", resource.getLockedBy());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], acumen.resource.model.ResourceMetadata)
	 */
	public Resource createResource(String nameSpace, String name, String userId, String fileType, byte[] data, ResourceMetadata metadata) throws ResourceException, StoreException {
		if (this.resourceExists(nameSpace, name)) {
			throw new ResourceException(String.format("The resource named '%s' under the namespace '%s' already exists.", name, nameSpace)); // ERROR duplicate resource
		} else {
			if (metadata == null) {
				metadata = new ResourceMetadata();
			}
			Resource resource = new Resource(data, metadata);
			resource.setNamespace(nameSpace);
			resource.setName(name);
			resource.setCreated(new Date());
			resource.setCommited(new Date());
			resource.setCreatedBy(userId);
			resource.setData(data);
			resource.setEncoding(this.getEncoding());
			resource.setType(fileType);

			assertCreationInvariance(resource); // invariance assertion
			this.addResource(resource);
			return resource;
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Resource createResource(String name, String userId, String fileType, String data) throws StoreException, ResourceException {
		return this.createResource(this.getNamespace(), name, userId, fileType, this.getEncoding().encode(data).array());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Resource createResource(String nameSpace, String name, String userId, String fileType, String data) throws StoreException, ResourceException {
		return this.createResource(nameSpace, name, userId, fileType, this.getEncoding().encode(data).array());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, byte[])
	 */
	public Resource createResource(String name, String userId, String fileType, byte[] data) throws StoreException, ResourceException {
		return this.createResource(this.getNamespace(), name, userId, fileType, data);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, byte[], acumen.resource.model.ResourceMetadata)
	 */
	public Resource createResource(String name, String userId, String fileType, byte[] data, ResourceMetadata metadata) throws StoreException, ResourceException {
		return this.createResource(this.getNamespace(), name, userId, fileType, data, metadata);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[])
	 */
	public Resource createResource(String nameSpace, String name, String userId, String fileType, byte[] data) throws StoreException, ResourceException {
		return this.createResource(nameSpace, name, userId, fileType, data, null);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResourceFromFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, acumen.resource.model.ResourceMetadata)
	 */
	public Resource createResourceFromFile(String nameSpace, String resourceName, String userId, String fileType, String path, ResourceMetadata metadata) throws StoreException, ResourceException {
		// get the information about the underlying file
		// and it's type
		File fileInfo = new File(path);
		String name = Is.NotNullOrEmpty(resourceName) ? resourceName : fileInfo.getName();
		String type;

		if (fileType != null) {
			type = fileType;
		} else if (fileInfo != null) {
			String[] parts = fileInfo.getName().split("[.]");
			type = parts[parts.length - 1];
		} else {
			type = DEFAULT_FILE_TYPE;
		}
		// read the actual binary data
		byte[] data;
		try {
			FileInputStream file = new FileInputStream(path);
			int size = file.available();
			data = new byte[size];
			file.read(data);
		} catch (FileNotFoundException err) {
			throw new StoreException(String.format("Unable to find the file '%s'.", path)); // ERROR file not found
		} catch (IOException err) {
			throw new StoreException(String.format("An error occured reading '%s'.", path)); // ERROR store barfed
		}
		// create the resource
		Resource resource = this.createResource(nameSpace, name, userId, type, data, metadata);
		return resource;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResourceFromFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Resource createResourceFromFile(String nameSpace, String resourceName, String userId, String fileType, String path) throws StoreException, ResourceException {
		return this.createResourceFromFile(nameSpace, resourceName, userId, fileType, path, null);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#createResourceFromFile(java.lang.String, java.lang.String, java.lang.String, acumen.resource.model.ResourceMetadata)
	 */
	public Resource createResourceFromFile(String nameSpace, String userId, String path, ResourceMetadata metadata) throws StoreException, ResourceException {
		return this.createResourceFromFile(nameSpace, null, userId, null, path, metadata);
	}

	public static void assertOpenForUpdateInvariance(IResource resource) throws IllegalArgumentException {
		Assert.NotNull("id", resource.getId());
		Assert.NotNull("namespace", resource.getNamespace());
		Assert.NotNull("name", resource.getName());
		Assert.NotNull("created", resource.getCreated());
		Assert.IsNull("commited", resource.getCommited());
		Assert.NotNull("created-by", resource.getCreatedBy());
		Assert.NotNull("data", resource.getData());
		Assert.NotNull("opened", resource.getOpened());
		Assert.NotNull("locked-by", resource.getLockedBy());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#openResourceForUpdate(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Resource openResourceForUpdate(String nameSpace, String name, String userId) throws StoreException, ResourceException {
		// is this resource already locked?
		Resource resource = this.getLockedInstance(nameSpace, name);
		if (resource != null) {
			// yes it is locked
			// does the user already own the lock?
			if (resource.getLockedBy().equals(userId)) {
				// yes they do
				// just pass the resource back to them
				// they might be continuing with an edit
				return resource;
			} else {
				// it'value not a user so throw an exception
				if (resource.getLockedBy().equals("DELETED")) {
					throw new ResourceException(String.format("The resource '%s:%s' has been deleted.", nameSpace, name)); // ERROR deleted
				} else {
					throw new ResourceException(String.format("The resource '%s:%s' has been locked.", nameSpace, name)); // ERROR locked
				}
			}
		} else {
			// its not locked so we can lock this file for our own use
			resource = this.getMostRecentInstance(nameSpace, name);
			if (resource != null) {
				resource.aquireNewId();
				resource.setOpened(new Date());
				resource.setCommited(null);
				resource.setLockedBy(userId);
				// we need to write back a record with no data
				// but pass back to the user the data for editting
				// so we need to copy the data aside, write then copy the data
				// back
				assertOpenForUpdateInvariance(resource); // invariance assertion
				this.addResource(resource);
			}
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#openResourceForUpdate(java.lang.String, java.lang.String)
	 */
	public Resource openResourceForUpdate(String name, String userId) throws StoreException, ResourceException {
		return this.openResourceForUpdate(this.getNamespace(), name, userId);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#replaceInResources(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void replaceInResources(String fileType, String find, String replace) throws StoreException {
		for (String id : this.getIds()) {
			Resource resource = this.getResourceInstance(id);
			if (resource.getType().equals(fileType)) {
				resource.setStringData(resource.getStringData().replace(find, replace));
				this.addResource(resource);
			}
		}
	}

	public static void assertUpdateInvariance(IResource resource) {
		Assert.NotNull("id", resource.getId());
		Assert.NotNull("namespace", resource.getNamespace());
		Assert.NotNull("name", resource.getName());
		Assert.NotNull("created", resource.getCreated());
		Assert.NotNull("commited", resource.getCommited());
		Assert.NotNull("created-by", resource.getCreatedBy());
		Assert.NotNull("data", resource.getData());
		Assert.NotNull("opened", resource.getOpened());
		Assert.IsNull("locked-by", resource.getLockedBy());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#updateResource(java.lang.String, java.lang.String, java.lang.String, byte[], acumen.resource.model.ResourceMetadata)
	 */
	public Resource updateResource(String nameSpace, String name, String userId, byte[] data,ResourceMetadata metadata) throws StoreException, ResourceException {
		Resource resource = this.getLockedInstance(nameSpace, name);
		if (resource != null) {
			if (resource.getLockedBy().equals(userId) || resource.getLockedBy().equals("DELETED")) {
				//resource.aquireNewId();
				resource.setLockedBy(null);
				resource.setCreatedBy(userId);
				resource.setCommited(new Date());
				resource.setData(data);
				resource.setMetadata(metadata);
				assertUpdateInvariance(resource); // invariance assertion
				this.addResource(resource);
			} else {
				throw new ResourceException(String.format("The resource '%s:%s' is locked.", nameSpace, name)); // ERROR resource locked
			}
		} else {
			throw new ResourceException(String.format("The resource '%s:%s' does not exist.", nameSpace, name)); // ERROR resource does not exist
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#updateResource(java.lang.String, java.lang.String, byte[], acumen.resource.model.ResourceMetadata)
	 */
	public Resource updateResource(String name, String userId, byte[] data,ResourceMetadata metadata) throws StoreException, ResourceException {
		return this.updateResource(this.getNamespace(), name, userId, data, metadata);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#updateResource(acumen.resource.model.IResource)
	 */
	public Resource updateResource(IResource resource) throws StoreException, ResourceException {
		return this.updateResource(resource.getNamespace(), resource.getName(), resource.getLockedBy(), resource.getData(),resource.getMetadata());
	}

	public static void assertMarkResourceAsDeletedInvariance(IResource resource) {
		Assert.NotNull("id", resource.getId());
		Assert.NotNull("namespace", resource.getNamespace());
		Assert.NotNull("name", resource.getName());
		Assert.NotNull("created", resource.getCreated());
		Assert.NotNull("commited", resource.getCommited());
		Assert.NotNull("created-by", resource.getCreatedBy());
		Assert.NotNull("data", resource.getData());
		Assert.NotNull("opened", resource.getOpened());
		Assert.AreEqual("locked-by", resource.getLockedBy(), "DELETED");
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#markResourceAsDeleted(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void markResourceAsDeleted(String nameSpace, String name, String userId) throws StoreException, ResourceException {
		// is this resource already locked?
		Resource resource = this.getLockedInstance(nameSpace, name);
		if (resource != null && !resource.getLockedBy().equals(userId)) {
			if (resource.getLockedBy().equals("DELETED")) {
				throw new ResourceException(String.format("The resource '%s:%s' has been deleted.", nameSpace, name)); // ERROR
				// resource
				// deleted
			} else {
				throw new ResourceException(String.format("The resource '%s:%s' is locked.", nameSpace, name)); // ERROR
				// resource
				// locked
			}
		} else {
			resource = this.getMostRecentInstance(nameSpace, name);
			if (resource != null) {
				resource.aquireNewId();
				resource.setLockedBy("DELETED");
				resource.setCommited(new Date());
				resource.setOpened(new Date());
				assertMarkResourceAsDeletedInvariance(resource);
				this.addResource(resource);
				this.cleanupResource(resource);
			}
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#markResourceAsDeleted(java.lang.String, java.lang.String)
	 */
	public void markResourceAsDeleted(String name, String userId) throws StoreException, ResourceException {
		this.markResourceAsDeleted(this.getNamespace(), name, userId);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#bindNamespace(java.lang.String, java.lang.String)
	 */
	public void bindNamespace(String nameSpace, String path) throws StoreException {
		File location = new File(path);
		if (location.isDirectory()) {
			this.unbindNamespace(nameSpace, path);
			String sql = "insert into filestore_binding\r\n" + "					(fsb_code, fsb_namespace, fsb_directory)\r\n" + "				values\r\n" + "					(?, ?, ?)";
			this.exec(sql, UUID.randomUUID().toString(), nameSpace, path);
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#unbindNamespace(java.lang.String, java.lang.String)
	 */
	public void unbindNamespace(String nameSpace, String path) throws StoreException {
		String sql = "delete from filestore_binding where fsb_namespace = ? and fsb_directory = ?";
		this.exec(sql, nameSpace, path);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#getBindingPathsForNameSpace(java.lang.String)
	 */
	public AcumenList<String> getBindingPathsForNameSpace(String nameSpace) throws StoreException {
		AcumenList<String> results = new AcumenList<String>();
		ResultSet reader = null;
		try {
			try {
				reader = this.read("select * from filestore_binding where fsb_namespace = ?", nameSpace);
				while (reader.next()) {
					String path = reader.getString("fsb_directory");
					results.add(path);
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage());
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#importDirectoryToNamespace(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void importDirectoryToNamespace(String nameSpace, String prefix, String path) throws StoreException, ResourceException {
		// current directory
		File dir = new File(path);
		if (dir.isDirectory() && !dir.getName().startsWith(".")) {
			// recurse down the directories
			File[] subDirs = dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory();
				}
			});

			for (File subdir : subDirs) {
				if (!subdir.getName().startsWith(".")) {
					try {
						String separator = System.getProperty("file.separator");
						String subPrefix = subdir.getCanonicalPath().replace(path.concat(separator), "");
						if (prefix.length() > 0) {
							subPrefix = prefix.concat("/").concat(subPrefix);
						}
						importDirectoryToNamespace(nameSpace, subPrefix, subdir.getAbsolutePath());
					} catch (IOException err) {
						throw new StoreException(err.getMessage());
					}
				}
			}
			// import the actual files
			File[] files = dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return !file.isDirectory();
				}
			});
			for (File file : files) {
				String name = Is.NullOrEmpty(prefix) ? file.getName() : prefix.concat("/").concat(file.getName());
				byte[] data;
				try {
					FileInputStream stream = new FileInputStream(file.getAbsoluteFile());
					int size = stream.available();
					data = new byte[size];
					stream.read(data);
				} catch (FileNotFoundException err) {
					throw new StoreException(String.format("Unable to find the file '%s'.", path)); // ERROR file not found
				} catch (IOException err) {
					throw new StoreException(String.format("An error occured reading '%s'.", path)); // ERROR store barfed
				}
				if (this.resourceExists(nameSpace, name)) {
					Resource resource = this.openResourceForUpdate(nameSpace, name, "system");
					resource.setData(data);
					this.updateResource(resource);
				} else {
					String[] parts = file.getName().split("[.]");
					String type = parts[parts.length - 1];
					this.createResource(nameSpace, name, "system", type, data);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#importDirectoryToNamespace(java.lang.String, java.lang.String)
	 */
	public void importDirectoryToNamespace(String nameSpace, String path) throws ResourceException, StoreException {
		this.importDirectoryToNamespace(nameSpace, "", path);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#synchroniseResource(acumen.resource.model.IResource)
	 */
	public void synchroniseResource(IResource resource) throws StoreException {
		AcumenList<String> bindings = this.getBindingPathsForNameSpace(resource.getNamespace());
		for (String binding : bindings) {
			// write the current resource to the current directory
			String separator = System.getProperty("file.separator");
			String path = binding.concat(separator).concat(resource.getName());
			writeDataToPath(path, resource);
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#cleanupResource(java.lang.String, java.lang.String)
	 */
	public void cleanupResource(String nameSpace, String resourceName) throws StoreException {
		AcumenList<String> bindings = this.getBindingPathsForNameSpace(nameSpace);
		for (String binding : bindings) {
			// remove the current resource to the current directory
			String separator = System.getProperty("file.separator");
			String path = binding.concat(separator).concat(resourceName);
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			String metaPath = path.concat(".meta");
			File meta = new File(metaPath);
			if (meta.exists()) {
				meta.delete();
			}
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#cleanupResource(acumen.resource.model.IResource)
	 */
	public void cleanupResource(IResource resource) throws StoreException {
		this.cleanupResource(resource.getNamespace(), resource.getName());
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#cleanupResource(java.lang.String)
	 */
	public void cleanupResource(String name) throws StoreException {
		this.cleanupResource(this.getNamespace(), name);
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#synchroniseNamespace(java.lang.String)
	 */
	public void synchroniseNamespace(String nameSpace) throws StoreException {
		// prepare directory information for our bound directories
		AcumenList<String> bindings = this.getBindingPathsForNameSpace(nameSpace);

		// iterate over each resource under the namespace
		AcumenDictionary<String, Resource> resources = this.getMostRecentForNamespace(nameSpace);
		for (Map.Entry<String, Resource> entry : resources.entrySet()) {
			Resource resource = entry.getValue();
			// iterate over each bound directory
			for (String binding : bindings) {
				// write the current resource to the current directory
				String separator = System.getProperty("file.separator");
				String path = binding.concat(separator).concat(resource.getName());
				writeDataToPath(path, this.getResourceInstance(resource.getId()));
			}
		}
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#synchroniseNamespace()
	 */
	public void synchroniseNamespace() throws StoreException {
		this.synchroniseNamespace(this.getNamespace());
	}

	public static void writeDataToPath (String path, IResource resource) throws StoreException {
		Assert.HasValue(path);
		// does the file already exist?
		try {
			File file = new File(path);
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				stream.write(resource.getData());
			} finally {
				stream.close();
			}
			// write out the metadata for the resource
			File meta = new File(path.concat(".meta"));
			BufferedWriter out = null;
			try {
		        out = new BufferedWriter(new FileWriter(meta.getAbsoluteFile()));
		        out.write(resource.getMetadata().toXml());
			} finally {
		        out.close();
		    }
		} catch (FileNotFoundException err) {
			throw new StoreException(err.getMessage());
		} catch (IOException err) {
			throw new StoreException(err.getMessage());	
		}		
	}

	/* (non-Javadoc)
	 * @see acumen.resource.store.IFileStore#nukeEverythingInTheStore()
	 */
	public void nukeEverythingInTheStore() throws StoreException {
		this.exec("truncate filestore");
	}

}