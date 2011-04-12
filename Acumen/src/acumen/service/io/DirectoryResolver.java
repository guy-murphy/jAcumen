/**
 * Author:	Guy J. Murphy
 * Date:	May 12, 2010
 * Time:	10:48:49 AM
 * File:	DirectoryResolver.java
 */
package acumen.service.io;

import java.io.File;
import java.io.IOException;

public class DirectoryResolver extends FileResolver {

	public DirectoryResolver(String root) {
		super(root);
	}

	@Override
	public File getFile () throws IOException {
		if (!super.getFile().isDirectory()) {
			throw new IOException("The DirectoryResolver points to a path that is not a directory.");
		}
		return super.getFile();
	}
	
	@Override
	public boolean exists () throws IOException {
		return (super.getFile().exists() && super.getFile().isDirectory());
			
	}
	
}
