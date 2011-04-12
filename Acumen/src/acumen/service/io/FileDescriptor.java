/* Author:	Guy J. Murphy
 * Date:	May 11, 2010
 * Time:	3:40:39 PM
 * File:	FileDescriptor.java
 */
package acumen.service.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;

import org.w3c.dom.Document;

import acumen.data.xml.XmlWriter;
import acumen.util.DataType;
import acumen.util.PropertySet;

public class FileDescriptor extends DataType {
	
	private File _originalFile;
	private File _metaFile;
	private PropertySet _metadata;
	
	public static FileDescriptor getInstance() throws IOException {
		return new FileDescriptor();
	}
	
	public static FileDescriptor getInstance (File file) throws IOException {
		return new FileDescriptor(file);
	}
	
	public static FileDescriptor getInstance (String path) throws IOException {
		return new FileDescriptor(path);
	}
	
	protected FileDescriptor () throws IOException {
		_init();
	}
	
	public FileDescriptor (File file) throws IOException {
		this.setFile(file);
		_init();
	}
	
	public FileDescriptor (String path) throws IOException {
		this.setFullPath(path);
		_init();
	}
	
	private void _init () throws IOException {
		_metaFile = null;
		_metadata = null;
		if (this.getMetaFile().exists()) {
			try {
				Document metafile = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getMetaFile());
				((PropertySet) this.getMetadata()).fromXml(metafile);
			} catch (Exception err) {
				throw new IOException(err);
			} 
		}
	}
	
	public File getFile () {
		return _originalFile;
	}
	
	public File setFile (File file) throws IOException {
		_originalFile = file;
		_init();
		return file;
	}
	
	public String getFullPath () {
		return _originalFile.getAbsolutePath();
	}
	
	public String setFullPath (String path) throws IOException {
		this.setFile(new File(path));
		return path;
	}
	
	public File getMetaFile () {
		if (_metaFile == null) {
			String path = this.getFullPath() + ".meta";
			_metaFile = new File(path);
		}
		return _metaFile;
	}
	
	public PropertySet getMetadata () {
		if (_metadata == null) {
			_metadata = new PropertySet();
		}
		return _metadata;
	}
	
	public String getName () {
		String fileName = this.getFullName();
		int pointPos = fileName.lastIndexOf('.');
		if (pointPos > -1) {
			return fileName.substring(0, fileName.lastIndexOf('.'));
		} else {
			return fileName;
		}
	}
	
	public String getFullName () {
		return this.getFile().getName();
	}
	
	public String getExtension () {
		String fileName = this.getFullName();
		int pointPos = fileName.lastIndexOf('.');
		if (pointPos > -1) {
			return fileName.substring(pointPos);
		} else {
			return null;
		}
	}
	
	public String getDescription () {
		return (this.getMetadata().containsKey("description")) ? this.getMetadata().get("description") : null;
	}
	
	public String setDescription (String description) {
		this.getMetadata().put("description", description);
		return description;
	}
	
	public String getOwner () {
		return (this.getMetadata().containsKey("owner")) ? this.getMetadata().get("owner") : null;
	}
	
	public String setOwner (String owner) {
		this.getMetadata().put("owner", owner);
		return owner;
	}
	
	public double getSize() {
		return (double)(this.getFile().length() / 1024);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
    	if (obj instanceof FileDescriptor && obj != null) {
    		FileDescriptor other = (FileDescriptor)obj;
    		return this.getFullPath() == null ? other.getFullPath() == null : this.getFullPath().equals(other.getFullPath());
    	}
    	return false;
	}

	@Override
	public int hashCode() {
		int hash = 1;
    	hash = hash * 31 + this.getFullPath().hashCode();
    	return hash;
	}

	public void writeContents(XmlWriter xml) throws IOException {
		xml.writeAttribute("name", this.getName());
		xml.writeAttribute("extension", this.getExtension());
		xml.writeAttribute("owner", this.getOwner());
		xml.writeAttribute("size", this.getSize());
		xml.writeAttribute("description", this.getDescription());
	}
	
	@Override
	public void toXml(XmlWriter xml) throws IOException {
		xml.writeEntity("file");
		this.writeContents(xml);
		xml.endEntity();
	}

}
