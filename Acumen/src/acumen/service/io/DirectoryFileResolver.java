/**
 * Author:	Guy J. Murphy
 * Date:	May 12, 2010
 * Time:	10:48:49 AM
 * File:	DirectoryFileResolver.java
 */
package acumen.service.io;

import java.io.File;
import java.io.IOException;

import acumen.util.AcumenDictionary;

public class DirectoryFileResolver<T extends FileDescriptor> extends DirectoryResolver {

	public DirectoryFileResolver(String root) {
		super(root);
	}

	private AcumenDictionary<String, T> _files;

	public static <D> D getInstance(Class<D> _class) {
		try {
			return _class.newInstance();
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public AcumenDictionary<String, T> getFiles() throws IOException {
		if (_files == null) {
			File[] contents = this.getFile().listFiles();
			_files = new AcumenDictionary<String, T>();
			for (File file : contents) {
				if (file.isFile() && !file.getName().endsWith(".meta")) {
					T descriptor = (T) T.getInstance(file);
					_files.put(descriptor.getFullName(), descriptor);
				}
			}
		}
		return _files;
	}

}
