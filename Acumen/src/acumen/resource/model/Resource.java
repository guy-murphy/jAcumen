package acumen.resource.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import acumen.data.xml.XmlWriter;

import acumen.util.*;

public class Resource extends DataType implements IResource {
	
	private final String DEFAULT_RESOURCE_TYPE = "unknown";
	
	private String _id;
	private String _namespace;
	private String _name;
	private Date _created;
	private Date _opened;
	private Date _commited;
	private int _branch;
	private int _rights;
	private String _type;
	private String _createdBy;
	private String _lockedBy;
	private byte[] _data;
	private ResourceMetadata _meta;
	private Charset _encoding;
	
	public Resource (String id, byte[] data, ResourceMetadata metadata) {
		_id = id;
		_data = data;
		_meta = metadata;
	}
	
	public Resource () {
		this(UUID.randomUUID().toString(), null);
	}
	public Resource (String id) {
		this(id, null);
	}
	public Resource (byte[] data) {
		this(UUID.randomUUID().toString(), data);
	}
	public Resource (byte[] data, ResourceMetadata metadata) { 
		this(UUID.randomUUID().toString(), data, metadata);
	}
	public Resource (String id, byte[] data) {
		this(id, data, new ResourceMetadata());
	}
	
	public Resource (IResource resource) {
		_id = resource.getId();
		_data = resource.getData();
		_meta = resource.getMetadata();
		_namespace = resource.getNamespace();
		_name = resource.getName();
		_branch = resource.getBranch();
		_commited = resource.getCommited();
		_created = resource.getCreated();
		_createdBy = resource.getCreatedBy();
		_lockedBy = resource.getLockedBy();
		_opened = resource.getOpened();
		_rights = resource.getRights();
	}
	
	@Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof IResource && obj != null) {
    		IResource other = (IResource)obj;
    		return this.getId() == null ? other.getId() == null : this.getId().equals(other.getId());
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = 1;
    	hash = hash * 31 + "Resource::".hashCode();
    	hash = hash * 31 + (this.getId() == null ? 0 : this.getId().hashCode());
    	return hash;
    }
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setId(java.lang.String)
	 */
	public void setId(String id) {
		_id = id;
	}

	/* (non-Javadoc)
	 * @see resource.model.IResource#getId()
	 */
	public String getId() {
		return _id;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setNamespace(java.lang.String)
	 */
	public void setNamespace(String namespace) {
		_namespace = namespace;
	}

	/* (non-Javadoc)
	 * @see resource.model.IResource#getNamespace()
	 */
	public String getNamespace() {
		return _namespace;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setName(java.lang.String)
	 */
	public void setName (String name) {
		_name = name;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getName()
	 */
	public String getName () {
		return _name;		
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setCreated(java.util.Date)
	 */
	public void setCreated (Date created) {
		_created = created;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getCreated()
	 */
	public Date getCreated () {
		return _created;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setOpened(java.util.Date)
	 */
	public void setOpened (Date opened) {
		_opened = opened;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getOpened()
	 */
	public Date getOpened () {
		return _opened;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setCommited(java.util.Date)
	 */
	public void setCommited (Date commited) {
		_commited = commited;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getCommited()
	 */
	public Date getCommited () {
		return _commited;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setBranch(int)
	 */
	public void setBranch (int branch) {
		_branch = branch;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getBranch()
	 */
	public int getBranch () {
		return _branch;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setRights(int)
	 */
	public void setRights (int rights) {
		_rights = rights;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getRights()
	 */
	public int getRights () {
		return _rights;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setType(java.lang.String)
	 */
	public void setType (String type) {
		_type = type;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getType()
	 */
	public String getType () {
		if (Is.NullOrEmpty(_type)) {
			_type = DEFAULT_RESOURCE_TYPE;
		}
		return _type;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setCreatedBy(java.lang.String)
	 */
	public void setCreatedBy (String createdBy) {
		_createdBy = createdBy;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getCreatedBy()
	 */
	public String getCreatedBy () {
		return _createdBy;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setLockedBy(java.lang.String)
	 */
	public void setLockedBy (String lockedBy) {
		_lockedBy = lockedBy;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getLockedBy()
	 */
	public String getLockedBy () {
		return _lockedBy;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setData(byte[])
	 */
	public void setData (byte[] data) {
		_data = data;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getData()
	 */
	public byte[] getData () {
		return _data;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getMetadata()
	 */
	public ResourceMetadata getMetadata () {
		if (_meta == null) {
			_meta = new ResourceMetadata();
		}
		return _meta;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setMetadata()
	 */	
	public void setMetadata (ResourceMetadata meta) {
		_meta = meta;
	}		
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getEncoding()
	 */
	public Charset getEncoding () {
		if (_encoding == null) {
			_encoding = Charset.forName("UTF8");
		}
		return _encoding;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setEncoding(java.nio.charset.Charset)
	 */
	public void setEncoding (Charset encoding) {
		_encoding = encoding;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getStringData()
	 */
	public String getStringData () {
		return StringUtil.stripNonValidXmlCharacters(new String(this.getData(), this.getEncoding()));
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#setStringData(java.lang.String)
	 */
	public void setStringData (String data) {
		String cleanText = StringUtil.stripNonValidXmlCharacters(data);
		this.setData(this.getEncoding().encode(cleanText).array());
		if (_type.equals("unknown")) {
			_type = "string";
		}
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#isLocked()
	 */
	public boolean isLocked () {
		return Is.NotNullOrEmpty(this.getLockedBy());
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#hasData()
	 */
	public boolean hasData () {
		return _data != null;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#size()
	 */
	public double size () {
		return (_data == null) ? 0 : _data.length / 1024;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#aquireNewId()
	 */
	public String aquireNewId () {
		_id = UUID.randomUUID().toString();
		return _id;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getOperation()
	 */
	public OperationType getOperation () {
		if (this.getOpened() == null && this.getCommited() != null) {
			return OperationType.Create;
		}
		if (this.getOpened() != null && this.getCommited() == null) {
			return OperationType.Open;
		}
		if (this.getOpened() != null && this.getCommited() != null) {
			if (this.getLockedBy() == "DELETED") {
				return OperationType.Delete;
			} else {
				return OperationType.Commit;
			}
		}
		return OperationType.Unknown;
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#getDateForCompare()
	 */
	public Date getDateForCompare() throws ResourceException {
		switch (this.getOperation()) {
			case Create:
				return this.getCommited();
			case Open:
				return this.getOpened();
			case Commit:
				return this.getCommited();
			case Delete:
				return this.getCommited();
			default:
				throw new ResourceException("Unrecognised filestore operation.");
		}
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#compareTo(resource.model.IResource)
	 */
	public int compareTo (IResource other) throws ResourceException {
		try {
			return this.getDateForCompare().compareTo(other.getDateForCompare());
		} catch (Exception err) {
			throw new ResourceException(err.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#compareTo(java.lang.Object)
	 */
	public int compareTo (Object obj) throws ResourceException {
		IResource other = (IResource)obj;
		return this.compareTo(other);
	}
	
	/* (non-Javadoc)
	 * @see resource.model.IResource#toString()
	 */
	public String toString () {
		return this.getStringData();
	}
		
	protected void writeAttributes (XmlWriter writer) throws IOException {
		writer.writeAttribute("id", this.getId());
		writer.writeAttribute("namespace", this.getNamespace());
		writer.writeAttribute("name", this.getName());
		writer.writeAttribute("type", this.getType());
		writer.writeAttribute("created-by", this.getCreatedBy());
		writer.writeAttribute("locked-by", this.getLockedBy());
		writer.writeAttribute("created-on", this.getCreated().toString());
		if (this.getOpened() != null) {
			writer.writeAttribute("opened-on", this.getOpened().toString());
		}
		if (this.getCommited() != null) {
			writer.writeAttribute("commited-on", this.getCommited().toString());
		}
		writer.writeAttribute("size", this.size());
		writer.writeAttribute("has-data", this.hasData());
	}

	@Override
	public void toXml (XmlWriter writer) throws IOException {
		writer.writeEntity("resource");
		this.writeAttributes(writer);
		if (this.getMetadata().size() > 0) {
			this.getMetadata().toXml(writer);
		}
		writer.endEntity();
	}
	

}
