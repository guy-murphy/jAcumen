package acumen.resource.model;

import java.nio.charset.Charset;
import java.util.Date;

import acumen.util.IDataType;

public interface IResource extends IDataType {

	public abstract void setId(String id);

	public abstract String getId();

	public abstract void setNamespace(String namespace);

	public abstract String getNamespace();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setCreated(Date created);

	public abstract Date getCreated();

	public abstract void setOpened(Date opened);

	public abstract Date getOpened();

	public abstract void setCommited(Date commited);

	public abstract Date getCommited();

	public abstract void setBranch(int branch);

	public abstract int getBranch();

	public abstract void setRights(int rights);

	public abstract int getRights();

	public abstract void setType(String type);

	public abstract String getType();

	public abstract void setCreatedBy(String createdBy);

	public abstract String getCreatedBy();

	public abstract void setLockedBy(String lockedBy);

	public abstract String getLockedBy();

	public abstract void setData(byte[] data);

	public abstract byte[] getData();

	public abstract ResourceMetadata getMetadata();
	
	public abstract void setMetadata(ResourceMetadata meta);	

	public abstract Charset getEncoding();

	public abstract void setEncoding(Charset encoding);

	public abstract String getStringData();

	public abstract void setStringData(String data);

	public abstract boolean isLocked();

	public abstract boolean hasData();

	public abstract double size();

	public abstract String aquireNewId();

	public abstract OperationType getOperation();

	public abstract Date getDateForCompare() throws ResourceException;

	public abstract int compareTo(IResource other) throws ResourceException;

	public abstract int compareTo(Object obj) throws ResourceException;

	public abstract String toString();

}