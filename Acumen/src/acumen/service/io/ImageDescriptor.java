/* Author:	Guy J. Murphy
 * Date:	May 11, 2010
 * Time:	3:40:39 PM
 * File: ImageDescriptor.java
 */
package acumen.service.io;

import java.io.File;
import java.io.IOException;

import acumen.data.xml.XmlWriter;

public class ImageDescriptor extends FileDescriptor {

	public static FileDescriptor getInstance() throws IOException {
		return new ImageDescriptor();
	}
	
	public static FileDescriptor getInstance (File file) throws IOException {
		return new ImageDescriptor(file);
	}
	
	public static FileDescriptor getInstance (String path) throws IOException {
		return new ImageDescriptor(path);
	}
	
	protected ImageDescriptor () throws IOException {
		super();
	}
	
	public ImageDescriptor (File file) throws IOException {
		super(file);
	}
	
	public ImageDescriptor (String path) throws IOException {
		super(path);	
	}
	
	public String getThumbDirectory () {
		if (this.getMetadata().containsKey("thumbdir")) {
			return this.getMetadata().get("thumbdir");
		} else {
			return "thumb";
		}
	}
	
	public String getIconDirectory () {
		if (this.getMetadata().containsKey("icondir")) {
			return this.getMetadata().get("icondir");
		} else {
			return "icon";
		}
	}
	
	@Override
	public void toXml(XmlWriter xml) throws IOException {
		xml.writeEntity("file");
		super.writeContents(xml);
		xml.writeAttribute("thumbs", this.getThumbDirectory());
		xml.writeAttribute("icons", this.getIconDirectory());
		xml.endEntity();
	}
	
}
